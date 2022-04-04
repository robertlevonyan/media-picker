plugins {
  id("com.android.application")
  kotlin("android")
}

android {
  compileSdk = 32
  defaultConfig {
    minSdk = 26
    targetSdk = 32
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  buildFeatures {
    viewBinding = true
    compose = true
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.2.0-alpha03"
  }
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("androidx.core:core-ktx:1.7.0")
  implementation("androidx.appcompat:appcompat:1.4.1")
  implementation("com.google.android.material:material:1.5.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.3")
  implementation("io.coil-kt:coil:1.4.0")
  implementation("io.coil-kt:coil-video:1.4.0")
  implementation("io.coil-kt:coil-compose:1.4.0")
  implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0")
  implementation("androidx.compose.compiler:compiler:1.1.1")
  implementation("androidx.compose.ui:ui:1.1.1")
  implementation("androidx.compose.material:material:1.1.1")
  implementation("androidx.compose.ui:ui-tooling:1.1.1")
  implementation("io.coil-kt:coil-compose:1.4.0")

  implementation(project(mapOf("path" to ":lib-compose")))
  implementation(project(mapOf("path" to ":lib")))
}
