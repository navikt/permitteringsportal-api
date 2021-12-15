package no.nav.permitteringsportal.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

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
}
