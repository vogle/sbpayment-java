package com.vogle.sbpayment.creditcard;

import static com.vogle.sbpayment.creditcard.DefaultCreditCardService.Feature.RETURN_CARD_BRAND;
import static com.vogle.sbpayment.creditcard.DefaultCreditCardService.Feature.RETURN_CUSTOMER_INFO;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.vogle.sbpayment.client.SpsResult;
import com.vogle.sbpayment.client.params.PaymentInfo;
import com.vogle.sbpayment.creditcard.params.ByCreditCard;
import com.vogle.sbpayment.creditcard.params.ByTrackingInfo;
import com.vogle.sbpayment.creditcard.responses.CardAuthorizeResponse;
import com.vogle.sbpayment.creditcard.responses.CardTranLookupMethodInfo;
import com.vogle.sbpayment.creditcard.responses.CardTranLookupResponse;
import com.vogle.sbpayment.creditcard.responses.CommitStatus;
import com.vogle.sbpayment.creditcard.responses.DefaultResponse;
import com.vogle.sbpayment.creditcard.responses.PaymentStatus;
import com.vogle.sbpayment.creditcard.responses.TransactionStatus;

/**
 * Tests for {@link CreditCardService} Transaction services
 *
 * @author Allan Im
 **/
public class CreditCardTransactionFlowTest extends AbstractSettings {

    private CreditCardService service;

    @Before
    public void init() {
        service = new DefaultCreditCardService(client(), RETURN_CARD_BRAND, RETURN_CUSTOMER_INFO);
    }

    private String authorize() {
        // given
        PaymentInfo paymentInfo = getDefaultPaymentInfo();
        ByCreditCard creditCard = getDefaultPayCreditCard();

        // when
        SpsResult<CardAuthorizeResponse> authorize = service.authorize(paymentInfo, creditCard);

        // then
        assertAuthorize(authorize);
        return authorize.getBody().getTrackingId();
    }

    private PaymentStatus lookup(String transactionId) {
        // when
        SpsResult<CardTranLookupResponse> lookup = service.lookup(transactionId);

        // then
        assertLookup(lookup);
        return lookup.getBody().getPayMethodInfo().mapPaymentStatus();
    }

    private void cancel(String transactionId) {
        // when
        SpsResult<DefaultResponse> cancel = service.cancel(transactionId);

        // then
        assertTransaction(cancel);
    }

    @Test
    public void authorizeThenCancel() {
        String transactionId = authorize();

        // lookup
        PaymentStatus status1 = lookup(transactionId);
        assertThat(status1).isEqualTo(PaymentStatus.AUTHORIZED);

        // cancel
        cancel(transactionId);

        // lookup
        PaymentStatus status2 = lookup(transactionId);
        assertThat(status2).isEqualTo(PaymentStatus.CANCELED);
    }

    @Test
    public void authThenCaptureThenRefund() {
        String transactionId = authorize();

        // capture
        SpsResult<DefaultResponse> capture = service.capture(transactionId);
        assertTransaction(capture);

        // refund
        SpsResult<DefaultResponse> refund = service.refund(transactionId);
        assertTransaction(refund);

        // lookup
        PaymentStatus status = lookup(transactionId);
        assertThat(status).isEqualTo(PaymentStatus.REFUNDED);
    }

    @Test
    public void authThenPartialCaptureThenRefund() {
        String transactionId = authorize();

        // capture
        SpsResult<DefaultResponse> capture = service.capture(transactionId, 500);
        assertTransaction(capture);

        // refund
        SpsResult<DefaultResponse> refund = service.cancel(transactionId);
        assertTransaction(refund);

        // lookup
        PaymentStatus status = lookup(transactionId);
        assertThat(status).isEqualTo(PaymentStatus.REFUNDED);
    }


    @Test
    public void authThenCaptureThenPartialRefund() {
        String transactionId = authorize();

        // capture
        SpsResult<DefaultResponse> capture = service.capture(transactionId);
        assertTransaction(capture);

        // refund
        SpsResult<DefaultResponse> refund100 = service.refund(transactionId, 100);
        assertTransaction(refund100);

        // lookup
        PaymentStatus status = lookup(transactionId);
        assertThat(status).isEqualTo(PaymentStatus.REFUNDED);
    }


