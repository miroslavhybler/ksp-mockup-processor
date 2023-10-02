@file:Suppress("UnstableApiUsage")

import java.io.FileInputStream
import java.util.Properties

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    jvmToolchain(jdkVersion = 8)
}

dependencies {
    implementation("androidx.annotation:annotation:1.7.0")
}

publishing {
    publications {
        register<MavenPublication>("ksp-mockup-annotations-publish") {
            groupId = "mir.oslav.mockup"
            artifactId = "annotations"
            version = "1.0.0"

            afterEvaluate {
                from(components.getByName("kotlin"))
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri(path = "https://maven.pkg.github.com/miroslavhybler/ksp-mockup/")

            val githubProperties = Properties()
            githubProperties.load(FileInputStream(rootProject.file("github.properties")))
            val username = githubProperties["github.username"].toString()
            val token = githubProperties["github.token"].toString()

            credentials {
                this.username = username
                this.password = token
            }
        }
    }
}