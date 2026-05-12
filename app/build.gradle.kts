plugins {
	java
	id("org.springframework.boot") version "4.0.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.financial.loan"
version = "0.0.1-SNAPSHOT"
description = "Backend service for auto loan automation"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(libs.spring.boot.security)
	implementation(libs.spring.boot.webmvc)
	implementation(project(":domain"))
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("jakarta.validation:jakarta.validation-api")
	developmentOnly(libs.spring.boot.docker.compose)

	testImplementation(libs.spring.boot.security.test)
	testImplementation(libs.spring.boot.webmvc.test)

	testRuntimeOnly(libs.junit.platform)
}

tasks.withType<Test> {
	useJUnitPlatform()
}
