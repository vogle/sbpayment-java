package com.vogle.sbpayment.client;

import com.vogle.sbpayment.client.convert.SpsDataConverter;
import com.vogle.sbpayment.client.receivers.ReceptionResult;
import com.vogle.sbpayment.client.receivers.SpsReceivedData;

import org.apache.http.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements for {@link SpsReceiver}
 *
 * @author Allan Im
 */
public class DefaultSpsReceiver implements SpsReceiver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SbpaymentSettings settings;
    private final DefaultSpsMapper mapper;

    public DefaultSpsReceiver(SbpaymentSettings settings) {
        Asserts.notNull(settings, "The Settings");
        SpsValidator.beanValidate(settings);

        this.settings = settings;
        this.mapper = new DefaultSpsMapper(settings);
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
        if (!settings.getMerchantId().equals(receivedData.getMerchantId())) {
            throw new InvalidAccessException("The merchant id is wrong: " + receivedData.getMerchantId());
        }

        // Check service id
        if (!settings.getServiceId().equals(receivedData.getServiceId())) {
            throw new InvalidAccessException("The service id is wrong: " + receivedData.getServiceId());
        }

        // Check hash code
        String hashcode = SpsDataConverter.makeSpsHashCode(receivedData, settings.getHashKey(), settings.getCharset());
        if (!hashcode.equals(receivedData.getSpsHashcode())) {
            throw new InvalidAccessException("The hashcode is wrong: " + receivedData.getSpsHashcode());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("SPS Receiver object : {}", receivedData);
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
