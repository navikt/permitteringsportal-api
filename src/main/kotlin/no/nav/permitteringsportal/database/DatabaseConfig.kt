package no.nav.permitteringsportal.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

class DatabaseConfig {

    private val host by lazy { System.getenv("DATABASE_HOST") }
    private val port by lazy { System.getenv("DATABASE_PORT") }
    private val database by lazy { System.getenv("DATABASE_DATABASE") }
    private val dbUsername by lazy { System.getenv("DATABASE_USERNAME") }
    private val dbPassword by lazy { System.getenv("DATABASE_PASSWORD") }

    val dataSource: HikariDataSource = HikariDataSource(
        HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://${host}:${port}/${database}"
            username = dbUsername
            password = dbPassword
            maximumPoolSize = 3
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
    )
}
