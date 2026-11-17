plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("com.android.library")
    `maven-publish`
}

// 1. CORRECCIÓN: "N" mayúscula y versión 2.0.3
group = "com.github.Nautiklot.Inspector"
version = "2.0.4"

kotlin {
    jvmToolchain(17)
    jvm()
    androidTarget {
        publishLibraryVariants("release")
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    js(IR) { browser(); nodejs() }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Centinela necesita conocer las anotaciones
                implementation(project(":annotations"))
            }
        }
    }
}

// 2. CORRECCIÓN: Bloque obligatorio de configuración Android
android {
    namespace = "com.twinkslab.centinela"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
