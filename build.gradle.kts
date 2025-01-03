import org.gradle.api.JavaVersion.VERSION_21
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") version libs.versions.kotlin
    alias(libs.plugins.kotlinter)
    application
}

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass = "xyz.malefic.waddlewaddle.WaddleKt"
}

repositories {
    mavenCentral()
}

tasks {
    withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            allWarningsAsErrors = false
            jvmTarget.set(JVM_21)
            freeCompilerArgs.add("-Xjvm-default=all")
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    java {
        sourceCompatibility = VERSION_21
        targetCompatibility = VERSION_21
    }

    register("formatAndLintKotlin") {
        group = "formatting"
        description = "Fix Kotlin code style deviations with kotlinter"
        dependsOn(formatKotlin)
        dependsOn(lintKotlin)
    }
}

dependencies {
    implementation(platform(libs.http4k.bom))
    implementation(libs.http4k.client.okhttp)
    implementation(libs.http4k.core)
    implementation(libs.http4k.format.jackson.yaml)
    implementation(libs.http4k.format.jackson)
    implementation(libs.http4k.server.undertow)
    testImplementation(libs.http4k.testing.approval)
    testImplementation(libs.http4k.testing.hamkrest)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.engine)
}
