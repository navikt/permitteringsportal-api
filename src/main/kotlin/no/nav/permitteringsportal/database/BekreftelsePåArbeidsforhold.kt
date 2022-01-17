package no.nav.permitteringsportal.database

import kotliquery.Row

const val bekreftelseTable = "bekreftelse_arbeidsforhold"
const val idColumn = "id"
const val fnrColumn = "fnr"
const val orgnrColumn = "orgnr"

data class BekreftelsePåArbeidsforhold(
    val id: String,
    val fnr: String,
    val orgnr: String
)

val toBekreftelsePåArbeidsforhold = { row: Row ->
    BekreftelsePåArbeidsforhold(
        row.string(idColumn),
        row.string(fnrColumn),
        row.string(orgnrColumn)
    )
}

/*
Data:
    id
    fnr
    bedriftnr
    bedriftNavn
    sendtInnAv
    sendtInnTidspunkt

Om Permitteringen
    tidligerePermittert
    tidligerePermittertFra
    tidligerePermittertTil
    årsak
    varsletAnsattDato
    startDato
    sluttDato
    utbetalingSluttDato
    permittertFulltid
Om arbeidstid for ansatt
    fastArbeidstid <- trengs den hvis man fyller i periode?
    fastArbeidstidPeriodeFra
    fastArbedidtidPeriodeTil
    avspaseringFra
    avspaseringTil
    sykepengerUtbetaltUker
    sykepengerUtbetaltDager
    harOpptjentFerie
Om Enighet
    enighet
    enighetDokument (fil lagres hvordan?)
    bedriftMistetLisens


Stort sett alt her er direkte koblet til en ansatt. Trenger ikke normalisere.

Men: Skal vi kunne oppdatere? eller være immutable og lage nye hver innsending for historikken?

Burde ha en "Hendelse"-tabell hvor man får immutable for hver innsending. Altså at hver innsending skaper en ny linje

Altså:

Tabeller:
AnsattBekreftelse
 - Id
 - Fnr
 - Orgnr
 - Bedrift
 - dagpengeSøknadsDato
 - dagpengeSøknadID ?

Skjemahendelse
 - Id
 - id på bekreftelse - FK til AnsattBekreftelse
 - Type (Innsendt, endring, sletting..)
 - All data fra sjema
 */
