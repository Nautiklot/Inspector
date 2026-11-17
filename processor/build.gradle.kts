plugins {
    alias(libs.plugins.kotlin.jvm)
    // ❌ ELIMINADO: id("com.android.library")
    `maven-publish`
}

// ✅ CORRECCIÓN: "N" mayúscula y versión 2.0.3
group = "com.github.Nautiklot.Inspector"
version = "2.0.4"

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

dependencies {
    testImplementation(libs.junit)
    implementation(project(":annotations"))
    implementation(libs.symbol.processing.api)
    compileOnly(libs.auto.service.annotations)

    implementation(libs.kotlinpoet.core)
    implementation(libs.kotlinpoet.ksp)
}
