package com.upmgeoinfo.culturamad.datamodel.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.provider.Settings.Global.getString
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.currentComposer
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.datamodel.CulturalEvent
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class ScheduleParser(
    val culturalEvent: CulturalEvent,
    private val useLongDateFormat: Boolean = false
) {
    /**
     * Parse a block of schedule information extracted from the data contained in a CulturalEvent object.
     *
     *@return String with a parsed version of a CulturalEvent's schedule.
     *
     *@param culturalEvent a CulturalEvent object.
     *@param useLongDateFormat boolean value indicating whether to use long date format or not.
     */
    private val parsedSchedule: String
        get() {
            val dateStart =
                if (culturalEvent.dateStart.isNotEmpty()){
                    LocalDateTime.parse(culturalEvent.dateStart, fullDateTimeFormat)
                }else null
            val dateEnd =
                if (culturalEvent.dateEnd.isNotEmpty()){
                    LocalDateTime.parse(culturalEvent.dateEnd, fullDateTimeFormat)
                }else null
            val hours =
                if (culturalEvent.hours.isNotEmpty()){
                    LocalTime.parse(culturalEvent.hours, hourFormat)
                }else null

            var days =
                if (culturalEvent.days.isNotEmpty()){
                    //TODO: FIX THIS ISSUE HERE
                    parseDays()
                    /*culturalEvent.days
                        .replace(oldValue = "MO", newValue = "Lunes")
                        .replace(oldValue = "TU", newValue = "Martes")
                        .replace(oldValue = "WE", newValue = "Miércoles")
                        .replace(oldValue = "TH", newValue = "Jueves")
                        .replace(oldValue = "FR", newValue = "Viernes")
                        .replace(oldValue = "SA", newValue = "Sábados")
                        .replace(oldValue = "SU", newValue = "Domingos")*/
                }else null

            val excludedDays =
                if (culturalEvent.excludedDays.isNotEmpty()){
                    parseExcludedDays()
                }else null

            var dateBlock: String? = null
            if (dateStart != null && dateEnd != null)
                dateBlock = "Desde el ${dateStart.format(onlyDayFormatter)} de ${dateStart.format(onlyMonthFormatter)} hasta el ${dateEnd.format(onlyDayFormatter)} de ${dateEnd.format(onlyMonthFormatter)}"
            //TODO: write it to take the message from the resources ie:dateBlock = Resources.getSystem().getString(R.string.ui_date_block, dateStart, dateEnd)
            //TODO: more cases for no start date or no end date}

            var daysHoursBlock: String? = null
            if (days != null && hours != null)
                daysHoursBlock = ", $days a las $hours horas"
                //Resources.getSystem().getString(R.string.ui_days_hours_block, days, hours.toString())
                //TODO: more cases for not days and no hours

            var excludedDaysBlock: String? = null
            if(excludedDays!!.isNotEmpty()){
                excludedDaysBlock = ". Excepto "
                for(i in 0..excludedDays.size - 1){
                    if(i == 0)
                        excludedDaysBlock += excludedDays[i].format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                    else if (i == excludedDays.size - 1)
                        excludedDaysBlock += " y " + excludedDays[i].format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                    else excludedDaysBlock += ", " + excludedDays[i].format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                }
            }

            var fullBlock: String? = null
            if (dateBlock != null && daysHoursBlock != null && excludedDays != null)
                fullBlock = dateBlock + daysHoursBlock + excludedDaysBlock

            return fullBlock ?: "Schedule Parsing Error."
        }

    private val fullDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")
    private val shortDateFormat = DateTimeFormatter.ofPattern("d/M/yyyy")
    //private val fullDateFormat  = DateTimeFormatter.ofPattern("yyyy, dd MMM")
    private val hourFormat = DateTimeFormatter.ofPattern("HH:mm")
    private val onlyDayFormatter = DateTimeFormatter.ofPattern("d", Locale.getDefault())
    private val onlyMonthFormatter = DateTimeFormatter.ofPattern("LLLL", Locale.getDefault())

    private fun parseDays(): String?{
        val daysString = culturalEvent.days
        val daysList = daysString.split(",").filter { it.isNotBlank() }.toMutableList()
        var daysParsed: String? = null

        if(daysList.isNotEmpty()){
            daysParsed = "todos los "
            for (i in 0..daysList.size - 1){
                val day: String? = null
                when (daysList[i]){
                    "MO" -> daysList[i] = "lunes"
                    "TU" -> daysList[i] = "martes"
                    "WE" -> daysList[i] = "miércoles"
                    "TH" -> daysList[i] = "jueves"
                    "FR" -> daysList[i] = "viernes"
                    "SA" -> daysList[i] = "sábados"
                    "SU" -> daysList[i] = "domingos"
                }
                if (i == 0) daysParsed += daysList[i]
                else if (i == daysList.size - 1) daysParsed += " y " + daysList[i]
                else daysParsed += ", " + daysList[i]
            }
        }

        return daysParsed
    }
    private fun parseExcludedDays(): List<LocalDate>{
        val excludedDaysString = culturalEvent.excludedDays
        val excludedDaysList = excludedDaysString.split(";").filter { it.isNotBlank() }
        val excludedDatesList = mutableListOf<LocalDate>()

        for (item in excludedDaysList){
            val excludedDate = LocalDate.parse(item, shortDateFormat)
            excludedDatesList.add(excludedDate)
        }
        /*excludedDaysList.forEach { dateString ->
            //try {
                val excludedDate = LocalDateTime.parse(dateString, shortDateFormat)
                excludedDatesList.add(excludedDate)
            //}catch (e: Exception){
            //    e.printStackTrace()
            //}
        }*/

        return excludedDatesList.toList()
    }

    fun showParsedSchedule() = parsedSchedule
}