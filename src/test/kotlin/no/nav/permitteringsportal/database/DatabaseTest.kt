package no.nav.permitteringsportal.database

import no.nav.permitteringsportal.setup.issuerConfig
import no.nav.permitteringsportal.startLokalApp
import no.nav.security.mock.oauth2.MockOAuth2Server
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class DatabaseTest {

    companion object {
        private val mockOAuth2Server = MockOAuth2Server()
        private val dataSource = LokalDatabaseConfig().dataSource
        init {
            mockOAuth2Server.shutdown()
            mockOAuth2Server.start()
        }
    }

    @Test
    fun `Skal kunne skrive og lese fra database`() {
        startLokalApp(dataSource, issuerConfig = issuerConfig(mockOAuth2Server)).use {
            LokalDatabaseConfig().deleteAll()
            val repository = Repository(dataSource)

            repository.leggTilNyBekreftelse("11111111111", "111111111");
            val hentAlt = repository.hentAlleBekreftelserForOrganisasjon("111111111");
            assertThat(hentAlt.size).isEqualTo(1)
        }
    }

    @Test
    fun `Skal lagre en ny hendelse sammen med bekreftelse for en organisasjon`() {
        startLokalApp(dataSource, issuerConfig = issuerConfig(mockOAuth2Server)).use {
            LokalDatabaseConfig().deleteAll()
            val repository = Repository(dataSource)

            repository.leggTilNyBekreftelse("11111111111", "111111111");
            val alleBekreftelserForOrganisasjon = repository.hentAlleBekreftelserForOrganisasjon("111111111");
            assertThat(alleBekreftelserForOrganisasjon.size).isEqualTo(1)
            assertThat(alleBekreftelserForOrganisasjon[0].orgnr).isEqualTo("111111111")
        }
    }

    @Test
    fun `Skal kunne lagre flere hendelser på en bekreftelse`() {
        startLokalApp(dataSource, issuerConfig = issuerConfig(mockOAuth2Server)).use {
            LokalDatabaseConfig().deleteAll()
            val repository = Repository(dataSource)

            val bekreftelseId = repository.leggTilNyBekreftelse("11111111111", "111111111");
            val hendelseId =
                repository.leggTilNyHendelsePåBekreftelse(bekreftelseId, "NY",100, Date(), Date())
            assertNotNull(hendelseId)
            val hendelseId2 =
                repository.leggTilNyHendelsePåBekreftelse(bekreftelseId, "ENDRING",100, Date(), Date())
            assertNotNull(hendelseId2)

            assertNotEquals(hendelseId, hendelseId2)

            val hentAlleHendelser =
                repository.hentAlleHendelserForBekreftelseOgOrganisasjon("111111111", bekreftelseId)

            assertNotNull(hentAlleHendelser)
            assertEquals(2, hentAlleHendelser.size)
        }

    }
}