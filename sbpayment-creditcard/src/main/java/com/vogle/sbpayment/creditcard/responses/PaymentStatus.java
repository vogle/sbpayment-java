/*
 * Copyright 2019 VOGLE Labs.
 *
 * This file is part of sbpayment-java - Sbpayment client.
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

package com.vogle.sbpayment.creditcard.responses;

/**
 * Payment Status
 *
 * @author Allan Im
 **/
public enum PaymentStatus {

    AUTHORIZED("1"), CAPTURED("2"), CANCELED("3"), REFUNDED("4");

    private String statusCode;

    PaymentStatus(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Gets a {@link PaymentStatus} from the code
     *
     * @param code Status code
     * @return PaymentStatus
     */
    public static PaymentStatus status(String code) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (code.equalsIgnoreCase(status.getPaymentStatusCode())) {
                return status;
            }
        }
        return null;
    }

    /**
     * Gets current status code by String
     *
     * @return Status code
     */
    public String getPaymentStatusCode() {
        return statusCode;
    }

}
