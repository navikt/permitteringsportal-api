package no.nav.permitteringsportal

import kotliquery.Row
import no.nav.permitteringsportal.database.BekreftelsePĂ„Arbeidsforhold
import no.nav.permitteringsportal.database.fnrColumn
import no.nav.permitteringsportal.database.idColumn
import no.nav.permitteringsportal.database.orgnrColumn
import java.util.*

const val dataFraAnsattTable = "data_fra_ansatt"
const val idColumnDataFraAnsatt = "id"
const val orgnrColumnDataFraAnsatt = "orgnr"
const val fnrColumnDataFraAnsatt = "fnr"


data class DataFraAnsatt(
        val id: UUID,
        val orgnr: String,
        val fnr: String
)

//val toDataFraAnsatt = { row: Row ->
//        DataFraAnsatt(
//                row.string(idColumnDataFraAnsatt),
//                row.string(orgnrColumnDataFraAnsatt),
//                row.string(fnrColumnDataFraAnsatt)
//        )
//}