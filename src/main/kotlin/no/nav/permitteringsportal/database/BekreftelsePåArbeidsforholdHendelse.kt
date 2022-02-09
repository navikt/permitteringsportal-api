package no.nav.permitteringsportal.database

import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotliquery.Row
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

const val bekreftelseHendelseTable = "bekreftelse_arbeidsforhold_hendelse"
const val idColumnHendelse = "id"
const val bekrefteldIdColumnHendelse = "bekreftelse_id"
const val typeColumnHendelse = "type"
const val stillingsprosentColumnHendelse = "stillingsprosent"
const val startDatoColumnHendelse = "start_dato"
const val sluttDatoColumnHendelse = "slutt_dato"

@Serializable
data class BekreftelsePåArbeidsforholdHendelse(
    val id: String,
    val bekreftelseId: String,
    val type: String,
    val stillingsprosent: Int,
    val startDato: kotlinx.datetime.LocalDateTime,
    val sluttDato: kotlinx.datetime.LocalDateTime
)

val toBekreftelsePåArbeidsforholdHendelse = { row: Row ->
    BekreftelsePåArbeidsforholdHendelse(
        row.string(idColumnHendelse),
        row.string(bekrefteldIdColumnHendelse),
        row.string(typeColumnHendelse),
        row.int(stillingsprosentColumnHendelse),
        row.localDateTime(startDatoColumnHendelse).toKotlinLocalDateTime(),
        row.localDateTime(sluttDatoColumnHendelse).toKotlinLocalDateTime()
    )
}
