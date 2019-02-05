package com.vogle.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.CheckstylePlugin
import org.gradle.api.plugins.quality.PmdPlugin

/**
 * The Code Quality Plugins
 * <pre>
 *     Include below
 *     - The Checkstyle Plugin: https://docs.gradle.org/current/userguide/checkstyle_plugin.html
 *     - The PMD Plugin: https://docs.gradle.org/current/userguide/pmd_plugin.html
 * </pre>
 *
 * @author Allan Im
 *
 */
class CodeQualityPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.withType(JavaPlugin) {
            applyCheckstyle(project)
            applyPmd(project)
        }
    }

    /**
     * Code Analysis by CheckStyle,
     */
    def applyCheckstyle = { Project project ->
        project.plugins.apply(CheckstylePlugin)
        project.checkstyle {
            ignoreFailures = true
        }
        project.tasks.withType(Checkstyle) {
            reports {
                xml.enabled = true
                html.enabled = true
            }
        }
    }


    /**
     * Code Analysis by PMD,
     */
    def applyPmd = { Project project ->
        project.plugins.apply(PmdPlugin)
        project.pmd {
            toolVersion = '6.+'
            ignoreFailures = true
            rulePriority = 5
            // Don't use the default gradle rule sets
            ruleSets = []
        }
    }

}
