package no.nav.permitteringsportal

// Brukes for å kjøre appen lokalt
fun main() {
    startLokalApp()
}

// Brukes for å kjøre appen i tester
fun startLokalApp() {

    val lokalDatabaseConfig = LokalDatabaseConfig()

    App(
        dataSource = lokalDatabaseConfig.dataSource
    ).start()
}
