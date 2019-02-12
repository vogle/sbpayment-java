package com.vogle.gradle


import org.gradle.api.Plugin
import org.gradle.api.Task
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.kt3k.gradle.plugin.CoverallsPluginExtension
import org.kt3k.gradle.plugin.coveralls.CoverallsTask

/**
 * Upload jacoco coverage to the Coveralls
 * <pre>
 *     Include below
 *     - coveralls-gradle-plugin: https://github.com/kt3k/coveralls-gradle-plugin
 * </pre>
 *
 * @author Allan Im
 *
 */
class CoverallsPlugin implements Plugin<ProjectInternal> {

    static final String COVERALLS_REPO_TOKEN = "COVERALLS_REPO_TOKEN"

    @Override
    void apply(ProjectInternal project) {

        project.plugins.withType(JacocoPlugin) {
            // create coveralls project extension
            project.extensions.create('coveralls', CoverallsPluginExtension)
            project.coveralls.service = 'LOCAL'

            // register coveralls task
            Task task = project.task('uploadJacocoToCoveralls', type: CoverallsTask)

            // set vars
            task.env = System.getenv()
            task.group = 'reporting'
            task.description = 'Upload Jacoco coverage to Coveralls.'
            task.dependsOn project.tasks.withType(JacocoReport).collect()

            String token = project.findProperty(COVERALLS_REPO_TOKEN)
            if (token) {
                task.env = [COVERALLS_REPO_TOKEN: token]
            }
        }

    }
}
