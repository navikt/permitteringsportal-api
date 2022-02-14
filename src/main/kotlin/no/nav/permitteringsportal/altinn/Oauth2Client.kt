package no.nav.permitteringsportal.altinn

import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import no.nav.security.token.support.client.core.ClientAuthenticationProperties
import no.nav.security.token.support.client.core.OAuth2GrantType
import no.nav.security.token.support.client.core.OAuth2ParameterNames
import no.nav.security.token.support.client.core.auth.ClientAssertion
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenResponse
import java.net.URI

class Oauth2Client(
    private val httpClient: HttpClient,
    private val tokenEndpointUrl: String,
    private val clientAuthProperties: ClientAuthenticationProperties,
) {
    suspend fun exchangeToken(token: String, audience: String): OAuth2AccessTokenResponse {
        val grant = GrantRequest.tokenExchange(token, audience)
        return httpClient.tokenRequest(
            tokenEndpointUrl,
            clientAuthProperties = clientAuthProperties,
            grantRequest = grant
        )
    }
}

data class GrantRequest(
    val grantType: OAuth2GrantType,
    val params: Map<String, String> = emptyMap()
) {
    companion object {
        fun tokenExchange(token: String, audience: String): GrantRequest =
            GrantRequest(
                grantType = OAuth2GrantType.TOKEN_EXCHANGE,
                params = mapOf(
                    OAuth2ParameterNames.SUBJECT_TOKEN_TYPE to "urn:ietf:params:oauth:token-type:jwt",
                    OAuth2ParameterNames.SUBJECT_TOKEN to token,
                    OAuth2ParameterNames.AUDIENCE to audience
                )
            )

    }
}

internal suspend fun HttpClient.tokenRequest(
    tokenEndpointUrl: String,
    clientAuthProperties: ClientAuthenticationProperties,
    grantRequest: GrantRequest
): OAuth2AccessTokenResponse =
    submitForm(
        url = tokenEndpointUrl,
        formParameters = Parameters.build {
            appendClientAuthParams(
                tokenEndpointUrl = tokenEndpointUrl,
                clientAuthProperties = clientAuthProperties
            )
            append(OAuth2ParameterNames.GRANT_TYPE, grantRequest.grantType.value)
            grantRequest.params.forEach {
                append(it.key, it.value)
            }
        }
    )

private fun ParametersBuilder.appendClientAuthParams(
    tokenEndpointUrl: String,
    clientAuthProperties: ClientAuthenticationProperties
) = apply {
    val clientAssertion = ClientAssertion(URI.create(tokenEndpointUrl), clientAuthProperties)
    append(OAuth2ParameterNames.CLIENT_ID, clientAuthProperties.clientId)
    append(OAuth2ParameterNames.CLIENT_ASSERTION_TYPE, clientAssertion.assertionType())
    append(OAuth2ParameterNames.CLIENT_ASSERTION, clientAssertion.assertion())
}
