package com.vogle.sbpayment.client;

import com.vogle.sbpayment.client.requests.SpsRequest;
import com.vogle.sbpayment.client.responses.SpsResponse;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link DefaultSpsClient}
 *
 * @author Allan Im
 */
public class SpsClientTest {

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
    public void execute() throws IOException {

        // given
        String merchantId = "VOGLE Labs";
        String serviceId = "Allan Im";
        SpsClient client = client(merchantId, serviceId);

        HttpResponse mockResponse = mock(HttpResponse.class);
        when(mockResponse.getStatusLine()).thenReturn(getStatusLine(200));
        when(mockResponse.getAllHeaders()).thenReturn(new Header[0]);
        when(mockResponse.getEntity()).thenReturn(
                new StringEntity("<sps-api-response id=\"ST02-00101-101\">\n" +
                        "<res_result>OK</res_result>\n" +
                        "<res_sps_transaction_id>X1234567890123456789012345678901</res_sps_transaction_id>\n" +
                        "<res_process_date>20120620144317</res_process_date>\n" +
                        "<res_date>20120620144318</res_date>\n" +
                        "</sps-api-response>"));
        HttpClient mockClient = mock(HttpClient.class);
        when(mockClient.execute(Mockito.any())).thenReturn(mockResponse);
        Whitebox.setInternalState(client, "httpClient", mockClient);

        // when
        TestRequest request = client.newRequest(TestRequest.class);
        SpsResult<TestResponse> responseEntity = client.execute(request);

        // then
        assertThat(responseEntity.getStatus()).isEqualTo(200);

    }

    @Test(expected = XmlMappingException.class)
    public void executeThenXmlMappingException() throws IOException {

        // given
        String merchantId = "VOGLE Labs";
        String serviceId = "Allan Im";
        SpsClient client = client(merchantId, serviceId);

        HttpResponse mockResponse = mock(HttpResponse.class);
        when(mockResponse.getStatusLine()).thenReturn(getStatusLine(200));
        when(mockResponse.getAllHeaders()).thenReturn(new Header[0]);
        when(mockResponse.getEntity()).thenReturn(new StringEntity(""));
        HttpClient mockClient = mock(HttpClient.class);
        when(mockClient.execute(Mockito.any())).thenReturn(mockResponse);
        Whitebox.setInternalState(client, "httpClient", mockClient);

        // when
        client.execute(client.newRequest(TestRequest.class));

    }

    @Test(expected = IllegalStateException.class)
    public void executeThenIllegalStateException() throws IOException {

        // given
        String merchantId = "VOGLE Labs";
        String serviceId = "Allan Im";
        SpsClient client = client(merchantId, serviceId);

        HttpClient mockClient = mock(HttpClient.class);
        when(mockClient.execute(Mockito.any())).thenThrow(IOException.class);
        Whitebox.setInternalState(client, "httpClient", mockClient);

        // when
        client.execute(client.newRequest(TestRequest.class));

    }

    @Test
    public void executeFailed() throws IOException {
        executeFailed(401);
        executeFailed(403);
        executeFailed(404);
        executeFailed(500);
        executeFailed(503);
        executeFailed(405);
    }

    private void executeFailed(final int status) throws IOException {
        // given
        String merchantId = "VOGLE Labs";
        String serviceId = "Allan Im";
        SpsClient client = client(merchantId, serviceId);

        HttpResponse mockResponse = mock(HttpResponse.class);
        when(mockResponse.getStatusLine()).thenReturn(getStatusLine(status));
        HttpClient mockClient = mock(HttpClient.class);
        when(mockClient.execute(Mockito.any())).thenReturn(mockResponse);
        Whitebox.setInternalState(client, "httpClient", mockClient);

        // when
        TestRequest request = client.newRequest(TestRequest.class);
        SpsResult<TestResponse> responseEntity = client.execute(request);

        // then
        assertThat(responseEntity.getStatus()).isEqualTo(status);
    }

    private StatusLine getStatusLine(int status) {
        return new StatusLine() {
            @Override
            public ProtocolVersion getProtocolVersion() {
                return null;
            }

            @Override
            public int getStatusCode() {
                return status;
            }

            @Override
            public String getReasonPhrase() {
                return null;
            }
        };
    }

    private Sbpayment settings(String merchantId, String serviceId) {
        return Sbpayment.newInstance(SpsConfig.builder()
                .apiUrl("http://vogle.com")
                .merchantId(merchantId)
                .serviceId(serviceId)
                .basicAuthId("BASIC_ID")
                .basicAuthPassword("BASIC_PASS")
                .hashKey("HASH_KEY")
                .build());
    }

    private SpsClient client(String merchantId, String serviceId) {
        Sbpayment sbpayment = settings(merchantId, serviceId);
        return sbpayment.getClient();
    }

    public static class TestRequest implements SpsRequest<TestResponse> {

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
        public Class<TestResponse> responseClass() {
            return TestResponse.class;
        }
    }

    public static class TestResponse implements SpsResponse {

        @Override
        public String getId() {
            return null;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public String getResult() {
            return "OK";
        }

        @Override
        public String getErrCode() {
            return null;
        }

        @Override
        public String getSpsTransactionId() {
            return null;
        }
    }
}