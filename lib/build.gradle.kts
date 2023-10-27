plugins {
  id("com.android.library")
  kotlin("android")
  id("kotlin-parcelize")
  id("com.vanniktech.maven.publish")
}

android {
  compileSdk = 34
  defaultConfig {
    minSdk = 19
    targetSdk = 34
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
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("com.google.android.material:material:1.10.0")
  implementation("androidx.core:core-ktx:1.13.0-alpha01")
  api("androidx.fragment:fragment-ktx:1.6.1")
}
