package no.nav.permitteringsportal.database

import no.nav.permitteringsportal.startLokalApp
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*
import kotlin.test.assertNotNull

class DatabaseTest {

    companion object {
        init {
            startLokalApp()
        }
    }

    @Test
    fun `Skal kunne skrive og lese fra database`() {
        val repository = Repository(dataSource = LokalDatabaseConfig().dataSource)

        repository.leggTilNyBekreftelse("11111111111", "111111111", 100, Date(), Date());
        val hentAlt = repository.hentAlleBekreftelserForOrganisasjon("111111111");
        assertThat(hentAlt.size).isEqualTo(1)
    }

    @Test
    fun `Skal lagre en ny hendelse sammen med bekreftelse for en organisasjon`() {
        val repository = Repository(dataSource = LokalDatabaseConfig().dataSource)

        repository.leggTilNyBekreftelse("11111111111", "111111111", 100, Date(), Date());
        val alleBekreftelserForOrganisasjon = repository.hentAlleBekreftelserForOrganisasjon("111111111");
        assertThat(alleBekreftelserForOrganisasjon.size).isEqualTo(1)
        assertThat(alleBekreftelserForOrganisasjon[0].orgnr).isEqualTo("111111111")
    }

    @Test
    fun `Skal kunne lagre flere hendelser på en bekreftelse`() {
        val repository = Repository(dataSource = LokalDatabaseConfig().dataSource)

        val bekreftelseId = repository.leggTilNyBekreftelse("11111111111", "111111111", 100, Date(), Date());
        val hendelseId =
            repository.leggTilNyHendelsePåBekreftelse(bekreftelseId, 100, Date(), Date())
        assertNotNull(hendelseId)
        val alleHendelserForBekreftelseOgOrganisasjon =
            repository.hentAlleHendelserForBekreftelseOgOrganisasjon("111111111")

    }
}