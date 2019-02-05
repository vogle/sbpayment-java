package com.vogle.gradle
/**
 * Sets Nexus information
 *
 * @author Allan Im
 *
 */
class NexusPublishPluginExtension {

    String orgName
    String orgUrl

    Boolean sign = Boolean.TRUE

    License license

    enum License {
        APACHE2, MIT, GNU2
    }
}
