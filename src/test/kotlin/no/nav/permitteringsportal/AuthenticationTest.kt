package no.nav.permitteringsportal

import com.github.kittinunf.fuel.Fuel
import no.nav.oppsett.mockConsumer
import no.nav.oppsett.mockProducer
import no.nav.permitteringsportal.altinn.AltinnService
import no.nav.permitteringsportal.kafka.BekreftelsePåArbeidsforholdService
import no.nav.permitteringsportal.database.LokalDatabaseConfig
import no.nav.permitteringsportal.setup.issuerConfig
import no.nav.permitteringsportal.setup.medArbeidsgiverToken
import no.nav.security.mock.oauth2.MockOAuth2Server
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AuthenticationTest {

    companion object {

        private val mockProducer = mockProducer()
        private val mockConsumer = mockConsumer()
        private val service = BekreftelsePåArbeidsforholdService(mockProducer, emptyList())
        private val altinnService = AltinnService()
        private val mockOAuth2Server = MockOAuth2Server()
        private val dataSource = LokalDatabaseConfig().dataSource
        init {
            mockOAuth2Server.shutdown()
            mockOAuth2Server.start()
        }
    }

    @Test
    fun `Skal ikke nå endepunkt uten token`() {
        startLokalApp(dataSource, issuerConfig = issuerConfig(mockOAuth2Server), mockConsumer, mockProducer, service, altinnService).use {
            val (_, response, _) = Fuel.get("http://localhost:8080/bekreftelse/12345").response()
            assertThat(response.statusCode).isEqualTo(401)
        }
    }

    @Test
    fun `Skal nå endepunkt med token`() {

        startLokalApp(dataSource, issuerConfig = issuerConfig(mockOAuth2Server), mockConsumer, mockProducer, service, altinnService).use {
            val (_, response, _) = Fuel.get("http://localhost:8080/bekreftelse/12345")
                .medArbeidsgiverToken(mockOAuth2Server)
                .response()
            assertThat(response.statusCode).isEqualTo(404) // 404 er ok her
        }
    }
}
