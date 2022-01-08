package com.show.version

import com.android.build.gradle.*
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.show.version.Dependencies.attachDependencies
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.plugins
import org.gradle.kotlin.dsl.withType


/**
 *  com.show.version
 *  2020/7/18
 *  10:46
 *  ShowMeThe
 */
class VersionPlugin : Plugin<Project> {

    override fun apply(project: Project) {
       config(project)
    }

    private fun config(project: Project) {
        project.plugins.withId("kotlin-android"){
            val extension = project.extensions.getByType(TestedExtension::class.java)
            when(extension){
                is AppExtension -> {

                 project.extensions.getByType<BaseAppModuleExtension>()
                    .applyAndroidCommons(project)
            }
                is LibraryExtension -> {
                    project.extensions.getByType<LibraryExtension>()
                        .applyLibraryCommons(project)

                }
            }
        }
    }

    /**
     * App基础配置
     */
    private fun BaseAppModuleExtension.applyAndroidCommons(project: Project) {
        apply {
            buildFeatures{
                dataBinding = true
                viewBinding = true
            }
        }
        appCommons(project)
    }

    /**
     * Library基础配置
     */
    private fun LibraryExtension.applyLibraryCommons(project: Project) {
        appCommons(project)
        buildFeatures{
            dataBinding = true
            viewBinding = true
        }
        project.applyLibraryDependencies()
    }


    private fun Project.applyLibraryDependencies(){
        dependencies.apply {
            /**
             * 添加base依赖
             */
            implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar","*.aar"))))

        }
    }



    /**
     * 基础配置
     */
    private fun TestedExtension.appCommons(project: Project){

        compileSdkVersion(Versions.compileSdkVersion)
        buildToolsVersion(Versions.buildToolsVersion)


        defaultConfig{
            minSdkVersion(Versions.minSdkVersion)
            targetSdkVersion(Versions.targetSdkVersion)
            versionName = Versions.versionName
            versionCode = Versions.versionCode
            multiDexEnabled = true

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        }

        lintOptions{
            isCheckReleaseBuilds = false
            isAbortOnError = false
        }

        buildTypes{
            getByName("debug").apply {
                buildConfigField("boolean","LOG_ENABLE","true")
            }

            getByName("release").apply {
                buildConfigField("boolean","LOG_ENABLE","false")
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }


        project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = Versions.jvmTarget
            }
        }
    }


    /**
     * 应用 kotlin 插件
     */
    private fun Project.applyKotlinPlugin() {
        plugins.apply("kotlin-android")
        plugins.apply("kotlin-parcelize")
        plugins.apply("kotlin-kapt")
    }



    object Versions {

        const val compileSdkVersion = 31

        const val buildToolsVersion = "31.0.0"

        const val minSdkVersion = 26

        const val versionName = "1.0.0"

        const val versionCode = 1

        const val targetSdkVersion = 30

        const val jvmTarget = "1.8"

        const val databinding = "4.2.0-alpha10"
    }


}