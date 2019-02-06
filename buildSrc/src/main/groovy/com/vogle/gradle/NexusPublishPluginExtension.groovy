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

    String releasesRepo = "https://oss.sonatype.org/service/local/staging/deploy/maven2"
    String snapshotRepo = "https://oss.sonatype.org/content/repositories/snapshots"

    License license

    enum License {
        APACHE2, MIT, GNU2
    }
}
