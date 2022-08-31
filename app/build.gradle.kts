import com.gcode.plugin.version.AndroidX
import com.gcode.plugin.version.Compose
import com.gcode.plugin.version.Libraries
import com.gcode.plugin.version.Version
import com.gcode.plugin.version.Google

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.gcode.plugin.version")
}

android {

    defaultConfig {
        applicationId = "com.gcode.gmusiccomposesamples"
        minSdk = Version.min_sdk_version
        targetSdk = Version.target_sdk_version
        versionCode = Version.version_code
        versionName = Version.version_name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),"proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }

    compileSdk = Version.compile_sdk_version
    buildToolsVersion = Version.build_tools_version

    sourceSets["main"].java.srcDir("src/main/kotlin")
}

dependencies {
    implementation(files("libs/VastTools_0.0.9_Cancey.jar"))

    implementation(AndroidX.core_ktx)
    implementation(AndroidX.appcompat)
    androidTestImplementation(AndroidX.junit)
    androidTestImplementation(AndroidX.espresso_core)

    testImplementation(Libraries.junit)

    implementation(Compose.compose_constraintlayout)
    implementation(Compose.compose_ui)
    implementation(Compose.compose_ui_tooling)
    implementation(Compose.compose_foundation)
    implementation(Compose.compose_material)
    implementation(Compose.compose_material_icons_core)
    implementation(Compose.compose_material_icons_extends)
    implementation(Compose.compose_activity)
    implementation(Compose.compose_lifecycle_viewmodel)
    implementation(Compose.compose_runtime)
    implementation(Compose.compose_runtime_livedata)
    implementation(Compose.compose_runtime_rxjava2)

    implementation(Google.material)
    implementation(Google.accompanist_insets)
    implementation(Google.accompanist_insets_ui)
    implementation(Google.accompanist_systemuicontroller)

    androidTestImplementation(Compose.compose_ui_test_junit4)
}