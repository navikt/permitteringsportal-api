package no.nav.permitteringsportal.altinn

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import no.nav.permitteringsportal.utils.environmentVariables
import no.nav.permitteringsportal.utils.log

private const val ALTINN_ORG_PAGE_SIZE = 500

class AltinnService(
    private val tokenClient: Oauth2Client,
    private val httpClient: HttpClient,
    private val altinnProxyUrl: String
) {

    suspend fun hentOrganisasjoner(token: String?): List<AltinnOrganisasjon> {
        val query = "&\$filter=Type+ne+'Person'+and+Status+eq+'Active'"

        return hentOrganisasjonerFraAltinn(token, query)
    }

    suspend fun hentOrganisasjonerBasertPåRettigheter(token: String, serviceKode: String, serviceEdition: String): List<AltinnOrganisasjon> {
        val query = ("&\$filter=Type+ne+'Person'+and+Status+eq+'Active'"
                + "&serviceCode=" + serviceKode
                + "&serviceEdition=" + serviceEdition)

        return hentOrganisasjonerFraAltinn(token, query)
    }

    suspend fun hentOrganisasjonerFraAltinn(token: String?, query: String): List<AltinnOrganisasjon> {
        log.info("hentOrganisasjonerFraAltinn")
        token?.let {
            val scopedAccessToken = tokenClient.exchangeToken(token, environmentVariables.altinnRettigheterAudience).accessToken

            val url: String = altinnProxyUrl + "reportees/?ForceEIAuthentication" + query

            var pageNumber = 0
            var hasMore = true
            var altinnOrganisasjoner = emptyList<AltinnOrganisasjon>()
            while (hasMore) {
                pageNumber++
                val urlWithPagesizeAndOffset =
                    url + "&\$top=" + ALTINN_ORG_PAGE_SIZE + "&\$skip=" + (pageNumber - 1) * ALTINN_ORG_PAGE_SIZE
                altinnOrganisasjoner = httpClient.get(urlWithPagesizeAndOffset) {
                    header(HttpHeaders.Authorization, "Bearer $scopedAccessToken")
                }
                // Todo: Handle pages when more than 500 entities
                hasMore = false

            }
            return altinnOrganisasjoner
        }
        log.warn("Ingen access token i request, kan ikke hente ny token til altinn-proxy")
        return emptyList()
    }

}
