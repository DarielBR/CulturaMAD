package com.upmgeoinfo.culturamad.services.json_parse.api_model

import com.google.gson.annotations.SerializedName

data class Area(
    @SerializedName("@id")
    val id: String?,
    //@SerializedName("locality")
    val locality: String?,
    @SerializedName("postal-code")
    val postalCode: String?,
    @SerializedName("street-address")
    val streetAddress: String?
)
