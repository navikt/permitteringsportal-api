package no.nav.permitteringsportal.setup

import com.github.kittinunf.fuel.core.Request
import no.nav.security.mock.oauth2.MockOAuth2Server

fun Request.medArbeidsgiverToken(): Request {
    return this.header("authorization", "Bearer $token")
}

// TODO: Dette funker ikke siden man bruker forskjellig instans enn den som starter i LokalApp.
//  Issuers blir derfor feil? (portnr. til serveren blir inkludert i issuer)
private val token = MockOAuth2Server().issueToken().serialize()
