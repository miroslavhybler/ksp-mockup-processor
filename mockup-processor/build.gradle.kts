plugins {
    kotlin("jvm")
        id("kotlin-kapt")
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