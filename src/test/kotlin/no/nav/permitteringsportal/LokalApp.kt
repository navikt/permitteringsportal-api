package no.nav.permitteringsportal

import com.zaxxer.hikari.HikariDataSource
import no.nav.oppsett.mockConsumer
import no.nav.oppsett.mockProducer
import no.nav.permitteringsportal.database.BekreftelsePåArbeidsforhold
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.producer.Producer
import no.nav.permitteringsportal.database.LokalDatabaseConfig
import no.nav.permitteringsportal.kafka.BekreftelsePåArbeidsforholdService
import no.nav.permitteringsportal.setup.issuerConfig
import no.nav.security.mock.oauth2.MockOAuth2Server
import java.util.*

// Brukes for å kjøre appen lokalt
fun main() {
    startLokalApp()
}

val uuid: UUID = UUID.randomUUID()
val dataFraAnsatt = DataFraAnsatt(
    uuid.toString(), "hello", "11111111111"
)

// Brukes for å kjøre appen i tester
fun startLokalApp(
    dataSource: HikariDataSource = LokalDatabaseConfig().dataSource,
    mockOAuth2Server: MockOAuth2Server = MockOAuth2Server(),
    producer: Producer<String, BekreftelsePåArbeidsforhold> = mockProducer(),
    consumer: Consumer<String, DataFraAnsatt> = mockConsumer()
): App {
    mockOAuth2Server.start(port = 18300)
    val app = App(dataSource = LokalDatabaseConfig().dataSource,
        issuerConfig = issuerConfig(mockOAuth2Server),producer, consumer)
    app.start()
    return app
}


