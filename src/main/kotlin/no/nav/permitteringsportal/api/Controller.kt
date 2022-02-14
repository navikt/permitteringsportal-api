import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import no.nav.permitteringsportal.altinn.AltinnService
import no.nav.permitteringsportal.altinn.MockAltinnService
import no.nav.permitteringsportal.database.BekreftelsePåArbeidsforhold
import no.nav.permitteringsportal.database.BekreftelsePåArbeidsforholdHendelse
import no.nav.permitteringsportal.database.Repository
import no.nav.permitteringsportal.utils.getFnrFraToken

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
                val hendelser =
                    repository.hentAlleHendelserForBekreftelseOgOrganisasjon(bekreftelsePåArbeidsforhold.orgnr, id)
                bekreftelsePåArbeidsforhold.hendelser = hendelser
                call.respond(toDTOFraBekreftelse(bekreftelsePåArbeidsforhold))
            }
        }
    }
}

fun Route.leggTilBekreftelse(repository: Repository, altinnService: AltinnService) {
    post("/bekreftelse") {
        val nyBekreftelse = call.receive<BekreftelsePåArbeidsforhold>()
        val fnr = getFnrFraToken(call.authentication)
        val altinnOrganisasjoner = fnr?.let { it -> altinnService.hentOrganisasjon(it, nyBekreftelse.orgnr) }

        val uuid = repository.leggTilNyBekreftelse(nyBekreftelse.fnr, nyBekreftelse.orgnr)
        call.respond(uuid)

    }
}

fun Route.oppdaterBekreftelse(repository: Repository) {
    put("/bekreftelse/{id}") {

    }
}

@Serializable
data class BekreftelsePåArbeidsforholdHendelseOutboundDTO(
    val id: String,
    val bekreftelseId: String,
    val type: String,
    val stillingsprosent: Int,
    val startDato: LocalDateTime,
    val sluttDato: LocalDateTime
)
@Serializable
data class BekreftelsePåArbeidsforholdOutboundDTO(
    val id: String,
    val fnr: String,
    val orgnr: String,
    var hendelser: List<BekreftelsePåArbeidsforholdHendelseOutboundDTO>
)

fun toDTOFraBekreftelse(bekreftelsePåArbeidsforhold: BekreftelsePåArbeidsforhold): BekreftelsePåArbeidsforholdOutboundDTO {
    return BekreftelsePåArbeidsforholdOutboundDTO(
        bekreftelsePåArbeidsforhold.id,
        bekreftelsePåArbeidsforhold.fnr,
        bekreftelsePåArbeidsforhold.orgnr,
        bekreftelsePåArbeidsforhold.hendelser.map { toDTOFraHendelse(it) }
    )
}

fun toDTOFraHendelse(bekreftelsePåArbeidsforholdHendelse: BekreftelsePåArbeidsforholdHendelse): BekreftelsePåArbeidsforholdHendelseOutboundDTO {
    return BekreftelsePåArbeidsforholdHendelseOutboundDTO(
        bekreftelsePåArbeidsforholdHendelse.id,
        bekreftelsePåArbeidsforholdHendelse.bekreftelseId,
        bekreftelsePåArbeidsforholdHendelse.type,
        bekreftelsePåArbeidsforholdHendelse.stillingsprosent,
        bekreftelsePåArbeidsforholdHendelse.startDato,
        bekreftelsePåArbeidsforholdHendelse.sluttDato
    )
}
