import org.jetbrains.dokka.DokkaConfiguration.Visibility
import java.io.FileInputStream
import java.util.Properties


plugins {
    kotlin("jvm")
    id("kotlin-kapt")
    id("org.jetbrains.dokka")
    id("maven-publish")
    id("com.google.devtools.ksp")
}

kotlin {
    jvmToolchain(jdkVersion = 8)
}


dependencies {
    implementation(project(":mockup-annotations"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.10-1.0.13")
    implementation("androidx.annotation:annotation:1.7.0")


    val autoServiceVersion = "1.0-rc7"
    kapt("com.google.auto.service:auto-service:$autoServiceVersion")
    compileOnly("com.google.auto.service:auto-service-annotations:$autoServiceVersion")

}


tasks.register("runMockupProcessor", JavaExec::class) {
    group = "Mockup"
    description = "Runs the KSP processor"

    classpath = sourceSets.getByName("main").runtimeClasspath

    this.mainClass.set("com.google.devtools.ksp.processing.Main")
    args("--symbol", "mir.oslav.mockup.processor.MockupProcessor")
}


tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("dokkaHtml"))

    dokkaSourceSets {
        configureEach {
            pluginsMapConfiguration.set(
                mutableMapOf(
                    "org.jetbrains.dokka.base.DokkaBase" to """{ "separateInheritedMembers": true}"""
                )
            )
            documentedVisibilities.set(
                 mutableListOf(
                     Visibility.PUBLIC,
                     Visibility.PRIVATE,
                     Visibility.PROTECTED,
                     Visibility.INTERNAL,
                     Visibility.PACKAGE
                 )
            )

            skipEmptyPackages.set(true)
            includeNonPublic.set(true)
            skipDeprecated.set(false)
            reportUndocumented.set(true)
            includes.from("${projectDir}/packages.md")
            description = ""
        }
    }
}


publishing {
    publications {
        register<MavenPublication>("ksp-mockup-publish") {
            groupId = "mir.oslav.mockup"
            artifactId = "processor"
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