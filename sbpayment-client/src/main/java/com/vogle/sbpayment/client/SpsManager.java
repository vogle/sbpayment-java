package com.vogle.sbpayment.client;

/**
 * Softbank payment Manager<br/>
 * It has Mapper, Client & Receiver
 *
 * @author Allan Im
 */
public interface SpsManager {

    /**
     * Gets made mapper
     *
     * @return SpsMapper
     */
    SpsMapper mapper();

    /**
     * Gets made client
     *
     * @return SpsClient
     */
    SpsClient client();

    /**
     * Gets made receiver
     *
     * @return SpsReceiver
     */
    SpsReceiver receiver();
}
