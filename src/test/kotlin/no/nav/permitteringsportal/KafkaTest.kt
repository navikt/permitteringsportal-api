package no.nav.permitteringsportal

import no.nav.oppsett.mockConsumer
import no.nav.oppsett.mockProducer
import no.nav.permitteringsportal.database.LokalDatabaseConfig
import no.nav.permitteringsportal.kafka.BekreftelsePåArbeidsforholdService
import no.nav.security.mock.oauth2.MockOAuth2Server
import org.junit.Test

import org.assertj.core.api.Assertions.assertThat
import java.util.*

class KafkaTest {
    val uuid: UUID = UUID.randomUUID()
    val dataFraAnsatt = DataFraAnsatt(
        uuid, "hello"
    )
    val dataSource  = LokalDatabaseConfig().dataSource
    val mockProducer = mockProducer()
    val mockConsumer = mockConsumer()
    val mockOAuth2Server = MockOAuth2Server()
    val service = BekreftelsePåArbeidsforholdService(mockProducer, listOf(dataFraAnsatt))
    @Test
    fun `skal sende melding pa kafka`() {
        startLokalApp(dataSource, mockOAuth2Server, mockConsumer, mockProducer,).use {
            service.sendUsendte()
            val meldingerSendtPåKafka = mockProducer.history()
            assertThat(meldingerSendtPåKafka.size).isEqualTo(1)
        }

    @Test
    fun `consumenten skal lese av topic`() {
        startLokalApp(dataSource, mockOAuth2Server, mockConsumer, mockProducer,).use {
            mottaKafkamelding(mockConsumer, dataFraAnsatt)
            val forventedeStillinger = listOf(
                dataFraAnsatt
            )
            assertThat(mockConsumer.).isEqualTo(1)
        }
    }


    }}

