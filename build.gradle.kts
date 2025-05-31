plugins {
    kotlin("jvm") version "2.1.20"
}

group = "io.samjones"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.13.0")
}

tasks.test {
    useJUnitPlatform()
}
