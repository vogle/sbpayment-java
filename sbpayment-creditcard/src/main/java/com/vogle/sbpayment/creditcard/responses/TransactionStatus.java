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
 * Transaction Status, '0' is Ok, '1' is Error
 *
 * @author Allan Im
 **/
public enum TransactionStatus {

    NORMAL("0"), ERROR("1");

    private String statusCode;

    TransactionStatus(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Gets a {@link TransactionStatus} from the code
     *
     * @param code Status code
     * @return TransactionStatus
     */
    public static TransactionStatus status(String code) {
        for (TransactionStatus status : TransactionStatus.values()) {
            if (code.equalsIgnoreCase(status.getTransactionStatusCode())) {
                return status;
            }
        }
        return ERROR;
    }

    /**
     * Gets current status code by String
     *
     * @return Status code
     */
    public String getTransactionStatusCode() {
        return statusCode;
    }

}
