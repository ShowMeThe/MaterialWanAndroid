import com.show.version.VersionPlugin

plugins{
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("app-version")
    id("core-dependency")
}

android{


    buildTypes {
        findByName("release")?.apply {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),file("proguard-rules.pro"))
        }
    }

    dependencies {
        api(fileTree(baseDir = "libs"){
            include("*.jar","*.aar")
        })
    }
}

