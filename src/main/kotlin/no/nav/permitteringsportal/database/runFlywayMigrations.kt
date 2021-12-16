package no.nav.permitteringsportal.database

import org.flywaydb.core.Flyway
import javax.sql.DataSource

fun runFlywayMigrations(dataSource: DataSource) {
    Flyway.configure()
        .locations("db.migration")
        .dataSource(dataSource)
        .load()
        .migrate()
}
