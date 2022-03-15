package no.nav.permitteringsportal

import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod
import hentBekreftelse
import hentOppgaver
import hentOrganisasjoner
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import leggTilBekreftelse
import no.nav.permitteringsportal.altinn.AltinnService
import no.nav.permitteringsportal.auth.Oauth2Client
import no.nav.permitteringsportal.database.LokalDatabaseConfig
import no.nav.permitteringsportal.database.Repository
import no.nav.permitteringsportal.database.runFlywayMigrations
import no.nav.permitteringsportal.minsideklient.MinSideNotifikasjonerService
import no.nav.permitteringsportal.minsideklient.getHttpClient
import no.nav.permitteringsportal.minsideklient.graphql.MinSideGraphQLKlient
import no.nav.permitteringsportal.utils.*
import no.nav.security.token.support.client.core.ClientAuthenticationProperties
import no.nav.security.token.support.ktor.IssuerConfig
import no.nav.security.token.support.ktor.TokenSupportConfig
import no.nav.security.token.support.ktor.tokenValidationSupport
import oppdaterBekreftelse
import sendInnBekreftelse
import sjekkInnlogget
import java.io.Closeable
import javax.sql.DataSource

class App(
    private val dataSource: DataSource,
    private val issuerConfig: IssuerConfig,
    private val minSideNotifikasjonerService: MinSideNotifikasjonerService,
    private val altinnService: AltinnService
) : Closeable {

    private val repository = Repository(dataSource)
    private val server = embeddedServer(Netty, port = 8080) {

        install(Authentication) {
            tokenValidationSupport(config = TokenSupportConfig(issuerConfig))
        }
        install(ContentNegotiation) {
            json()
        }
        routing {
            authenticate {
                sjekkInnlogget()
                hentOrganisasjoner(altinnService)
                hentOppgaver(repository)
                sendInnBekreftelse(repository,minSideNotifikasjonerService)
                leggTilBekreftelse(repository, altinnService)
                hentBekreftelse(repository)
                oppdaterBekreftelse(repository)
            }

            get("/permitteringsportal-api/internal/isAlive") { call.respond(HttpStatusCode.OK) }
            get("/permitteringsportal-api/internal/isReady") { call.respond(HttpStatusCode.OK) }

        }
    }

    fun start() {
        runFlywayMigrations(dataSource)
        server.start()
    }

    override fun close() {
        server.stop(0, 0)
    }
}

fun main() {
    //hardkodet for lokal kjoring
    val httpClient = getHttpClient()

    // Token X
    val authProperties = ClientAuthenticationProperties.builder()
        .clientId(environmentVariables.tokenXClientId)
        .clientJwk(environmentVariables.tokenXPrivateJWK)
        .clientAuthMethod(ClientAuthenticationMethod.PRIVATE_KEY_JWT)
        .build()

    val azureAuthProperties = ClientAuthenticationProperties.builder()
        .clientId(environmentVariables.azureClientId)
        .clientJwk(environmentVariables.azureJWK)
        .clientAuthMethod(ClientAuthenticationMethod.PRIVATE_KEY_JWT)
        .build()

    val defaultHttpClient = getDefaultHttpClient()

    val tokenExchangeClient = Oauth2Client(defaultHttpClient, authProperties, azureAuthProperties)
    val altinnService = AltinnService(tokenExchangeClient, defaultHttpClient, environmentVariables.altinnProxyUrl)

    val minSideGraphQLKlient = MinSideGraphQLKlient(environmentVariables.urlTilNotifikasjonIMiljo, defaultHttpClient, tokenExchangeClient)
    val minSideNotifikasjonerService = MinSideNotifikasjonerService(minSideGraphQLKlient, tokenExchangeClient)

    // TODO: Koble mot PostgreSQL i miljø når vi har landa litt mer detaljer på schema
    val databaseConfig = LokalDatabaseConfig() // DatabaseConfig()

    val issuerConfig = IssuerConfig(
        name = "tokenx",
        discoveryUrl = System.getenv("TOKEN_X_WELL_KNOWN_URL"),
        acceptedAudience = listOf(System.getenv("TOKEN_X_CLIENT_ID"))
    )

    App(
        dataSource = databaseConfig.dataSource,
        issuerConfig = issuerConfig,
        minSideNotifikasjonerService,
        altinnService
    ).start()
}
