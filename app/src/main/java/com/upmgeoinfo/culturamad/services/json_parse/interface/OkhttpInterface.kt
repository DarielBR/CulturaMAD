package com.upmgeoinfo.culturamad.services.json_parse.`interface`

import com.google.gson.GsonBuilder
import com.upmgeoinfo.culturamad.datamodel.CulturalEvent
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
//    val startIndex = jsonString?.indexOf('[') ?: -1
//    val endIndex = jsonString?.lastIndexOf(']') ?: -1
//    val trimmedString = jsonString?.subSequence(startIndex,endIndex+1).toString() ?: ""
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
    //return@withContext gsonObject.fromJson(jsonString, ApiJsonFile::class.java)
    try {
        return@withContext gsonObject.fromJson(jsonString, ApiJsonFile::class.java)
    }catch (e: Exception){
        throw JsonParseException("Error while parsing Json String -> " + e.localizedMessage)
    }
}

/*suspend fun getListOfData(uri: String): List<CulturalEventMadrid> = withContext(Dispatchers.IO){
    val gson = GsonBuilder().setLenient().create()
    val jsonString = getJsonStringFromUri(uri)
    var isInFailure = false
    var elementsList = emptyList<CulturalEventMadrid>()
    try {
        elementsList = gson.fromJson(jsonString, Array<CulturalEventMadrid>::class.java).toList()
    }catch (e: Exception){
        *//*Toast.makeText(
            context,
            "Error while parsing Json String -> " + e.localizedMessage,
            Toast.LENGTH_LONG
        ).show()*//*
        //throw JsonParseException("Error while parsing Json String List -> " + e.localizedMessage)
        isInFailure = true
    }
    if (isInFailure){//TODO:Falta el else de este if
        try {
            //val stringList = getJsonStringsListFromUri(uri)
            val listOfJsonStringElements = getJsonStringsListFromString(jsonString)
            if (listOfJsonStringElements.isNotEmpty()){
                var failCasesCoutn = 0
                val mutableElementsLis = emptyList<CulturalEventMadrid>().toMutableList()
                listOfJsonStringElements.forEach {
                    try {
                        mutableElementsLis.add(
                            gson.fromJson(it, CulturalEventMadrid::class.java)
                        )
                    }catch (innerE: Exception){
                        failCasesCoutn++
                    }
                }
                elementsList = mutableElementsLis.toList()
            }else{
                *//*Toast.makeText(
                    context,
                    "Json String List is empty.",
                    Toast.LENGTH_LONG
                ).show()*//*
                throw JsonParseException("Json String is empty.")
            }
        }catch (e: Exception){
            *//*Toast.makeText(
                context,
                "Error while parsing Json String List -> " + e.localizedMessage,
                Toast.LENGTH_LONG
            ).show()*//*
            throw JsonParseException("Error while parsing Json String List -> " + e.localizedMessage)
        }
    }
    return@withContext elementsList//gson.fromJson(jsonString, Array<CulturalEventMadrid>::class.java).toList()
}*/

class JsonParseException(message: String): Exception(message)

object JsonFile {

    const val uri = "https://datos.madrid.es/egob/catalogo/206974-0-agenda-eventos-culturales-100.json"
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
            id = it.id?.toInt(),
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
            /*longitude = if(it.location == null) ""
            else it.location.longitude.toString(),*/
            address = it.address?.area?.streetAddress ?: "",
            /*address = if(it.address == null || it.address.area == null) ""
            else{
                it.address.area.streetAddress
            },*/
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