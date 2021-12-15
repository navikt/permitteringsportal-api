package no.nav.permitteringsportal

import no.nav.permitteringsportal.setup.LokalDatabaseConfig
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.token.support.ktor.IssuerConfig

// Brukes for å kjøre appen lokalt
fun main() {
    startLokalApp()
}

fun startLokalApp(
    mockOAuth2Server: MockOAuth2Server = MockOAuth2Server()
): App {

    mockOAuth2Server.start(port = 18300)

    val issuer = "default"
    val acceptedAudience = "default"
    val issuerConfig = IssuerConfig(
        name = "arbeidsgiver",
        discoveryUrl = mockOAuth2Server.wellKnownUrl(issuer).toString(),
        acceptedAudience = listOf(acceptedAudience)
    )

    val app = App(
        dataSource = LokalDatabaseConfig().dataSource,
        issuerConfig = issuerConfig
    )

    app.start()

    return app
}
