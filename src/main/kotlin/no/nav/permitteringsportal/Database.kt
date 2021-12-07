package no.nav.permitteringsportal

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using
import org.flywaydb.core.Flyway

class Database {

    private val dataSource: HikariDataSource = hikari()

    init {
        val flyway = Flyway.configure()
            .locations("db.migration")
            .dataSource(dataSource)
            .load()
        flyway.migrate()
    }

    companion object {
        private fun hikari(): HikariDataSource {
            val config = HikariConfig().apply {
                driverClassName = "org.h2.Driver"
                jdbcUrl = "jdbc:h2:mem:test"
                maximumPoolSize = 3
                isAutoCommit = false
                transactionIsolation = "TRANSACTION_REPEATABLE_READ"
                validate()
            }
            return HikariDataSource(config)
        }
    }

    fun runQuery() {
        using(sessionOf(dataSource)) { session ->
            session.run(
                queryOf("""
                select 1 from bekreftelse_arbeidsforhold
            """.trimIndent()).asExecute)
        }
    }
}
