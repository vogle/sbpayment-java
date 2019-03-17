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

package com.vogle.sbpayment.payeasy;

import com.vogle.sbpayment.client.InvalidAccessException;
import com.vogle.sbpayment.client.Sbpayment;
import com.vogle.sbpayment.client.SpsMapper;
import com.vogle.sbpayment.client.SpsResult;
import com.vogle.sbpayment.client.convert.SpsDataConverter;
import com.vogle.sbpayment.client.params.PaymentInfo;
import com.vogle.sbpayment.client.requests.RequestHelper;
import com.vogle.sbpayment.payeasy.params.PayEasy;
import com.vogle.sbpayment.payeasy.params.TerminalValue;
import com.vogle.sbpayment.payeasy.receivers.PayEasyDepositInfo;
import com.vogle.sbpayment.payeasy.receivers.PayEasyDepositReceived;
import com.vogle.sbpayment.payeasy.receivers.PayEasyExpiredCancelReceived;
import com.vogle.sbpayment.payeasy.responses.PayEasyPaymentResponse;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DefaultPayEasyPayment}
 *
 * @author Allan Im
 **/
public class PayEasyTest {

    private static int BILL_LIMIT_DAY = 5;

    private String merchantId;
    private String serviceId;

    private PayEasyPayment payment;
    private PayEasyPayment paymentWithLink;
    private SpsMapper mapper;

