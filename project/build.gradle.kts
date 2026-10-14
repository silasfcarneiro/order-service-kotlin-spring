plugins {
    kotlin("jvm") version "2.3.21" apply false
    kotlin("plugin.spring") version "2.3.21" apply false
    id("org.springframework.boot") version "4.1.1" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

// configuração COMUM aplicada a todos os subprojetos (módulos)
subprojects {
    group = "com.silas"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    // quando o módulo usar Kotlin, já ganha a biblioteca de testes
    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        dependencies {
            "testImplementation"(kotlin("test"))
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
