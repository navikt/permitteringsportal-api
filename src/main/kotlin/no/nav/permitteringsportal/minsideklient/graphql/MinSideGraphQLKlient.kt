package no.nav.permitteringsportal.minsideklient.graphql

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import io.ktor.client.*
import kotlinx.coroutines.runBlocking
import no.nav.permitteringsportal.graphql.`generated"`.OpprettNyBeskjed
import java.net.URL

class MinSideGraphQLKlient(val endpoint: String, val httpClient: HttpClient) {
    fun opprettNyBeskjed(
        virksomhetsnummer: String,
        lenke: String,
        eksternId: String
    ) {

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
            val resultat = client.execute(query);

        }


        return
    }
}
