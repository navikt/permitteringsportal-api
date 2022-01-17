package no.nav.permitteringsportal.database

import java.util.*

const val bekreftelseHendelseTable = "bekreftelse_arbeidsforhold_hendelse"
const val idColumnHendelse = "id"
const val bekrefteldIdColumnHendelse = "bekreftelse_id"
const val stillingsprosentColumnHendelse = "stillingsprosent"
const val startDatoColumnHendelse = "start_dato"
const val sluttDatoColumnHendelse = "slutt_dato"

data class BekreftelsePåArbeidsforholdHendelse(
    val id: String,
    val bekreftelseId: String,
    val stillingsprosent: Int,
    val startDato: Date,
    val sluttDato: Date
) {

}
