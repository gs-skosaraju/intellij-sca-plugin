plugins {
    java
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.advancedsca"
version = "1.0. 0"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
}

intellij {
    version.set("2023.2")
    type.set("IC")
    plugins.set(listOf("java", "maven", "gradle", "org.jetbrains.kotlin"))
}

kotlin {
    jvmToolchain(17)
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    
    patchPluginXml {
        sinceBuild.set("232")
        untilBuild. set("241.*")
    }
    
    buildSearchableOptions {
        enabled = false
    }
}