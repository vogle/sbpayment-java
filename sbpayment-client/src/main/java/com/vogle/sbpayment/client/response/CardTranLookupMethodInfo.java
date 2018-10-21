package com.vogle.sbpayment.client.response;

import com.vogle.sbpayment.client.CreditCardBrand;
import com.vogle.sbpayment.client.DealingsType;
import com.vogle.sbpayment.client.convert.CipherString;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Lookup Credit-Card information
 *
 * @author Allan Im
 **/
@Data
public class CardTranLookupMethodInfo {

    @CipherString
    @JacksonXmlProperty(localName = "cc_company_code")
    private String ccCompanyCode;

    @CipherString
    @JacksonXmlProperty(localName = "cardbrand_code")
    private String cardbrandCode;

    @CipherString
    @JacksonXmlProperty(localName = "recognized_no")
    private String recognizedNo;

    @CipherString
    @JacksonXmlProperty(localName = "commit_status")
    private String commitStatus;

    @CipherString
    @JacksonXmlProperty(localName = "payment_status")
    private String paymentStatus;

    @CipherString
    @JacksonXmlProperty(localName = "res_pay_method_info_detail")
    private PayMethodInfoDetail payMethodInfoDetail;

    @Data
    public static class PayMethodInfoDetail {

        @CipherString
        @JacksonXmlProperty(localName = "cc_number")
        private String ccNumber;

        @CipherString
        @JacksonXmlProperty(localName = "cc_expiration")
        private String ccExpiration;

        @CipherString
        @JacksonXmlProperty(localName = "dealings_type")
        private String dealingsType;

        @CipherString
        @JacksonXmlProperty(localName = "divide_times")
        private String divideTimes;

        public DealingsType mapDealingsType() {
            return DealingsType.type(dealingsType);
        }
    }

    public CreditCardBrand mapCreditCardBrand() {
        return CreditCardBrand.brand(cardbrandCode);
    }

    public CommitStatus mapCommitStatus() {
        return CommitStatus.status(commitStatus);
    }

    public PaymentStatus mapPaymentStatus() {
        return PaymentStatus.status(paymentStatus);
    }
}
