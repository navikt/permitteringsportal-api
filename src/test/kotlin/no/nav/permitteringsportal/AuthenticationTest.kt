package no.nav.permitteringsportal

import com.github.kittinunf.fuel.Fuel
import no.nav.oppsett.mockConsumer
import no.nav.oppsett.mockProducer
import no.nav.permitteringsportal.kafka.DagpengeMeldingService
import no.nav.permitteringsportal.database.LokalDatabaseConfig
import no.nav.permitteringsportal.setup.medArbeidsgiverToken
import no.nav.security.mock.oauth2.MockOAuth2Server
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class AuthenticationTest {

    companion object {

        val mockProducer = mockProducer()
        val mockConsumer = mockConsumer()
        val uuid: UUID = UUID.randomUUID()
        val dataFraAnsatt = DataFraAnsatt(
            uuid, "hello"
        )
        val service = DagpengeMeldingService(mockProducer, listOf(dataFraAnsatt))
        private val mockOAuth2Server = MockOAuth2Server()
        private val dataSource = LokalDatabaseConfig().dataSource
        init {
            startLokalApp(dataSource, mockOAuth2Server, mockConsumer, mockProducer,service )
        }
    }

    @Test
    fun `Skal ikke nå endepunkt uten token`() {
        val (_, response, _) = Fuel.get("http://localhost:8080/sikret-endepunkt").response()
        assertThat(response.statusCode).isEqualTo(401)
    }

    @Test
    fun `Skal nå endepunkt med token`() {
        val (_, response, _) = Fuel.get("http://localhost:8080/sikret-endepunkt")
            .medArbeidsgiverToken(mockOAuth2Server)
            .response()
        assertThat(response.statusCode).isEqualTo(200)
    }
}
