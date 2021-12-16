package no.nav.permitteringsportal

import java.time.LocalDate
import java.util.*

data class DataFraAnsatt(
        val id: UUID,
        val bedriftsnummer: String,
        val fnr: String,
        val sendtInnTidspunkt: Date,
        val varsletAnsattDato: LocalDate?,
        val varsletNavDato: LocalDate?,
        val startDato: LocalDate?
)