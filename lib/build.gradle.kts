plugins {
  id("com.android.library")
  kotlin("android")
  id("kotlin-parcelize")
  id("com.vanniktech.maven.publish")
}

android {
  compileSdk = 32
  defaultConfig {
    minSdk = 19
    targetSdk = 32
    testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    vectorDrawables.useSupportLibrary = true
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  buildFeatures {
    viewBinding = true
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
  lint {
    abortOnError = false
  }
}

allprojects {
  plugins.withId("com.vanniktech.maven.publish") {
    mavenPublish {
      sonatypeHost = com.vanniktech.maven.publish.SonatypeHost.S01
    }
  }
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("androidx.appcompat:appcompat:1.4.1")
  implementation("androidx.constraintlayout:constraintlayout:2.1.3")
  implementation("com.google.android.material:material:1.5.0")
  implementation("androidx.core:core-ktx:1.9.0-alpha02")
  api("androidx.fragment:fragment-ktx:1.4.1")
}
