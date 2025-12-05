plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.advancedsca"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google. code.gson:gson:2. 10.1")
}

intellij {
    version. set("2023.2")
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
    
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    
    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("241.*")
    }
    
    buildSearchableOptions {
        enabled = false
    }
}