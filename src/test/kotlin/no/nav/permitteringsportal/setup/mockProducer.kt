package no.nav.oppsett

import no.nav.permitteringsportal.DataFraAnsatt
import org.apache.kafka.clients.producer.MockProducer

fun mockProducer(): MockProducer<String, DataFraAnsatt> {
    return MockProducer(true, null, null)
}
