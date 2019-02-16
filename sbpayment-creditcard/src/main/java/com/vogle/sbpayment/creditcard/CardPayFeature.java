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
 * Credit-Card payment Features
 */
public enum CardPayFeature {
    /**
     * When sending customer information, return it from Softbank payment.<br/>
     * 顧客コードを送るとき、ソフトバングペイメントから顧客情報を返却する。
     */
    RETURN_CUSTOMER_INFO,

    /**
     * When sending credit-card information, return credit-card brand.<br/>
     * カード情報を送るとき、カードブランド情報を返却する。
     */
    RETURN_CARD_BRAND
}
