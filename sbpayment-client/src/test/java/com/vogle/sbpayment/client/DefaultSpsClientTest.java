package com.vogle.sbpayment.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.vogle.sbpayment.client.requests.SpsRequest;
import com.vogle.sbpayment.client.responses.SpsResponse;

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
        SpsClient client = client(merchantId, serviceId);

        // then
        assertThat(client).isNotNull();
    }

    @Test
    public void newRequest() {

        // given
        String merchantId = "VOGLE Labs";
        String serviceId = "Allan Im";
        SpsClient client = client(merchantId, serviceId);

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
        SpsClient client = client(merchantId, serviceId);

        // when
        client.newRequest(SpsRequest.class);

    }

    @Test
    public void execute() throws Exception {

        // given
        String merchantId = "VOGLE Labs";
        String serviceId = "Allan Im";
        SpsClient client = client(merchantId, serviceId);

        // when
        TestRequest request = client.newRequest(TestRequest.class);
        SpsResult responseEntity = client.execute(request);

        // then
        assertThat(responseEntity.getStatus()).isEqualTo(405);

    }

    private SpsConfig settings(String merchantId, String serviceId) {
        return SpsConfig.builder()
                .apiUrl("http://vogle.com")
                .merchantId(merchantId)
                .serviceId(serviceId)
                .basicAuthId("BASIC_ID")
                .basicAuthPassword("BASIC_PASS")
                .hashKey("HASH_KEY")
                .build();
    }

    private SpsClient client(String merchantId, String serviceId) {
        SpsManager manager = new DefaultSpsManager(settings(merchantId, serviceId));
        return manager.client();
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
        public String getMerchantId() {
            return this.merchantId;
        }

        @Override
        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        @Override
        public String getServiceId() {
            return this.serviceId;
        }

        @Override
        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        @Override
        public String getRequestDate() {
            return this.requestDate;
        }

        @Override
        public void setRequestDate(String requestDate) {
            this.requestDate = requestDate;
        }

        @Override
        public Integer getLimitSecond() {
            return this.limitSecond;
        }

        @Override
        public void setLimitSecond(Integer limitSecond) {
            this.limitSecond = limitSecond;
        }

        @Override
        public String getSpsHashcode() {
            return null;
        }

        @Override
        public void setSpsHashcode(String spsHashcode) {

        }

        @Override
        public Class<SpsResponse> responseClass() {
            return null;
        }
    }
}