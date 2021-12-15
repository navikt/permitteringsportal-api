package no.nav.permitteringsportal

import no.nav.permitteringsportal.database.LokalDatabaseConfig
import no.nav.permitteringsportal.setup.issuerConfig
import no.nav.security.mock.oauth2.MockOAuth2Server

// Brukes for å kjøre appen lokalt
fun main() {
    startLokalApp()
}

fun startLokalApp(
    mockOAuth2Server: MockOAuth2Server = MockOAuth2Server()
): App {

    mockOAuth2Server.start(port = 18300)

    val app = App(
        dataSource = LokalDatabaseConfig().dataSource,
        issuerConfig = issuerConfig(mockOAuth2Server)
    )

    app.start()

    return app
}
