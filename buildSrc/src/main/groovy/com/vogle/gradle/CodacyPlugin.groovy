package com.vogle.gradle

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.tasks.JavaExec
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport

/**
 * Upload jacoco coverage to the codacy
 *
 * @author Allan Im
 *
 */
class CodacyPlugin implements Plugin<ProjectInternal> {

    static final String CODACY_PROJECT_TOKEN = "CODACY_PROJECT_TOKEN"
    static final String CODACY_CONFIGURATION_NAME = "codacy"

    @Override
    void apply(ProjectInternal project) {

        project.extensions.create('codacy', CodacyPluginExtension)

        if (!project.state.executed) {
            project.afterEvaluate { Project p ->
                addConfiguration(p)
                addTask(p)
            }
        } else {
            addConfiguration(project)
            addTask(project)
        }
    }

    def addConfiguration = { Project project ->
        Configuration codacyConf = project.getConfigurations().create(CODACY_CONFIGURATION_NAME)
        codacyConf.setVisible(false)
        codacyConf.setTransitive(true)
        codacyConf.setDescription("The Codacy use to get library.")
        codacyConf.defaultDependencies(new Action<DependencySet>() {
            @Override
            void execute(DependencySet dependencies) {
                dependencies.add(project.getDependencies().create(
                        "com.codacy:codacy-coverage-reporter:${project.codacy.toolVersion}"))
            }
        })
    }

    def addTask = { Project project ->
        project.plugins.withType(JacocoPlugin) {

            String token = project.findProperty(CODACY_PROJECT_TOKEN)
            project.tasks.create('uploadJacocoToCodacy', JavaExec) {
                group = 'reporting'
                description = 'Upload Jacoco coverage to Codacy.'

                classpath = project.configurations.getByName(CODACY_CONFIGURATION_NAME)
                dependsOn project.tasks.withType(JacocoReport).collect()

                main = 'com.codacy.CodacyCoverageReporter'

                if (token) {
                    args = [
                            "report",
                            "-l", "Java",
                            "-r", project.codacy.jacocoReportPath,
                            "-t", token
                    ]
                } else {
                    args = [
                            "report",
                            "-l", "Java",
                            "-r", project.codacy.jacocoReportPath
                    ]
                }
            }
        }
    }

}
