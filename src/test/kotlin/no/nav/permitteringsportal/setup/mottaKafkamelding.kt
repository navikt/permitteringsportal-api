package no.nav.permitteringsportal.setup

import no.nav.permitteringsportal.DataFraAnsatt
import no.nav.permitteringsportal.kafka.permitteringsmeldingtopic
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer

fun mottaKafkamelding(consumer: MockConsumer<String, DataFraAnsatt>, data: DataFraAnsatt, offset: Long = 0) {
    val melding = ConsumerRecord(permitteringsmeldingtopic, 0, offset, data.id.toString(), data)
    consumer.schedulePollTask {
        consumer.addRecord(melding)
    }
}