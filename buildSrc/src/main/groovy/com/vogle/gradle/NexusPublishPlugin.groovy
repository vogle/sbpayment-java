package com.vogle.gradle

import nebula.plugin.info.scm.GitScmProvider
import nebula.plugin.info.scm.ScmInfoPlugin
import nebula.plugin.publishing.maven.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.plugins.signing.SigningPlugin

/**
 * Sets Maven Publish plugins
 * <pre>
 *     Include below
 *     - Maven Publish Plugin: https://docs.gradle.org/current/userguide/publishing_maven.htm
 *     - The Signing Plugin: https://docs.gradle.org/current/userguide/signing_plugin.html
 *     - nebula.maven-publish: https://plugins.gradle.org/plugin/nebula.maven-publish
 * </pre>
 *
 * @author Allan Im
 *
 */
class NexusPublishPlugin implements Plugin<Project> {

    static String RELEASES_REPO_KEY = 'nexusReleasesRepo'
    static String SNAPSHOT_REPO_KEY = 'nexusSnapshotRepo'
    static String USERNAME_KEY = 'nexusUsername'
    static String PASSWORD_KEY = 'nexusPassword'

    @Override
    void apply(Project project) {
        // apply gradle maven publish
        project.plugins.apply(org.gradle.api.publish.maven.plugins.MavenPublishPlugin)

        // apply nebula.maven-publish
        project.plugins.with {
            apply MavenNebulaPublishPlugin
            apply MavenBasePublishPlugin
            apply MavenCompileOnlyPlugin
            apply MavenResolvedDependenciesPlugin
            apply MavenDeveloperPlugin
        }

        // set nexus
        project.extensions.create('nexus', NexusPublishPluginExtension)
        project.publishing {
            publications {
                withType(MavenPublication) { MavenPublication publication ->
                    if (!project.state.executed) {
                        project.afterEvaluate { Project p ->
                            configureOrganization(publication, p)
                            configureLicense(publication, p)
                            applySigning(publication, p)
                        }
                    } else {
                        configureOrganization(publication, project)
                        configureLicense(publication, project)
                        applySigning(publication, project)
                    }
                }
            }
        }

        // apply URL
        applyUrl(project)

        // apply repositories
        applyRemoteRepository(project)

        // create Task
        project.tasks.create('publishToNexus') {
            group = 'publishing'
            description = 'Publishes all Maven publications to the Nexus repository.'
            dependsOn project.tasks.withType(PublishToMavenRepository).matching {
                it.repository == project.publishing.repositories.nexus
            }
        }
    }

    def configureOrganization = { MavenPublication publication, Project project ->
        if (project.nexus.orgName) {
            publication.pom.organization {
                name = project.nexus.orgName
                url = project.nexus.orgUrl
            }
        }
    }

    def configureLicense = { MavenPublication publication, Project project ->
        if (NexusPublishPluginExtension.License.APACHE2 == project.nexus.license) {
            publication.pom.licenses {
                license {
                    name = 'The Apache Software License, Version 2.0'
                    url = 'http://www.apache.org/licenses/LICENSE-2.0.txt'
                    distribution = 'repo'
                }
            }
        } else if (NexusPublishPluginExtension.License.MIT == project.nexus.license) {
            publication.pom.licenses {
                license {
                    name = 'MIT License'
                    url = 'http://www.opensource.org/licenses/mit'
                    distribution = 'repo'
                }
            }
        } else if (NexusPublishPluginExtension.License.GNU2 == project.nexus.license) {
            publication.pom.licenses {
                license {
                    name = 'GNU General Public License v2'
                    url = 'http://www.gnu.org/licenses/gpl-2.0.html'
                    distribution = 'repo'
                }
            }
        }
    }

    def applySigning = { MavenPublication publication, Project project ->
        if (project.nexus.sign) {
            project.plugins.apply(SigningPlugin)
            project.signing {
                sign project.publishing.publications.nebula
            }
        }
    }

    def applyUrl = { Project project ->
        project.plugins.withType(ScmInfoPlugin) { ScmInfoPlugin scmInfo ->
            project.publishing {
                publications {
                    withType(MavenPublication) {
                        pom {
                            if (scmInfo.selectedProvider instanceof GitScmProvider) {
                                url = MavenScmPlugin.calculateUrlFromOrigin(scmInfo.extension.origin, project)
                            }
                            scm {
                                url = scmInfo.extension.origin
                            }
                        }
                    }
                }
            }
        }
    }

    def applyRemoteRepository = { Project project ->
        if (project.hasProperty(RELEASES_REPO_KEY) && project.hasProperty(SNAPSHOT_REPO_KEY)) {
            project.publishing {
                repositories {
                    maven {
                        def releasesRepoUrl = project.property(RELEASES_REPO_KEY)
                        def snapshotsRepoUrl = project.property(SNAPSHOT_REPO_KEY)

                        name = 'nexus'
                        url = project.version.endsWith('SNAPSHOT') ? snapshotsRepoUrl : releasesRepoUrl
                        credentials {
                            username = project.property(USERNAME_KEY)
                            password = project.property(PASSWORD_KEY)
                        }
                    }
                }
            }
        } else {
            project.logger.warn('The nexus repositories("nexusReleasesRepo", "nexusSnapshotRepo") MUST be stored ' +
                    'in your gradle.properties file in your users home directory.')
        }
    }
}
