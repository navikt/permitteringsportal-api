package no.nav.permitteringsportal.database

import kotliquery.Row
import no.nav.permitteringsportal.database.Repository.Companion.id
import no.nav.permitteringsportal.database.Repository.Companion.stillingsprosent

data class BekreftelsePåArbeidsforhold(
    val id: String,
    val stillingsprosent: Int,
)

val toBekreftelsePåArbeidsforhold = { row: Row ->
    BekreftelsePåArbeidsforhold(
        row.string(id),
        row.int(stillingsprosent)
    )
}
