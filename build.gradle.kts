plugins {
  id("com.android.application") version "8.9.0" apply false
  id("com.android.library") version "8.9.0" apply false
  id("org.jetbrains.dokka") version "1.9.20" apply false
  id("com.vanniktech.maven.publish") version "0.31.0" apply false
  kotlin("android") version "1.9.21" apply false
}

tasks.register("clean", Delete::class) {
  delete(rootProject.layout.buildDirectory)
}
