@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.VariantDimension


@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.appdistribution")
    kotlin("plugin.serialization")
}

android {

    compileSdk = 34
    buildToolsVersion = "33.0.1"

    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
    }

    sourceSets {
        getByName("main") {
            java.srcDir("libs")
        }
        getByName("androidTest") {
            assets.srcDirs("$projectDir/schemas")
        }
    }

    packaging {
        resources.excludes.add("META-INF/robovm/ios/robovm.xml")
        resources.excludes.add("META-INF/DEPENDENCIES")
    }

    defaultConfig {
        minSdk = 23
        targetSdk = 34
        multiDexEnabled = true
        resourceConfigurations.addAll(listOf("en", "es", "tr"))
        applicationId = "com.example.firebaseappdistributionsample"
        namespace = "com.example.firebaseappdistributionsample"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        resValue("bool", "DEV_MODE", "false")
        resourceConfigurations.addAll(listOf("en", "es", "tr"))
        stringResValue("auth_url", "https://auth.stage.carex.ai/")

        versionCode = 1
        versionName = "1.0.0"

        ndk {
            abiFilters.apply {
                add("armeabi-v7a")
                add("x86")
                add("arm64-v8a")
                add("x86_64")
            }
        }

        firebaseAppDistribution {
            artifactType = "APK"
            serviceCredentialsFile = "firebase-creds.json"
        }
    }

    buildTypes {
        debug {
            isShrinkResources = false
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    flavorDimensions.add("version")
    productFlavors {

        create("dev") {
            applicationIdSuffix = ".dev"
            dimension = "version"
            stringResValue("app_name", "FAD - DEV")
            firebaseAppDistribution {
                groups = "dev_tester_dist"
                appId = "1:1084397942743:android:13e814856eb35d6c3e3d92\n"
            }
        }
        create("stage") {
            applicationIdSuffix = ".stage"
            dimension = "version"
            stringResValue("app_name", "FAD - STAGE")
            firebaseAppDistribution {
                groups = "stage_tester_dist"
                appId = "1:1084397942743:android:24a3031efa0a745b3e3d92"
            }
        }
    }

    lint { checkReleaseBuilds = true }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    composeOptions { kotlinCompilerExtensionVersion = "1.5.15" }

    kotlinOptions { jvmTarget = JavaVersion.VERSION_11.toString() }

    androidResources {
        noCompress.add("tflite")
        noCompress.add("lite")
    }

    packaging {
        resources.excludes.add("META-INF/robovm/ios/robovm.xml")
        resources.excludes.add("META-INF/DEPENDENCIES")

        jniLibs.pickFirsts.add("lib/arm64-v8a/libc++_shared.so")
        jniLibs.pickFirsts.add("lib/armeabi-v7a/libc++_shared.so")
        jniLibs.pickFirsts.add("lib/x86/libc++_shared.so")
        jniLibs.pickFirsts.add("lib/x86_64/libc++_shared.so")
    }
}

configurations {
    all {
        exclude(module = "httpclient")
        exclude(module = "commons-logging")
    }
}

dependencies {

    implementation(platform(libs.firebase.bom))
    implementation(platform(libs.compose.bom))

    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

    // Data Binding
    kapt(libs.roomCompiler)

    androidTestImplementation(platform(libs.compose.bom))
    implementation(libs.compose.material3)
    implementation(libs.compose.material)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui)
    implementation(libs.compose.uiTooling)
    implementation(libs.compose.uiTooling.preview)
    implementation(libs.compose.material.iconsExtended)
    implementation(libs.androidx.material.icons.core)
    implementation("androidx.compose.material3:material3-window-size-class")

    implementation(libs.compose.activity)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.compose.runtime.livedata)
    implementation(libs.androidx.runtime.rxjava2)

    // Kotlin
    implementation(libs.navFragmentKtx)
    implementation(libs.navUiKtx)
    implementation(libs.compose.navigation)

    // Androidx
    implementation(libs.androidx.core)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycleViewmodel)
    implementation(libs.androidx.lifecycleReactivestreams)
    implementation(libs.androidx.lifecycleExtensions)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.legacy)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.vectordrawable)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.activity.ktx)

    implementation(libs.gson)

    // Kotlin
    implementation(libs.kotlin.stdlib.jdk8)

    // ZXing QR code
    implementation(libs.core)


    implementation(libs.kotlinx.serialization.json.v171)
}


val releaseDevToFirebase by tasks.registering {

    group = "releaseDca"
    description = "Deploys the dev version to Firebase"

    val buildTask = tasks.getByName("assembleDevDebug")
    val uploadTask = tasks.getByName("appDistributionUploadDevDebug")

    uploadTask.mustRunAfter(buildTask)

    uploadTask.dependsOn(buildTask)
    dependsOn(uploadTask)

}

val releaseStageToFirebase by tasks.registering {

    val buildTask = tasks.getByName("assembleStageDebug")
    val uploadTask = tasks.getByName("appDistributionUploadStageDebug")

    uploadTask.mustRunAfter(buildTask)

    uploadTask.dependsOn(buildTask)
    dependsOn(uploadTask)

}

fun VariantDimension.stringResValue(key: String, value: String) {
    resValue("string", key, value)
}