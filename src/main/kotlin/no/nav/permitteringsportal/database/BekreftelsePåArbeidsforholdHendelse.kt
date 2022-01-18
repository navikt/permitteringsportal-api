package no.nav.permitteringsportal.database

import kotliquery.Row
import java.util.*

const val bekreftelseHendelseTable = "bekreftelse_arbeidsforhold_hendelse"
const val idColumnHendelse = "id"
const val bekrefteldIdColumnHendelse = "bekreftelse_id"
const val typeColumnHendelse = "type"
const val stillingsprosentColumnHendelse = "stillingsprosent"
const val startDatoColumnHendelse = "start_dato"
const val sluttDatoColumnHendelse = "slutt_dato"

data class BekreftelsePåArbeidsforholdHendelse(
    val id: String,
    val bekreftelseId: String,
    val type: String,
    val stillingsprosent: Int,
    val startDato: Date,
    val sluttDato: Date
)

val toBekreftelsePåArbeidsforholdHendelse = { row: Row ->
    BekreftelsePåArbeidsforholdHendelse(
        row.string(idColumnHendelse),
        row.string(bekrefteldIdColumnHendelse),
        row.string(typeColumnHendelse),
        row.int(stillingsprosentColumnHendelse),
        row.sqlDate(startDatoColumnHendelse),
        row.sqlDate(sluttDatoColumnHendelse)
    )
}
