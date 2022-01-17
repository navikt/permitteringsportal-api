package no.nav.permitteringsportal.database

import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using
import java.util.*
import javax.sql.DataSource

class Repository(private val dataSource: DataSource) {

    fun leggTilNyBekreftelse(fnr: String, orgnr: String, stillingsprosent: Int, startDato: Date, sluttDato: Date): String {
        val uuidBekreftelse = UUID.randomUUID().toString()
        val uuidBekreftelseHendelse = UUID.randomUUID().toString()
        val bekreftelseQuery = queryOf(

            """
                insert into $bekreftelseTable ($idColumn, $fnrColumn, $orgnrColumn)
                values (?, ?, ?)
            """.trimIndent(),
            uuidBekreftelse,
            fnr,
            orgnr
        ).asUpdate
        val bekfreftelseHendelseQuery = queryOf(
            """
               insert into $bekreftelseHendelseTable ($idColumnHendelse, $bekrefteldIdColumnHendelse, $stillingsprosentColumnHendelse, $startDatoColumnHendelse, $sluttDatoColumnHendelse) 
               values (?, ?, ?, ?, ?)
            """.trimIndent(),
            uuidBekreftelseHendelse,
            uuidBekreftelse,
            stillingsprosent,
            startDato,
            sluttDato
        ).asUpdate

        val rowsAffected = using(sessionOf(dataSource)) {
            it.transaction { tx ->
                tx.run(bekreftelseQuery)
                tx.run(bekfreftelseHendelseQuery)
            }
        }

        return uuidBekreftelse
    }

    fun leggTilNyHendelsePåBekreftelse(bekreftelseId: String, stillingsprosent: Int, startDato: Date, sluttDato: Date): String {
        val uuidBekreftelseHendelse = UUID.randomUUID().toString()
        val bekreftelseHendelseQuery = queryOf(
            """
               insert into $bekreftelseHendelseTable ($idColumnHendelse, $bekrefteldIdColumnHendelse, $stillingsprosentColumnHendelse, $startDatoColumnHendelse, $sluttDatoColumnHendelse) 
               values (?, ?, ?, ?, ?)
            """.trimIndent(),
            uuidBekreftelseHendelse,
            bekreftelseId,
            stillingsprosent,
            startDato,
            sluttDato
        ).asUpdate

        val rowsAffected = using(sessionOf(dataSource)) {
            it.transaction { tx ->
                tx.run(bekreftelseHendelseQuery)
            }
        }
        return uuidBekreftelseHendelse
    }

    fun hentAlleBekreftelserForOrganisasjon(orgnr: String): List<BekreftelsePåArbeidsforhold> {
        val query = queryOf("""
            select * from $bekreftelseTable where $orgnrColumn = ?
        """.trimIndent(),
            orgnr
            ).map(toBekreftelsePåArbeidsforhold)
            .asList

        return using(sessionOf(dataSource)) { it.run(query) }
    }



    fun hentAlleHendelserForBekreftelseOgOrganisasjon(orgnr: String): List <BekreftelsePåArbeidsforholdHendelse> {
        val alleBekreftelserForOrganisasjon = hentAlleBekreftelserForOrganisasjon(orgnr)


        return emptyList()
    }

//    fun hentBekreftelseHendelse(id: String): BekreftelsePåArbeidsforholdHendelse {
//        return
//    }

}
