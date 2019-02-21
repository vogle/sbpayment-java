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

package com.vogle.sbpayment.creditcard.params;

import org.junit.Test;

import static com.vogle.sbpayment.creditcard.DealingsType.INSTALLMENT;
import static com.vogle.sbpayment.creditcard.DealingsType.LUMP_SUM;
import static com.vogle.sbpayment.creditcard.DealingsType.REVOLVING;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ParamUtils}
 *
 * @author Allan Im
 */
public class ParamUtilsTest {

    @Test
    public void convertBool() {
        assertThat(ParamUtils.convertBool(true)).isEqualTo("1");
        assertThat(ParamUtils.convertBool(false)).isEqualTo("0");
    }

    @Test
    public void convertDealingsType() {
        assertThat(ParamUtils.convertDealingsType(INSTALLMENT, 1)).isEqualTo(LUMP_SUM);
        assertThat(ParamUtils.convertDealingsType(REVOLVING, 1)).isEqualTo(REVOLVING);
        assertThat(ParamUtils.convertDealingsType(INSTALLMENT, 3)).isEqualTo(INSTALLMENT);
    }

    @Test
    public void convertDivideTimes() {
        assertThat(ParamUtils.convertDivideTimes(LUMP_SUM, 20)).isNull();
        assertThat(ParamUtils.convertDivideTimes(INSTALLMENT, 1)).isNull();
        assertThat(ParamUtils.convertDivideTimes(INSTALLMENT, 3)).isEqualTo("003");
        assertThat(ParamUtils.convertDivideTimes(INSTALLMENT, 20)).isEqualTo("020");
        assertThat(ParamUtils.convertDivideTimes(INSTALLMENT, 211)).isEqualTo("211");
    }
}