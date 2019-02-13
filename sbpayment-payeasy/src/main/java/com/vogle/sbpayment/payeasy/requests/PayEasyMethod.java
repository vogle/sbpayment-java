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

package com.vogle.sbpayment.payeasy.requests;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.client.convert.MultiByteString;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * PayEasy information
 *
 * @author Allan Im
 **/
@Data
public class PayEasyMethod {

    @Pattern(regexp = "[012]")
    @CipherString
    @JacksonXmlProperty(localName = "issue_type")
    private String issueType;

    @Pattern(regexp = "[^ -~\uff61-\uff9f]{1,10}")
    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "last_name")
    private String lastName;

    @Pattern(regexp = "[^ -~\uff61-\uff9f]{1,10}")
    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "first_name")
    private String firstName;

    @Pattern(regexp = "[^[\u30a1-\u30fc]+$]{1,10}")
    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "last_name_kana")
    private String lastNameKana;

    @Pattern(regexp = "[^[\u30a1-\u30fc]+$]{1,10}")
    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "first_name_kana")
    private String firstNameKana;

    @CipherString
    @Pattern(regexp = "[0-9]{3}")
    @JacksonXmlProperty(localName = "first_zip")
    private String firstZip;

    @CipherString
    @Pattern(regexp = "[0-9]{4}")
    @JacksonXmlProperty(localName = "second_zip")
    private String secondZip;

    @Pattern(regexp = "[^ -~\uff61-\uff9f]{1,25}")
    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "add1")
    private String add1;

    @Pattern(regexp = "[^ -~\uff61-\uff9f]{1,25}")
    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "add2")
    private String add2;

    @Pattern(regexp = "[^ -~\uff61-\uff9f]{0,50}")
    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "add3")
    private String add3;

    @Pattern(regexp = "[0-9]{1,11}")
    @CipherString
    @JacksonXmlProperty(localName = "tel")
    private String tel;

    @NotEmpty
    @Email
    @CipherString
    @JacksonXmlProperty(localName = "mail")
    private String mail;

    @NotEmpty
    @CipherString
    @JacksonXmlProperty(localName = "seiyakudate")
    private String seiyakudate;

    @Pattern(regexp = "[OL]")
    @CipherString
    @JacksonXmlProperty(localName = "payeasy_type")
    private String payeasyType;

    @Pattern(regexp = "[PDSAL]")
    @CipherString
    @JacksonXmlProperty(localName = "terminal_value")
    private String terminalValue;

    @Size(max = 4)
    @CipherString
    @JacksonXmlProperty(localName = "pay_csv")
    private String payCsv;

    @Pattern(regexp = "[^[\u30a1-\u30fc\uff10-\uff19\uff41-\uff5a\uff21-\uff3a]+$]{1,24}")
    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "bill_info_kana")
    private String billInfoKana;

    @Pattern(regexp = "[^ -~\uff61-\uff9f]{1,12}")
    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "bill_info")
    private String billInfo;

    @Pattern(regexp = "[^ -~\uff61-\uff9f]{0,25}")
    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "bill_note")
    private String billNote;

    //    @DateTimeFormat(pattern = "yyyyMMddHHmmss")
    @CipherString
    @JacksonXmlProperty(localName = "bill_date")
    private String billDate;


}
