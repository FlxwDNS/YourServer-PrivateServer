plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":privateserver-api"))
    implementation("de.rapha149.signgui:signgui:2.3.6")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
