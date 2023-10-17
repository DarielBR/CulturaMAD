package com.upmgeoinfo.culturamad.datamodel

import com.google.gson.annotations.SerializedName
import com.upmgeoinfo.culturamad.viewmodels.main.model.CulturalEvent
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

    val uri = "https://datos.madrid.es/egob/catalogo/206974-0-agenda-eventos-culturales-100.json"
    val dataList: List<CulturalEventMadrid> by lazy {
        runBlocking {
            getListOfData()
            /*try{ com.upmgeoinfo.culturamad.services.json_parse.`interface`.getListOfData(uri) }
            catch (e: JsonParseException){ throw e}*/
        }
    }

    val transformedDataList = dataList.map {
        CulturalEvent(
            id = it.id.toInt(),
            category = if(it.category == null) ""
            else{
                it.category
                    .subSequence(
                        it.category.indexOfLast { it == '/' } + 1,
                        it.category.lastIndex + 1
                    ).toString()
            },
            title = it.title,
            description = it.description,
            latitude = if(it.location == null) ""
            else it.location.latitude.toString(),
            longitude = if(it.location == null) ""
            else it.location.longitude.toString(),
            address = if(it.address == null || it.address.area == null) ""
            else{
                it.address.area.streetAddress
            },
            district = if(it.address == null || it.address.district == null) ""
            else {
                it.address.district.Id
                    .subSequence(
                        it.address.district.Id.indexOfLast { it == '/' } + 1,
                        it.address.district.Id.lastIndex + 1
                    ).toString()
            },
            neighborhood = if(it.address == null || it.address.area == null) ""
            else {
                it.address.area.Id
                    .subSequence(
                        it.address.area.Id.indexOfLast { it == '/' } + 1,
                        it.address.area.Id.lastIndex + 1
                    ).toString()
            },
            days = if(it.recurrence == null) ""
            else it.recurrence.days,
            frequency = if(it.recurrence == null) ""
            else it.recurrence.frequency,
            interval = if(it.recurrence == null) 0
            else it.recurrence.interval.toInt(),
            dateStart = it.dtstart,
            dateEnd = it.dtend,
            hours = it.time,
            excludedDays = it.excludedDays,
            place = it.eventLocation,
            host = if(it.organization == null)""
            else it.organization.organizationName,
            price = it.price,
            link = it.link,
            bookmark = false,
            review = 0
        )
    }
}

