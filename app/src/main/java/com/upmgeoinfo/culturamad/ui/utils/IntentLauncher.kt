package com.upmgeoinfo.culturamad.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import com.google.android.gms.maps.model.LatLng
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.viewmodels.main.model.CulturalEvent
import java.util.Calendar

class IntentLauncher(
    private val culturalEvent: CulturalEvent = CulturalEvent(),
    private val context: Context? = null
){
    fun scheduleIntent(){
        val startYear = culturalEvent.dateStart.subSequence(0..3).toString()
        val startMonth = culturalEvent.dateStart.subSequence(5..6).toString()
        val startDay = culturalEvent.dateStart.subSequence(8..9).toString()
        val hour: String
        val minutes: String
        if(culturalEvent.hours != ""){
            hour = culturalEvent.hours.subSequence(0..1).toString()
            minutes = culturalEvent.hours.subSequence(3..4).toString()
        }else{
            hour = "00"
            minutes = "00"
        }

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, startYear.toInt())
        calendar.set(Calendar.MONTH, startMonth.toInt())
        calendar.set(Calendar.DAY_OF_MONTH, startDay.toInt())
        if(culturalEvent.hours != ""){
            calendar.set(Calendar.HOUR_OF_DAY, hour.toInt())
            calendar.set(Calendar.MINUTE, minutes.toInt())
        }else{
            calendar.set(Calendar.HOUR_OF_DAY, "00".toInt())
            calendar.set(Calendar.MINUTE, "00".toInt())
        }

        val intent = Intent(Intent.ACTION_INSERT)
        intent.data = CalendarContract.Events.CONTENT_URI
        intent.putExtra(CalendarContract.Events.TITLE, culturalEvent.title)
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, culturalEvent.address)
        if(culturalEvent.hours != ""){
            intent.putExtra(
                CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calendar.timeInMillis
            )
            intent.putExtra(
                CalendarContract.EXTRA_EVENT_END_TIME,
                calendar.timeInMillis + 60 * 60 * 1000
            )
        }else {
            intent.putExtra(CalendarContract.Events.ALL_DAY, true)
        }

        context?.startActivity(intent)
    }

    fun shareIntent(){
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, culturalEvent.title)
            putExtra(
                Intent.EXTRA_TEXT,
                culturalEvent.link
            )
        }
        val title = context?.resources?.getString(R.string.event_share)
        context?.startActivity(Intent.createChooser(intent, title))
    }

    fun browserUriIntent(){
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(culturalEvent.link)
        )
        context?.startActivity(intent)
    }

    fun directionsIntent(
        myLocation: LatLng
    ){
        val myLat = myLocation.latitude
        val myLng = myLocation.longitude
        val latitude = culturalEvent.latitude
        val longitude = culturalEvent.longitude
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?saddr=$myLat,$myLng&daddr=$latitude,$longitude")
        )
        intent.setPackage("com.google.android.apps.maps")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(intent)
    }

    fun mapsIntent(){
        val latitude = culturalEvent.latitude
        val longitude = culturalEvent.longitude
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("geo:$latitude,$longitude")
        )
        intent.setPackage("com.google.android.apps.maps")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(intent)
    }

    fun voidIntent(){}
}


