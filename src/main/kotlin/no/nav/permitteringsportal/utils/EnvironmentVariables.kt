package no.nav.permitteringsportal.utils

data class EnvironmentVariables(
    val altinnProxyUrl: String,
    val tokenEndpointUrl: String,
    val tokenXClientId: String,
    val tokenXPrivateJWK: String,
    val altinnRettigheterAudience: String,
    val urlTilNotifikasjonIMiljo: String,
    val kafkaBrokers: String,
    val kafkaTruststorePath: String,
    val kafkaCredstorePassword: String,
    val kafkaKeystorePath: String
    )

val tokenEndpointUrl = when(Cluster.current) {
    Cluster.DEV_GCP -> "https://tokendings.dev-gcp.nais.io/token"
    Cluster.PROD_GCP -> "https://tokendings.prod-gcp.nais.io/token"
}

val altinnRettigheterAudience = when(Cluster.current) {
    Cluster.DEV_GCP -> "dev-gcp:arbeidsgiver:altinn-rettigheter-proxy"
    Cluster.PROD_GCP -> "prod-gcp:arbeidsgiver:altinn-rettigheter-proxy"
}

val notifikasjonerAudience = when(Cluster.current) {
    Cluster.DEV_GCP -> "dev-gcp:arbeidsgiver:altinn-rettigheter-proxy"
    Cluster.PROD_GCP -> "prod-gcp:arbeidsgiver:altinn-rettigheter-proxy"
}

val urlTilNotifikasjonIMiljo = when(Cluster.current) {
    Cluster.DEV_GCP -> "https://ag-notifikasjon-produsent-api.dev.nav.no/api/graphql"
    Cluster.PROD_GCP -> "https://ag-notifikasjon-produsent-api.intern.nav.no/api/graphql"
}

val environmentVariables = EnvironmentVariables(
    "http://altinn-rettigheter-proxy.arbeidsgiver/altinn-rettigheter-proxy/ekstern/altinn/api/serviceowner/",
    tokenEndpointUrl,
    System.getenv("TOKEN_X_CLIENT_ID"),
    System.getenv("TOKEN_X_PRIVATE_JWK"),
    altinnRettigheterAudience,
    urlTilNotifikasjonIMiljo,
    System.getenv("KAFKA_BROKERS"),
    System.getenv("KAFKA_TRUSTSTORE_PATH"),
    System.getenv("KAFKA_CREDSTORE_PASSWORD"),
    System.getenv("KAFKA_KEYSTORE_PATH")
)