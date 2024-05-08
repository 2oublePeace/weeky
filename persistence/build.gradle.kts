plugins {
    id("org.jetbrains.kotlinx.kover") version "0.7.6"
    id("org.sonarqube") version "5.0.0.4638"
    kotlin("plugin.jpa") version "1.9.22"
}

sonar {
    properties {
        property("sonar.projectKey", "2oublePeace_weeky")
        property("sonar.organization", "2oublepeace")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.token", System.getenv("SONAR_TOKEN"))
        property("sonar.coverage.jacoco.xmlReportPaths", "${project.projectDir}/build/reports/kover/report.xml")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.liquibase:liquibase-core")
    runtimeOnly("org.postgresql:postgresql")
    testRuntimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
