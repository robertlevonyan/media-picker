plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.compose)
  alias(libs.plugins.kotlin.android)
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
  namespace = "com.robertlevonyan.picker.demo"
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.coil)
  implementation(libs.coil.video)
  implementation(libs.coil.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.compose.foundation)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui.tooling)
//  implementation(libs.picker)
  implementation(project(":lib"))
  implementation(project(":lib-compose"))
}
