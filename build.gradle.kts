plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("org.javassist:javassist:3.28.0-GA")
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.shadowJar {
    manifest {
        attributes(
            "Manifest-Version" to "1.0",
            "Main-Class" to "me.txmc.instexample.Main",
            "Premain-Class" to "me.txmc.instexample.jagent.AgentMain",
            "Agent-Class" to "me.txmc.instexample.jagent.AgentMain",
            "Can-Redefine-Classes" to "true",
            "Can-Retransform-Classes" to "true",
            "Can-Set-Native-Method-Prefix" to "true"
        )
    }
}

group = "me.txmc"
version = "1.0-SNAPSHOT"
description = "An example of how to do self attach instrumentation in java 9+"

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
