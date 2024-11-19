// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://dl.bintray.com/kotlin/kotlinx/")
        maven("https://jcenter.bintray.com")
    }

    dependencies {
        classpath(libs.androidGradlePlugin)
        classpath(libs.kotlinGradlePlugin)
        classpath(libs.navGradlePlugin)
        classpath(libs.googleServices)
        classpath(libs.firebase.gradleCrashlytics)
        classpath(libs.firebase.gradlePerf)
        classpath(libs.kotlinxMetadataJvm)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("com.diffplug.gradle.spotless") version "3.13.0"
    id("com.google.firebase.appdistribution") version "4.0.0" apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

allprojects {
    apply(plugin = "eclipse")
    apply(plugin = "idea")

    version = "1.0"

    repositories {
        mavenLocal()
        mavenCentral()
        google()
        maven("https://jitpack.io")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/releases/")
        maven("https://jcenter.bintray.com")
    }
}

tasks.named("eclipse") {
    doLast {
        delete(".project")
    }
}