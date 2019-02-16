package com.vogle.sbpayment.payeasy;

import lombok.Data;

/**
 * Online：オンライン（ATM）方式
 *
 * @author Allan Im
 */
@Data
public class OnlineType {

    /**
     * 請求内容漢字、ATM 等に表示されます。（全角）
     */
    private String billInfo;

    /**
     * 請求内容カナ、ATM 等に表示されます。（全角英数カナ）
     */
    private String billInfoKana;

    /**
     * 支払期限、受注日時からデフォルトの支払期限の設定値内での指定が可能です。
     * ウェルネットを利用されている加盟店の場合、支払期限は当日指定が可能です。
     * 本日は「0」を基準として、日数を加算。デフォルトは「３日」
     */
    private int billLimitDay = 3;

}
