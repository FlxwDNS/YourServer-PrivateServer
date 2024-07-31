plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":privateserver-api"))
}

tasks.withType<Jar> {
    archiveFileName.set("easycloudservice.jar")
}