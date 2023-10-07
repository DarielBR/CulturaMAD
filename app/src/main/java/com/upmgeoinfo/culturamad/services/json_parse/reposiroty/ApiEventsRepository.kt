package com.upmgeoinfo.culturamad.services.json_parse.reposiroty

import com.upmgeoinfo.culturamad.services.json_parse.api_model.ApiItem
import com.upmgeoinfo.culturamad.services.json_parse.`interface`.GraphApiService

class ApiEventsRepository(private val apiService: GraphApiService){
    suspend fun getItemsFromGraph(): List<ApiItem>{
        return apiService.getItemsFromGraph()
    }
}