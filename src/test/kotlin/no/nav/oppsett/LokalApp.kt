package no.nav.oppsett

import no.nav.permitteringsportal.App
import org.apache.kafka.clients.consumer.Consumer

// Brukes for å kjøre appen lokalt
fun main() {
    startLokalApp()
}

// Brukes for å kjøre appen i tester
fun startLokalApp(
    consumer: Consumer<String, String> = mockConsumer()
): App {
    val app = App(consumer)
    app.start()
    return app
}
