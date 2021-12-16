package no.nav.permitteringsportal

import no.nav.oppsett.mockConsumer
import no.nav.oppsett.mockProducer
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.producer.Producer

// Brukes for å kjøre appen lokalt
fun main() {
    startLokalApp()
}

// Brukes for å kjøre appen i tester
fun startLokalApp(
    consumer: Consumer<String, String> = mockConsumer(),
    producer: Producer<String, String> = mockProducer()
): App {
    val app = App(consumer, producer)
    app.start()
    return app
}

