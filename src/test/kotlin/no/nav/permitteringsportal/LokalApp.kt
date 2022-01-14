package no.nav.permitteringsportal

import no.nav.oppsett.mockConsumer
import no.nav.oppsett.mockProducer
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.producer.Producer
import no.nav.permitteringsportal.database.LokalDatabaseConfig
import no.nav.permitteringsportal.kafka.DagpengeMeldingService
import no.nav.permitteringsportal.setup.issuerConfig
import no.nav.security.mock.oauth2.MockOAuth2Server
import java.util.*

// Brukes for å kjøre appen lokalt
fun main() {
    startLokalApp()
}

val uuid: UUID = UUID.randomUUID()
val dataFraAnsatt = DataFraAnsatt(
    uuid, "hello"
)

// Brukes for å kjøre appen i tester
fun startLokalApp(
    mockOAuth2Server: MockOAuth2Server = MockOAuth2Server(),
    consumer: Consumer<String, DataFraAnsatt> = mockConsumer(),
    producer: Producer<String, DataFraAnsatt> = mockProducer(),
    dagpengeMeldingService: DagpengeMeldingService = DagpengeMeldingService(producer, listOf(dataFraAnsatt))

): App {
    mockOAuth2Server.start(port = 18300)
    val app = App(dataSource = LokalDatabaseConfig().dataSource,
        issuerConfig = issuerConfig(mockOAuth2Server),consumer, producer, dagpengeMeldingService)
    app.start()
    return app
}


