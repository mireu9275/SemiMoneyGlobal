plugins {
    kotlin("jvm") version "1.9.23"
    id("maven-publish")
}

group = "kr.eme.semiMoneyGlobal"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

kotlin {
    jvmToolchain(17)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.jar {
    archiveFileName = "${project.name}-${project.version}.jar"
    destinationDirectory = file("C:\\Users\\Home\\Desktop\\Develop\\minecraft\\Bukkit\\paper 1.21.4 (Semicolon Primary Colony)\\plugins")
    manifest {
        attributes["main-Class"] = "kr.eme.semiMoneyGlobal.SemiMoneyGlobal"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "kr.eme.semiMoneyGlobal"
            artifactId = project.name
            version = project.version.toString()
            from(components["kotlin"])
        }
    }
}