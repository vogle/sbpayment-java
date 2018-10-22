package com.vogle.sbpayment.client;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DefaultSpsClient}
 *
 * @author Allan Im
 */
public class SpsHttpClientTest {


    private DefaultSpsClient client(String merchantId, String serviceId, String timeZone) {
        SpsClientSettings settings = new SpsClientSettings();
        settings.setTimeZone(timeZone);
        settings.setMerchantId(merchantId);
        settings.setServiceId(serviceId);

        return new DefaultSpsClient(settings);
    }

    private SpsRequest request() {
        return new SpsRequest() {
            private String merchantId;
            private String serviceId;
            private Integer limitSecond;
            private String requestDate;

            @Override
            public String getId() {
                return null;
            }

            @Override
            public void setMerchantId(String merchantId) {
                this.merchantId = merchantId;
            }

            @Override
            public String getMerchantId() {
                return this.merchantId;
            }

            @Override
            public void setServiceId(String serviceId) {
                this.serviceId = serviceId;
            }

            @Override
            public String getServiceId() {
                return this.serviceId;
            }

            @Override
            public void setRequestDate(String requestDate) {
                this.requestDate = requestDate;
            }

            @Override
            public String getRequestDate() {
                return this.requestDate;
            }

            @Override
            public void setLimitSecond(Integer limitSecond) {
                this.limitSecond = limitSecond;
            }

            @Override
            public Integer getLimitSecond() {
                return this.limitSecond;
            }

            @Override
            public void setSpsHashcode(String spsHashcode) {

            }

            @Override
            public String getSpsHashcode() {
                return null;
            }

            @Override
            public Class responseClass() {
                return null;
            }
        };
    }

    @Test
    public void getSettings() {
        // given
        String merchantId = "VOGLE Labs";
        String serviceId = "Allan Im";
        String timeZone = "JST";

        // when
        DefaultSpsClient client = client(merchantId, serviceId, timeZone);

        // then
        assertThat(client).isNotNull();
        assertThat(client.getSettings()).isNotNull();
        assertThat(client.getSettings().getMerchantId()).isEqualTo(merchantId);
        assertThat(client.getSettings().getTimeZone()).isEqualTo(timeZone);
    }

    @Test
    public void setCommonElements() {

        // given
        String merchantId = "VOGLE Labs";
        String serviceId = "Allan Im";
        String timeZone = "JST";
        DefaultSpsClient client = client(merchantId, serviceId, timeZone);

        // when
        SpsRequest request = request();
//        client.setCommonElementsTo(request);

        // then
        assertThat(request.getMerchantId()).isEqualTo(merchantId);
        assertThat(request.getServiceId()).isEqualTo(serviceId);
        assertThat(request.getLimitSecond()).isEqualTo(client.getSettings().getAllowableSecondOnRequest());
        assertThat(request.getRequestDate()).isNotEmpty();

    }
}