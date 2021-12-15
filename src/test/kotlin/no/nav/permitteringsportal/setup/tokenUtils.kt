package no.nav.permitteringsportal.setup

import com.github.kittinunf.fuel.core.Request
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.token.support.ktor.IssuerConfig

fun Request.medArbeidsgiverToken(mockOAuth2Server: MockOAuth2Server): Request {
    return this.header("authorization", "Bearer ${token(mockOAuth2Server)}")
}

private fun token(mockOAuth2Server: MockOAuth2Server): String {
    return mockOAuth2Server.issueToken().serialize()
}


fun issuerConfig(mockOAuth2Server: MockOAuth2Server): IssuerConfig {
    val issuer = "default"
    val acceptedAudience = "default"

    return IssuerConfig(
        name = "arbeidsgiver",
        discoveryUrl = mockOAuth2Server.wellKnownUrl(issuer).toString(),
        acceptedAudience = listOf(acceptedAudience)
    )
}
