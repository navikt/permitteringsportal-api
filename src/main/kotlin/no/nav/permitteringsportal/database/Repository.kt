package no.nav.permitteringsportal.database

import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using
import java.util.*
import javax.sql.DataSource

class Repository(private val dataSource: DataSource) {

    fun insert(prosent: Int): Int {
        val uuid = UUID.randomUUID().toString()
        val query = queryOf(
            "insert into $table ($id, $stillingsprosent) values (?, ?)",
            uuid,
            prosent
        ).asUpdate

        return using(sessionOf(dataSource)) { it.run(query) }
    }

    fun hentAlt(): List<BekreftelsePåArbeidsforhold> {
        val query = queryOf("select * from $table")
            .map(toBekreftelsePåArbeidsforhold)
            .asList

        return using(sessionOf(dataSource)) { it.run(query) }
    }

    companion object {
        const val table = "bekreftelse_arbeidsforhold"
        const val id = "id"
        const val stillingsprosent = "stillingsprosent"
    }
}

