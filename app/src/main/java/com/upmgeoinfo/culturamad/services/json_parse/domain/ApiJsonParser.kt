package com.upmgeoinfo.culturamad.services.json_parse.domain

import com.upmgeoinfo.culturamad.services.json_parse.reposiroty.ApiEventsRepository

class ApiJsonParser (
    private val apiEventsRepository: ApiEventsRepository
){
    /*suspend operator fun invoke(): List<ApiItem>{
        return apiEventsRepository.getItemsFromGraph()
    }*/
}