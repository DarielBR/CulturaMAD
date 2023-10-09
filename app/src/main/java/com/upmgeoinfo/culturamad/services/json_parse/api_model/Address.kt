package com.upmgeoinfo.culturamad.services.json_parse.api_model

import com.google.gson.annotations.SerializedName

data class Address(
    //@SerializedName("district")
    val district: District?,
    //@SerializedName("area")
    val area: Area?
)
