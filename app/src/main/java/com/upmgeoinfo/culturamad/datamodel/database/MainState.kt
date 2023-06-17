package com.upmgeoinfo.culturamad.datamodel.database

import com.upmgeoinfo.culturamad.datamodel.CulturalEvent

data class MainState (
    var items: List<CulturalEvent> = emptyList()
)