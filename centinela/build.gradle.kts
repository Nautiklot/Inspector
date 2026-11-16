plugins {
    alias(libs.plugins.kotlin.multiplatform)
    `maven-publish`
}
group = "com.github.Nautiklot.Inspector"
version = "2.0.1"
kotlin {
    jvm()
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