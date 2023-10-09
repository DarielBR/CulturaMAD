package com.upmgeoinfo.culturamad.services.json_parse.api_model

import com.google.gson.annotations.SerializedName

data class ApiJsonFile(
    @SerializedName("@context")
    val context: ApiContext?,
    @SerializedName("@graph")
    var graph: List<Event>?
)
