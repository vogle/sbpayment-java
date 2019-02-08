package com.vogle.gradle.javaproject

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.testing.jacoco.plugins.JacocoPlugin

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
        project.plugins.withType(JavaBasePlugin) {
            applyJacoco(project)
        }
    }

    /**
     * Apply Coverage metrics for Java code by jacoco,
     */
    def applyJacoco = { Project project ->
        project.plugins.apply(JacocoPlugin)
        project.jacoco {
            toolVersion = '0.8.+'
        }
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
