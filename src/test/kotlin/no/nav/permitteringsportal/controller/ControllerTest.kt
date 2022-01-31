import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.jackson.objectBody
import no.nav.oppsett.mockConsumer
import no.nav.oppsett.mockProducer
import no.nav.permitteringsportal.database.BekreftelsePåArbeidsforhold
import no.nav.permitteringsportal.database.BekreftelsePåArbeidsforholdHendelse
import no.nav.permitteringsportal.database.LokalDatabaseConfig
import no.nav.permitteringsportal.kafka.BekreftelsePåArbeidsforholdService
import no.nav.permitteringsportal.setup.issuerConfig
import no.nav.permitteringsportal.setup.medArbeidsgiverToken
import no.nav.permitteringsportal.startLokalApp
import no.nav.security.mock.oauth2.MockOAuth2Server

import org.assertj.core.api.Assertions
import org.junit.Test
import java.util.*


class ControllerTest {

    companion object {
        val mockProducer = mockProducer()
        val mockConsumer = mockConsumer()
        val service = BekreftelsePåArbeidsforholdService(mockProducer, emptyList())
        private val mockOAuth2Server = MockOAuth2Server()
        private val dataSource = LokalDatabaseConfig().dataSource
        init {
            mockOAuth2Server.shutdown()
            mockOAuth2Server.start()
        }
    }
    @Test
    fun skal_kunne_legge_til_å_hente_bekreftelse() {
        startLokalApp(dataSource, issuerConfig(mockOAuth2Server), mockConsumer, mockProducer, service).use {
            val nyBekreftelse = BekreftelsePåArbeidsforhold("", "123456789", "123456789")
            val nyHendelse = BekreftelsePåArbeidsforholdHendelse("", "", "NY", 100, Date(), Date())

            Fuel.post("http://localhost:8080/bekreftelse")
                .medArbeidsgiverToken(mockOAuth2Server)
                .objectBody(nyBekreftelse)
                .response()

            // Få tak på id og legg til en hendelse
            Fuel.put("http://localhost:8080/bekreftelse/1234")
                .medArbeidsgiverToken(mockOAuth2Server)
                .objectBody(nyHendelse)
                .response()

            // Hent bekreftelse og verifiser at hendelse kommer med og har riktig data
            val (_, response, _) = Fuel.get("http://localhost:8080/bekreftelse/1234")
                .medArbeidsgiverToken(mockOAuth2Server)
                .response()
            Assertions.assertThat(response.statusCode).isEqualTo(200)
        }

    }
}