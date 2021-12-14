package no.nav.permitteringsportal

import no.nav.permitteringsportal.setup.LokalDatabaseConfig
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.token.support.ktor.IssuerConfig

// Brukes for å kjøre appen lokalt
fun main() {
    startLokalApp()
}

// Brukes for å kjøre appen i tester
fun startLokalApp(): App {
    val mockOAuth2Server = MockOAuth2Server()
    mockOAuth2Server.start(port = 18300)

    val issuer = "default"
    val issuerConfig = IssuerConfig(
        name = issuer,
        discoveryUrl = "http://localhost:18300/$issuer/.well-known/openid-configuration",
        acceptedAudience = listOf("default")
    )

    val app = App(
        dataSource = LokalDatabaseConfig().dataSource,
        issuerConfig = issuerConfig
    )

    app.start()

    return app
}
