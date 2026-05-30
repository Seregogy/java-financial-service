plugins {
	id("java")
}

group = "com.financial.loan.domain"
version = "unspecified"

repositories {
	mavenCentral()
}

dependencies {
	testImplementation(platform(libs.junit.bom))
	testImplementation(libs.junit.jupiter)

	compileOnly(libs.lombok)
	annotationProcessor(libs.lombok)
}

tasks.test {
	useJUnitPlatform()
}