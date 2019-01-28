package com.vogle.sbpayment.payeasy;

import com.vogle.sbpayment.client.InvalidAccessException;
import com.vogle.sbpayment.client.SpsClient;
import com.vogle.sbpayment.client.SpsReceiver;
import com.vogle.sbpayment.client.SpsResult;
import com.vogle.sbpayment.client.SpsValidator;
import com.vogle.sbpayment.client.params.PaymentInfo;
import com.vogle.sbpayment.payeasy.params.IssueType;
import com.vogle.sbpayment.payeasy.params.PayEasy;
import com.vogle.sbpayment.payeasy.params.TerminalValue;
import com.vogle.sbpayment.payeasy.receivers.PayEasyDepositReceived;
import com.vogle.sbpayment.payeasy.receivers.PayEasyExpiredCancelReceived;
import com.vogle.sbpayment.payeasy.requests.PayEasyMethod;
import com.vogle.sbpayment.payeasy.requests.PayEasyPaymentRequest;
import com.vogle.sbpayment.payeasy.responses.PayEasyPaymentResponse;

import static com.vogle.sbpayment.client.requests.RequestMapper.avoidNull;
import static com.vogle.sbpayment.client.requests.RequestMapper.dateOnly;
import static com.vogle.sbpayment.client.requests.RequestMapper.mapItem;

/**
 * Implements for {@link PayEasyService}
 *
 * @author Allan Im
 **/
public class DefaultPayEasyService implements PayEasyService {

    private final String depositId = "NT01-00103-703";
    private final String expiredId = "NT01-00104-703";

    private final SpsClient client;
    private final SpsReceiver receiver;

    private PayEasyType type;
    private String payCsv;
    private String billInfo;
    private String billInfoKana;
    private int billLimitDay = 3;

    /**
     * Make LinkType Pay-Easy
     *
     * @param client   The SpsClient
     * @param receiver The SpsReceiver
     * @param payCsv   金融機関コード、情報リンク方式の場合のみ必須です。ただし、電算システムを利用の場合は不要です。
     */
    public DefaultPayEasyService(SpsClient client, SpsReceiver receiver, String payCsv) {
        this.type = PayEasyType.LINK;
        this.client = client;
        this.receiver = receiver;
        this.payCsv = payCsv;
    }

    /**
     * Make OnlineType Pay-Easy
     *
     * @param client       The SpsClient
     * @param receiver     The SpsReceiver
     * @param billInfo     請求内容漢字、ATM 等に表示されます。（全角）
     * @param billInfoKana 請求内容カナ、ATM 等に表示されます。（全角英数カナ）
     */
    public DefaultPayEasyService(SpsClient client, SpsReceiver receiver, String billInfo, String billInfoKana) {
        this.type = PayEasyType.ONLINE;
        this.client = client;
        this.receiver = receiver;
        this.billInfo = billInfo;
        this.billInfoKana = billInfoKana;
    }

    /**
     * 支払期限、受注日時からデフォルトの支払期限の設定値内での指定が可能です。
     * ウェルネットを利用されている加盟店の場合、支払期限は当日指定が可能です。
     * 本日は「0」を基準として、日数を加算。デフォルトは「３日」
     */
    public void updateBillLimitDay(int billLimitDay) {
        this.billLimitDay = billLimitDay;
    }


    @Override
    public SpsResult<PayEasyPaymentResponse> payment(PaymentInfo paymentInfo, PayEasy payEasy) {
        SpsValidator.beanValidate(paymentInfo, payEasy);

        PayEasyPaymentRequest request = client.newRequest(PayEasyPaymentRequest.class);

        if (PayEasyType.LINK.equals(type)) {
            SpsValidator.assertsNotNull("payCsv", payCsv);
        }
        if (PayEasyType.ONLINE.equals(type)) {
            SpsValidator.assertsNotNull("billInfo", billInfo);
            SpsValidator.assertsNotNull("billInfoKana", billInfoKana);
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
        payInfo.setIssueType(avoidIssueType(payEasy.getIssueType()));
        payInfo.setLastName(payEasy.getLastName());
        payInfo.setFirstName(payEasy.getFirstName());
        payInfo.setLastNameKana(payEasy.getLastNameKana());
        payInfo.setFirstNameKana(payEasy.getFirstNameKana());
        payInfo.setFirstZip(avoidNull(payEasy.getFirstZip(), "000"));
        payInfo.setSecondZip(avoidNull(payEasy.getSecondZip(), "0000"));
        payInfo.setAdd1(avoidNull(payEasy.getAdd1(), "\u3000"));
        payInfo.setAdd2(avoidNull(payEasy.getAdd2(), "\u3000"));
        payInfo.setAdd3(payEasy.getAdd3());
        payInfo.setTel(payEasy.getTel());
        payInfo.setMail(payEasy.getMail());
        payInfo.setSeiyakudate(dateOnly(request.getRequestDate()));
        payInfo.setPayeasyType(this.type.code());
        payInfo.setTerminalValue(avoidTerminalValue(payEasy.getTerminalValue()));
        payInfo.setPayCsv(this.payCsv);
        payInfo.setBillInfoKana(this.billInfoKana);
        payInfo.setBillInfo(this.billInfo);
        payInfo.setBillDate(dateOnly(request.getRequestDate(), this.billLimitDay));

        request.setPayEasyMethod(payInfo);


        return client.execute(request);
    }

    @Override
    public PayEasyDepositReceived receiveDeposit(String xml) throws InvalidAccessException {
        SpsValidator.assertsNotEmpty(xml, "xml");

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

    private String avoidIssueType(IssueType origin) {
        return (origin == null) ? IssueType.ISSUED.code() : origin.code();
    }

    private String avoidTerminalValue(TerminalValue origin) {
        return (origin == null) ? TerminalValue.PC.code() : origin.code();
    }

}
