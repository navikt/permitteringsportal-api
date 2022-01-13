package no.nav.permitteringsportal

import io.mockk.InternalPlatformDsl.toStr
import io.mockk.verify
import no.nav.oppsett.mockConsumer
import no.nav.oppsett.mockProducer
import no.nav.permitteringsportal.kafka.DagpengeMeldingService
import no.nav.permitteringsportal.setup.dataFraAnsatt
import no.nav.permitteringsportal.setup.mottaKafkamelding
import no.nav.security.mock.oauth2.MockOAuth2Server
import org.junit.Test

import no.nav.permitteringsportal.kafka.permitteringsmeldingtopic

import org.apache.kafka.common.TopicPartition
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class KafkaTest {
    @Test
    fun `skal lese topic fra consumer`() {
        val uuid: UUID = UUID.randomUUID()
        val dataFraAnsatt = DataFraAnsatt(
            uuid, "hello"
        )

        val mockProducer = mockProducer()
        val mockConsumer = mockConsumer()
        val mockOAuth2Server = MockOAuth2Server()
        val service = DagpengeMeldingService(mockProducer, listOf(dataFraAnsatt))
        startLokalApp(mockOAuth2Server, mockConsumer, mockProducer,).use {
            service.sendUsendte()
            val meldingerSendtPåKafka = mockProducer.history()
            assertThat(meldingerSendtPåKafka.size).isEqualTo(1)
        }
    }}