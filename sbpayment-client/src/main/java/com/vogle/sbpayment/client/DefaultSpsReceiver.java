package com.vogle.sbpayment.client;

import com.vogle.sbpayment.client.convert.SpsDataConverter;
import com.vogle.sbpayment.client.receivers.ReceptionResult;
import com.vogle.sbpayment.client.receivers.SpsReceivedData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements for {@link SpsReceiver}
 *
 * @author Allan Im
 */
public class DefaultSpsReceiver implements SpsReceiver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String merchantId;
    private final String serviceId;

    private final SpsMapper mapper;

    public DefaultSpsReceiver(String merchantId, String serviceId, SpsMapper mapper) {
        this.merchantId = merchantId;
        this.serviceId = serviceId;
        this.mapper = mapper;
    }

    /**
     * Receive the XML
     *
     * @param xml               The XML to convert
     * @param receivedDataClass The Class to be converted
     * @return The received data Object
     * @throws InvalidAccessException This Exception is occurred if The receivers is NOT valid
     */
    @Override
    public <T extends SpsReceivedData> T receive(String xml, Class<T> receivedDataClass)
            throws InvalidAccessException {

        if (logger.isDebugEnabled()) {
            logger.debug("SPS Receiver data : {}", xml);
        }

        T receivedData = mapper.xmlToObject(xml, receivedDataClass);

        // Check merchant id
        if (!merchantId.equals(receivedData.getMerchantId())) {
            throw new InvalidAccessException("The merchant id is wrong: " + receivedData.getMerchantId());
        }

        // Check service id
        if (!serviceId.equals(receivedData.getServiceId())) {
            throw new InvalidAccessException("The service id is wrong: " + receivedData.getServiceId());
        }

        // Check hash code
        String hashcode = SpsDataConverter.makeSpsHashCode(receivedData, mapper.getHashKey(), mapper.getCharset());
        if (!hashcode.equals(receivedData.getSpsHashcode())) {
            throw new InvalidAccessException("The hashcode is wrong: " + receivedData.getSpsHashcode());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("SPS Received data : {}", receivedData);
        }

        return receivedData;
    }

    /**
     * Make a success result
     *
     * @param featureId SPS Feature ID
     * @return Result XML
     */
    @Override
    public String resultSuccessful(String featureId) {
        SpsValidator.assertsNotEmpty(featureId, "featureId");
        return mapper.objectToXml(new ReceptionResult(featureId));
    }

    /**
     * Make a failure result
     *
     * @param featureId    SPS Feature ID
     * @param errorMessage Error message to forward
     * @return Result XML
     */
    @Override
    public String resultFailed(String featureId, String errorMessage) {
        SpsValidator.assertsNotEmpty(featureId, "featureId");
        SpsValidator.assertsNotEmpty(errorMessage, "errorMessage");
        return mapper.objectToXml(new ReceptionResult(featureId, errorMessage));
    }

}
