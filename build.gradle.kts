buildscript {
    ext {
        compose_version = '1.6.0-alpha02'
        room_version = '2.6.1'
        nav_version = '2.6.0'
    }

    dependencies {
        classpath libs.hilt.android.gradle.plugin
    }

}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application)
    //id 'com.android.application' version '8.1.4' apply false

    alias(libs.plugins.android.library)
    //id 'com.android.library' version '8.1.4' apply false

    alias(libs.plugins.kotlin.android)
    //id 'org.jetbrains.kotlin.android' version '2.0.20' apply false
    alias(libs.plugins.dagger.hilt.android)
    //id 'com.google.dagger.hilt.android' version "2.51.1" apply false

    alias(libs.plugins.ksp)
    //id 'com.google.devtools.ksp' version "2.0.20-1.0.24" apply false
}

tasks.register('clean', Delete) {
    delete rootProject.buildDir
}