plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("kotlin-parcelize")
  id("com.vanniktech.maven.publish")
}

android {
  compileSdk = 34

  defaultConfig {
    minSdk = 21
    targetSdk = 34

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
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.3"
  }
  namespace = "com.robertlevonyan.compose.picker"
}

dependencies {
  implementation("com.google.android.material:material:1.10.0")

  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.appcompat:appcompat:1.6.1")

  implementation("com.google.accompanist:accompanist-insets:0.23.0")
  implementation("androidx.activity:activity-compose:1.8.0")
  implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
  implementation("androidx.compose.compiler:compiler:1.5.3")
  implementation("androidx.compose.ui:ui:1.5.4")
  implementation("androidx.compose.material:material:1.5.4")
  implementation("androidx.compose.ui:ui-tooling:1.5.4")
}
