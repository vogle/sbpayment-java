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

package com.vogle.sbpayment.creditcard.responses;

/**
 * Commit Status
 *
 * @author Allan Im
 **/
public enum CommitStatus {

    UNPROCESSED("0"), COMMIT("1"), COMMITTED_CANCEL("2"), UNCOMMITTED_CANCEL("3");

    private String statusCode;

    CommitStatus(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Gets {@link CommitStatus} from the code
     *
     * @param code commit status code
     * @return CommitStatus
     */
    public static CommitStatus status(String code) {
        for (CommitStatus status : CommitStatus.values()) {
            if (code.equalsIgnoreCase(status.getCommitStatusCode())) {
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
    public String getCommitStatusCode() {
        return statusCode;
    }

}
