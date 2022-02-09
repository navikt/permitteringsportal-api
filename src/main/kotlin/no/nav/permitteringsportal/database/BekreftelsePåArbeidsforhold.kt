package no.nav.permitteringsportal.database


import kotlinx.serialization.Serializable
import kotliquery.Row

const val bekreftelseTable = "bekreftelse_arbeidsforhold"
const val idColumn = "id"
const val fnrColumn = "fnr"
const val orgnrColumn = "orgnr"

@Serializable
data class BekreftelsePåArbeidsforhold(
    val id: String,
    val fnr: String,
    val orgnr: String,
    var hendelser: List<BekreftelsePåArbeidsforholdHendelse>
)

val toBekreftelsePåArbeidsforhold = { row: Row ->
    BekreftelsePåArbeidsforhold(
        row.string(idColumn),
        row.string(fnrColumn),
        row.string(orgnrColumn),
        emptyList()
    )
}
