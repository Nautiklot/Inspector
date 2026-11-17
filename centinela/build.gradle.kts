plugins {
    alias(libs.plugins.kotlin.multiplatform)
    `maven-publish`
}
group = "com.github.nautiklot.Inspector"
version = "2.0.2"
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