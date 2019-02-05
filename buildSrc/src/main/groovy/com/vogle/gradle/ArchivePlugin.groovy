package com.vogle.gradle

import nebula.plugin.info.InfoPlugin
import nebula.plugin.publishing.publications.JavadocJarPlugin
import nebula.plugin.publishing.publications.SourceJarPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.plugins.JavaPlugin

/**
 * The Archive Plugins
 * <pre>
 *     Include below
 *     - nebula.info: https://plugins.gradle.org/plugin/nebula.info
 *     - nebula.source-jar: https://plugins.gradle.org/plugin/nebula.source-jar
 *     - nebula.javadoc-jar: https://plugins.gradle.org/plugin/nebula.javadoc-jar
 * </pre>
 *
 * @author Allan Im
 *
 */
class ArchivePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.withType(JavaPlugin) {
            project.extensions.create('archive', ArchivePluginExtension)
            project.plugins.apply(InfoPlugin)

            if (!project.state.executed) {
                project.afterEvaluate {
                    configureSourceJar(project)
                    configureJavadocJar(project)
                }
            } else {
                configureSourceJar(project)
                configureJavadocJar(project)
            }

        }
    }

    def configureSourceJar = { Project project ->
        if (project.archive.sources) {
            project.plugins.apply(SourceJarPlugin)
            project.artifacts.add(Dependency.ARCHIVES_CONFIGURATION, project.sourceJar)
        }
    }

    def configureJavadocJar = { Project project ->
        if (project.archive.javadoc) {
            project.plugins.apply(JavadocJarPlugin)
            project.artifacts.add(Dependency.ARCHIVES_CONFIGURATION, project.javadocJar)
        }
    }

}