    @Test
    public void authThenPartialCaptureThenPartialRefund() {
        String transactionId = authorize();

        // capture
        SpsResult<DefaultResponse> capture = service.capture(transactionId, 500);
        assertTransaction(capture);

        // refund
        SpsResult<DefaultResponse> refund = service.refund(transactionId, 200);
        assertTransaction(refund);

        // lookup
        PaymentStatus status = lookup(transactionId);
        assertThat(status).isEqualTo(PaymentStatus.REFUNDED);
    }

    @Test
    public void reauthorizeThenCancel() {
        String orgTransactionId = authorize();

        // reauthorize
        PaymentInfo newPaymentInfo = getDefaultPaymentInfo();
        ByTrackingInfo existingCard = ByTrackingInfo.builder()
                .trackingId(orgTransactionId)
                .build();

        SpsResult<CardAuthorizeResponse> reauthorize = service.reauthorize(newPaymentInfo, existingCard);
        String transactionId = reauthorize.getBody().getTrackingId();
        assertAuthorize(reauthorize);

        // lookup
        assertThat(lookup(transactionId)).isEqualTo(PaymentStatus.AUTHORIZED);

        // cancel
        cancel(transactionId);

        // lookup
        assertThat(lookup(transactionId)).isEqualTo(PaymentStatus.CANCELED);
    }

    @Test
    public void reauthorizeWithOptionThenCancel() {
        String orgTransactionId = authorize();

        // reauthorize
        PaymentInfo newPaymentInfo = getDefaultPaymentInfo();
        ByTrackingInfo existingCard = ByTrackingInfo.builder()
                .trackingId(orgTransactionId)
                .dealingsType(DealingsType.INSTALLMENT)
                .divideTimes(5)
                .build();

        SpsResult<CardAuthorizeResponse> reauthorize = service.reauthorize(newPaymentInfo, existingCard);
        assertAuthorize(reauthorize);

        String transactionId = reauthorize.getBody().getTrackingId();

        // lookup
        assertThat(lookup(transactionId)).isEqualTo(PaymentStatus.AUTHORIZED);

        // cancel
        cancel(transactionId);

        // lookup
        assertThat(lookup(transactionId)).isEqualTo(PaymentStatus.CANCELED);
    }

    private PaymentInfo getDefaultPaymentInfo() {
        return PaymentInfo.builder()
                .customerCode("TEST_CUSTOMER")
                .orderId(orderNo())
                .itemId("ORDERITEMID")
                .itemName("日本語カタカナ")
                .amount(1080)
                .tax(80)
                .build();
    }

    private ByCreditCard getDefaultPayCreditCard() {
        return ByCreditCard.builder()
                .number("4123450131003312")
                .expiration("202412")
                .securityCode("123")
                .build();
    }

    private void assertAuthorize(SpsResult<CardAuthorizeResponse> authorize) {
        assertCommon(authorize);

        assertThat(authorize.getBody().getTrackingId()).isNotBlank();
        assertThat(authorize.getBody().getPayMethodInfo()).isNotNull();
        assertThat(authorize.getBody().getPayMethodInfo().mapCreditCardBrand())
                .isEqualTo(CreditCardBrand.VISA);
    }

    private void assertTransaction(SpsResult<DefaultResponse> tran) {
        assertCommon(tran);
    }

    private void assertLookup(SpsResult<CardTranLookupResponse> lookup) {
        assertCommon(lookup);
        assertThat(lookup.getBody().mapTransactionStatus()).isEqualTo(TransactionStatus.NORMAL);

        CardTranLookupMethodInfo cardInfo = lookup.getBody().getPayMethodInfo();
        assertThat(cardInfo.mapCreditCardBrand()).isEqualTo(CreditCardBrand.VISA);
        assertThat(cardInfo.mapCommitStatus()).isEqualTo(CommitStatus.UNPROCESSED);
    }

    private void assertCommon(SpsResult<?> result) {
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getHeaders()).isNotNull();
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getResult()).isEqualToIgnoringCase("OK");
        assertThat(result.getBody().getSpsTransactionId()).isNotBlank();
    }
}
