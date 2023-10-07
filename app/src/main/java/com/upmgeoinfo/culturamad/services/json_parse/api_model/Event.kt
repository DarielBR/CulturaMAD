package com.upmgeoinfo.culturamad.services.json_parse.api_model

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("@id")
    val id: String?,
    @SerializedName("@type")
    val type: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("free")
    val free: Int?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("dtstart")
    val dtstart: String?,
    @SerializedName("dtend")
    val dtend: String?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("excludedDays")
    val excludedDays: String?,
    @SerializedName("uid")
    val uid: String?,
    @SerializedName("link")
    val link: String?,
    @SerializedName("event-location")
    val eventLocation: String?,
    @SerializedName("references")
    val references: References?,
    @SerializedName("relation")
    val relation: Relation?,
    @SerializedName("address")
    val address: Address?,
    @SerializedName("location")
    val location: Location?,
    @SerializedName("organization")
    val organization: Organization?
)
