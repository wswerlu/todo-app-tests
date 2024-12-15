plugins {
    id("java")
    id("org.springframework.boot") version "3.2.2"
    id("io.qameta.allure") version "2.11.2"
    id("io.freefair.lombok") version "8.6"
}

val allureVersion = "2.25.0"
val springBootVersion = "3.2.2"

group = "org.todo"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_19
    targetCompatibility = JavaVersion.VERSION_19
}

repositories {
    mavenCentral()
}

dependencies {
    // rest assured
    implementation("io.rest-assured:rest-assured:5.4.0")
    implementation("io.qameta.allure:allure-rest-assured:${allureVersion}")

    // websocket
    implementation("org.java-websocket:Java-WebSocket:1.5.7")

    // junit
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.junit.jupiter:junit-jupiter-api:5.9.1")

    // test data
    implementation("net.datafaker:datafaker:2.1.0")
    implementation("org.instancio:instancio-junit:4.4.0")

    // spring
    implementation("org.springframework.boot:spring-boot-starter-aop:${springBootVersion}")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${springBootVersion}")
    implementation("org.springframework:spring-web:6.1.12")

    // tools
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
}

tasks.test {
    useJUnitPlatform {
        val includeTags = project.properties["includeTags"] as String?

        if (!includeTags.isNullOrBlank()) {
            includeTags(includeTags)
        }
    }
}

allure {
    report {
        version.set(allureVersion)
    }
    adapter {
        autoconfigure.set(true)
        aspectjWeaver.set(true)
        allureJavaVersion.set(allureVersion)
    }
}