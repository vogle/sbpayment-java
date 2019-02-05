package com.vogle.gradle

import io.franzbecker.gradle.lombok.LombokPlugin
import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile
import org.gradle.plugins.ide.eclipse.EclipsePlugin
import org.gradle.plugins.ide.idea.IdeaPlugin

/**
 * Sets Java Library plugins
 * <pre>
 *     Include below
 *     - The Java Library Plugin: https://docs.gradle.org/current/userguide/java_library_plugin.html
 *     - io.spring.dependency-management: https://plugins.gradle.org/plugin/io.spring.dependency-management
 *     - io.franzbecker.gradle-lombok: https://plugins.gradle.org/plugin/io.franzbecker.gradle-lombok
 * </pre>
 *
 * @author Allan Im
 *
 */
class JavaLibraryPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        // apply java-library
        project.plugins.apply(org.gradle.api.plugins.JavaLibraryPlugin)
        project.tasks.withType(AbstractCompile).each {
            it.options.encoding = 'UTF-8'
        }

        if (BasePlugin.BuildType.DEBUG == project.ext.env) {
            project.plugins.withType(IdeaPlugin) {
                project.idea.module {
                    downloadJavadoc = true
                    downloadSources = true
                }
            }
            project.plugins.withType(EclipsePlugin) {
                project.eclipse.classpath {
                    downloadSources = true
                    downloadJavadoc = true
                }
            }
        }

        project.javadoc {
            source = project.sourceSets.main.allJava
            title = project.description
            failOnError = false
            options.locale = "en"
            options.charSet = "UTF-8"
            options.encoding = "UTF-8"
            options.use = true
            options.author = true
            options.version = false
            options.addStringOption('Xdoclint:none', '-quiet')

            if (JavaVersion.current().isJava9Compatible()) {
                options.addBooleanOption('html5', true)
            }
        }

        // apply io.spring.dependency-management
        project.plugins.apply(DependencyManagementPlugin)

        // apply io.franzbecker.gradle-lombok
        project.plugins.apply(LombokPlugin)
        project.lombok {
            version = '1.+'
        }
    }
}
