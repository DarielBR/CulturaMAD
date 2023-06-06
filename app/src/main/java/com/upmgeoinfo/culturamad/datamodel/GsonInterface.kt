package com.upmgeoinfo.culturamad.datamodel

import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
//https://datos.madrid.es/egob/catalogo/206974-0-agenda-eventos-culturales-100.json
suspend fun getJsonStringFromUri(): String = withContext(Dispatchers.IO){
    val url = "https://datos.madrid.es/egob/catalogo/206974-0-agenda-eventos-culturales-100.json"
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .build()
    val response = client.newCall(request).execute()
    val jsonString = response.body()?.string()
    val startIndex = jsonString?.indexOf('[') ?: -1
    //val endIndex = jsonString?.lastIndexOf(']') ?: -1
    val endIndex = jsonString?.indexOf(']') ?: -1
    //val trimmedString = jsonString?.substring(startIndex) ?: ""
    var trimmedString = jsonString?.subSequence(startIndex,endIndex+1).toString() ?: ""
    return@withContext trimmedString
}

suspend fun getListOfData(): List<CulturalEventMadrid> = withContext(Dispatchers.IO){
    val gson = GsonBuilder().setLenient().create()
    val jsonString = getJsonStringFromUri()
    return@withContext gson.fromJson(jsonString, Array<CulturalEventMadrid>::class.java).toList()
}