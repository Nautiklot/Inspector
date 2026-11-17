plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("com.android.library")
    `maven-publish`
}

// 1. CORRECCIÓN: "N" mayúscula y versión 2.0.3
group = "com.github.Nautiklot.Inspector"
version = "2.0.3"

kotlin {
    // Soporte para Android y Backend (Ktor/Spring)
    jvm()
    androidTarget {
        publishLibraryVariants("release")
    }

    // Soporte para ecosistema Apple (iPhone físico y simuladores)
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    // Soporte para Web (JavaScript)
    js(IR) {
        browser()
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Aquí irían dependencias comunes si las tuvieras
            }
        }
    }
}

// 2. CORRECCIÓN: Bloque obligatorio de Android
android {
    namespace = "com.twinkslab.annotations"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }
}
