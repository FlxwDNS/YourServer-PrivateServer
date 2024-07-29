plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("de.rapha149.signgui:signgui:2.3.6")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}