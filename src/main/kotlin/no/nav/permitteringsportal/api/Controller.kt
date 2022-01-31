import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.permitteringsportal.database.BekreftelsePåArbeidsforhold
import no.nav.permitteringsportal.database.Repository
import java.util.*


fun Route.hentOppgaver(repository: Repository) {
    get("/oppgave") {
        call.respond(HttpStatusCode.OK)
        // repository.hentOppgaver(orgnr)
    }
}
fun Route.sendInnBekreftelse(repository: Repository) {
    post("/oppgave") {
        call.respond(HttpStatusCode.Created)
        repository.leggTilNyBekreftelse("fnr", "orgnr")
    }
}
fun Route.hentBekreftelse(repository: Repository) {
    get("/bekreftelse/{id}") {
        val id = call.parameters["id"]
        if (id != null) {
            val bekreftelsePåArbeidsforhold = repository.hentBekreftelseMedId(id)
            if (bekreftelsePåArbeidsforhold != null) {
                call.respond(bekreftelsePåArbeidsforhold)
            }
        }
    }
}
fun Route.leggTilBekreftelse(repository: Repository) {
    post("/bekreftelse") {
        val nyBekreftelse = call.receive<BekreftelsePåArbeidsforhold>()
        repository.leggTilNyBekreftelse(nyBekreftelse.fnr, nyBekreftelse.orgnr)
    }
}

fun Route.oppdatereBekreftelse(repository: Repository) {
    put("/bekreftelse/{id}") {

    }
}