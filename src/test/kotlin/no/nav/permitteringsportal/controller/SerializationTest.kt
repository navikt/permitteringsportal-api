package no.nav.permitteringsportal.controller

import BekreftelsePåArbeidsforholdHendelseOutboundDTO
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.Test
import kotlin.test.assertNotNull


class SerializationTest {

    @Test
    fun skal_kunne_serialisere_datoer() {
        val mapper = jacksonObjectMapper().apply {
            registerModule(JavaTimeModule())
        }
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        mapper.dateFormat = StdDateFormat().withColonInTimeZone(true)
        val startDato = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val sluttDato = Clock.System.now().toLocalDateTime(TimeZone.UTC)

        val nyHendelse = BekreftelsePåArbeidsforholdHendelseOutboundDTO("", "", "NY", 100, startDato, sluttDato)

        val serialisertHendelse = mapper.writeValueAsString(nyHendelse)

        assertNotNull(serialisertHendelse)
        println(serialisertHendelse)
    }
}