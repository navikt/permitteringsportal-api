package no.nav.permitteringsportal.minsideklient

import no.nav.permitteringsportal.minsideklient.graphql.MinSideGraphQLKlient

class MinSideNotifikasjonerService(private val minSideGraphQLClient : MinSideGraphQLKlient) {

    fun sendBeskjed(virksomhetsnummer: String,
                    lenke: String,
                    eksternId: String) {
        minSideGraphQLClient.opprettNyBeskjed(virksomhetsnummer, lenke, eksternId)
    }
}
