package com.upmgeoinfo.culturamad.services.json_parse.api_model

import com.google.gson.annotations.SerializedName

data class ApiContext(
    //@SerializedName("c")
    val c: String,
    //@SerializedName("dcterms")
    val dcterms: String,
    //@SerializedName("geo")
    val geo: String,
    //@SerializedName("loc")
    val loc: String,
    //@SerializedName("org")
    val org: String,
    //@SerializedName("vcard")
    val vcard: String,
    //@SerializedName("schema")
    val schema: String,
    //@SerializedName("title")
    val title: String,
    //@SerializedName("id")
    val id: String,
    //@SerializedName("relation")
    val relation: String,
    //@SerializedName("references")
    val references: String,
    //@SerializedName("address")
    val address: String,
    //@SerializedName("area")
    val area: String,
    //@SerializedName("district")
    val district: String,
    //@SerializedName("locality")
    val locality: String,
    @SerializedName("postal-code")
    val postalCode: String,
    @SerializedName("street-address")
    val streetAddress: String,
    //@SerializedName("location")
    val location: String,
    //@SerializedName("latitude")
    val latitude: String,
    //@SerializedName("longitude")
    val longitude: String,
    //@SerializedName("organization")
    val organization: String,
    @SerializedName("organization-desc")
    val organizationDesc: String,
    @SerializedName("accesibility")
    val accessibility: String,
    //@SerializedName("services")
    val services: String,
    //@SerializedName("schedule")
    val schedule: String,
    @SerializedName("organization-name")
    val organizationName: String,
    //@SerializedName("description")
    val description: String,
    //@SerializedName("link")
    val link: String,
    //@SerializedName("uid")
    val uid: String,
    //@SerializedName("dtstart")
    val dtstart: String,
    //@SerializedName("dtend")
    val dtend: String,
    //@SerializedName("time")
    val time: String,
    @SerializedName("excluded-days")
    val excludedDays: String,
    @SerializedName("event-location")
    val eventLocation: String,
    //@SerializedName("free")
    val free: String,
    //@SerializedName("price")
    val price: String,
    //@SerializedName("recurrence")
    val recurrence: String,
    //@SerializedName("days")
    val days: String,
    //@SerializedName("frequency")
    val frequency: String,
    //@SerializedName("interval")
    val interval: String,
    //@SerializedName("audience")
    val audience: String
)
