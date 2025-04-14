plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.compose)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.parcelize)
  alias(libs.plugins.maven.publish)
}

android {
  compileSdk = 35

  defaultConfig {
    minSdk = 21
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
