package com.upmgeoinfo.culturamad.services.json_parse.utils

import android.os.Build
import androidx.annotation.RequiresApi
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
    val txtDateBlock1: String,//Desde_
    val txtDateBlock2: String,//_hasta_
    val txtParseDays1: String, //Todos los_
    val txtParseDays2: String,//_y_
    val txtMO: String,
    val txtTU: String,
    val txtWE: String,
    val txtTH: String,
    val txtFR: String,
    val txtSA: String,
    val txtSU: String,
    val txtDaysHoursBlock1: String,//_a_las_
    val txtDaysHoursBlock2: String,//_horas.
) {

    //formatters
    private val fullDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")
    private val shortDateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy")
    private val hourFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val onlyDayFormatter = DateTimeFormatter.ofPattern("d", Locale.getDefault())
    private val onlyMonthFormatter = DateTimeFormatter.ofPattern("LLLL", Locale.getDefault())
    private val shortDateDayMonth = DateTimeFormatter.ofPattern("LLL d", Locale.getDefault())

    private val parsedSchedule: String
        get() {
            val dateStart =
                if (culturalEvent.dateStart.isNotEmpty()){
                    LocalDateTime.parse(culturalEvent.dateStart, fullDateTimeFormatter)
                }else null
            val dateEnd =
                if (culturalEvent.dateEnd.isNotEmpty()){
                    LocalDateTime.parse(culturalEvent.dateEnd, fullDateTimeFormatter)
                }else null
            val hours =
                if (culturalEvent.hours.isNotEmpty()){
                    LocalTime.parse(culturalEvent.hours, hourFormatter)
                }else null

            val days =
                if (culturalEvent.days.isNotEmpty()){
                    parseDays()
                }else null

            val excludedDays =
                if (culturalEvent.excludedDays.isNotEmpty()){
                    parseExcludedDays()
                }else null

            /**
             * dateBlock:
             * Es -> Desde sep 22 hasta oct 30
             * En -> From Sep 22 tp Oct 30
             *
             * Only 1 case: dateStart and dateEnd always present
             */
            var dateBlock: String? = null
            if (dateStart != null && dateEnd != null)
                dateBlock = txtDateBlock1 + dateStart.format(shortDateDayMonth) +
                            txtDateBlock2 + dateEnd.format(shortDateDayMonth) + " "

            /**
             * daysHoursBlock:
             * Es -> , todos los lunes, miércoles y viernes a las 19:50 H.
             * En -> , on Monday, Wednesday and Friday at 19:50 H
             *
             * 4 cases:
             *      days and hours present
             *      only hours
             *      only days
             *      none
             */
            var daysHoursBlock: String? = null
            if (days != null && hours != null)
                daysHoursBlock = days + txtDaysHoursBlock1 + hours + txtDaysHoursBlock2 + "."
            else if (days == null && hours != null)
                daysHoursBlock = txtDaysHoursBlock1 + hours + txtDaysHoursBlock2 + "."
            else if (days != null && hours == null)
                daysHoursBlock = days + "."

            /**
             * excludedDaysBloc:
             * Es -> Excepto sep 30, oct 1 y oct 2.
             * En -> Except Sep 30, Oct 1 and Oct 2.
             *
             * 2 cases:
             *      no excluded days
             *      one or more excluded days
             */
            var excludedDaysBlock: String? = null
            if(excludedDays!!.isNotEmpty()){
                excludedDaysBlock = "Excepto "
                for(i in 0..excludedDays.size - 1){
                    if(i == 0)
                        excludedDaysBlock += excludedDays[i].format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                    else if (i == excludedDays.size - 1)
                        excludedDaysBlock += txtParseDays2 + excludedDays[i].format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                    else excludedDaysBlock += ", " + excludedDays[i].format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                }
            }

            /**
             * fullBlock:
             *
             */
            var fullBlock = dateBlock
            if (daysHoursBlock != null )
                fullBlock += daysHoursBlock
            if (excludedDaysBlock != null )
                fullBlock += "\n" + excludedDaysBlock

            return fullBlock ?: "Schedule Parsing Error."
        }



    private fun parseDays(): String?{
        val daysString = culturalEvent.days
        val daysList = daysString.split(",").filter { it.isNotBlank() }.toMutableList()
        var daysParsed: String? = null

        if(daysList.isNotEmpty()){
            daysParsed = txtParseDays1
            for (i in 0..daysList.size - 1){
                val day: String? = null
                when (daysList[i]){
                    "MO" -> daysList[i] = txtMO
                    "TU" -> daysList[i] = txtTU
                    "WE" -> daysList[i] = txtWE
                    "TH" -> daysList[i] = txtTH
                    "FR" -> daysList[i] = txtFR
                    "SA" -> daysList[i] = txtSA
                    "SU" -> daysList[i] = txtSU
                }
                //Creating the string
                if (daysList.size == 1)
                    daysParsed += daysList[i] + ", "
                else{
                    if (i == 0)
                        daysParsed += daysList[i]
                    else if (i > 0 && i < daysList.size - 1)
                        daysParsed += ", " + daysList[i]
                    else
                        daysParsed += txtParseDays2 + daysList[i] + ", "
                }
            }
        }

        return daysParsed
    }
    private fun parseExcludedDays(): List<LocalDate>{
        val excludedDaysString = culturalEvent.excludedDays
        val excludedDaysList = excludedDaysString.split(";").filter { it.isNotBlank() }
        val excludedDatesList = mutableListOf<LocalDate>()

        for (item in excludedDaysList){
            val excludedDate = LocalDate.parse(item, shortDateFormatter)
            excludedDatesList.add(excludedDate)
        }

        return excludedDatesList.toList()
    }

    fun showParsedSchedule() = parsedSchedule
}