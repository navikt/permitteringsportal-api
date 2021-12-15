package no.nav.permitteringsportal

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import no.nav.permitteringsportal.database.DatabaseConfig
import no.nav.permitteringsportal.database.Repository
import no.nav.permitteringsportal.database.runFlywayMigrations
import no.nav.permitteringsportal.utils.Cluster
import no.nav.permitteringsportal.utils.log
import no.nav.security.token.support.ktor.IssuerConfig
import no.nav.security.token.support.ktor.TokenSupportConfig
import no.nav.security.token.support.ktor.tokenValidationSupport
import java.io.Closeable
import javax.sql.DataSource

class App(
    private val dataSource: DataSource,
    private val issuerConfig: IssuerConfig

): Closeable {

    private val repository = Repository(dataSource)

    private val server = embeddedServer(Netty, port = 8080) {

        install(Authentication) {
            tokenValidationSupport(config = TokenSupportConfig(issuerConfig))
        }

        routing {
            authenticate {
                get("/sikret-endepunkt") { call.respond(HttpStatusCode.OK) }
            }

            get("/internal/isAlive") { call.respond(HttpStatusCode.OK) }
            get("/internal/isReady") { call.respond(HttpStatusCode.OK) }
        }
    }

    fun start() {
        runFlywayMigrations(dataSource)
        server.start()
    }

    override fun close() {
        server.stop(0, 0)
    }
}

fun main() {
    log("main").info("Starter app i cluster: ${Cluster.current}")

    val databaseConfig = DatabaseConfig()

    val issuerConfig = IssuerConfig(
        name = "arbeidsgiver",
        discoveryUrl = System.getenv("TOKEN_X_WELL_KNOWN_URL"),
        acceptedAudience = listOf(System.getenv("TOKEN_X_CLIENT_ID"))
    )

    App(
        dataSource = databaseConfig.dataSource,
        issuerConfig = issuerConfig
    ).start()
}
