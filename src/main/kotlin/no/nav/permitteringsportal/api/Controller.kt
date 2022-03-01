import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import no.nav.permitteringsportal.altinn.AltinnService
import no.nav.permitteringsportal.database.BekreftelsePåArbeidsforhold
import no.nav.permitteringsportal.database.BekreftelsePåArbeidsforholdHendelse
import no.nav.permitteringsportal.database.Repository
import no.nav.permitteringsportal.minsideklient.MinSideNotifikasjonerService
import no.nav.permitteringsportal.utils.getFnrFraToken
import no.nav.permitteringsportal.utils.log
import no.nav.permitteringsportal.utils.urlTilNotifikasjonIMiljo

fun Route.sjekkInnlogget() {
    get("/permitteringsportal-api/api/sjekk-innlogget") {
        call.respond(HttpStatusCode.OK)
    }
}
fun Route.hentOrganisasjoner(altinnService: AltinnService) {
    get("/permitteringsportal-api/api/organisasjoner") {
        call.respond(altinnService.hentOrganisasjoner(this.context.getAccessToken()))
    }
}

fun Route.hentOppgaver(repository: Repository) {
    get("/oppgave") {
        call.respond(HttpStatusCode.OK)
        // repository.hentOppgaver(orgnr)
    }
}

fun Route.sendInnBekreftelse(repository: Repository,minSideNotifikasjonerService: MinSideNotifikasjonerService) {
    post("/permitteringsportal-api/api/send-bekreftelse") {
        call.respond(HttpStatusCode.Created)
        repository.leggTilNyBekreftelse("fnr", "orgnr")
        minSideNotifikasjonerService.sendBeskjed("99999999", urlTilNotifikasjonIMiljo,"eksternid")
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
        // val altinnOrganisasjoner = fnr?.let { it -> altinnService.hentOrganisasjon(this.context.getAccessToken(), it, nyBekreftelse.orgnr) }

        val uuid = repository.leggTilNyBekreftelse(nyBekreftelse.fnr, nyBekreftelse.orgnr)
        call.respond(uuid)

    }
}

fun Route.oppdaterBekreftelse(repository: Repository) {
    put("/bekreftelse/{id}") {

    }
}

private fun ApplicationCall.getAccessToken(): String? {
    val authorizationHeader = request.parseAuthorizationHeader()
    if(authorizationHeader is HttpAuthHeader.Single && authorizationHeader.authScheme == "Bearer") {
        return authorizationHeader.blob
    }
    return null
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
