package no.nav.permitteringsportal.altinn

class AltinnService(
    private val tokenClient: Oauth2Client
) {

    fun hentOrganisasjon(fnr: String, orgnr: String): AltinnOrganisasjon {

        // Hent organisasjoner fra altinn-rettigheter-proxy

        return AltinnOrganisasjon("","","","","","")
    }

}
