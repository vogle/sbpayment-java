package com.vogle.gradle.javaproject

import nebula.plugin.info.InfoPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.publish.ivy.IvyPublication
import org.gradle.api.publish.ivy.plugins.IvyPublishPlugin
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Javadoc

/**
 * The Archive Plugins
 * <pre>
 *     Include below
 *     - nebula.info: https://plugins.gradle.org/plugin/nebula.info
 *
 *     Added Tasks
 *     - sourceJar: create a source jar, file name is artifactId-version-sources.jar
 *     - javadocJar: create a javadoc jar, file name is artifactId-version-javadoc.jar
 * </pre>
 *
 * @author Allan Im
 *
 */
class ArchivePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.withType(JavaBasePlugin) {
            project.plugins.apply(InfoPlugin)

            configureSourceJar(project)
            configureJavadocJar(project)
            configurePublishingArtifact(project)

        }
    }

    def configureSourceJar = { Project project ->
        project.tasks.create('sourceJar', Jar) {
            group 'build'
            description = 'Assembles a jar archive containing the project sources.'
            from project.sourceSets.main.allSource
            classifier 'sources'
            extension 'jar'
            dependsOn project.tasks.getByName('classes')
        }
        project.artifacts.add(Dependency.ARCHIVES_CONFIGURATION, project.sourceJar)
    }

    def configureJavadocJar = { Project project ->
        Javadoc javadocTask = (Javadoc) project.tasks.getByName('javadoc')
        project.tasks.create('javadocJar', Jar) {
            group 'build'
            description = 'Assembles a jar archive containing the Javadocs.'
            from javadocTask.destinationDir
            classifier 'javadoc'
            extension 'jar'
            dependsOn javadocTask
        }
        project.artifacts.add(Dependency.ARCHIVES_CONFIGURATION, project.javadocJar)
    }

    def configurePublishingArtifact = { Project project ->
        project.plugins.withType(MavenPublishPlugin) {
            project.publishing {
                publications {
                    withType(MavenPublication) {
                        artifact project.tasks.sourceJar
                        artifact project.tasks.javadocJar
                    }
                }
            }
        }
        project.plugins.withType(IvyPublishPlugin) {
            project.publishing {
                publications {
                    withType(IvyPublication) {
                        artifact(project.tasks.sourceJar) {
                            type 'sources'
                            conf 'sources'
                        }
                        artifact(project.tasks.javadocJar) {
                            type 'sources'
                            conf 'sources'
                        }
                    }
                }
            }
        }
    }

}
