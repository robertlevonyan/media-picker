plugins {
  id("com.android.library")
  kotlin("android")
  id("kotlin-parcelize")
  id("com.vanniktech.maven.publish")
}

android {
  compileSdk = 35
  defaultConfig {
    minSdk = 21

    testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    vectorDrawables.useSupportLibrary = true
  }
  buildFeatures {
    viewBinding = true
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
  lint {
    abortOnError = false
  }
  namespace = "com.robertlevonyan.components.picker"
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("androidx.appcompat:appcompat:1.7.0")
  implementation("androidx.constraintlayout:constraintlayout:2.2.1")
  implementation("com.google.android.material:material:1.12.0")
  implementation("androidx.core:core-ktx:1.15.0")
  api("androidx.fragment:fragment-ktx:1.8.6")
}