    public PayEasyTest() {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(System.getProperty("user.dir") + File.separator
                + "../config/it2.properties"));
        } catch (IOException ignored) {
            // ignored
        }

        merchantId = (String) p.get("sbpayment.merchantId");
        serviceId = (String) p.get("sbpayment.serviceId");

        Sbpayment sbpayment = Sbpayment.newInstance(p);
        mapper = sbpayment.getMapper();

        OnlineType onlineType = new OnlineType();
        onlineType.setBillInfo("株式会社");
        onlineType.setBillInfoKana("カブシキガイシャ");
        onlineType.setBillLimitDay(BILL_LIMIT_DAY);
        payment = PayEasyPayment.newInstance(sbpayment, onlineType);

        LinkType linkType = new LinkType();
        linkType.setPayCsv("9999");
        linkType.setBillLimitDay(BILL_LIMIT_DAY);
        paymentWithLink = PayEasyPayment.newInstance(sbpayment, linkType);
    }

    @Test
    public void payment() {

        // when
        PaymentInfo paymentInfo = getDefaultPaymentInfo();
        PayEasy payEasy = getPayEasy();
        SpsResult<PayEasyPaymentResponse> payment = this.payment.payment(paymentInfo, payEasy);

        // then
        assertThat(payment).isNotNull();
        assertThat(payment.getStatus()).isEqualTo(200);
        assertThat(payment.getHeaders()).isNotNull();
        assertThat(payment.getBody()).isNotNull();
        assertThat(payment.getBody().isSuccess()).isTrue();
        assertThat(payment.getBody().getSpsTransactionId()).isNotEmpty();

        PayEasyPaymentResponse res = payment.getBody();
        assertThat(res.getTrackingId()).isNotBlank();
        assertThat(res.getPayEasyInfo()).isNotNull();
        assertThat(res.getPayEasyInfo().getInvoiceNo()).isNotEmpty();
        assertThat(res.getPayEasyInfo().getBillDate())
            .isEqualTo(RequestHelper.dateOnly(res.getDate(), BILL_LIMIT_DAY));
        assertThat(res.getPayEasyInfo().getSkno()).isNotEmpty();
        assertThat(res.getPayEasyInfo().getCustNumber()).isNotEmpty();
    }

    @Test
    public void paymentWithLinkType() {

        // when
        PaymentInfo paymentInfo = getDefaultPaymentInfo();
        PayEasy payEasy = getPayEasy();
        SpsResult<PayEasyPaymentResponse> payment = this.paymentWithLink.payment(paymentInfo, payEasy);

        // then
        assertThat(payment).isNotNull();
        assertThat(payment.getStatus()).isEqualTo(200);
        assertThat(payment.getHeaders()).isNotNull();
        assertThat(payment.getBody()).isNotNull();
        //        assertThat(payment.getBody().isSuccess()).isTrue();
        //        assertThat(payment.getBody().getSpsTransactionId()).isNotEmpty();
        //
        //        PayEasyPaymentResponse res = payment.getBody();
        //        assertThat(res.getTrackingId()).isNotBlank();
        //        assertThat(res.getPayEasyInfo()).isNotNull();
        //        assertThat(res.getPayEasyInfo().getInvoiceNo()).isNotEmpty();
        //        assertThat(res.getPayEasyInfo().getBillDate())
        //                .isEqualTo(RequestHelper.dateOnly(res.getDate(), BILL_LIMIT_DAY));
        //        assertThat(res.getPayEasyInfo().getSkno()).isNotEmpty();
        //        assertThat(res.getPayEasyInfo().getCustNumber()).isNotEmpty();
    }

    @Test
    public void convertDepositReceived() throws Exception {
        // given test data
        PayEasyDepositReceived temp = new PayEasyDepositReceived();
        temp.setId("NT01-00103-703");
        temp.setMerchantId(merchantId);
        temp.setServiceId(serviceId);
        temp.setSpsTransactionId("xxxxxxxxxxxxxxxxxxxxx");
        temp.setTrackingId("1234567890");
        temp.setRecDatetime("20171010");

        PayEasyDepositInfo depositInfo = new PayEasyDepositInfo();
        depositInfo.setType("1");
        depositInfo.setAmount("1000");
        depositInfo.setAmountTotal("1000");
        depositInfo.setMail("email@vogle.com");
        temp.setDepositInfo(depositInfo);

        temp.setRequestDate("20171010101010");
        temp.setSpsHashcode(SpsDataConverter.makeSpsHashCode(temp, mapper.getHashKey(), mapper.getCharset()));

        // given xml
        String xml = mapper.objectToXml(temp);

        // when
        PayEasyDepositReceived received = payment.receiveDeposit(xml);

        // then
        assertThat(received.getId()).isEqualTo(temp.getId());
        assertThat(received.getMerchantId()).isEqualTo(temp.getMerchantId());
        assertThat(received.getServiceId()).isEqualTo(temp.getServiceId());
        assertThat(received.getSpsTransactionId()).isEqualTo(temp.getSpsTransactionId());
        assertThat(received.getTrackingId()).isEqualTo(temp.getTrackingId());
        assertThat(received.getRecDatetime()).isEqualTo(temp.getRecDatetime());
        assertThat(received.getRequestDate()).isEqualTo(temp.getRequestDate());
        assertThat(received.getDepositInfo().getType()).isEqualTo("1");
        assertThat(received.getDepositInfo().getAmount()).isEqualTo("1000");
        assertThat(received.getDepositInfo().getAmountTotal()).isEqualTo("1000");
        assertThat(received.getDepositInfo().getMail()).isEqualTo("email@vogle.com");

    }

    @Test(expected = InvalidAccessException.class)
    public void convertDepositReceivedWithWrongId() throws Exception {
        // given test data
        PayEasyDepositReceived temp = new PayEasyDepositReceived();
        temp.setId("NT01-00103-701");
        temp.setMerchantId(merchantId);
        temp.setServiceId(serviceId);
        temp.setSpsTransactionId("x");
        temp.setTrackingId("123");
        temp.setRecDatetime("20191010");

        temp.setRequestDate("20191010101010");
        temp.setSpsHashcode(SpsDataConverter.makeSpsHashCode(temp, mapper.getHashKey(), mapper.getCharset()));

        // given xml
        String xml = mapper.objectToXml(temp);

        // when
        payment.receiveDeposit(xml);

    }

    @Test
    public void successDepositResponse() {
        String response = payment.successDeposit();
        assertThat(response).isNotEmpty()
            .contains("<?xml version=\"1.0\" encoding=\"" + mapper.getCharset() + "\"?>")
            .contains("<sps-api-response id=\"NT01-00103-703\">")
            .contains("<res_result>OK</res_result>");
    }

    @Test
    public void failDepositResponse() throws Exception {
        String errMsg = "ERROR Message";
        String response = payment.failDeposit(errMsg);
        assertThat(response).isNotEmpty()
            .contains("<?xml version=\"1.0\" encoding=\"" + mapper.getCharset() + "\"?>")
            .contains("<sps-api-response id=\"NT01-00103-703\">")
            .contains("<res_result>NG</res_result>")
            .contains("<res_err_msg>" + Base64.getEncoder().encodeToString(errMsg.getBytes(mapper.getCharset()))
                + "</res_err_msg>");
    }

    @Test
    public void convertExpiredReceived() throws Exception {
        // test data
        PayEasyExpiredCancelReceived temp = new PayEasyExpiredCancelReceived();
        temp.setId("NT01-00104-703");
        temp.setMerchantId(merchantId);
        temp.setServiceId(serviceId);
        temp.setSpsTransactionId("xxxxxxxxxxxxxxxxxxxxx");
        temp.setTrackingId("1234567890");
        temp.setRecDatetime("20181010");
        temp.setRequestDate("20181010101010");

        temp.setSpsHashcode(SpsDataConverter.makeSpsHashCode(temp, mapper.getHashKey(), mapper.getCharset()));

        // request data
        String xml = mapper.objectToXml(temp);

        PayEasyExpiredCancelReceived request = payment.receiveExpiredCancel(xml);

        assertThat(request.getId()).isEqualTo(temp.getId());
        assertThat(request.getMerchantId()).isEqualTo(temp.getMerchantId());
        assertThat(request.getSpsTransactionId()).isEqualTo(temp.getSpsTransactionId());
        assertThat(request.getServiceId()).isEqualTo(temp.getServiceId());
        assertThat(request.getTrackingId()).isEqualTo(temp.getTrackingId());
        assertThat(request.getRecDatetime()).isEqualTo(temp.getRecDatetime());
        assertThat(request.getRequestDate()).isEqualTo(temp.getRequestDate());

    }

    @Test(expected = InvalidAccessException.class)
    public void convertExpiredReceivedWithWrongId() throws Exception {
        // test data
        PayEasyExpiredCancelReceived temp = new PayEasyExpiredCancelReceived();
        temp.setId("NT01-00104-701");
        temp.setMerchantId(merchantId);
        temp.setServiceId(serviceId);
        temp.setSpsTransactionId("xxxxxxxxxxxxxxxxxxxxx");
        temp.setTrackingId("1234");
        temp.setRecDatetime("20191110");
        temp.setRequestDate("20191010101011");

        temp.setSpsHashcode(SpsDataConverter.makeSpsHashCode(temp, mapper.getHashKey(), mapper.getCharset()));

        // request data
        String xml = mapper.objectToXml(temp);

        // when
        payment.receiveExpiredCancel(xml);

    }


    @Test
    public void successExpiredResponse() {
        String response = payment.successExpiredCancel();
        assertThat(response).isNotEmpty()
            .contains("<?xml version=\"1.0\" encoding=\"" + mapper.getCharset() + "\"?>")
            .contains("<sps-api-response id=\"NT01-00104-703\">")
            .contains("<res_result>OK</res_result>");
    }

    @Test
    public void failExpiredResponse() {
        String errMsg = "エラー発生";
        String response = payment.failExpiredCancel(errMsg);
        assertThat(response).isNotEmpty()
            .contains("<?xml version=\"1.0\" encoding=\"" + mapper.getCharset() + "\"?>")
            .contains("<sps-api-response id=\"NT01-00104-703\">")
            .contains("<res_result>NG</res_result>")
            .contains("<res_err_msg>" + Base64.getEncoder().encodeToString(errMsg.getBytes(mapper.getCharset()))
                + "</res_err_msg>");
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

    private PayEasy getPayEasy() {
        return PayEasy.builder()
            .lastName("株式").firstName("会社")
            .lastNameKana("カブシキ").firstNameKana("カイシャ")
            .tel("08011112222")
            .mail("email@vogle.com")
            .terminalValue(TerminalValue.PC)
            .build();
    }

    private String orderNo() {
        Random random = new Random();
        return "VO" + dayPattern() + random.nextInt(99_999);
    }

    private String dayPattern() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        long time = System.currentTimeMillis();
        return fmt.format(new Date(time));
    }
}
