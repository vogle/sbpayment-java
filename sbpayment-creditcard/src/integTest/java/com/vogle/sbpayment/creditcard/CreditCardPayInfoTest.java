package com.vogle.sbpayment.creditcard;

import static com.vogle.sbpayment.creditcard.DefaultCreditCardService.Feature.RETURN_CARD_BRAND;
import static com.vogle.sbpayment.creditcard.DefaultCreditCardService.Feature.RETURN_CUSTOMER_INFO;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.vogle.sbpayment.client.SpsResult;
import com.vogle.sbpayment.client.params.Item;
import com.vogle.sbpayment.client.params.PaymentInfo;
import com.vogle.sbpayment.creditcard.params.ByCreditCard;
import com.vogle.sbpayment.creditcard.params.CardInfoResponseType;
import com.vogle.sbpayment.creditcard.responses.CardAuthorizeResponse;
import com.vogle.sbpayment.creditcard.responses.CardTranLookupMethodInfo;
import com.vogle.sbpayment.creditcard.responses.CardTranLookupMethodInfo.PayMethodInfoDetail;
import com.vogle.sbpayment.creditcard.responses.CardTranLookupResponse;
import com.vogle.sbpayment.creditcard.responses.CommitStatus;
import com.vogle.sbpayment.creditcard.responses.PaymentStatus;

/**
 * Tests for {@link CreditCardService} to get payment info
 *
 * @author Allan Im
 **/
public class CreditCardPayInfoTest extends AbstractSettings {

    private CreditCardService service;

    @Before
    public void init() {
        service = new DefaultCreditCardService(client(), RETURN_CARD_BRAND, RETURN_CUSTOMER_INFO);
    }

    @Test
    public void payLumpSumAndNumberLower4() throws Exception {
        PaymentInfo paymentInfo = getDefaultPaymentInfo();
        ByCreditCard creditCard = getDefaultPayCreditCard();

        // test pay
        SpsResult<CardTranLookupResponse> lookup = testPay(paymentInfo, creditCard, CardInfoResponseType.LOWER4);

        PayMethodInfoDetail detail = lookup.getBody().getPayMethodInfo().getPayMethodInfoDetail();
        assertThat(detail).isNotNull();
        assertThat(detail.getCcNumber()).isNotEmpty();
        assertThat(detail.getCcNumber().startsWith("****")).isTrue();
        assertThat(detail.getCcNumber().endsWith("3312")).isTrue();
        assertThat(detail.getCcExpiration()).isEqualTo("202412");
        assertThat(detail.getDealingsType()).isEqualTo(DealingsType.LUMP_SUM.code());
        assertThat(detail.getDivideTimes()).isNullOrEmpty();

    }

    @Test
    public void payInstallmentAndNumberMask() throws Exception {
        PaymentInfo paymentInfo = getDefaultPaymentInfo();
        ByCreditCard creditCard = getDefaultPayCreditCard();
        creditCard.setDealingsType(DealingsType.INSTALLMENT);
        creditCard.setDivideTimes(3);

        // test pay
        SpsResult<CardTranLookupResponse> lookup = testPay(paymentInfo, creditCard, CardInfoResponseType.All_MASK);

        PayMethodInfoDetail detail = lookup.getBody().getPayMethodInfo().getPayMethodInfoDetail();
        assertThat(detail).isNotNull();
        assertThat(detail.getCcNumber()).isNotEmpty();
        assertThat(detail.getCcNumber().startsWith("****")).isTrue();
        assertThat(detail.getCcNumber().endsWith("****")).isTrue();
        assertThat(detail.getCcExpiration()).isEqualTo("202412");
        assertThat(detail.getDealingsType()).isEqualTo(DealingsType.INSTALLMENT.code());
        assertThat(detail.getDivideTimes()).isEqualTo("003");

    }

