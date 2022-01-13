package no.nav.permitteringsportal.kafka

import no.nav.permitteringsportal.DataFraAnsatt
import no.nav.permitteringsportal.utils.log
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.errors.WakeupException
import java.io.Closeable
import java.time.Duration
import java.util.*
import javax.xml.crypto.Data

val svarTopic = "dagpengedok"

class DagpengeMeldingService(
    private val producer: Producer<String, DataFraAnsatt>,
    private val usendteForespørsler: List<DataFraAnsatt>
) {
        fun sendUsendte() {
            if (usendteForespørsler.isNotEmpty()) {
                log.info("Fant ${usendteForespørsler.size} usendte forespørsler")
            }

            usendteForespørsler
                .forEach { usendtForespørsel ->
                        val melding = ProducerRecord(svarTopic, usendtForespørsel.id.toString(), usendtForespørsel)

                        producer.send(melding) { _, exception ->
                            if (exception == null) {
                                log.info("Sendte melding")
                            } else {
                                log.error("Det skjedde noe feil under sending til Kafka", exception)
                            }
                        }
                    }
                }
}
