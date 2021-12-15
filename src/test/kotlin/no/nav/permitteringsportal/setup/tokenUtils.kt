package no.nav.permitteringsportal.setup

import com.github.kittinunf.fuel.core.Request
import no.nav.security.mock.oauth2.MockOAuth2Server

fun Request.medArbeidsgiverToken(mockOAuth2Server: MockOAuth2Server): Request {
    return this.header("authorization", "Bearer ${token(mockOAuth2Server)}")
}

private fun token(mockOAuth2Server: MockOAuth2Server): String {
    return mockOAuth2Server.issueToken().serialize()
}
