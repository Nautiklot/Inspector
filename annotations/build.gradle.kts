plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    `maven-publish`
}
publishing {
    publications {
        // Creamos una publicación llamada "maven"
        create<MavenPublication>("maven") {
            // El Group ID suele ser la ruta de tu repositorio en GitHub
            // Ejemplo: com.github.tuUsuarioDeGithub
            groupId = "com.github.nautiklot"

            // El nombre específico de este módulo
            artifactId = "annotations"

            // La versión inicial de tu librería
            version = "1.0.1"

            // Le indicamos que empaquete el código compilado usando el componente de Java/Kotlin
            from(components["java"])
        }
    }
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
