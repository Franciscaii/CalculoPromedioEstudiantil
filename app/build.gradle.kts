plugins {
    id("com.android.application")
    id("com.google.gms.google-services")  // Este plugin debe estar aquí para Firebase
}

android {
    namespace = "com.example.franciscafigueroaeva2"
    compileSdk = 34  // SDK actualizado

    defaultConfig {
        applicationId = "com.example.franciscafigueroaeva2"
        minSdk = 26  // Mínimo SDK que soportas
        targetSdk = 33  // Objetivo SDK
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Dependencias de Firebase
    implementation("com.google.firebase:firebase-database:21.0.0")  // Firebase Database

    // Puedes eliminar firebase-core, ya no es necesario
    // implementation("com.google.firebase:firebase-core:21.1.1")  // Elimina esta línea

    // Dependencias para pruebas
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
