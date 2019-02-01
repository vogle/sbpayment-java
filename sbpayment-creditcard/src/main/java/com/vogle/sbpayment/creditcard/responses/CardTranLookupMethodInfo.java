package com.vogle.sbpayment.creditcard.responses;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.creditcard.CreditCardBrand;
import com.vogle.sbpayment.creditcard.DealingsType;

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

    /**
     * Gets a {@link CreditCardBrand}
     */
    public CreditCardBrand getCreditCardBrand() {
        return CreditCardBrand.brand(cardbrandCode);
    }

    /**
     * Gets a {@link CommitStatus}
     */
    public CommitStatus getCommitStatusType() {
        return CommitStatus.status(commitStatus);
    }

    /**
     * Gets a {@link PaymentStatus}
     */
    public PaymentStatus getPaymentStatusType() {
        return PaymentStatus.status(paymentStatus);
    }

    /**
     * Payment Method details
     */
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

        /**
         * Gets a {@link DealingsType}
         * @return DealingsType
         */
        public DealingsType getDealingsTypeEnum() {
            return DealingsType.type(dealingsType);
        }
    }
}
