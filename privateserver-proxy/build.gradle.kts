plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

repositories {
    mavenCentral()
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    implementation(project(":privateserver-api"))

    compileOnly("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
}

tasks.withType<Jar> {
    archiveFileName.set("YourServer-Proxy.jar")
}