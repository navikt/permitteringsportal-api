package no.nav.permitteringsportal

import io.mockk.InternalPlatformDsl.toStr
import io.mockk.verify
import no.nav.oppsett.mockConsumer
import no.nav.oppsett.mockProducer
import no.nav.permitteringsportal.setup.dataFraAnsatt
import no.nav.permitteringsportal.setup.mottaKafkamelding
import no.nav.security.mock.oauth2.MockOAuth2Server
import org.junit.Test

import no.nav.permitteringsportal.kafka.permitteringsmeldingtopic

import org.apache.kafka.common.TopicPartition

val svarTopic = TopicPartition(permitteringsmeldingtopic, 0)
class KafkaTest {
    @Test
    fun `skal lese topic fra consumer`() {
        val mockProducer = mockProducer()
        val mockConsumer = mockConsumer()
        val mockOAuth2Server = MockOAuth2Server()
    /*
     startLokalApp(mockOAuth2Server, mockConsumer, mockProducer).use {
            mottaKafkamelding(mockConsumer, dataFraAnsatt)

            val forventetData = listOf(
                dataFraAnsatt
            )
         verify(timeout = 3000) {
             mockOAuth2Server.(forventedeStillinger, hentIndeksNavn(indeksversjon))
         }

         //log("logg " + mockConsumer.subscribe(listOf(svarTopic.topic())).toStr())
         val hei = mockConsumer.subscribe(listOf(svarTopic.topic())).toStr()

            assert(hei.equals(forventetData[0].toString()))
        }

     */
}}