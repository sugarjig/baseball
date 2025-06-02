plugins {
    kotlin("jvm") version "2.1.20"
}

group = "io.samjones"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-rng-sampling:1.6")
    implementation("org.apache.commons:commons-rng-simple:1.6")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
