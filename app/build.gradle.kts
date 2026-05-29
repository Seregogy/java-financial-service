plugins {
	java
	id("org.springframework.boot") version "4.0.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.financial.loan"
version = "0.0.1-SNAPSHOT"
description = "Backend service for auto loan automation"

repositories {
	mavenCentral()
}

dependencies {
	implementation(project(":domain"))
	implementation(project(":persistence"))

	implementation(libs.spring.boot.security)
	implementation(libs.spring.boot.webmvc)
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("jakarta.validation:jakarta.validation-api")
	developmentOnly(libs.spring.boot.docker.compose)

	testImplementation(libs.spring.boot.security.test)
	testImplementation(libs.spring.boot.webmvc.test)
	implementation(libs.spring.boot.validation)
	implementation(libs.spring.boot.starter.jdbc)
	implementation(libs.spring.boot.starter.jooq)

	testRuntimeOnly(libs.junit.platform)

	compileOnly(libs.lombok)
	annotationProcessor(libs.lombok)

	implementation(libs.jjwt.api)
	implementation(libs.jjwt.impl)
	implementation(libs.jjwt.jackson)

	implementation(libs.postgres.driver)

}

tasks.withType<Test> {
	useJUnitPlatform()
}
