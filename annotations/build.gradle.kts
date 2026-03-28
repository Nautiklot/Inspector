plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    `maven-publish`
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.nautiklot"
            artifactId = "annotations"
            version = "1.2.1"
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
