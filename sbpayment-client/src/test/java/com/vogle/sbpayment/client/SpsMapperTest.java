/*
 * Copyright 2019 Vogle Labs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vogle.sbpayment.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Data;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DefaultSpsMapper}
 *
 * @author Allan Im
 */
public class SpsMapperTest {

    @Test
    public void getCharset() {
        // when
        SpsMapper mapper = new DefaultSpsMapper(cipherEnabled());

        // then
        assertThat(mapper.getCharset()).isEqualTo(Charset.forName("Shift_JIS"));
    }

    @Test
    public void getHashKey() {
        // when
        SpsMapper mapper = new DefaultSpsMapper(cipherEnabled());

        // then
        assertThat(mapper.getHashKey()).isEqualTo("HASH_KEY2020");
    }

    @Test
    public void xmlToObject() {
        // given
        SpsMapper mapper = new DefaultSpsMapper(cipherEnabled());

        // when
        TestObject result = mapper.xmlToObject("<sps id='ST02-00101-101'/>", TestObject.class);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("ST02-00101-101");
    }

    @Test
    public void xmlToObjectWithCipherDisabled() {
        // given
        SpsMapper mapper = new DefaultSpsMapper(cipherDeisabled());

        // when
        TestObject result = mapper.xmlToObject("<sps id='ST02-00101-102'/>", TestObject.class);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("ST02-00101-102");
    }

    @Test(expected = XmlMappingException.class)
    public void xmlToObjectWithMappingFail() {
        // given
        SpsMapper mapper = new DefaultSpsMapper(cipherDeisabled());

        // when
        mapper.xmlToObject("<sps id='ST02-00101-102'>", TestObject.class);
    }

    @Test
    public void objectToXml() {
        // given
        SpsMapper mapper = new DefaultSpsMapper(cipherEnabled());
        TestObject testObject = new TestObject();
        testObject.setId("ST01-00101-000");

        // when
        String xml = mapper.objectToXml(testObject);

        // then
        assertThat(xml).isNotNull();
    }

    @Test
    public void objectToXmlWithCipherDisabled() {
        // given
        SpsMapper mapper = new DefaultSpsMapper(cipherDeisabled());
        TestObject testObject = new TestObject();
        testObject.setId("ST01-00101-000");

        // when
        String xml = mapper.objectToXml(testObject);

        // then
        assertThat(xml).isNotNull();
    }

    @Test(expected = XmlMappingException.class)
    public void objectToXmlWithMappingFail() throws Exception {
        // given
        SpsMapper mapper = new DefaultSpsMapper(cipherDeisabled());
        TestObject testObject = new TestObject();

        XmlMapper xmlMapper = Mockito.mock(XmlMapper.class);
        Mockito.when(xmlMapper.writeValueAsString(Mockito.any())).thenThrow(JsonProcessingException.class);
        Whitebox.setInternalState(mapper, "xmlMapper", xmlMapper);

        // when
        mapper.objectToXml(testObject);
    }

    private SpsConfig.CipherInfo cipherEnabled() {
        return SpsConfig.builder()
            .apiUrl("http://vogle.com")
            .hashKey("HASH_KEY2020")
            .cipherEnabled(true)
            .desKey("DES_KEY")
            .desInitKey("INIT_KEY")
            .build().getCipherInfo();
    }

    private SpsConfig.CipherInfo cipherDeisabled() {
        return SpsConfig.builder()
            .apiUrl("http://vogle.com")
            .hashKey("HASH_KEY")
            .cipherEnabled(false)
            .desKey("DES_KEY1")
            .desInitKey("INIT_KEY1")
            .build().getCipherInfo();
    }

    @Data
    private static class TestObject {
        private String id;
    }
}