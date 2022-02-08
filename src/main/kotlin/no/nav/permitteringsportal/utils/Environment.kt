package no.nav.permitteringsportal.utils

import java.lang.Exception

object Environment {
    private val miljøvariabler: MutableMap<String, String> = HashMap()
    fun getUrlTilNotifikasjonIMiljo(): String {
            val miljo = System.getenv("NAIS_CLUSTER_NAME")
            if (miljo.equals("prod")){
                return "https://ag-notifikasjon-produsent-api.intern.nav.no/api/graphql"
            }
            return "https://ag-notifikasjon-produsent-api.dev.nav.no/api/graphql"
    }
    fun get(s: String): String? {
        return miljøvariabler[s] ?: System.getenv(s)
    }

    /**
     * For testkode: Bør brukes kun før startLokalApp(...) for å unngå forvirring om hvilken konfig som gjelder
     */
    fun set(s: String, value: String) {
        miljøvariabler[s] = value
    }
}