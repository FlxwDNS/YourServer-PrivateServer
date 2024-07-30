plugins {
    id("java")
}

repositories {
    mavenCentral()
    maven("https://repo.thesimplecloud.eu/artifactory/list/gradle-release-local/")
}

dependencies {
    implementation("de.rapha149.signgui:signgui:2.3.6")
    implementation("commons-io:commons-io:2.16.1")

    // simplecloud
    compileOnly("eu.thesimplecloud.simplecloud:simplecloud-api:2.4.1")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}