package no.nav.permitteringsportal


import no.nav.permitteringsportal.database.BekreftelsePåArbeidsforhold
import no.nav.permitteringsportal.utils.log
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.errors.WakeupException

import java.io.Closeable
import java.time.Duration

val svarTopic = TopicPartition("pto.stilling-fra-nav-oppdatert-v2", 0)

class BekreftelsePåArbeidsforholService(
    private val consumer: Consumer<String, BekreftelsePåArbeidsforhold>,
): Closeable {
    fun start() {
        try {
            consumer.subscribe(listOf(svarTopic.topic()))
            log.info(
                "Starter å konsumere topic ${svarTopic.topic()} med groupId ${consumer.groupMetadata().groupId()}"
            )

            while (true) {
                val records: ConsumerRecords<String, BekreftelsePåArbeidsforhold> =
                    consumer.poll(Duration.ofSeconds(5))

                if (records.count() == 0) continue
                records.map { it.value() }.forEach {
                    //do something

                }

                consumer.commitSync()

                log.info("Committet offset ${records.last().offset()} til Kafka")
            }
        } catch (exception: WakeupException) {
            log.info("Fikk beskjed om å lukke consument med groupId ${consumer.groupMetadata().groupId()}")
        } catch (exception: Exception) {
            log.error("Feil ved konsumering av svar på bekreftelse av arbeidsforhold.",exception)
            isOk = false
        } finally {
            consumer.close()
        }
    }

    override fun close() {
        // Vil kaste WakeupException i konsument slik at den stopper, thread-safe.
        consumer.wakeup()
    }

    private var isOk = true

    fun isOk() = isOk
}
