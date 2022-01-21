package no.nav.oppsett

import no.nav.permitteringsportal.DataFraAnsatt
import no.nav.permitteringsportal.database.BekreftelsePåArbeidsforhold
import org.apache.kafka.clients.producer.MockProducer

fun mockProducer(): MockProducer<String, BekreftelsePåArbeidsforhold> {
    return MockProducer(true, null, null)
}
