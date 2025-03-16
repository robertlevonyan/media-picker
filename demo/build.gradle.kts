plugins {
  id("com.android.application")
  kotlin("android")
}

android {
  compileSdk = 35
  defaultConfig {
    minSdk = 26
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  buildFeatures {
    viewBinding = true
    compose = true
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.3"
  }
  namespace = "com.robertlevonyan.picker.demo"
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("androidx.core:core-ktx:1.15.0")
  implementation("androidx.appcompat:appcompat:1.7.0")
  implementation("com.google.android.material:material:1.12.0")
  implementation("androidx.constraintlayout:constraintlayout:2.2.1")
  implementation("io.coil-kt:coil:2.7.0")
  implementation("io.coil-kt:coil-video:2.4.0")
  implementation("io.coil-kt:coil-compose:2.7.0")
  implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")
  implementation("androidx.compose.compiler:compiler:1.5.15")
  implementation("androidx.compose.ui:ui:1.7.8")
  implementation("androidx.compose.material:material:1.7.8")
  implementation("androidx.compose.ui:ui-tooling:1.7.8")
  implementation("io.coil-kt:coil-compose:2.7.0")
  implementation("com.robertlevonyan.compose:picker:1.1.1")
}
