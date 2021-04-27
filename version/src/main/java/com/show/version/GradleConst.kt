package com.show.version

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.extra
import java.text.SimpleDateFormat
import java.util.*


/**
 * Library 基础依赖配置
 */
const val api = "api"
const val implementation = "implementation"
const val kapt = "kapt"

fun getDateTime() = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",Locale.getDefault()).format(Date().time)

/**
 * 添加依赖
 */
fun DependencyHandler.api(dependency: Any){
    add(api,dependency)
}

fun DependencyHandler.implementation(dependency: Any){
    add(implementation,dependency)
}

fun DependencyHandler.kapt(dependency: Any){
    add(kapt,dependency)
}


object AndroidX{

    const val appcompat = "1.3.0-rc01"
    const val animation = "1.0.0-alpha02"
    const val activity = "1.3.0-alpha06"
    const val core = "1.6.0-alpha01"
    const val fragment = "1.3.2"
    const val transition_ktx = "1.4.0"
    const val fragment_ktx = "1.3.0-rc01"
    const val legacy_support_v4 = "1.0.0"
    const val work_runtime = "2.7.0-alpha02"
    const val multidex = "2.0.1"


    const val constraintlayout = "2.1.0-beta01"
    const val recyclerview = "1.2.0-beta02"
    const val material = "1.4.0-alpha02"
    const val gridlayout = "1.0.0"
    const val viewpager2 = "1.1.0-alpha01"
    const val palette = "1.0.0"
    const val swipe = "1.2.0-alpha01"

    const val room = "2.3.0-rc01"

    const val lifecycle = "2.3.0"
}


object Http{

    const val okhttp3 = "5.0.0-alpha.2"
    const val retrofit = "2.9.0"
    const val converter_gson = "2.9.0"
    const val converter_moshi = "2.9.0"
    const val gson = "2.8.6"
    const val moshi_kotlin = "1.12.0"

}

object Kotlin{

    const val coroutines = "1.4.2"

}


object Extra{

    const val autosize = "1.2.1"

    const val glide = "4.12.0"

    const val mmkv = "1.2.7"



    const val kInject = "1.5.0"
    const val clock = "1.0.0"
    const val liveBus = "1.1.0"
    const val permission = "1.0.0"
    const val initializer = "1.0.2"

}


object Dependencies {

    fun DependencyHandler.attachDependencies(project: Project){

        /**
         * Kotlin
         */
        api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${project.rootProject.extra["kotlin_version"]}")
        api("org.jetbrains.kotlin:kotlin-reflect:${project.rootProject.extra["kotlin_version"]}")


        /**
         * Base
         */
        api("androidx.activity:activity:${AndroidX.activity}")
        api("androidx.appcompat:appcompat:${AndroidX.appcompat}")
        api("androidx.core:core-animation:${AndroidX.animation}")
        api("androidx.transition:transition-ktx:${AndroidX.transition_ktx}")
        api("androidx.core:core:${AndroidX.core}")
        api("androidx.core:core-ktx:${AndroidX.core}")
        api("androidx.fragment:fragment:${AndroidX.fragment}")
        api("androidx.fragment:fragment-ktx:${AndroidX.fragment_ktx}")
        api( "androidx.multidex:multidex:${AndroidX.multidex}")
        api("androidx.work:work-runtime:${AndroidX.work_runtime}")
        api("androidx.work:work-multiprocess:${AndroidX.work_runtime}")
        api("androidx.work:work-runtime-ktx:${AndroidX.work_runtime}")
        api("androidx.legacy:legacy-support-v4:${AndroidX.legacy_support_v4}")

        /**
         * UI
         */
        api("androidx.constraintlayout:constraintlayout:${AndroidX.constraintlayout}")
        api("androidx.recyclerview:recyclerview:${AndroidX.recyclerview}")
        api("androidx.gridlayout:gridlayout:${AndroidX.gridlayout}")
        api("com.google.android.material:material:${AndroidX.material}")
        api("androidx.viewpager2:viewpager2:${AndroidX.viewpager2}")
        api("androidx.palette:palette:${AndroidX.palette}")
        api("androidx.swiperefreshlayout:swiperefreshlayout:${AndroidX.swipe}")
        //api("me.jessyan:autosize:${Extra.autosize}")

        /**
         * Http
         */
        api("com.google.code.gson:gson:${Http.gson}")
        api("com.squareup.moshi:moshi-kotlin:${Http.moshi_kotlin}")
        api( "com.squareup.retrofit2:retrofit:${Http.retrofit}")
        api("com.squareup.retrofit2:converter-gson:${Http.converter_gson}")
        api("com.squareup.okhttp3:okhttp:${Http.okhttp3}")
        api("com.squareup.okhttp3:logging-interceptor:${Http.okhttp3}")
        api("com.squareup.retrofit2:converter-moshi:${Http.converter_moshi}")
        kapt("com.squareup.moshi:moshi-kotlin-codegen:${Http.moshi_kotlin}")

        /**
         * ROOM
         */
        api("androidx.room:room-runtime:${AndroidX.room}")
        kapt("androidx.room:room-compiler:${AndroidX.room}")
        api("androidx.room:room-ktx:${AndroidX.room}")
        /**
         * MMKV
         */
        api("com.tencent:mmkv-static:${Extra.mmkv}")

        /**
         * GLIDE
         */
        api("com.github.bumptech.glide:glide:${Extra.glide}")
        kapt("com.github.bumptech.glide:compiler:${Extra.glide}")
        api("com.github.bumptech.glide:gifencoder-integration:${Extra.glide}")
        api("com.github.bumptech.glide:okhttp3-integration:${Extra.glide}")

        /**
         * RECYCLE
         */
        api("androidx.lifecycle:lifecycle-viewmodel:${AndroidX.lifecycle}")
        api("androidx.lifecycle:lifecycle-common-java8:${AndroidX.lifecycle}")
        api("androidx.lifecycle:lifecycle-livedata:${AndroidX.lifecycle}")
        api("androidx.lifecycle:lifecycle-viewmodel-ktx:${AndroidX.lifecycle}")
        api("androidx.lifecycle:lifecycle-runtime-ktx:${AndroidX.lifecycle}")
        api("androidx.lifecycle:lifecycle-livedata-ktx:${AndroidX.lifecycle}")
        api("androidx.lifecycle:lifecycle-process:${AndroidX.lifecycle}")
        api("androidx.lifecycle:lifecycle-service:${AndroidX.lifecycle}")



        /**
         * COROUTINES
         */
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Kotlin.coroutines}")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Kotlin.coroutines}")


        api ("com.github.ShowMeThe:LiveDataEvent:${Extra.liveBus}")
        api("com.github.ShowMeThe:Kinject:${Extra.kInject}")
        api("com.github.ShowMeThe:Permission:${Extra.permission}")
        api("com.github.ShowMeThe:InitializerApp:${Extra.initializer}")
        api("com.github.ShowMeThe:Kclock:${Extra.clock}")
    }

}