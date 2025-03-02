plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    api(libs.commons.math3)
    implementation(libs.guava)
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.24") // Версия Lombok
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
    withJavadocJar()
    sourceSets {
        main {
            java.srcDirs("src/main/java")  // Путь для Java
            kotlin.srcDirs("src/main/kotlin")  // Путь для Kotlin
        }
    }
}

tasks.named<Jar>("sourcesJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}





tasks.withType<JavaCompile>().configureEach {
    options.release.set(17)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            groupId = "dev.loveeev"
            artifactId = "hikariLib"
            version = "0.1.0"
        }
    }
    repositories {
        mavenLocal()
    }
}


tasks.named<Test>("test") {
    useJUnitPlatform()
}
