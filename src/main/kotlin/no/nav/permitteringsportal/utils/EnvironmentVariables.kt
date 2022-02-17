package no.nav.permitteringsportal.utils

data class EnvironmentVariables(
    val altinnProxyUrl: String,
    val tokenEndpointUrl: String,
    val tokenXClientId: String,
    val tokenXPrivateJWK: String,
    val altinnRettigheterAudience: String
    ) {

}

val tokenEndpointUrl = when(Cluster.current) {
    Cluster.DEV_GCP -> "https://tokendings.dev-gcp.nais.io/token"
    Cluster.PROD_GCP -> "https://tokendings.prod-gcp.nais.io/token"
}

val altinnRettigheterAudience = when(Cluster.current) {
    Cluster.DEV_GCP -> "dev-gcp:arbeidsgiver:altinn-rettigheter-proxy"
    Cluster.PROD_GCP -> "prod-gcp:arbeidsgiver:altinn-rettigheter-proxy"
}

val environmentVariables = EnvironmentVariables(
    "http://altinn-rettigheter-proxy.arbeidsgiver/altinn-rettigheter-proxy/ekstern/altinn/api/serviceowner/",
    tokenEndpointUrl,
    System.getenv("TOKEN_X_CLIENT_ID"),
    System.getenv("TOKEN_X_PRIVATE_JWK"),
    altinnRettigheterAudience
)