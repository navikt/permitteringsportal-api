package no.nav.permitteringsportal.altinn

import kotlinx.serialization.Serializable

@Serializable
data class AltinnOrganisasjon(
    val Name: String,
    val Type: String,
    val OrganizationNumber: String,
    val OrganizationForm: String,
    val Status: String,
    val ParentOrganizationNumber: String?
) {
}