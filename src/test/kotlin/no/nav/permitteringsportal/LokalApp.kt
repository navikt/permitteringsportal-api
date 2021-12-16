package no.nav.permitteringsportal

import no.nav.oppsett.mockConsumer
import no.nav.oppsett.mockProducer
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.producer.Producer
import no.nav.permitteringsportal.database.LokalDatabaseConfig
import no.nav.permitteringsportal.setup.issuerConfig
import no.nav.security.mock.oauth2.MockOAuth2Server

// Brukes for å kjøre appen lokalt
fun main() {
    startLokalApp()
}

// Brukes for å kjøre appen i tester
fun startLokalApp(
    mockOAuth2Server: MockOAuth2Server = MockOAuth2Server(),
    consumer: Consumer<String, String> = mockConsumer(),
    producer: Producer<String, String> = mockProducer()
): App {
    mockOAuth2Server.start(port = 18300)
    val app = App(dataSource = LokalDatabaseConfig().dataSource,
        issuerConfig = issuerConfig(mockOAuth2Server),consumer, producer)
    app.start()
    return app
}


