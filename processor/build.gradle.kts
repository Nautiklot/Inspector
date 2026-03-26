plugins {
    kotlin("kapt")
    kotlin("jvm")
    `maven-publish`
}
kapt {
    mapDiagnosticLocations = true
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.nautiklot"
            artifactId = "processor"
            version = "1.0.0"
            from(components["java"])
        }
    }
}


dependencies {
    testImplementation(libs.junit)
    implementation(project(":annotations"))
    implementation(libs.symbol.processing.api)
    compileOnly(libs.auto.service.annotations)
    kapt(libs.auto.service)
}