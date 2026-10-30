plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.compose)
  alias(libs.plugins.kotlin.parcelize)
  alias(libs.plugins.maven.publish)
}

android {
  compileSdk = 37

  defaultConfig {
    minSdk = 23
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  buildFeatures {
    compose = true
  }
  namespace = "com.robertlevonyan.compose.picker"
}

kotlin {
  compilerOptions {
    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    optIn.add("kotlin.RequiresOptIn")
  }
}

dependencies {
  implementation(libs.material)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.compose.foundation)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.tooling)
}
