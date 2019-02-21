package com.vogle.gradle.javaproject


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
import org.gradle.api.plugins.ProjectReportsPlugin
import org.gradle.plugins.ide.eclipse.EclipsePlugin
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.tooling.UnsupportedVersionException
import org.gradle.util.VersionNumber

/**
 * The Base Plugins
 * <pre>
 *     Include below
 *     - The Base Plugin: https://docs.gradle.org/current/userguide/base_plugin.html
 *     - The Project Report: https://docs.gradle.org/current/userguide/project_report_plugin.html
 *     - The IDEA Plugin: https://docs.gradle.org/current/userguide/idea_plugin.html
 *     - The Eclipse Plugin: https://docs.gradle.org/current/userguide/eclipse_plugin.html
 * </pre>
 *
 * @author Allan Im
 *
 */
class BasePlugin implements Plugin<Project> {

    static int PLUGINS_IN_PROJECT = 0

    static boolean isFirstPlugin() {
        PLUGINS_IN_PROJECT == 1
    }

    static void updatePluginCount() {
        PLUGINS_IN_PROJECT++
    }

    @Override
    void apply(Project project) {
        Gradle gradle = project.gradle
        VersionNumber version = VersionNumber.parse(gradle.gradleVersion)
        VersionNumber requiredVersion = new VersionNumber(2, 12, 0, null)
        if (version.baseVersion < requiredVersion) {
            throw new UnsupportedVersionException("Your gradle version ($version) is too old. " +
                    "Plugin requires Gradle $requiredVersion+")
        }

        updatePluginCount()

        if (firstPlugin) {
            Package pkg = this.class.getPackage()
            String thisPluginVersion = (pkg == null) ? "" : pkg.getImplementationVersion()

            def logLine = "+----------------------------------------------------------+"

            project.logger.quiet "${logLine}"
            project.logger.quiet " .-.-. .-.-. .-.-. .-.-. .-.-.          Java Project Plugin "
            project.logger.quiet " '. V )'. O )'. G )'. L )'. E )               by vogle labs "
            project.logger.quiet "   ).'   ).'   ).'   ).'   ).' ${new Date()} "
            project.logger.quiet "${logLine}"

            if (System.getProperty('os.name')) {
                project.logger.quiet " OS : ${System.getProperty('os.name')} (${System.getProperty('os.version')})"
            }
            if (System.getProperty('java.version')) {
                project.logger.quiet " JRE : v${System.getProperty('java.version')}"
            }
            if (System.getProperty('java.vm.name')) {
                project.logger.quiet " JVM : ${System.getProperty('java.vm.name')}"
            }
            project.logger.quiet " Gradle : v${project.gradle.gradleVersion}"
            if (thisPluginVersion) {
                project.logger.quiet " vogle-javaproject-plugin : v${thisPluginVersion}"
            }
            project.logger.quiet "${logLine}"
        }

        // Apply base
        project.plugins.apply(org.gradle.api.plugins.BasePlugin)

        // Apply IDEA
        project.plugins.apply(IdeaPlugin)

        // Apply Eclipse
        project.plugins.apply(EclipsePlugin)

    }

}
