package com.vogle.gradle.javaproject

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Java Library plugins
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
        project.plugins.apply(JavaPlugin)
        project.plugins.apply(org.gradle.api.plugins.JavaLibraryPlugin)
    }
}
