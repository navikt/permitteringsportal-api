plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("io.ktor:ktor-server-core:1.6.4")
    implementation("io.ktor:ktor-server-netty:1.6.4")

    implementation("ch.qos.logback:logback-classic:1.2.6")
    implementation("net.logstash.logback:logstash-logback-encoder:6.6")

    implementation("org.flywaydb:flyway-core:8.0.3")
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("com.github.seratch:kotliquery:1.6.1")

    implementation("no.nav.security:token-validation-ktor:1.3.9")
    testImplementation("no.nav.security:mock-oauth2-server:0.4.0")

    implementation("com.github.kittinunf.fuel:fuel:2.3.1")

    implementation("com.h2database:h2:1.4.200")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("org.assertj:assertj-core:3.19.0")
}

application {
    mainClass.set("no.nav.permitteringsportal.AppKt")
}
