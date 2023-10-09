package com.upmgeoinfo.culturamad.services.json_parse.api_model

import com.google.gson.annotations.SerializedName

data class Recurrence(
    @SerializedName("days")
    val days: String = "",
    @SerializedName("interval")
    val interval: Int = 0,
    @SerializedName("frequency")
    val frequency: String = ""
)
