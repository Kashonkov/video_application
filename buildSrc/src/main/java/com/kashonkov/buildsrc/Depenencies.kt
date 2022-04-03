package com.kashonkov.buildsrc

import org.gradle.api.artifacts.dsl.DependencyHandler

object Versions {
    val coreVersion = "1.7.0"
    val lifecycleVersion = "2.4.1"
    val materialVersion = "1.4.0"
    val junitVersion = "4.13.2"
    val junitExtVersion = "1.1.3"
    val espressoVersion = "3.4.0"
    val constraintLayoutVersion = "2.1.3"
    val appcompatVersion = "2.4.1"
    val retrofitVersion = "2.9.0"
    val retrofitGsonVersion = "2.5.0"
    val retrofitLoggingVersion = "4.9.0"
    val hiltVersion = "2.41"
}


private fun DependencyHandler.implementation(depName: String) {
    add("implementation", depName)
}

private fun DependencyHandler.kapt(depName: String) {
    add("kapt", depName)
}

private fun DependencyHandler.compileOnly(depName: String) {
    add("compileOnly", depName)
}

private fun DependencyHandler.api(depName: String) {
    add("api", depName)
}

private fun DependencyHandler.testImplementation(depName: String) {
    add("testImplementation", depName)
}

private fun DependencyHandler.androidTestImplementation(depName: String) {
    add("androidTestImplementation", depName)
}

private fun DependencyHandler.debugImplementation(depName: String) {
    add("debugImplementation", depName)
}

fun DependencyHandler.core() {
    implementation("androidx.core:core-ktx:${Versions.coreVersion}")
}

fun DependencyHandler.lifecycle() {
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}")
}

fun DependencyHandler.appCompat() {
    implementation("androidx.appcompat:appcompat:${Versions.appcompatVersion}")
}

fun DependencyHandler.design() {
    implementation("com.google.android.material:material:${Versions.materialVersion}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}")
}

fun DependencyHandler.tests() {
    testImplementation("junit:junit:${Versions.junitVersion}")
    androidTestImplementation("androidx.test.ext:${Versions.junitExtVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.espressoVersion}")
}

fun DependencyHandler.retrofit() {
    api("com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}")
    api("com.squareup.retrofit2:converter-gson:${Versions.retrofitGsonVersion}")
    debugImplementation("com.squareup.okhttp3:logging-interceptor:${Versions.retrofitLoggingVersion}")
}

fun DependencyHandler.hilt() {
    implementation("com.google.dagger:hilt-android:${Versions.hiltVersion}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.hiltVersion}")
}

