buildscript {
    repositories {
        jcenter()
        google()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
    }
}

plugins {
    id("org.gradle.kotlin.kotlin-dsl") version "1.4.0"
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
    google()
    jcenter()
}

dependencies {
    compileOnly(gradleApi())
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
    compileOnly("com.android.tools.build:gradle:4.1.2")
}

gradlePlugin {
    plugins {
        create("app-version") {
            id = "app-version"
            implementationClass = "com.show.version.VersionPlugin"
        }

        create("core-dependency") {
            id = "core-dependency"
            implementationClass = "com.show.version.CoreDependencyPlugin"
        }
        create("app-dependency") {
            id = "app-dependency"
            implementationClass = "com.show.version.AppDependencyPlugin"
        }
    }
}