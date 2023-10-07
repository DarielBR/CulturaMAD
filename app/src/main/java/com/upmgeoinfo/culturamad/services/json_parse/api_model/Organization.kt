package com.upmgeoinfo.culturamad.services.json_parse.api_model

import com.google.gson.annotations.SerializedName

data class Organization(
    @SerializedName("organization-name")
    val organizationName: String?,
    @SerializedName("accesibility")
    val accessibility: String?
)
