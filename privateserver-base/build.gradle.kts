plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

repositories {
    mavenCentral()

    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven("https://repo.xenondevs.xyz/releases")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    implementation(project(":privateserver-api"))

    implementation("net.wesjd:anvilgui:1.10.0-SNAPSHOT")

    implementation("xyz.xenondevs.invui:invui:1.33")
    implementation("com.github.FlxwDNS.AscanAPI:ascan-view:ad5d86f8fa")
    implementation("com.github.FlxwDNS.AscanAPI:ascan-common:ad5d86f8fa")

    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
}

tasks.withType<Jar> {
    archiveFileName.set("YourServer-Lobby.jar")
}