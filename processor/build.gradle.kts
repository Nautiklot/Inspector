plugins {
    kotlin("jvm")
    `maven-publish`
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.nautiklot"
            artifactId = "processor"
            version = "1.1.0"
            from(components["java"])
        }
    }
}


dependencies {
    testImplementation(libs.junit)
    implementation(project(":annotations"))
    implementation(libs.symbol.processing.api)
    compileOnly(libs.auto.service.annotations)
}
