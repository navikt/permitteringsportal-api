package no.nav.permitteringsportal.utils

data class EnvironmentVariables(
    val altinnProxyUrl: String,
    val tokenXEndpointUrl: String,
    val tokenXClientId: String,
    val tokenXPrivateJWK: String,
    val azureADTokenEndpointUrl: String,
    val azureJWK: String,
    val azureClientId: String,
    val altinnRettigheterAudience: String,
    val urlTilNotifikasjonIMiljo: String,
    val notifikasjonerScope: String,
    val kafkaBrokers: String,
    val kafkaTruststorePath: String,
    val kafkaCredstorePassword: String,
    val kafkaKeystorePath: String
    ) {

}

val tokenXEndpointUrl = when(Cluster.current) {
    Cluster.DEV_GCP -> "https://tokendings.dev-gcp.nais.io/token"
    Cluster.PROD_GCP -> "https://tokendings.prod-gcp.nais.io/token"
}

val azureADTokenEndpointUrl = when(Cluster.current) {
    Cluster.DEV_GCP -> "https://tokendings.dev-gcp.nais.io/token"
    Cluster.PROD_GCP -> "https://tokendings.prod-gcp.nais.io/token"
}

val altinnRettigheterAudience = when(Cluster.current) {
    Cluster.DEV_GCP -> "dev-gcp:arbeidsgiver:altinn-rettigheter-proxy"
    Cluster.PROD_GCP -> "prod-gcp:arbeidsgiver:altinn-rettigheter-proxy"
}

val notifikasjonerScope = when(Cluster.current) {
    Cluster.DEV_GCP -> "dev-gcp:arbeidsgiver:notifikasjon-produsent-api"
    Cluster.PROD_GCP -> "prod-gcp:arbeidsgiver:notifikasjon-produsent-api"
}

val urlTilNotifikasjonIMiljo = when(Cluster.current) {
    Cluster.DEV_GCP -> "https://ag-notifikasjon-produsent-api.dev.nav.no/api/graphql"
    Cluster.PROD_GCP -> "https://ag-notifikasjon-produsent-api.intern.nav.no/api/graphql"
}

val environmentVariables = EnvironmentVariables(
    "http://altinn-rettigheter-proxy.arbeidsgiver/altinn-rettigheter-proxy/ekstern/altinn/api/serviceowner/",
    tokenXEndpointUrl,
    System.getenv("TOKEN_X_CLIENT_ID"),
    System.getenv("TOKEN_X_PRIVATE_JWK"),
    System.getenv("AZURE_OPENID_CONFIG_TOKEN_ENDPOINT"),
    System.getenv("AZURE_APP_CLIENT_ID"),
    System.getenv("AZURE_APP_JWKS"),
    altinnRettigheterAudience,
    urlTilNotifikasjonIMiljo,
    notifikasjonerScope,
    System.getenv("KAFKA_BROKERS"),
    System.getenv("KAFKA_TRUSTSTORE_PATH"),
    System.getenv("KAFKA_CREDSTORE_PASSWORD"),
    System.getenv("KAFKA_KEYSTORE_PATH")
)