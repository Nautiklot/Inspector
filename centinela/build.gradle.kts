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
            create<MavenPublication>("maven") {
                groupId = "com.github.nautiklot"
                artifactId = "centinela"
                version = "1.0.0"
            }
        }
    }
}
kapt {
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
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.kotlin.test)
}