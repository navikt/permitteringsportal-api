package no.nav.permitteringsportal

import no.nav.permitteringsportal.setup.LokalDatabaseConfig
import no.nav.security.mock.oauth2.MockOAuth2Server

// Brukes for å kjøre appen lokalt
fun main() {
    startLokalApp()
}

// Brukes for å kjøre appen i tester
fun startLokalApp(): App {
    val mockOAuth2Server = MockOAuth2Server()
    mockOAuth2Server.start(port = 18300)

    val app = App(
        dataSource = LokalDatabaseConfig().dataSource
    )

    app.start()

    return app
}
