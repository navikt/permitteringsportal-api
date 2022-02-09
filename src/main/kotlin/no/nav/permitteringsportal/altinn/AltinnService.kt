package no.nav.permitteringsportal.altinn

class AltinnService {
    fun hentOrganisasjoner(fnr: String): List<AltinnOrganisasjon> {
        return listOf<AltinnOrganisasjon>(
            AltinnOrganisasjon("STORFOSNA OG FREDRIKSTAD REGNSKAP", "Business", "910825569", "BEDR", "Active", "910825550"),
            AltinnOrganisasjon("BALLSTAD OG HORTEN", "Enterprise", "910825550", "AS", "Active", null),
            AltinnOrganisasjon("BAREKSTAD OG YTTERVÅG REGNSKAP", "Enterprise", "910998250", "AS", "Active", null),
            AltinnOrganisasjon("BALLSTAD OG HORTEN", "Business", "810514442", "BEDR", "Active", "910998250")
        )
    }

    fun hentOrganisasjon(fnr: String, orgnr: String): AltinnOrganisasjon {
        val organisasjoner = hentOrganisasjoner(fnr)
        return organisasjoner.first { it.OrganizationNumber == orgnr }
    }
}