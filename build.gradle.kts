plugins {
    java
    kotlin("jvm") version "1.5.31"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.junit.jupiter:junit-jupiter:5.4.2")
    testImplementation("junit", "junit", "4.12")
}

application {
    this.applicationName = "EM-ds-test-task"
    mainClassName = "com.flaax.em.MainKt"
}

tasks.withType<Test>().all {
    useJUnitPlatform()
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}