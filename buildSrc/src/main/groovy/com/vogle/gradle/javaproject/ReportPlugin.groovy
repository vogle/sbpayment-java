package com.vogle.gradle.javaproject

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestReport
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport

/**
 * The Report Plugins
 * <pre>
 *     Include below
 *     - The JaCoCo Plugin: https://docs.gradle.org/current/userguide/jacoco_plugin.html
 * </pre>
 *
 * @author Allan Im
 *
 */
class ReportPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        // add generating test report task
        addAllTestRootReportTask(project)

        // add generating jacoco report task
        applyJacoco(project)
        addTaskJacocoCoverageReport(project)
    }

    /**
     * Apply Coverage metrics for Java code by jacoco,
     */
    def applyJacoco = { Project project ->
        project.plugins.apply(JacocoPlugin)
        project.jacoco {
            toolVersion = '0.8.+'
        }
        if (project.hasProperty('jacocoTestReport')) {
            project.jacocoTestReport {
                reports {
                    html.enabled = true
                    xml.enabled = true
                    csv.enabled = false
                }
                executionData = project.fileTree(project.buildDir).include("/jacoco/*.exec")
            }
            project.test.finalizedBy(project.tasks.jacocoTestReport)
            if (project.plugins.hasPlugin(IntegrationTestPlugin)) {
                project.integrationTest.finalizedBy(project.tasks.jacocoTestReport)
            }
        }
    }

    /**
     * Create All tests reports
     */
    def addAllTestRootReportTask = { Project project ->
        if (project == project.rootProject && !project.subprojects.isEmpty()) {
            project.tasks.create('allTestReport', TestReport) {
                description = 'Aggregates test reports of all projects.'
                group 'reporting'

                destinationDir = project.file("$project.buildDir/reports/tests")
                reportOn project.subprojects*.tasks*.withType(Test).collect()
            }
        }
    }

    /**
     * Create Jacoco reports
     */
    def addTaskJacocoCoverageReport = { Project project ->
        if (project == project.rootProject && !project.subprojects.isEmpty()) {
            project.tasks.create('jacocoCoverageReport', JacocoReport) {
                description = 'Generates an aggregate report from all subprojects'
                group = "reporting"

                executionData project.fileTree(project.rootDir.absolutePath).include("**/build/jacoco/*.exec")

                project.subprojects.each {
                    sourceSets it.sourceSets.main
                }

                reports {
                    xml.enabled true
                    html.enabled true
                }
            }
        }
    }
}