    @Test
    public void payBonusLumpSumAndNumberMask() throws Exception {
        PaymentInfo paymentInfo = getDefaultPaymentInfo();
        ByCreditCard creditCard = getDefaultPayCreditCard();
        creditCard.setDealingsType(DealingsType.BONUS_LUMP_SUM);
        creditCard.setDivideTimes(3);

        // test pay
        SpsResult<CardTranLookupResponse> lookup = testPay(paymentInfo, creditCard, CardInfoResponseType.All_MASK);

        PayMethodInfoDetail detail = lookup.getBody().getPayMethodInfo().getPayMethodInfoDetail();
        assertThat(detail).isNotNull();
        assertThat(detail.getCcNumber()).isNotEmpty();
        assertThat(detail.getCcNumber().startsWith("****")).isTrue();
        assertThat(detail.getCcNumber().endsWith("****")).isTrue();
        assertThat(detail.getCcExpiration()).isEqualTo("202412");
        assertThat(detail.getDealingsType()).isEqualTo(DealingsType.BONUS_LUMP_SUM.code());
        assertThat(detail.getDivideTimes()).isNullOrEmpty();

    }

    @Test
    public void payRevolvingAndNumberMask() throws Exception {
        PaymentInfo paymentInfo = getDefaultPaymentInfo();
        ByCreditCard creditCard = getDefaultPayCreditCard();
        creditCard.setDealingsType(DealingsType.REVOLVING);
        creditCard.setDivideTimes(3);

        // test pay
        SpsResult<CardTranLookupResponse> lookup = testPay(paymentInfo, creditCard, CardInfoResponseType.All_MASK);

        PayMethodInfoDetail detail = lookup.getBody().getPayMethodInfo().getPayMethodInfoDetail();
        assertThat(detail).isNotNull();
        assertThat(detail.getCcNumber()).isNotEmpty();
        assertThat(detail.getCcNumber().startsWith("****")).isTrue();
        assertThat(detail.getCcNumber().endsWith("****")).isTrue();
        assertThat(detail.getCcExpiration()).isEqualTo("202412");
        assertThat(detail.getDealingsType()).isEqualTo(DealingsType.REVOLVING.code());
        assertThat(detail.getDivideTimes()).isNullOrEmpty();

    }

    @Test
    public void payAddItems() throws Exception {
        PaymentInfo paymentInfo = getDefaultPaymentInfo();
        ByCreditCard creditCard = getDefaultPayCreditCard();

        paymentInfo.setAmount(0);
        paymentInfo.setTax(0);

        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Item item = Item.builder()
                    .itemId("ITEM_ID_" + i).itemName("ID" + i).itemCount(3).itemAmount(108).itemTax(0).build();
            items.add(item);
            paymentInfo.setAmount(paymentInfo.getAmount() + item.getItemAmount());
        }
        paymentInfo.setItems(items);

        // test pay
        SpsResult<CardTranLookupResponse> lookup = testPay(paymentInfo, creditCard, CardInfoResponseType.NONE);

        PayMethodInfoDetail detail = lookup.getBody().getPayMethodInfo().getPayMethodInfoDetail();
        assertThat(detail).isNull();

    }

    private PaymentInfo getDefaultPaymentInfo() {
        return PaymentInfo.builder()
                .customerCode("TEST_CUSTOMER")
                .orderId(orderNo())
                .itemId("ORDERITEMID")
                .itemName("日本語")
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

    private SpsResult<CardTranLookupResponse> testPay(PaymentInfo paymentInfo, ByCreditCard creditCard,
                                                      CardInfoResponseType responseType) {

        // authorize
        SpsResult<CardAuthorizeResponse> authorize = service.authorize(paymentInfo, creditCard);
        String transactionId = authorize.getBody().getTrackingId();

        // commit
        service.commit(transactionId);

        // lookup
        SpsResult<CardTranLookupResponse> lookup = service.lookup(transactionId, responseType);

        assertThat(lookup).isNotNull();
        assertThat(lookup.getStatus()).isEqualTo(200);
        assertThat(lookup.getBody()).isNotNull();
        assertThat(lookup.getBody().isSuccess()).isTrue();
        assertThat(lookup.getBody().getSpsTransactionId()).isNotBlank();

        CardTranLookupMethodInfo cardInfo = lookup.getBody().getPayMethodInfo();
        assertThat(cardInfo).isNotNull();
        assertThat(cardInfo.getCcCompanyCode()).isNotEmpty();
        assertThat(cardInfo.mapCreditCardBrand()).isEqualTo(CreditCardBrand.VISA);
        assertThat(cardInfo.getRecognizedNo()).isNotEmpty();
        assertThat(cardInfo.mapCommitStatus()).isEqualTo(CommitStatus.COMMIT);
        assertThat(cardInfo.mapPaymentStatus()).isEqualTo(PaymentStatus.AUTHORIZED);

        return lookup;
    }
}
