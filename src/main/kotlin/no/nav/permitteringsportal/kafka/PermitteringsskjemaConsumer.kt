package no.nav.permitteringsvarsel.notifikasjon.kafka

import no.nav.permitteringsportal.kafka.permitteringsmeldingtopic
import no.nav.permitteringsportal.utils.log
import no.nav.permitteringsportal.utils.log
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.errors.WakeupException
import java.io.Closeable
import java.time.Duration

class DataFraAnsatt(
        private val consumer: Consumer<String, String>,
) : Closeable {

    fun start() {
        try {
            consumer.subscribe(listOf(permitteringsmeldingtopic))
            log.info("Starter å konsumere topic ${permitteringsmeldingtopic} med groupId ${consumer.groupMetadata().groupId()}")
            while (true) {
                val records: ConsumerRecords<String, String> = consumer.poll(Duration.ofSeconds(5))
                if (records.count() == 0) continue
                consumer.commitSync()

                log.info("Committet offset ${records.last().offset()} til Kafka")
            }
        } catch (exception: WakeupException) {
            log.info("Fikk beskjed om å lukke consument med groupId ${consumer.groupMetadata().groupId()}")
        } catch (exception: Exception) {

            //Liveness.kill("Noe galt skjedde i konsument", exception)
        } finally {
            consumer.close()
        }
    }

    override fun close() {
        // Vil kaste WakeupException i konsument slik at den stopper, thread-safe.
        consumer.wakeup()
    }
}