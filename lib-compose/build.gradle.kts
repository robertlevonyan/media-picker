plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("kotlin-parcelize")
  id("com.vanniktech.maven.publish")
}

android {
  compileSdk = 35

  defaultConfig {
    minSdk = 21

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
    freeCompilerArgs = freeCompilerArgs.toMutableList().apply {
      addAll(
        listOf(
          "-opt-in=kotlin.RequiresOptIn",
          "-Xallow-jvm-ir-dependencies",
          "-P",
          "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
      )
    }
  }
  buildFeatures {
    compose = true
  }
  namespace = "com.robertlevonyan.compose.picker"
}

dependencies {
  implementation("com.google.android.material:material:1.12.0")

  implementation("androidx.core:core-ktx:1.15.0")
  implementation("androidx.appcompat:appcompat:1.7.0")

  implementation("com.google.accompanist:accompanist-insets:0.23.0")
  implementation("androidx.activity:activity-compose:1.10.1")
  implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")
  implementation("androidx.compose.compiler:compiler:1.5.15")
  implementation("androidx.compose.ui:ui:1.7.8")
  implementation("androidx.compose.material:material:1.7.8")
  implementation("androidx.compose.ui:ui-tooling:1.7.8")
}
