import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform.getCurrentOperatingSystem

plugins {
    application
    id("org.bytedeco.gradle-javacpp-platform").version("1.5.9")
    id("org.beryx.runtime").version("1.12.7")
}

val platform = when {
    getCurrentOperatingSystem().isWindows -> "win"
    getCurrentOperatingSystem().isLinux -> "linux"
    getCurrentOperatingSystem().isMacOsX -> "mac"
    else -> throw UnsupportedOperationException("Operating system ${getCurrentOperatingSystem()} not supported yet")
}

repositories {
    mavenCentral()
}

runtime {
    jpackage {
        imageName = rootProject.name
        skipInstaller = true
    }
}

dependencies {
    implementation(group = "org.openjfx", name = "javafx-base", version = "21.0.1", classifier = platform)
    implementation(group = "org.openjfx", name = "javafx-graphics", version = "21.0.1", classifier = platform)
    implementation(group = "org.openjfx", name = "javafx-controls", version = "21.0.1", classifier = platform)
    implementation(group = "org.openjfx", name = "javafx-fxml", version = "21.0.1", classifier = platform)
    implementation(group = "org.openjfx", name = "javafx-web", version = "21.0.1", classifier = platform)
    implementation("com.fasterxml.jackson.core:jackson-core:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "5.9.3")
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("pl.kul.todo.Main")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}