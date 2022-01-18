package no.nav.permitteringsportal

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