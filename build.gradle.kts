plugins {
    kotlin("jvm") version "1.9.0"
    application
    id("org.openjfx.javafxplugin") version "0.0.13"
}

javafx {
    setVersion("20")
    setModules(listOf("javafx.controls"))
}

group = "com.andmal"
version = "0.0.2"

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly("org.openjfx:javafx-graphics:$javafx.version:win")
    runtimeOnly("org.openjfx:javafx-graphics:$javafx.version:linux")
    runtimeOnly("org.openjfx:javafx-graphics:$javafx.version:mac")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(20)
}

application {
    mainClass.set("Main")
}

tasks.jar {
    manifest {
        attributes(mapOf("Main-Class" to "Main"))
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
//        configurations.runtimeClasspath.get().filter { it.isDirectory() ? it : zipTree(it) }

    })

}