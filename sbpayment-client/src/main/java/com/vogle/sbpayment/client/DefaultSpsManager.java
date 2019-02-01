package com.vogle.sbpayment.client;

/**
 * Softbank payment Manager<br/>
 * It has Mapper, Client & Receiver
 *
 * @author Allan Im
 */
public class DefaultSpsManager implements SpsManager{

    private final SpsMapper mapper;
    private final SpsClient client;
    private final SpsReceiver receiver;

    /**
     * Create Manager
     *
     * @param config The Softbank Payment Configuration
     */
    public DefaultSpsManager(SpsConfig config) {
        this.mapper = new DefaultSpsMapper(config);
        this.client = new DefaultSpsClient(config, this.mapper);
        this.receiver = new DefaultSpsReceiver(config, this.mapper);
    }

    /**
     * Gets made mapper
     *
     * @return SpsMapper
     */
    @Override
    public SpsMapper mapper() {
        return this.mapper;
    }

    /**
     * Gets made client
     *
     * @return SpsClient
     */
    @Override
    public SpsClient client() {
        return this.client;
    }

    /**
     * Gets mad receiver
     *
     * @return SpsReceiver
     */
    @Override
    public SpsReceiver receiver() {
        return this.receiver;
    }
}
