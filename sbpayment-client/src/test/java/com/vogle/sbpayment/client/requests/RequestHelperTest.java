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

package com.vogle.sbpayment.client.requests;

import com.vogle.sbpayment.client.params.Item;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RequestHelper}
 *
 * @author Allan Im
 */
public class RequestHelperTest {

    @Test
    public void mapItem() {
        // given
        List<Item> items = new ArrayList<>();
        items.add(Item.builder()
            .itemId("ID1").itemName("NAME1").itemAmount(100).itemTax(8).itemCount(2)
            .build());
        items.add(Item.builder()
            .itemId("ID2").itemName("NAME2").itemAmount(1000).itemTax(80).itemCount(4)
            .build());

        // when
        List<PayDetail> payDetails = RequestHelper.mapItem(items);

        // then
        assertThat(payDetails).isNotNull();
        assertThat(payDetails.size()).isEqualTo(2);
        assertThat(payDetails.stream().filter(payDetail -> payDetail.getDtlItemId().equals("ID1"))
            .collect(Collectors.toList()).size()).isEqualTo(1);
    }

    @Test
    public void flag() {
        // when
        String enable = RequestHelper.flag(true);
        String disable = RequestHelper.flag(false);

        // then
        assertThat(enable).isEqualTo("1");
        assertThat(disable).isEqualTo("0");
    }

    @Test
    public void dateOnly() {
        // when
        String date = RequestHelper.dateOnly("20190101121208");

        // then
        assertThat(date).isEqualTo("20190101");
    }

    @Test
    public void dateOnlyToAdd() {
        // when
        String date1 = RequestHelper.dateOnly("20190101121208", 3);

        // then
        assertThat(date1).isEqualTo("20190104");

        // when
        String date2 = RequestHelper.dateOnly("20190101121208", -3);

        // then
        assertThat(date2).isEqualTo("20181229");
    }

    @Test(expected = IllegalArgumentException.class)
    public void dateOnlyWithException() {
        // when
        RequestHelper.dateOnly("2019");
    }

    @Test
    public void avoidNull() {
        // when
        String str = RequestHelper.avoidNull(null, "default");

        // then
        assertThat(str).isEqualTo("default");
    }
}