package com.vogle.gradle.javaproject

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.testing.Test

/**
 * Integration Test Plugin
 *
 * <pre>
 *     Added Tasks
 *     - integrationTest: To execute integration test
 * </pre>
 *
 * @author Allan Im
 *
 */
class IntegrationTestPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.plugins.withType(JavaBasePlugin) {

            project.sourceSets {
                integTest
            }

            project.dependencies {
                integTestImplementation project.sourceSets.main.output
                integTestImplementation project.sourceSets.test.output
                integTestImplementation project.configurations.testImplementation
                integTestRuntime project.configurations.testRuntime
            }

            project.tasks.create('integrationTest', Test) {
                group = 'verification'
                description = 'Runs integration tests.'

                testClassesDirs = project.sourceSets.integTest.output.classesDirs
                classpath = project.sourceSets.integTest.runtimeClasspath

                // always run integration tests after unit tests in order to fail fast
                mustRunAfter project.test
            }

            // Run integration tests during the 'check' lifecycle (which already includes 'test')
            project.check.dependsOn project.integrationTest

            project.sourceSets.integTest.allSource.srcDirs.flatten()*.mkdirs()

        }
    }
}
