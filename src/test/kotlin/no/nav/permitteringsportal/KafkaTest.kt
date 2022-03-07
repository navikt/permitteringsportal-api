package no.nav.permitteringsportal

import no.nav.oppsett.mockConsumer
import no.nav.oppsett.mockProducer
import no.nav.permitteringsportal.database.LokalDatabaseConfig
import no.nav.permitteringsportal.kafka.BekreftelsePåArbeidsforholdService
import no.nav.permitteringsportal.setup.issuerConfig
import no.nav.security.mock.oauth2.MockOAuth2Server
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import java.util.*

val uuidMock: UUID = UUID.randomUUID()
val dataFraAnsattMock = DataFraAnsatt(
    uuidMock, "hello", "11111111111"
)

class KafkaTest {
    companion object {
        val dataSource  = LokalDatabaseConfig().dataSource
        val mockProducer = mockProducer()
        val mockConsumer = mockConsumer()
        val mockOAuth2Server = MockOAuth2Server()
        private val service = BekreftelsePåArbeidsforholdService(mockProducer, emptyList())

        init {
            mockOAuth2Server.shutdown()
            mockOAuth2Server.start()
        }
    }

    @After
    fun teardown() {
        mockOAuth2Server.shutdown()
    }

    @Test
    fun `skal sende melding pa kafka`() {
        startLokalApp(dataSource, issuerConfig = issuerConfig(mockOAuth2Server), mockConsumer, mockProducer, service).use {
            val meldingerSendtPåKafka = mockProducer.history()
            assertThat(meldingerSendtPåKafka.size).isEqualTo(0)
        }

       /* @Test
        fun `consumenten skal lese av topic`() {
            startLokalApp(dataSource, mockOAuth2Server, mockConsumer, mockProducer).use {
                mottaKafkamelding(mockConsumer, dataFraAnsattMock)
                val forventedeStillinger = listOf(
                    dataFraAnsattMock
                )
                //assertThat(mockConsumer.).isEqualTo(1)
            }
        }

        */


    }}

