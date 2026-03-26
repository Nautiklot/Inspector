plugins {
    kotlin("kapt")
    kotlin("jvm")
}
kapt {
    // Le dice a kapt que haga el esfuerzo de mapear el error al archivo .kt
    mapDiagnosticLocations = true
}


dependencies {
    testImplementation(libs.junit)
    implementation(project(":annotations"))
    implementation("com.google.devtools.ksp:symbol-processing-api:2.2.0-2.0.2")
    compileOnly("com.google.auto.service:auto-service-annotations:1.1.1")
    kapt("com.google.auto.service:auto-service:1.1.1")

    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.5.0")
    // 2. La librería estándar de pruebas de Kotlin (asegúrate de usar tu versión de Kotlin)
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.22")
    // 3. El motor de JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
}