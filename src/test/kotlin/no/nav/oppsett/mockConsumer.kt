package no.nav.oppsett

import no.nav.permitteringsportal.kafka.permitteringsmeldingtopic
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.common.TopicPartition
import java.util.*

val topic = TopicPartition(permitteringsmeldingtopic, 0)

fun mockConsumer() = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST).apply {
    schedulePollTask {
        rebalance(listOf(topic))
        updateBeginningOffsets(mapOf(Pair(topic, 0)))
        addRecord(ConsumerRecord(permitteringsmeldingtopic, 0, 0, UUID.randomUUID().toString(), "lokal melding"))
    }
}
