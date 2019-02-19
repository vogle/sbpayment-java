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

import lombok.Data;
import org.junit.Test;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotEmpty;

/**
 * Tests for {@link ValidationHelper}
 * @author Allan Im
 */
public class ValidationHelperTest {


    @Test(expected = ConstraintViolationException.class)
    public void beanValidate() {
        ValidationHelper.beanValidate("name", new TestObject());
    }

    @Test(expected = IllegalArgumentException.class)
    public void beanValidateWithNull() {
        ValidationHelper.beanValidate(null, null);
    }

    @Test
    public  void assertsNotEmpty() {
        ValidationHelper.assertsNotEmpty("name", "Toronto");
    }

    @Test(expected = IllegalArgumentException.class)
    public  void assertsNotEmptyWithInvalid() {
        ValidationHelper.assertsNotEmpty("name", "");
    }

    @Test
    public  void assertsNotNull() {
        ValidationHelper.assertsNotNull("name", "hello");
    }

    @Test(expected = IllegalArgumentException.class)
    public  void assertsNotNullWithInvalid() {
        ValidationHelper.assertsNotNull("name", null);
    }

    @Test
    public  void assertsAmount() {
        ValidationHelper.assertsAmount(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public  void assertsAmountWithInvalid() {
        ValidationHelper.assertsAmount(0);
    }

    @Data
    private static class TestObject {

        @NotEmpty
        private String test;
    }
}