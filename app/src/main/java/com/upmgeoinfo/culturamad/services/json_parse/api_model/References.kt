package com.upmgeoinfo.culturamad.services.json_parse.api_model

import com.google.gson.annotations.SerializedName

data class References(
    @SerializedName("@id")
    val id: String?
)
