plugins {
    id("java")
}

allprojects {
    apply(plugin = "java-library")

    group = "dev.flxwdns.privateserver"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }

    dependencies {
        "implementation"("dev.httpmarco.evelon:evelon-common:1.0.44-SNAPSHOT")
        "implementation"("dev.httpmarco.evelon:evelon-sql-mariadb:1.0.44-SNAPSHOT")

        "compileOnly"("org.projectlombok:lombok:1.18.34")
        "annotationProcessor"("org.projectlombok:lombok:1.18.34")
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        sourceCompatibility = JavaVersion.VERSION_21.toString()
        targetCompatibility = JavaVersion.VERSION_21.toString()
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
