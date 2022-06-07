import com.show.version.getDateTime

plugins{
    id("com.android.application")
    id("Orca")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("app-version")
    id("app-dependency")
}


android{
    buildFeatures.dataBinding = true
    buildFeatures.viewBinding = true

    val kotlinVersion :String by rootProject.extra

    signingConfigs{
        create("release"){
            storeFile = file("\\key.jks")
            storePassword = "123456"
            keyAlias = "key0"
            keyPassword = "123456"
        }
    }


    defaultConfig{
        applicationId = "com.show.wanandroid"
        resConfigs("zh","zh-rCN")

        splits {
            abi {
                isEnable = true
                reset()
                include("arm64-v8a")
                isUniversalApk = false
            }
        }
    }

    Orca.go{
        storeSet{
            create("url"){
                value = "https://www.wanandroid.com/"
            }

        }
        signature = "-815485697"
    }

    buildTypes{

        findByName("release")?.apply {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),file("proguard-rules.pro"))
            signingConfig = signingConfigs.findByName("release")
        }

        findByName("debug")?.apply {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),file("proguard-rules.pro"))
            signingConfig = signingConfigs.findByName("release")
        }

    }


    applicationVariants.all {
        outputs.forEach { output ->
            val outputImp = output as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            var name = ""
            splits.abiFilters.forEach {
                name = "${name}-${it}"
            }
            outputImp.apply {
                outputFileName = "App-${buildType.name}${name}-${getDateTime()}.apk"
            }
        }
    }



    dependencies {
        implementation(fileTree(baseDir = "libs"){
            include("*.jar","*.aar")
        })
        //debugImplementation("com.squareup.leakcanary:leakcanary-android:2.7")
        implementation(project(":core"))
        implementation(project(":skinlib"))

        implementation("com.show.flutter_wan:flutter_release:1.0")
    }

}



