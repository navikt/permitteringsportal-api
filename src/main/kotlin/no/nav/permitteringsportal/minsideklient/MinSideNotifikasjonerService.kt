package no.nav.permitteringsportal.minsideklient

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import io.ktor.client.*
import no.nav.permitteringsportal.altinn.Oauth2Client
import no.nav.permitteringsportal.minsideklient.graphql.MinSideGraphQLKlient
import no.nav.permitteringsportal.utils.environmentVariables
import java.net.URL

class MinSideNotifikasjonerService(private val minSideGraphQLClient : MinSideGraphQLKlient, private val tokenClient: Oauth2Client) {

    suspend fun sendBeskjed(virksomhetsnummer: String,
                    lenke: String,
                    eksternId: String, token: String?) {

        minSideGraphQLClient.opprettNyBeskjed(virksomhetsnummer, lenke, eksternId )
    }
}
