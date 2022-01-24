import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.permitteringsportal.database.Repository


fun Route.hentOppgaver(repository: Repository) {
    get("/oppgave") {
        call.respond(HttpStatusCode.OK)
        // repository.hentOppgaver(orgnr)
    }
}

fun Route.sendInnBekreftelse(repository: Repository) {
    post("/oppgave") {
        call.respond(HttpStatusCode.Created)
    }
}

fun Route.hentBekreftelse(repository: Repository) {
    get("/bekreftelse/{id}") {

    }
}

fun Route.oppdatereBekreftelse(repository: Repository) {
    put("/bekreftelse/{id}") {

    }
}

