package no.nav.permitteringsportal.utils

import io.ktor.auth.*
import no.nav.security.token.support.ktor.TokenValidationContextPrincipal

fun getFnrFraToken(authContext: AuthenticationContext): String? {
    val principal = authContext.principal<TokenValidationContextPrincipal>()
    val claims = principal?.context?.getClaims("arbeidsgiver")
    return claims?.getStringClaim("pid")
}
