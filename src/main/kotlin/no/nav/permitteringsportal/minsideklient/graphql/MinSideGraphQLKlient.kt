package no.nav.permitteringsportal.minsideklient.graphql

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import io.ktor.client.*
import kotlinx.coroutines.runBlocking
import no.nav.permitteringsportal.altinn.Oauth2Client
import no.nav.permitteringsportal.graphql.`generated"`.OpprettNyBeskjed
import no.nav.permitteringsportal.utils.environmentVariables
import java.net.URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class MinSideGraphQLKlient(val endpoint: String, val httpClient: HttpClient, val tokenClient: Oauth2Client,) {
    suspend fun opprettNyBeskjed(
        virksomhetsnummer: String,
        lenke: String,
        eksternId: String,
    ) {

            val scopedAccessToken = tokenClient.machine2machine(environmentVariables.azureADTokenEndpointUrl, environmentVariables.notifikasjonerScope).accessToken

            val client = GraphQLKtorClient(
                url = URL(endpoint),
                httpClient = httpClient
            )

            runBlocking {
                val query = OpprettNyBeskjed(variables = OpprettNyBeskjed.Variables(
                    virksomhetsnummer,
                    eksternId,
                    lenke
                ));
                val resultat = client.execute(query) {
                    header(HttpHeaders.Authorization, "Bearer $scopedAccessToken")
                };
            }


        return
    }
}
