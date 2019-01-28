package com.vogle.sbpayment.client;

import com.vogle.sbpayment.client.receivers.SpsReceivedData;

/**
 * Softbank Payment Receiver
 *
 * @author Allan Im
 */
public interface SpsReceiver {

    /**
     * Receive XML
     *
     * @param xml               The XML to convert
     * @param receivedDataClass The Class to be converted
     * @return The received data Object
     * @throws InvalidAccessException This Exception is occurred if The receivers is NOT valid
     */
    <T extends SpsReceivedData> T receive(String xml, Class<T> receivedDataClass) throws InvalidAccessException;

    /**
     * Make a success result
     *
     * @param featureId SPS Feature ID
     * @return Result XML
     */
    String resultSuccessful(String featureId);

    /**
     * Make a failure result
     *
     * @param featureId    SPS Feature ID
     * @param errorMessage Error message to forward
     * @return Result XML
     */
    String resultFailed(String featureId, String errorMessage);


}
