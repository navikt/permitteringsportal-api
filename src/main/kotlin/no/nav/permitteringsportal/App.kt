package no.nav.permitteringsportal

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import no.nav.permitteringsportal.utils.log

class App {

    fun start() {
        log.info("starter app")

        val database = Database()
        database.runQuery()

        embeddedServer(Netty, port = 8080) {
            routing {
                get("/internal/isAlive") { call.respond(HttpStatusCode.OK) }
                get("/internal/isReady") { call.respond(HttpStatusCode.OK) }
            }
        }.start(wait = true)
    }

}

fun main() {
    App().start()
}
