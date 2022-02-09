package no.nav.permitteringsportal

import com.zaxxer.hikari.HikariDataSource
import no.nav.oppsett.mockConsumer
import no.nav.oppsett.mockProducer
import no.nav.permitteringsportal.altinn.AltinnService
import no.nav.permitteringsportal.database.BekreftelsePåArbeidsforhold
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.producer.Producer
import no.nav.permitteringsportal.database.LokalDatabaseConfig
import no.nav.permitteringsportal.kafka.BekreftelsePåArbeidsforholdService
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
    consumer: Consumer<String, DataFraAnsatt> = mockConsumer(),
    producer: Producer<String, BekreftelsePåArbeidsforhold> = mockProducer(),
    bekreftelsePåArbeidsforholdService: BekreftelsePåArbeidsforholdService = BekreftelsePåArbeidsforholdService(producer, emptyList()),
    altinnService: AltinnService = AltinnService()
): App {
    val httpClient = getHttpClient()
    val minSideGraphQLKlient = MinSideGraphQLKlient("localhost", httpClient)
    val minSideNotifikasjonerService = MinSideNotifikasjonerService(minSideGraphQLKlient)
    val app = App(dataSource = LokalDatabaseConfig().dataSource,
        issuerConfig ,consumer, producer, bekreftelsePåArbeidsforholdService, minSideNotifikasjonerService, altinnService)
    app.start()
    return app
}
