plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    kotlin("kapt")
    `maven-publish`
}

android {
    namespace = "com.inspector.centinela"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}
afterEvaluate{
    publishing {
        publications {
            // Creamos una publicación llamada "maven"
            create<MavenPublication>("maven") {
                // El Group ID suele ser la ruta de tu repositorio en GitHub
                // Ejemplo: com.github.tuUsuarioDeGithub
                groupId = "com.github.nautiklot"

                // El nombre específico de este módulo
                artifactId = "centinela"

                // La versión inicial de tu librería
                version = "1.0.0"
            }
        }
    }
}
kapt {
    // Le dice a kapt que haga el esfuerzo de mapear el error al archivo .kt
    mapDiagnosticLocations = true
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(kotlin("reflect"))
    implementation(project(":annotations"))
    kapt(project(":processor"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.22")
}