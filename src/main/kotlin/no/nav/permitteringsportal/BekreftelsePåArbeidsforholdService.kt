package no.nav.permitteringsportal


import no.nav.permitteringsportal.database.BekreftelsePåArbeidsforhold
import no.nav.permitteringsportal.database.Repository
import no.nav.permitteringsportal.utils.log
import no.nav.permitteringsvarsel.notifikasjon.kafka.DataFraAnsattConsumer
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.errors.WakeupException

import java.io.Closeable
import java.time.Duration

val svarTopic = TopicPartition("pto.stilling-fra-nav-oppdatert-v2", 0)

class BekreftelsePåArbeidsforholService(
    private val consumer: Consumer<String, DataFraAnsatt>,
    private val repository: Repository
): Closeable {
    private val dataFraAnsattConsumer = DataFraAnsattConsumer(consumer, repository)
    fun start() {
        dataFraAnsattConsumer.start()
    }

    override fun close() {
        // Vil kaste WakeupException i konsument slik at den stopper, thread-safe.
        consumer.wakeup()
    }

    private var isOk = true

    fun isOk() = isOk
}
