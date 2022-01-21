package no.nav.permitteringsportal

import kotliquery.Row

const val dataFraAnsattTable = "data_fra_ansatt"
const val idColumnDataFraAnsatt = "id"
const val orgnrColumnDataFraAnsatt = "orgnr"
const val fnrColumnDataFraAnsatt = "fnr"


data class DataFraAnsatt(
        val id: String,
        val orgnr: String,
        val fnr: String
)

val toDataFraAnsatt = { row: Row ->
        DataFraAnsatt(
                row.string(idColumnDataFraAnsatt),
                row.string(orgnrColumnDataFraAnsatt),
                row.string(fnrColumnDataFraAnsatt)
        )
}