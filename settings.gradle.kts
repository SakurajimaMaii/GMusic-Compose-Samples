dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "SampleGMusicCompose"
include(":app")
includeBuild("../PluginVersion")
