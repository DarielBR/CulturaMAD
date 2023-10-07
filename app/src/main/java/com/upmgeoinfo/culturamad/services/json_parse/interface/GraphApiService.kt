package com.upmgeoinfo.culturamad.services.json_parse.`interface`

import com.upmgeoinfo.culturamad.services.json_parse.api_model.ApiItem
import retrofit2.http.GET

interface GraphApiService {
    @GET("206974-0-agenda-eventos-culturales-100.json")
    suspend fun getItemsFromGraph(): List<ApiItem>
}