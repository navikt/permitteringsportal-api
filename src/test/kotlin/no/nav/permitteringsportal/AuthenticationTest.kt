package no.nav.permitteringsportal

import com.github.kittinunf.fuel.Fuel
import no.nav.permitteringsportal.setup.medArbeidsgiverToken
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AuthenticationTest {

    companion object {
        init {
            startLokalApp()
        }
    }

    @Test
    fun `Skal ikke nå endepunkt uten token`() {
        val (_, response, _) = Fuel.get("http://localhost:8080/sikret-endepunkt").response()
        assertThat(response.statusCode).isEqualTo(401)
    }

    @Test
    fun `Skal nå endepunkt med token`() {
        val (_, response, _) = Fuel.get("http://localhost:8080/sikret-endepunkt")
            .medArbeidsgiverToken()
            .response()
        assertThat(response.statusCode).isEqualTo(200)
    }
}
