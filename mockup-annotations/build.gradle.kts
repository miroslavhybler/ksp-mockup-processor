@file:Suppress("UnstableApiUsage")

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
    implementation("androidx.annotation:annotation:1.7.1")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from (components.getByName("kotlin"))
                groupId = "mir.oslav.mockup"
                artifactId = "annotations"
                version = "1.1.1"
                pom {
                    description.set("Jitpack.io deploy")
                }
            }

        }
        repositories {
            mavenLocal()
        }
    }
}