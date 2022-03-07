package no.nav.permitteringsportal.minsideklient

import no.nav.permitteringsportal.auth.Oauth2Client
import no.nav.permitteringsportal.minsideklient.graphql.MinSideGraphQLKlient

class MinSideNotifikasjonerService(private val minSideGraphQLClient : MinSideGraphQLKlient, private val tokenClient: Oauth2Client) {

    suspend fun sendBeskjed(virksomhetsnummer: String,
                    lenke: String,
                    eksternId: String, token: String?) {

        minSideGraphQLClient.opprettNyBeskjed(virksomhetsnummer, lenke, eksternId )
    }
}
