plugins {
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
}
group = "com.github.Nautiklot.Inspector"
version = "2.0.0"
publishing {
    publications {
        create<MavenPublication>("maven") {
            // Ya no necesitas declarar groupId, artifactId ni version aquí.
            // Gradle los tomará de las variables globales de arriba automáticamente.
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
