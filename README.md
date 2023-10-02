# ksp-mockup
Ksp mockup is simple ksp (Kotlin Symbol Processing) library for generating fake mockup data. These are
handy to be used in previews in Jetpack Compose applications.

## Add Library
Add maven repository and library dependency to your app's gradle files. Make sure to add ksp plugin too.
Minimum supported sdk is 21 (Android 5 Lollipop). CompileSdk sdk is 34 (Android 14).

**Project's build.gradle.ksp**
```kotlin
//Project's build.gradle.kts
plugins {
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
}
```

**Project's settings.gradle.ksp**
```kotlin
dependencyResolutionManagement {
  repositories {
    maven(url = "https://maven.pkg.github.com/miroslavhybler/Maven")
  }
}
```

**Application's module build.gradle.kts**
```kotlin
plugins {
    id("com.google.devtools.ksp")
}

dependencies {
    val mockupVersion= "1.0.0"
    implementation("mir.oslav.mockup:annotations:$mockupVersion")
    ksp("mir.oslav.mockup:annotations:$mockupVersion")
    ksp("mir.oslav.mockup:processor:$mockupVersion")
}
```

### Usage
Annotate desired classes with @Mockup.

```kotlin
@Mockup
data class User constructor(
    val id: Int,
    val firstName: String,
    val lastName: String,
)
```

Build your project and access generated data through generated Mockup object.

```kotlin
val user = Mockup.user.single
val usersLis = Mockup.user.list
```

### Supported types
- Simple types like Int, Long, String, ...
- Lists
- Arrays with known type (like IntArray, FloatArray, ...) are generated empty
- Classes annotated with @Mockup annotation


### Used resources
Images for previews taken from [Pixabay](https://www.pixabay.com/).