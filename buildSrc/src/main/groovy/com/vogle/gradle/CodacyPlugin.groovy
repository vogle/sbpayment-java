package com.vogle.gradle

import org.gradle.api.Action
import org.gradle.api.GradleException
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

            // make arguments
            List<String> args = new ArrayList<>()
            args.add("report")
            args.add("-l")
            args.add("Java")

            // find coverage report and setup
            String xmlDest = project.codacy.jacocoReportPath
            if (!xmlDest) {
                project.tasks.withType(JacocoReport).all { report ->
                    xmlDest = report.reports.xml.destination
                }
            }
            if (xmlDest) {
                args.add("-r")
                args.add(xmlDest)
            } else {
                throw new GradleException(
                        "You have to JacocoReport, Either apply the Jacoco plugin or set up a report file")
            }

            // find token and set up
            String token = project.findProperty(CODACY_PROJECT_TOKEN)
            if (token) {
                args.add("-t")
                args.add(token)
            }

            // create task
            project.tasks.create('uploadJacocoToCodacy', JavaExec) {
                it.group = 'reporting'
                it.description = 'Upload Jacoco coverage to Codacy.'

                it.classpath = project.configurations.getByName(CODACY_CONFIGURATION_NAME)
                it.dependsOn project.tasks.withType(JacocoReport).collect()

                it.main = 'com.codacy.CodacyCoverageReporter'
                it.args(args)
            }
        }
    }

}