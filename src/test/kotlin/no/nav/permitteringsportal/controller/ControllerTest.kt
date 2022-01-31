package no.nav.permitteringsportal.controller

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.jackson.objectBody
import com.github.kittinunf.fuel.jackson.responseObject
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
import no.nav.security.mock.oauth2.http.objectMapper
import org.assertj.core.api.Assertions
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

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

            val (request1, response1, result1) = Fuel.post("http://localhost:8080/bekreftelse")
                .medArbeidsgiverToken(mockOAuth2Server)
                .objectBody(nyBekreftelse)
                .responseString()

            val uuid = result1.component1()
            // Få tak på id og legg til en hendelse
            Fuel.put("http://localhost:8080/bekreftelse/${uuid}")
                .medArbeidsgiverToken(mockOAuth2Server)
                .objectBody(nyHendelse)
                .response()

            // Hent bekreftelse og verifiser at hendelse kommer med og har riktig data
            val bekreftelsePåArbeidsforholdLagret = Fuel.get("http://localhost:8080/bekreftelse/${uuid}")
                .medArbeidsgiverToken(mockOAuth2Server)
                .responseObject<BekreftelsePåArbeidsforhold>(mapper = objectMapper)
                .third
                .get()


            println(bekreftelsePåArbeidsforholdLagret)
            assertEquals(bekreftelsePåArbeidsforholdLagret.fnr, nyBekreftelse.fnr)
            assertEquals(bekreftelsePåArbeidsforholdLagret.orgnr, nyBekreftelse.orgnr)

        }
    }

    @Test
    fun skal_legge_bekreftelse_på_topic_etter_innsendelse() {
        // Legg til bekfreftelse

        // Oppdater med skjema-data (hendelse)

        // Send inn

        // Assert at det legges riktig bekreftelse på topic
        assert(false)
    }

    @Test
    fun skal_finnes_oppgave_å_hente_etter_konsumsjon_av_ping() {
        // Konsumere fra ping-topic

        // Hent fra api

        // Assert at det er riktig data
        assert(false)
    }
}