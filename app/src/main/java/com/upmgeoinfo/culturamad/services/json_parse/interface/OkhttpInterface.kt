package com.upmgeoinfo.culturamad.services.json_parse.`interface`

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

suspend fun getJsonStringFrom(uri: String): String = withContext(Dispatchers.IO){
    //val url = "https://datos.madrid.es/egob/catalogo/206974-0-agenda-eventos-culturales-100.json"
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(uri)
        .build()
    val response = client.newCall(request).execute()
    val jsonString = response.body()?.string()
    val startIndex = jsonString?.indexOf('[') ?: -1
    val endIndex = jsonString?.indexOf(']') ?: -1
    val trimmedString = jsonString?.subSequence(startIndex,endIndex+1).toString() ?: ""
    return@withContext trimmedString
}

suspend fun getJsonStringFromUri(uri: String): List<String> = withContext(Dispatchers.IO) {
    //val url = "https://datos.madrid.es/egob/catalogo/206974-0-agenda-eventos-culturales-100.json"
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(uri)
        .build()
    val response = client.newCall(request).execute()
    val jsonString = response.body()?.string()
    val startIndex = jsonString?.indexOf('{') ?: -1
    val endIndex = jsonString?.indexOfLast { it == '}' } ?: -1
    val trimmedString = jsonString?.subSequence(startIndex, endIndex + 1).toString() ?: ""
    return@withContext trimmedString.split("},")
}