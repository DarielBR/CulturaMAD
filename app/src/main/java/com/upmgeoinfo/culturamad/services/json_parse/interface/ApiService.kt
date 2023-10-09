package com.upmgeoinfo.culturamad.services.json_parse.`interface`

import com.upmgeoinfo.culturamad.services.json_parse.api_model.ApiJsonFile
import com.upmgeoinfo.culturamad.services.json_parse.api_model.Event
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("206974-0-agenda-eventos-culturales-100.json")
    suspend fun getJsonFile(): Response<ApiJsonFile>
}