package com.vogle.sbpayment.client;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * Receiving data from Softbank Payment
 *
 * @author Allan Im
 **/
@ToString
public class SpsResponseEntity<T extends SpsResponse> {

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

    public SpsResponseEntity() {
        this(999);
    }

    public SpsResponseEntity(int status) {
        this(status, null, null);
    }

    /**
     * Create a new {@code HttpEntity} with the given body and headers.
     *
     * @param status  the entity status
     * @param body    the entity body
     * @param headers the entity headers
     */
    public SpsResponseEntity(int status, Map<String, String> headers, T body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public boolean isSuccessfulConnection() {
        return 200 == status;
    }

}
