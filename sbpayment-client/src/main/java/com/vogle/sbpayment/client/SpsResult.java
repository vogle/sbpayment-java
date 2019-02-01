package com.vogle.sbpayment.client;

import java.util.Map;

import lombok.Getter;
import lombok.ToString;

import com.vogle.sbpayment.client.responses.SpsResponse;

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


    /**
     * Construct a default result
     */
    public SpsResult() {
        this(999);
    }

    /**
     * Construct a result with response status code
     *
     * @param status The HTTP Status code
     */
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

    /**
     * To return true if it is successful response after connecting to sbpayment.
     */
    public boolean isSuccessfulConnection() {
        return 200 == status;
    }

    /**
     * To return true if the response data is successful
     */
    public boolean isSuccess() {
        return isSuccessfulConnection() && body.isSuccess();
    }
}
