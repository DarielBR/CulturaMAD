package com.upmgeoinfo.culturamad.services.json_parse.`interface`

import com.google.gson.GsonBuilder
import com.upmgeoinfo.culturamad.viewmodels.main.model.CulturalEvent
import com.upmgeoinfo.culturamad.services.json_parse.api_model.ApiJsonFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

suspend fun getJsonStringFromUri(uri: String): String = withContext(Dispatchers.IO){
    try{
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(uri)
            .build()
        val response = client.newCall(request).execute()
        val jsonString = response.body()?.string()

        return@withContext jsonString!!
    }
    catch (e: Exception){
        throw JsonParseException("Error while fetching Json file from API resource -> " + e.localizedMessage)
    }
}

suspend fun parseJsonString(uri: String)= withContext(Dispatchers.IO){
    val jsonString = getJsonStringFromUri(uri)
    val  gsonObject = GsonBuilder()
        .setLenient()
        .create()

    try {
        return@withContext gsonObject.fromJson(jsonString, ApiJsonFile::class.java)
    }catch (e: Exception){
        throw JsonParseException("Error while parsing Json String -> " + e.localizedMessage)
    }
}

class JsonParseException(message: String): Exception(message)

object JsonFile {

    private const val uri = "https://datos.madrid.es/egob/catalogo/206974-0-agenda-eventos-culturales-100.json"
    private val fileData: ApiJsonFile by lazy {
        runBlocking {
            try {
                parseJsonString(uri)
            }catch (e: JsonParseException){
                throw e
            }
        }
    }
    val eventsList = fileData.graph?.map {
        CulturalEvent(
            id = it.id ?: 0,
            category = if (it.type == null) ""
            else {
                it.type
                    .subSequence(
                        it.type.indexOfLast { it == '/' } + 1,
                        it.type.lastIndex + 1
                    ).toString()
            },
            title = it.title ?: "",
            description = it.description ?: "",
            latitude = it.location?.latitude.toString(),
            longitude = it.location?.longitude.toString(),
            address = it.address?.area?.streetAddress ?: "",
            district = if (it.address == null || it.address.district == null) ""
            else {
                it.address.district.id!!
                    .subSequence(
                        it.address.district.id.indexOfLast { it == '/' } + 1,
                        it.address.district.id.lastIndex + 1
                    ).toString()
            },
            neighborhood = if (it.address == null || it.address.area == null) ""
            else {
                it.address.area.id!!
                    .subSequence(
                        it.address.area.id.indexOfLast { it == '/' } + 1,
                        it.address.area.id.lastIndex + 1
                    ).toString()
            },
            days = it.recurrence?.days ?: "",
            frequency = it.recurrence?.frequency ?: "",
            interval = it.recurrence?.interval ?: 0,
            dateStart = it.dtstart ?: "",
            dateEnd = it.dtend ?: "",
            hours = it.time ?: "",
            excludedDays = it.excludedDays ?: "",
            place = it.eventLocation ?: "",
            host = it.organization?.organizationName ?: "",
            price = it.price ?: "",
            link = it.link ?: "",
            bookmark = false,
            review = 0
        )
    }
}