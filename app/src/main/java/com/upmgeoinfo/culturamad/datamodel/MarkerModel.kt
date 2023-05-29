package com.upmgeoinfo.culturamad.datamodel

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.runBlocking

data class CulturalEventMadrid(
    @SerializedName("address")
    val address: Address,
    @SerializedName("references")
    val references: References,
    @SerializedName("@type")
    var category: String = "",
    @SerializedName("link")
    val link: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("relation")
    val relation: Relation,
    @SerializedName("recurrence")
    val recurrence: Recurrence,
    @SerializedName("uid")
    val uid: String = "",
    @SerializedName("price")
    val price: String = "",
    @SerializedName("event-location")
    val eventLocation: String = "",
    @SerializedName("organization")
    val organization: Organization,
    @SerializedName("excluded-days")
    val excludedDays: String = "",
    @SerializedName("location")
    val location: Location,
    @SerializedName("@id")
    val aid: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("time")
    val time: String = "",
    @SerializedName("free")
    val free: Int = 0,
    @SerializedName("dtend")
    val dtend: String = "",
    @SerializedName("dtstart")
    val dtstart: String = ""
)

data class Recurrence(
    @SerializedName("days")
    val days: String = "",
    @SerializedName("interval")
    val interval: Int = 0,
    @SerializedName("frequency")
    val frequency: String = ""
)
data class Area(
    @SerializedName("postal-code")
    val postalCode: String = "",
    @SerializedName("street-address")
    val streetAddress: String = "",
    @SerializedName("locality")
    val locality: String = "",
    @SerializedName("@id")
    val Id: String = ""
)


data class Relation(
    @SerializedName("@id")
    val Id: String = ""
)


data class Organization(
    @SerializedName("accesibility")
    val accesibility: String = "",
    @SerializedName("organization-name")
    val organizationName: String = ""
)


data class Address(
    @SerializedName("area")
    val area: Area,
    @SerializedName("district")
    val district: District
    )

data class References(
    @SerializedName("@id")
    val Id: String = ""
)

data class District(
    @SerializedName("@id")
    val Id: String = ""
)

data class Location(
    @SerializedName("latitude")
    val latitude: Double = 0.0,
    @SerializedName("longitude")
    val longitude: Double = 0.0
)

object MarkerData {
    val dataList: List<CulturalEventMadrid> by lazy {
        runBlocking {
            getListOfData()
        }
    }
}