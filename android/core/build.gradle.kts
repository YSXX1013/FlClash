import com.android.build.gradle.tasks.MergeSourceSetFolders

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.follow.clash.core"
    compileSdk = 35
    ndkVersion = "27.1.12297006"

    defaultConfig {
        minSdk = 21
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.register<Copy>("copyNativeLibs") {
    doFirst {
        delete("src/main/jniLibs")
    }
    from("../../libclash/android")
    into("src/main/jniLibs")
}

tasks.withType<MergeSourceSetFolders>().configureEach {
    dependsOn("copyNativeLibs")
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
}

afterEvaluate {
    tasks.named("assembleDebug").configure {
        dependsOn("copyNativeLibs")
    }
    tasks.named("assembleRelease").configure {
        dependsOn("copyNativeLibs")
    }
}