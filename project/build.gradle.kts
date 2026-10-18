import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    kotlin("jvm") version "2.3.21" apply false
    kotlin("plugin.spring") version "2.3.21" apply false
    id("org.springframework.boot") version "4.1.1" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

// --- Agente do OpenTelemetry (baixado pelo Gradle, fora do classpath dos serviços) ---
repositories {
    mavenCentral()
}

val otelAgent: Configuration by configurations.creating

dependencies {
    otelAgent("io.opentelemetry.javaagent:opentelemetry-javaagent:2.30.0")
}

val copyOtelAgent by tasks.registering(Copy::class) {
    from(otelAgent)
    into(layout.projectDirectory.dir("otel"))
    rename { "opentelemetry-javaagent.jar" }
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

    // quando o módulo for uma aplicação Spring Boot, o bootRun sobe com o agente do OpenTelemetry
    pluginManager.withPlugin("org.springframework.boot") {
        tasks.named<BootRun>("bootRun") {
            dependsOn(rootProject.tasks.named("copyOtelAgent"))
            jvmArgs("-javaagent:${rootProject.projectDir}/otel/opentelemetry-javaagent.jar")
            environment("OTEL_SERVICE_NAME", project.name)
            environment("OTEL_EXPORTER_OTLP_ENDPOINT", "http://localhost:4318")
            environment("OTEL_TRACES_SAMPLER", "parentbased_traceidratio")
            environment("OTEL_TRACES_SAMPLER_ARG", "0.1")   // guarda 10% dos traces
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
