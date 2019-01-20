package com.vogle.sbpayment.client;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DefaultSpsClient}
 *
 * @author Allan Im
 */
public class DefaultSpsClientTest {


    @Test
    public void client() {
        // given
        String merchantId = "VOGLE Labs";
        String serviceId = "Allan Im";

        // when
        DefaultSpsClient client = client(merchantId, serviceId);

        // then
        assertThat(client).isNotNull();
    }

    @Test
    public void newRequest() {

        // given
        String merchantId = "VOGLE Labs";
        String serviceId = "Allan Im";
        DefaultSpsClient client = client(merchantId, serviceId);

        // when
        TestRequest request = client.newRequest(TestRequest.class);

        // then
        assertThat(request.getMerchantId()).isEqualTo(merchantId);
        assertThat(request.getServiceId()).isEqualTo(serviceId);
        assertThat(request.getLimitSecond()).isEqualTo(600);
        assertThat(request.getRequestDate()).isNotEmpty();

    }

    @Test(expected = IllegalArgumentException.class)
    public void newRequestWithFail() {

        // given
        String merchantId = "VOGLE Labs";
        String serviceId = "Allan Im";
        DefaultSpsClient client = client(merchantId, serviceId);

        // when
        client.newRequest(SpsRequest.class);

    }

    @Test
    public void execute() throws Exception {

        // given
        String merchantId = "VOGLE Labs";
        String serviceId = "Allan Im";
        DefaultSpsClient client = client(merchantId, serviceId);

        // when
        TestRequest request = client.newRequest(TestRequest.class);
        SpsResult responseEntity = client.execute(request);

        // then
        assertThat(responseEntity.getStatus()).isEqualTo(405);

    }

    private SpsClientSettings settings(String merchantId, String serviceId) {
        SpsClientSettings settings = new SpsClientSettings();
        settings.setMerchantId(merchantId);
        settings.setServiceId(serviceId);
        settings.setBasicAuthId("BASIC_ID");
        settings.setBasicAuthPassword("BASIC_PASS");
        settings.setHashKey("HASH_KEY");
        settings.setApiUrl("http://vogle.com");

        return settings;
    }

    private DefaultSpsClient client(String merchantId, String serviceId) {
        return new DefaultSpsClient(settings(merchantId, serviceId));
    }

    public static class TestRequest implements SpsRequest<SpsResponse> {

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
        public Class<SpsResponse> responseClass() {
            return null;
        }
    }
}