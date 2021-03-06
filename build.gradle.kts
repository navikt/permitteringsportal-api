plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.31"
    id("com.expediagroup.graphql") version "5.2.0"
    application
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://packages.confluent.io/maven/")
    }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("com.expediagroup:graphql-kotlin-ktor-client:5.2.0")
    val ktor_version = "1.6.4"
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("io.ktor:ktor-client-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-jackson:$ktor_version")
    implementation("io.ktor:ktor-client-auth:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    testImplementation("io.ktor:ktor-client-mock:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")

    implementation("com.graphql-java:graphql-java:16.2")
    implementation("com.expediagroup:graphql-kotlin-ktor-client:5.2.0")

    implementation("ch.qos.logback:logback-classic:1.2.6")
    implementation("net.logstash.logback:logstash-logback-encoder:6.6")

    implementation("org.flywaydb:flyway-core:8.0.3")
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("com.github.seratch:kotliquery:1.6.1")

    implementation("no.nav.security:token-validation-ktor:1.3.9")
    implementation("no.nav.security:token-client-core:1.3.9")
    testImplementation("no.nav.security:mock-oauth2-server:0.4.0")

    implementation("com.github.kittinunf.fuel:fuel:2.3.1")
    implementation("com.github.kittinunf.fuel:fuel-jackson:2.3.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")

    // TODO: Endre til testImplementation n??r vi er kobla mot PostgreSQL i milj??
    implementation("com.h2database:h2:1.4.200")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("org.assertj:assertj-core:3.19.0")
    testImplementation("io.mockk:mockk:1.12.2")

}

val graphqlGenerateClient by tasks.getting(com.expediagroup.graphql.plugin.gradle.tasks.GraphQLGenerateClientTask::class) {
    packageName.set("no.nav.permitteringsportal.graphql.generated\"")
    schemaFile.set(file("src/main/resources/schema.graphql"))
    serializer.set(com.expediagroup.graphql.plugin.gradle.config.GraphQLSerializer.KOTLINX)
}

application {
    mainClass.set("no.nav.permitteringsportal.AppKt")
}
