plugins {
    alias(libs.plugins.kotlin.multiplatform)
    `maven-publish`
}
group = "com.github.nautiklot.Inspector"
version = "2.0.0"
kotlin {
    // 1. Soporte para Android y Backend (Ktor/Spring)
    jvm()

    // 2. Soporte para ecosistema Apple (iPhone físico y simuladores)
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    // 3. Soporte para Web (JavaScript)
    js(IR) {
        browser()
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }
    }
}
