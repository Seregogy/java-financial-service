plugins {
	id("nu.studer.jooq") version "10.2.1"
	id("java")
}

group = "com.financial.loan.persistence"
version = "unspecified"

repositories {
	mavenCentral()
}

dependencies {
    implementation(project(":domain"))

	testImplementation(platform(libs.junit.bom))
	testImplementation(libs.junit.jupiter)

	implementation(libs.postgres.driver)
	jooqGenerator(libs.postgres.driver)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

tasks.test {
	useJUnitPlatform()
}

jooq {
	configurations {
		create("main") {
			jooqConfiguration.apply {
				logging = org.jooq.meta.jaxb.Logging.WARN

				jdbc.apply {
					driver = "org.postgresql.Driver"
					url = "jdbc:postgresql://localhost:5432/auto-loan-service"
					user = "postgres"
                    password = "postgres"
				}

				generator.apply {
					database.apply {
						name = "org.jooq.meta.postgres.PostgresDatabase"
						inputSchema = "public"
					}

					generate.apply {
						isDeprecated = false
						isRecords = true
						isFluentSetters = true
					}

					target.apply {
						packageName = "com.financial.loan.persistence.model"
						directory = "src/main/java/generated"
					}
				}
			}
		}
	}
}