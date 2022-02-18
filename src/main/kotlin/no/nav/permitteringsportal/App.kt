package no.nav.permitteringsportal

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod
import hentBekreftelse
import hentOppgaver
import hentOrganisasjoner
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import leggTilBekreftelse
import no.nav.permitteringsportal.altinn.AltinnService
import no.nav.permitteringsportal.altinn.MockAltinnService
import no.nav.permitteringsportal.altinn.Oauth2Client
import no.nav.permitteringsportal.database.BekreftelsePåArbeidsforhold
import no.nav.permitteringsportal.database.LokalDatabaseConfig
import no.nav.permitteringsportal.database.Repository
import no.nav.permitteringsportal.database.runFlywayMigrations
import no.nav.permitteringsportal.kafka.BekreftelsePåArbeidsforholdService
import no.nav.permitteringsportal.kafka.consumerConfig
import no.nav.permitteringsportal.kafka.producerConfig
import no.nav.permitteringsportal.minsideklient.MinSideNotifikasjonerService
import no.nav.permitteringsportal.minsideklient.getHttpClient
import no.nav.permitteringsportal.minsideklient.graphql.MinSideGraphQLKlient
import no.nav.permitteringsportal.utils.*
import no.nav.permitteringsvarsel.notifikasjon.kafka.DataFraAnsattConsumer
import no.nav.security.token.support.client.core.ClientAuthenticationProperties
import no.nav.security.token.support.client.core.ClientProperties
import no.nav.security.token.support.client.core.OAuth2GrantType
import no.nav.security.token.support.ktor.IssuerConfig
import no.nav.security.token.support.ktor.TokenSupportConfig
import no.nav.security.token.support.ktor.tokenValidationSupport
import oppdaterBekreftelse
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import sendInnBekreftelse
import sjekkInnlogget
import java.io.Closeable
import java.net.URI
import java.util.*
import javax.sql.DataSource
import kotlin.concurrent.thread

class App(
    private val dataSource: DataSource,
    private val issuerConfig: IssuerConfig,
    private val consumer: Consumer<String, DataFraAnsatt>,
    private val producer: Producer<String, BekreftelsePåArbeidsforhold>,
    private val bekreftelsePåArbeidsforholdService: BekreftelsePåArbeidsforholdService,
    private val minSideNotifikasjonerService: MinSideNotifikasjonerService,
    private val altinnService: AltinnService
) : Closeable {

    private val repository = Repository(dataSource)
    private val dataFraAnsattConsumer: DataFraAnsattConsumer = DataFraAnsattConsumer(consumer, repository, minSideNotifikasjonerService)

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
                sendInnBekreftelse(repository)
                leggTilBekreftelse(repository, altinnService)
                hentBekreftelse(repository)
                oppdaterBekreftelse(repository)
            }

            get("/internal/isAlive") { call.respond(HttpStatusCode.OK) }
            get("/internal/isReady") { call.respond(HttpStatusCode.OK) }

        }
    }

    fun start() {
        runFlywayMigrations(dataSource)
        server.start()
        thread {
            dataFraAnsattConsumer.start();
        }
    }

    override fun close() {
        server.stop(0, 0)
        dataFraAnsattConsumer.close()
    }
}

fun main() {
    val consumer: Consumer<String, DataFraAnsatt> = KafkaConsumer<String, DataFraAnsatt>(consumerConfig())
    val producer: Producer<String, BekreftelsePåArbeidsforhold> =
        KafkaProducer<String, BekreftelsePåArbeidsforhold>(producerConfig())

    //har ikke implementert database og mottak av foresporsler enda.
    val uuid: UUID = UUID.randomUUID()
    val dataFraAnsatt = DataFraAnsatt(
        uuid, "hello",
        "123456678"
    )
    //dette er mock
    val dagpengeMeldingService = BekreftelsePåArbeidsforholdService(producer, emptyList())

    //hardkodet for lokal kjoring
    val httpClient = getHttpClient()
    val minSideGraphQLKlient = MinSideGraphQLKlient(environmentVariables.urlTilNotifikasjonIMiljo, httpClient)
    val minSideNotifikasjonerService = MinSideNotifikasjonerService(minSideGraphQLKlient)

    // Token X
    val authProperties = ClientAuthenticationProperties.builder()
        .clientId(environmentVariables.tokenXClientId)
        .clientJwk(environmentVariables.tokenXPrivateJWK)
        .clientAuthMethod(ClientAuthenticationMethod.PRIVATE_KEY_JWT)
        .build()

    val defaultHttpClient = getDefaultHttpClient()

    val tokenExchangeClient = Oauth2Client(defaultHttpClient, environmentVariables.tokenEndpointUrl, authProperties)
    val altinnService = AltinnService(tokenExchangeClient, defaultHttpClient, environmentVariables.altinnProxyUrl)

    log("main").info("Starter app i cluster: ${Cluster.current}")

    // TODO: Koble mot PostgreSQL i miljø når vi har landa litt mer detaljer på schema
    val databaseConfig = LokalDatabaseConfig() // DatabaseConfig()

    val issuerConfig = IssuerConfig(
        name = "arbeidsgiver",
        discoveryUrl = System.getenv("TOKEN_X_WELL_KNOWN_URL"),
        acceptedAudience = listOf(System.getenv("TOKEN_X_CLIENT_ID"))
    )

    App(
        dataSource = databaseConfig.dataSource,
        issuerConfig = issuerConfig,
        consumer,
        producer,
        dagpengeMeldingService,
        minSideNotifikasjonerService,
        altinnService
    ).start()
}
