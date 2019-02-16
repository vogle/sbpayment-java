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
import com.vogle.sbpayment.client.SpsClient;
import com.vogle.sbpayment.client.SpsReceiver;
import com.vogle.sbpayment.client.SpsResult;
import com.vogle.sbpayment.client.ValidationHelper;
import com.vogle.sbpayment.client.params.PaymentInfo;
import com.vogle.sbpayment.client.requests.RequestHelper;
import com.vogle.sbpayment.payeasy.params.IssueType;
import com.vogle.sbpayment.payeasy.params.PayEasy;
import com.vogle.sbpayment.payeasy.params.TerminalValue;
import com.vogle.sbpayment.payeasy.receivers.PayEasyDepositReceived;
import com.vogle.sbpayment.payeasy.receivers.PayEasyExpiredCancelReceived;
import com.vogle.sbpayment.payeasy.requests.PayEasyMethod;
import com.vogle.sbpayment.payeasy.requests.PayEasyPaymentRequest;
import com.vogle.sbpayment.payeasy.responses.PayEasyPaymentResponse;

import static com.vogle.sbpayment.client.requests.RequestHelper.dateOnly;
import static com.vogle.sbpayment.client.requests.RequestHelper.mapItem;

/**
 * Implements for {@link PayEasyPayment}
 *
 * @author Allan Im
 **/
public class DefaultPayEasyPayment implements PayEasyPayment {

    private final String depositId = "NT01-00103-703";
    private final String expiredId = "NT01-00104-703";

    private final SpsClient client;
    private final SpsReceiver receiver;

    private final PayEasyType type;

    private String payCsv;
    private String billInfo;
    private String billInfoKana;
    private int billLimitDay;

    /**
     * Make LinkType Pay-Easy
     *
     * @param sbpayment The {@link Sbpayment}
     * @param linkType  LinkType info：情報リンク方式（インターネットでの支払い）
     */
    protected DefaultPayEasyPayment(Sbpayment sbpayment, LinkType linkType) {
        this.type = PayEasyType.LINK;
        this.client = sbpayment.getClient();
        this.receiver = sbpayment.getReceiver();
        this.payCsv = linkType.getPayCsv();
        this.billLimitDay = linkType.getBillLimitDay();
    }

    /**
     * Make OnlineType Pay-Easy
     *
     * @param sbpayment  The {@link Sbpayment}
     * @param onlineType OnlineType info：オンライン（ATM）方式
     */
    protected DefaultPayEasyPayment(Sbpayment sbpayment, OnlineType onlineType) {
        this.type = PayEasyType.ONLINE;
        this.client = sbpayment.getClient();
        this.receiver = sbpayment.getReceiver();
        this.billInfo = onlineType.getBillInfo();
        this.billInfoKana = onlineType.getBillInfoKana();
        this.billLimitDay = onlineType.getBillLimitDay();
    }

    @Override
    public SpsResult<PayEasyPaymentResponse> payment(PaymentInfo paymentInfo, PayEasy payEasy) {
        ValidationHelper.beanValidate(paymentInfo, payEasy);

        PayEasyPaymentRequest request = client.newRequest(PayEasyPaymentRequest.class);

        if (PayEasyType.LINK.equals(type)) {
            ValidationHelper.assertsNotNull("payCsv", payCsv);
        }
        if (PayEasyType.ONLINE.equals(type)) {
            ValidationHelper.assertsNotNull("billInfo", billInfo);
            ValidationHelper.assertsNotNull("billInfoKana", billInfoKana);
        }

        // payment info
        request.setCustCode(paymentInfo.getCustomerCode());
        request.setOrderId(paymentInfo.getOrderId());
        request.setItemId(paymentInfo.getItemId());
        request.setItemName(paymentInfo.getItemName());
        request.setTax(paymentInfo.getTax());
        request.setAmount(paymentInfo.getAmount());
        request.setFree1(paymentInfo.getFree1());
        request.setFree2(paymentInfo.getFree2());
        request.setFree3(paymentInfo.getFree3());
        request.setOrderRowno(paymentInfo.getOrderRowNo());

        // item details
        request.setPayDetails(mapItem(paymentInfo.getItems()));

        // pay-easy info
        PayEasyMethod payInfo = new PayEasyMethod();
        payInfo.setIssueType(avoidNull(payEasy.getIssueType()));
        payInfo.setLastName(payEasy.getLastName());
        payInfo.setFirstName(payEasy.getFirstName());
        payInfo.setLastNameKana(payEasy.getLastNameKana());
        payInfo.setFirstNameKana(payEasy.getFirstNameKana());
        payInfo.setFirstZip(RequestHelper.avoidNull(payEasy.getFirstZip(), "000"));
        payInfo.setSecondZip(RequestHelper.avoidNull(payEasy.getSecondZip(), "0000"));
        payInfo.setAdd1(RequestHelper.avoidNull(payEasy.getAdd1(), "\u3000"));
        payInfo.setAdd2(RequestHelper.avoidNull(payEasy.getAdd2(), "\u3000"));
        payInfo.setAdd3(payEasy.getAdd3());
        payInfo.setTel(payEasy.getTel());
        payInfo.setMail(payEasy.getMail());
        payInfo.setSeiyakudate(dateOnly(request.getRequestDate()));
        payInfo.setPayeasyType(this.type.code());
        payInfo.setTerminalValue(avoidNull(payEasy.getTerminalValue()));
        payInfo.setPayCsv(this.payCsv);
        payInfo.setBillInfoKana(this.billInfoKana);
        payInfo.setBillInfo(this.billInfo);
        payInfo.setBillDate(dateOnly(request.getRequestDate(), this.billLimitDay));

        request.setPayEasyMethod(payInfo);


        return client.execute(request);
    }

    @Override
    public PayEasyDepositReceived receiveDeposit(String xml) throws InvalidAccessException {
        ValidationHelper.assertsNotEmpty(xml, "xml");

        PayEasyDepositReceived request = receiver.receive(xml, PayEasyDepositReceived.class);

        // Check feature id
        if (!depositId.equals(request.getId())) {
            throw new InvalidAccessException("It is not deposit process : " + request.getId());
        }

        return request;
    }

    @Override
    public String successDeposit() {
        return receiver.resultSuccessful(depositId);
    }

    @Override
    public String failDeposit(String errorMessage) {
        return receiver.resultFailed(depositId, errorMessage);
    }

    @Override
    public PayEasyExpiredCancelReceived receiveExpiredCancel(String xml) throws InvalidAccessException {
        PayEasyExpiredCancelReceived request = receiver.receive(xml, PayEasyExpiredCancelReceived.class);

        // Check feature id
        if (!expiredId.equals(request.getId())) {
            throw new InvalidAccessException("It is not deposit process : " + request.getId());
        }

        return request;
    }

    @Override
    public String successExpiredCancel() {
        return receiver.resultSuccessful(expiredId);
    }

    @Override
    public String failExpiredCancel(String errorMessage) {
        return receiver.resultFailed(expiredId, errorMessage);
    }

    private String avoidNull(IssueType origin) {
        return (origin == null) ? IssueType.ISSUED.code() : origin.code();
    }

    private String avoidNull(TerminalValue origin) {
        return (origin == null) ? TerminalValue.PC.code() : origin.code();
    }

}
