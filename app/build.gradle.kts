plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.yggdrasil"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.yggdrasil"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "Yggdrasil_Android13"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("../yggdrasil.jks")
            storePassword = "password123"
            keyAlias = "yggdrasil"
            keyPassword = "password123"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    
    // Media3 for playback logic
    implementation("androidx.media3:media3-exoplayer:1.2.1")
    implementation("androidx.media3:media3-session:1.2.1")
    implementation("androidx.media3:media3-ui:1.2.1")

    // Room for the Database (Tags and Playlists)
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    // Material Design components
    implementation("com.google.android.material:material:1.11.0")

    // Glide for Album Art
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)
}