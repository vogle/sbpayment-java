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

import java.nio.charset.Charset;
import java.util.Properties;
import java.util.TimeZone;

/**
 * Softbank payment<br/>
 * It has Mapper, Client & Receiver
 *
 * @author Allan Im
 */
public class DefaultSbpayment implements Sbpayment {

    private final SpsConfig config;

    private SpsMapper mapper;
    private SpsClient client;
    private SpsReceiver receiver;

    /**
     * Create Sbpayment
     */
    public DefaultSbpayment() {
        this.config = new SpsConfig();
    }

    /**
     * Create Sbpayment from properties
     */
    public DefaultSbpayment(Properties properties) {
        this.config = SpsConfig.from(properties);
    }

    /**
     * Softbank payment data charset, Default is "Shift_JIS".<br/>
     * 基本"Shift_JIS"で使用
     */
    public void setCharset(String charset) {
        this.config.setCharset(Charset.forName(charset));
    }

    /**
     * Softbank Payment Server TimeZone, Default is "JST".<br/>
     * "JST"を使用
     */
    public void setTimeZone(String timeZone) {
        this.config.setTimeZone(TimeZone.getTimeZone(timeZone));
    }

    /**
     * API Service URL. <br/>
     * API サビースの接続先
     */
    public void setApiUrl(String apiUrl) {
        this.config.setApiUrl(apiUrl);
    }

    /**
     * Softbank Payment Information
     *
     * @param merchantId        MerchantId from Softbank Payment.
     * @param serviceId         ServiceId from Softbank Payment.
     * @param basicAuthId       Basic authentication ID.
     * @param basicAuthPassword Basic authentication password.
     */
    public void setCredentials(String merchantId, String serviceId, String basicAuthId, String basicAuthPassword) {
        this.config.setMerchantId(merchantId);
        this.config.setServiceId(serviceId);
        this.config.setBasicAuthId(basicAuthId);
        this.config.setBasicAuthPassword(basicAuthPassword);
    }

    /**
     * Hash key, ハッシュキー
     */
    public void setHashKey(String hashKey) {
        this.config.setHashKey(hashKey);
    }

    /**
     * Allowable time on request.(Second)<br/>
     * リクエスト時の許容時間(秒)
     */
    public void setAllowableSecondOnRequest(int allowableSecondOnRequest) {
        this.config.setAllowableSecondOnRequest(allowableSecondOnRequest);
    }

    /**
     * If set enable Cipher, It have to set 'desKey' & 'desInitKey'<br/>
     * 3DES 暗号化使用
     */
    public void enableCipher() {
        this.config.setCipherEnabled(true);
    }

    /**
     * If set disable Cipher <br/>
     * 3DES 暗号化使用しない
     */
    public void disableCipher() {
        this.config.setCipherEnabled(false);
    }

    /**
     * 3DES cipher
     *
     * @param desKey     3DES cipher key. / 3DES 暗号化キー
     * @param desInitKey 3DES initialization key. / 3DES 初期化キー
     */
    public void setDesEncryptKey(String desKey, String desInitKey) {
        this.config.setDesKey(desKey);
        this.config.setDesInitKey(desInitKey);
    }


    /**
     * Gets made getMapper
     *
     * @return SpsMapper
     */
    @Override
    public SpsMapper getMapper() {
        if (mapper == null) {
            mapper = new DefaultSpsMapper(config.getCipherInfo());
        }
        return new DefaultSpsMapper(config.getCipherInfo());
    }

    /**
     * Gets made getClient
     *
     * @return SpsClient
     */
    @Override
    public SpsClient getClient() {
        if (client == null) {
            client = new DefaultSpsClient(config.getClientInfo(), getMapper());
        }
        return client;
    }

    /**
     * Gets made getReceiver
     *
     * @return SpsReceiver
     */
    @Override
    public SpsReceiver getReceiver() {
        if (receiver == null) {
            receiver = new DefaultSpsReceiver(config.getSpsInfo(), getMapper());
        }
        return receiver;
    }

}
