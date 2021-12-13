package no.nav.permitteringsportal

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import no.nav.permitteringsportal.database.BekreftelsePåArbeidsforhold
import no.nav.permitteringsportal.database.DatabaseConfig
import no.nav.permitteringsportal.database.Repository
import no.nav.permitteringsportal.database.runFlywayMigrations
import no.nav.permitteringsportal.utils.Cluster
import no.nav.permitteringsportal.utils.log
import javax.sql.DataSource

class App(
    private val dataSource: DataSource
) {

    fun start() {
        runFlywayMigrations(dataSource)

        val repository = Repository(dataSource)


        embeddedServer(Netty, port = 8080) {
            routing {
                get("/internal/isAlive") { call.respond(HttpStatusCode.OK) }
                get("/internal/isReady") { call.respond(HttpStatusCode.OK) }
            }
        }.start(wait = true)
    }
}

fun main() {
    log("main").info("Starter app i cluster: ${Cluster.current}")

    val databaseConfig = DatabaseConfig()

    App(
        dataSource = databaseConfig.dataSource
    ).start()
}
