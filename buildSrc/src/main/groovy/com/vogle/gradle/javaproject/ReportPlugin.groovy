package com.vogle.gradle.javaproject

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestReport
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoMerge
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
        addTaskTestRootReport(project)

        // add generating jacoco report task
        applyJacoco(project)
        addTaskJacocoRootReport(project)
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
            }
            project.test.finalizedBy(project.tasks.jacocoTestReport)
        }

    }

    /**
     * Create All tests reports
     */
    def addTaskTestRootReport = { Project project ->
        if (project == project.rootProject && !project.subprojects.isEmpty()) {
            project.tasks.create('testRootReport', TestReport) {
                description = 'Aggregates test reports of all projects.'
                group 'reporting'

                destinationDir = project.file("$project.buildDir/reports/tests")
                reportOn project.subprojects*.tasks*.withType(Test)
            }

            project.check.dependsOn project.testRootReport
        }
    }

    /**
     * Create Jacoco reports
     */
    def addTaskJacocoRootReport = { Project project ->
        if (project == project.rootProject && !project.subprojects.isEmpty()) {
            project.tasks.create('jacocoMergeToRoot', JacocoMerge) {
                description = 'Aggregates JaCoCo test coverage reports of all projects.'
                group = LifecycleBasePlugin.VERIFICATION_GROUP

                dependsOn project.subprojects*.tasks*.withType(Test)
                executionData project.subprojects*.tasks*.withType(Test)
                doFirst {
                    executionData = project.files(executionData.findAll { it.exists() })
                }
            }

            project.tasks.create('jacocoRootReport', JacocoReport) {
                description = 'Generates an aggregate report from all subprojects'
                dependsOn project.jacocoMergeToRoot
                group = "reporting"

                additionalSourceDirs project.files(project.subprojects.sourceSets.main.allSource.srcDirs)
                additionalClassDirs project.files(project.subprojects.sourceSets.main.output)
                executionData project.jacocoMergeToRoot.destinationFile

                reports {
                    html.enabled = true
                    xml.enabled = true
                }
            }

            project.check.dependsOn project.jacocoRootReport
        }
    }


}
