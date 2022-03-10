package no.nav.permitteringsportal

import com.zaxxer.hikari.HikariDataSource
import io.mockk.mockk
import no.nav.permitteringsportal.altinn.AltinnService
import no.nav.permitteringsportal.database.LokalDatabaseConfig
import no.nav.permitteringsportal.minsideklient.MinSideNotifikasjonerService
import no.nav.permitteringsportal.minsideklient.getHttpClient
import no.nav.permitteringsportal.minsideklient.graphql.MinSideGraphQLKlient
import no.nav.permitteringsportal.setup.issuerConfig
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.token.support.ktor.IssuerConfig

// Brukes for å kjøre appen i tester
fun startLokalApp(
    dataSource: HikariDataSource = LokalDatabaseConfig().dataSource,
    issuerConfig: IssuerConfig = issuerConfig(MockOAuth2Server()),
    altinnService: AltinnService = mockk(relaxed = true)
): App {
    val httpClient = getHttpClient()
    val minSideGraphQLKlient = MinSideGraphQLKlient("localhost", httpClient, mockk(relaxed = true))
    val minSideNotifikasjonerService = MinSideNotifikasjonerService(minSideGraphQLKlient, mockk(relaxed = true))
    val app = App(dataSource = LokalDatabaseConfig().dataSource, issuerConfig, minSideNotifikasjonerService, altinnService)
    app.start()
    return app
}
