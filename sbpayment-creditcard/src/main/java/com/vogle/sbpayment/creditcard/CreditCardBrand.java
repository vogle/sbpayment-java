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

package com.vogle.sbpayment.creditcard;

/**
 * Card Brand Type
 *
 * @author Allan Im
 **/
public enum CreditCardBrand {

    JCB("J"), VISA("V"), MASTER("M"), AMEX("A"), DINERS("D"), OTHER("X");

    private String spsCardbrandCode;

    CreditCardBrand(String spsCardbrandCode) {
        this.spsCardbrandCode = spsCardbrandCode;
    }

    /**
     * Gets a brand
     * <p>
     * Code: Brand
     * "J": JCB
     * "V": VISA
     * "M": MASTER
     * "A": AMEX
     * "D": DINERS
     * </p>
     *
     * @param code brand code
     * @return Card Brand
     */
    public static CreditCardBrand brand(String code) {
        for (CreditCardBrand brand : CreditCardBrand.values()) {
            if (code.equalsIgnoreCase(brand.getSpsCardbrandCode())) {
                return brand;
            }
        }
        return OTHER;
    }

    /**
     * Gets Brand Code
     *
     * @return "J", "V", "M", "A" or "D"
     */
    public String getSpsCardbrandCode() {
        return spsCardbrandCode;
    }
}
