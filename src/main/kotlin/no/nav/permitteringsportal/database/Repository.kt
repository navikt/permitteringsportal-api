package no.nav.permitteringsportal.database

import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using
import no.nav.permitteringsportal.*
import no.nav.permitteringsportal.fnrColumnDataFraAnsatt
import java.lang.IllegalArgumentException
import java.util.*
import javax.sql.DataSource

class Repository(private val dataSource: DataSource) {

    fun leggTilNyBekreftelse(fnr: String, orgnr: String, type: String, stillingsprosent: Int, startDato: Date, sluttDato: Date): String {
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
               insert into $bekreftelseHendelseTable ($idColumnHendelse, $bekrefteldIdColumnHendelse, $typeColumnHendelse, $stillingsprosentColumnHendelse, $startDatoColumnHendelse, $sluttDatoColumnHendelse) 
               values (?, ?, ?, ?, ?, ?)
            """.trimIndent(),
            uuidBekreftelseHendelse,
            uuidBekreftelse,
            type,
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

    fun leggTilNyHendelsePåBekreftelse(bekreftelseId: String, type: String, stillingsprosent: Int, startDato: Date, sluttDato: Date): String {
        val uuidBekreftelseHendelse = UUID.randomUUID().toString()
        val bekreftelseHendelseQuery = queryOf(
            """
               insert into $bekreftelseHendelseTable ($idColumnHendelse, $bekrefteldIdColumnHendelse, $typeColumnHendelse, $stillingsprosentColumnHendelse, $startDatoColumnHendelse, $sluttDatoColumnHendelse) 
               values (?, ?, ?, ?, ?, ?)
            """.trimIndent(),
            uuidBekreftelseHendelse,
            bekreftelseId,
            type,
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

    fun hentBekreftelseMedId(bekreftelseId: String): BekreftelsePåArbeidsforhold? {
        val query = queryOf(
            """
            select * from $bekreftelseTable where $idColumn = ?
        """.trimIndent(),
            bekreftelseId
        ).map(toBekreftelsePåArbeidsforhold).asSingle

        return using(sessionOf(dataSource)) { it.run(query) }
    }

    fun hentAlleHendelserForBekreftelseOgOrganisasjon(orgnr: String, bekreftelseId: String): List<BekreftelsePåArbeidsforholdHendelse> {
        val bekreftelse = hentBekreftelseMedId(bekreftelseId)

        if (bekreftelse != null) {
            if(bekreftelse.orgnr != orgnr) {
                throw IllegalArgumentException("Ikke sammenheng mellom orgnr i bekreftelse og hendelse")
            }
            val query = queryOf("""
                select * from $bekreftelseHendelseTable where $bekrefteldIdColumnHendelse = ?
            """.trimIndent(), bekreftelseId
            ).map(toBekreftelsePåArbeidsforholdHendelse).asList

            return using(sessionOf(dataSource)) { it.run(query) }
        }
        return emptyList()
    }

    fun leggTilNyOppgave(dataFraAnsatt: DataFraAnsatt): String {
        val uuidNyOppgave = UUID.randomUUID().toString()
        val leggTilNyOppgaveQuery = queryOf(
            """
               insert into $dataFraAnsattTable ($idColumnDataFraAnsatt, $orgnrColumnDataFraAnsatt, $fnrColumnDataFraAnsatt) 
               values (?, ?, ?)
            """.trimIndent(),
            uuidNyOppgave,
            dataFraAnsatt.orgnr,
            dataFraAnsatt.fnr
        ).asUpdate
        val rowsAffected = using(sessionOf(dataSource)) {
            it.transaction { tx ->
                tx.run(leggTilNyOppgaveQuery)
            }
        }
        return uuidNyOppgave
    }

    fun hentOppgave(oppgaveId: String): BekreftelsePåArbeidsforhold?{
        val query = queryOf(
            """
            select * from $dataFraAnsattTable where $idColumnDataFraAnsatt = ?
        """.trimIndent(),
            oppgaveId
        ).map(toBekreftelsePåArbeidsforhold).asSingle

        return using(sessionOf(dataSource)) { it.run(query) }
    }

//    fun hentBekreftelseHendelse(id: String): BekreftelsePåArbeidsforholdHendelse {
//        return
//    }

}
