package no.nav.permitteringsportal.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using

// TODO: Flytt til test-sources når vi er kobla mot PostgreSQL i miljø
class LokalDatabaseConfig {

    val dataSource: HikariDataSource = HikariDataSource(
        HikariConfig().apply {
            driverClassName = "org.h2.Driver"
            jdbcUrl = "jdbc:h2:mem:test"
            maximumPoolSize = 3
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
    )

    fun deleteAll() {
        val queryBekreftelse = queryOf("""
            delete from $bekreftelseTable
        """.trimIndent()).asUpdate
        val queryBekreftelseHendelse = queryOf("""
            delete from $bekreftelseHendelseTable
        """.trimIndent()).asUpdate

        using(sessionOf(dataSource)) {
            it.transaction { tx ->
                tx.run(queryBekreftelseHendelse)
                tx.run(queryBekreftelse)
            }
        }
    }
}
