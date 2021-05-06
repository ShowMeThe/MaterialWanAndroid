package com.show.version

import com.android.build.gradle.*
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.getByType

class AppDependencyPlugin : Plugin<Project>{

    override fun apply(project: Project) {
       config(project)
    }

    private fun config(project: Project) {
        project.plugins.withId("kotlin-kapt"){
            val extension = project.extensions.getByType(TestedExtension::class.java)
            when(extension){
                is AppExtension -> {
                    applyLibraryCommons(project)
                }

            }
        }
    }

    /**
     * Library基础配置
     */
    private fun applyLibraryCommons(project: Project) {
        project.applyLibraryDependencies()
    }

    private fun Project.applyLibraryDependencies(){
        dependencies.apply {
            /**
             * 添加依赖
             */
           attachDependencies()
        }
    }

    fun DependencyHandler.attachDependencies(){


        kapt("androidx.room:room-compiler:${AndroidX.room}")
        kapt("com.squareup.moshi:moshi-kotlin-codegen:${Http.moshi_kotlin}")

        implementation("com.github.ShowMeThe:SlideBack:v2.1.0")
    }

}