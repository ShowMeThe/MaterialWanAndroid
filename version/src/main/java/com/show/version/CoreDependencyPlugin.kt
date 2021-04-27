package com.show.version

import com.android.build.gradle.*
import com.show.version.Dependencies.attachDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer


class CoreDependencyPlugin :  Plugin<Project> {

    override fun apply(project: Project) {
        config(project)
    }

    private fun config(project: Project) {
        project.plugins.withId("kotlin-kapt"){
            val extension = project.extensions.getByType(TestedExtension::class.java)
            when(extension){
                is LibraryExtension -> {
                    applyLibraryCommons(project)
                }

            }
        }
    }

    /**
     * Library基础配置
     */
    private fun applyLibraryCommons(project: Project) {

        project.applyLibraryDependencies(project)
    }

    private fun Project.applyLibraryDependencies(project: Project){
        dependencies.apply {
            /**
             * 添加base依赖
             */
            attachDependencies(project)
        }
    }

}