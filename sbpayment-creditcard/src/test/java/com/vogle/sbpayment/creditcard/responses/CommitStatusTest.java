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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CommitStatus}
 *
 * @author Allan Im
 */
public class CommitStatusTest {

    @Test
    public void status() {
        assertThat(CommitStatus.status("0")).isEqualTo(CommitStatus.UNPROCESSED);
        assertThat(CommitStatus.status("1")).isEqualTo(CommitStatus.COMMIT);
        assertThat(CommitStatus.status("2")).isEqualTo(CommitStatus.COMMITTED_CANCEL);
        assertThat(CommitStatus.status("3")).isEqualTo(CommitStatus.UNCOMMITTED_CANCEL);
        assertThat(CommitStatus.status("NONE")).isNull();
    }

    @Test
    public void getCommitStatusCode() {
        assertThat(CommitStatus.UNPROCESSED.getCommitStatusCode()).endsWith("0");
        assertThat(CommitStatus.COMMIT.getCommitStatusCode()).endsWith("1");
        assertThat(CommitStatus.COMMITTED_CANCEL.getCommitStatusCode()).endsWith("2");
        assertThat(CommitStatus.UNCOMMITTED_CANCEL.getCommitStatusCode()).endsWith("3");
    }
}