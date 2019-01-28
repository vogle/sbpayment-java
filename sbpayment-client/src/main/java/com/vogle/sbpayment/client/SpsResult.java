package com.vogle.sbpayment.client;

import com.vogle.sbpayment.client.responses.SpsResponse;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * Received data from Softbank Payment
 *
 * @author Allan Im
 **/
@ToString
public class SpsResult<T extends SpsResponse> {

    /**
     * HTTP Status Code
     */
    @Getter
    private final int status;

    /**
     * HTTP Headers
     */
    @Getter
    private final Map<String, String> headers;

    /**
     * HTTP Body
     */
    @Getter
    private final T body;

    public SpsResult() {
        this(999);
    }

    public SpsResult(int status) {
        this(status, null, null);
    }

    /**
     * Create a new {@code HttpEntity} with the given body and headers.
     *
     * @param status  the entity status
     * @param body    the entity body
     * @param headers the entity headers
     */
    public SpsResult(int status, Map<String, String> headers, T body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public boolean isSuccessfulConnection() {
        return 200 == status;
    }

}
