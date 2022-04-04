plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("kotlin-parcelize")
  id("com.vanniktech.maven.publish")
}

android {
  compileSdk = 32

  defaultConfig {
    minSdk = 21
    targetSdk = 32

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
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
    kotlinCompilerExtensionVersion = "1.2.0-alpha03"
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
  implementation("com.google.android.material:material:1.5.0")

  implementation("androidx.core:core-ktx:1.7.0")
  implementation("androidx.appcompat:appcompat:1.4.1")

  implementation("com.google.accompanist:accompanist-insets:0.23.0")
  implementation("androidx.activity:activity-compose:1.4.0")
  implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0")
  implementation("androidx.compose.compiler:compiler:1.1.1")
  implementation("androidx.compose.ui:ui:1.1.1")
  implementation("androidx.compose.material:material:1.1.1")
  implementation("androidx.compose.ui:ui-tooling:1.1.1")
}
