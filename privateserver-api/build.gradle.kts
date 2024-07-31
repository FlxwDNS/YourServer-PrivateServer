plugins {
    id("java")
}

repositories {
    mavenCentral()
    maven("https://repo.thesimplecloud.eu/artifactory/list/gradle-release-local/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    implementation("net.wesjd:anvilgui:1.10.0-SNAPSHOT")
    implementation("commons-io:commons-io:2.16.1")

    // simplecloud
    compileOnly("eu.thesimplecloud.simplecloud:simplecloud-api:2.4.1")
}