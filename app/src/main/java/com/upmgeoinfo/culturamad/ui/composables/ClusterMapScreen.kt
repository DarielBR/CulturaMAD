package com.upmgeoinfo.culturamad.ui.composables

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.datamodel.Address
import com.upmgeoinfo.culturamad.datamodel.Area
import com.upmgeoinfo.culturamad.datamodel.CulturalEventMadrid
import com.upmgeoinfo.culturamad.datamodel.District
import com.upmgeoinfo.culturamad.datamodel.Location
import com.upmgeoinfo.culturamad.datamodel.MarkerData
import com.upmgeoinfo.culturamad.datamodel.Organization
import com.upmgeoinfo.culturamad.datamodel.Recurrence
import com.upmgeoinfo.culturamad.datamodel.References
import com.upmgeoinfo.culturamad.datamodel.Relation

/*@Preview(showBackground = false)
@Preview(showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ClusterMapScreenPreview(){
    CulturaMADTheme() {
        fun ClusterMapScreen(

        )
    }
}*/

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@MapsComposeExperimentalApi
@Composable
fun ClusterMapScreen(
    fuseLocationClient: FusedLocationProviderClient,
    searchValue: String,
    categoryDance: Boolean,
    categoryMusic: Boolean,
    categoryPainting: Boolean,
    categoryTheatre: Boolean
){
    /**
     * Obtaining Location, permission requests is done before [MapScreen] function os called.
     * thus, will not be controlled here. ([@suppressLint("MissingPermission")])
     */
    var myLocation = LatLng(0.0, 0.0)
    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    if (locationPermissionState.status.isGranted) {
        fuseLocationClient.getLastLocation().addOnSuccessListener { location ->
            if (location != null) {
                myLocation = LatLng(location.latitude, location.longitude)
            }
        }
    }
    /**
     * Creating location and a CameraPositionState to move our map camera to Plaza del Sol :)
     */
    val madrid = LatLng(40.4169087, -3.7035386)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(madrid, 10f)//usar madrid
    }
    /**
     *context and darkTheme will be used to configure the MapView and other logic ahead.
     */
    val context = LocalContext.current
    val darkTheme: Boolean = isSystemInDarkTheme()
    /**
     * Map Properties and UISettings
     */
    val myProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = locationPermissionState.status.isGranted,
                mapStyleOptions =   if(!darkTheme) MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_silver)
                else MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_dark)
            )
        )
    }
    val myUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false,
                compassEnabled = false
            )
        )
    }
    /**
     * Cluster Manager
     */
    lateinit var clusterManager: ClusterManager<CulturalEventMadridItem>
    /**
     * ModalBottomSheet handling values and variables
     */
    var openBottomSheet by remember { mutableStateOf(false) }
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )
    /**
     * Applying correct size to our window attending to the navigation mode set in the device
     */
    var isNavigationBarVisible by remember{ mutableStateOf(false) }
    val resources = context.resources
    val resourceId = resources.getIdentifier("config_navBarInteractionMode", "integer", "android")
    val interactionMode = resources.getInteger(resourceId)
    var currentEventToShow by remember { mutableStateOf<CulturalEventMadridItem>(createEmptyCulturalEvent()) }
    /**
     * 0-> 3 button mode
     * 1-> 2 button mode
     * 2-> gesture mode
     *
     * gesture 84, buttons 168
     */
    isNavigationBarVisible = interactionMode < 2
    /**
     * GoogleMap declaration
     */
    GoogleMap(
        cameraPositionState = cameraPositionState,
        modifier = Modifier
            .padding(bottom = if (!isNavigationBarVisible) 0.dp else 48.dp)
            .fillMaxSize(),
        properties = myProperties,
        uiSettings = myUiSettings,
    ){
        MapEffect() { map ->
            clusterManager = ClusterManager(context,map)
            map.setOnCameraIdleListener(clusterManager)
            map.setOnMarkerClickListener(clusterManager)

            refreshCulturalEvents(
                clusterManager = clusterManager,
                searchValue = searchValue,
                categoryDance = categoryDance,
                categoryMusic = categoryMusic,
                categoryPainting = categoryPainting,
                categoryTheatre = categoryTheatre
            )
            clusterManager.setOnClusterItemInfoWindowClickListener(){
                currentEventToShow = it
                openBottomSheet = true
            }
        }
    }
    /**
     * MyLocation and Compass button declaration
     */
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .padding(end = 11.dp, bottom = if (!isNavigationBarVisible) 58.dp else 108.dp)
            .fillMaxSize()
    ) {
        var clickedOnce by remember { mutableStateOf(false) }
        MapButton(
            onClick = {//mejorar el behavior del zoom, si ya esta en un 15f dejarlo ahi.
                var zoomLevel = 12f
                if (!clickedOnce) {
                    clickedOnce = !clickedOnce
                } else {
                    clickedOnce = !clickedOnce
                    zoomLevel = 15f
                }
                cameraPositionState.position = CameraPosition(myLocation, zoomLevel, 0f, 0f)
            },
            drawableResource = R.drawable.cmad_mylocation
        )
        MapButton(
            onClick = {
                val currentCameraState = cameraPositionState
                cameraPositionState.position = CameraPosition(
                    currentCameraState.position.target,
                    currentCameraState.position.zoom,
                    currentCameraState.position.tilt,
                    0.0f
                )
            },
            drawableResource = R.drawable.cmad_compass
        )
    }
    /**
     * ModalBottomSheet declaration
     */
    if(openBottomSheet){
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState
        ) {
            EventCard(culturalEventMadridItem = currentEventToShow)
        }
    }

}//function end

fun refreshCulturalEvents(
    clusterManager: ClusterManager<CulturalEventMadridItem>,
    searchValue: String,
    categoryDance: Boolean,
    categoryMusic: Boolean,
    categoryPainting: Boolean,
    categoryTheatre: Boolean
){

    val culturalEventsMadrid = MarkerData.dataList

    for (culturalEvent in culturalEventsMadrid) {//First filter for valid culturalEvent
        if (culturalEvent.location != null
            && culturalEvent.address.district != null
            && culturalEvent.category != null
            && culturalEvent.recurrence != null
            && culturalEvent.address != null
            && culturalEvent.address.district != null
            && culturalEvent.address.area != null
        ) {//Filter by categories if any.
            if (categoryDance && culturalEvent.category.contains("DanzaBaile")) {
                if (searchValue == "") {
                    clusterManager.addItem(createCulturalEventMadridItem(culturalEvent))
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dtstart.contains(searchValue, true)
                    || culturalEvent.address.district.Id.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    clusterManager.addItem(createCulturalEventMadridItem(culturalEvent))
                }
            } else if (categoryMusic && culturalEvent.category.contains("Musica")) {
                if (searchValue == "") {
                    clusterManager.addItem(createCulturalEventMadridItem(culturalEvent))
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dtstart.contains(searchValue, true)
                    || culturalEvent.address.district.Id.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    clusterManager.addItem(createCulturalEventMadridItem(culturalEvent))
                }
            } else if (categoryPainting && culturalEvent.category.contains("Exposiciones")) {
                if (searchValue == "") {
                    clusterManager.addItem(createCulturalEventMadridItem(culturalEvent))
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dtstart.contains(searchValue, true)
                    || culturalEvent.address.district.Id.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    clusterManager.addItem(createCulturalEventMadridItem(culturalEvent))
                }
            } else if (categoryTheatre && culturalEvent.category.contains("TeatroPerformance")) {
                if (searchValue == "") {
                    clusterManager.addItem(createCulturalEventMadridItem(culturalEvent))
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dtstart.contains(searchValue, true)
                    || culturalEvent.address.district.Id.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    clusterManager.addItem(createCulturalEventMadridItem(culturalEvent))
                }
            } else if (!categoryDance && !categoryMusic && !categoryPainting && !categoryTheatre) {
                if (searchValue == "") {
                    clusterManager.addItem(createCulturalEventMadridItem(culturalEvent))
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dtstart.contains(searchValue, true)
                    || culturalEvent.address.district.Id.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    clusterManager.addItem(createCulturalEventMadridItem(culturalEvent))
                }
            }
        }
    }
}

fun createCulturalEventMadridItem(culturalEvent: CulturalEventMadrid): CulturalEventMadridItem {
    return CulturalEventMadridItem(
        eventID = culturalEvent.id,
        eventLocation = LatLng(culturalEvent.location.latitude, culturalEvent.location.longitude),
        eventTitle = culturalEvent.title,
        eventDescription = culturalEvent.description,
        eventCategory = culturalEvent.category,
        eventAddress = culturalEvent.address.area.streetAddress,
        eventDistrict = culturalEvent.address.district.Id,
        eventNeighborhood = culturalEvent.address.area.Id,
        eventDays = culturalEvent.recurrence.days,
        eventFrequency = culturalEvent.recurrence.frequency,
        eventInterval = culturalEvent.recurrence.interval,
        eventStart = culturalEvent.dtstart,
        eventEnd = culturalEvent.dtend,
        eventExcludedDays = culturalEvent.excludedDays,
        eventTime = culturalEvent.time,
        eventPlace = culturalEvent.eventLocation,
        eventHost = culturalEvent.organization.organizationName,
        eventPrice = culturalEvent.price,
        eventLink = culturalEvent.link
    )
}

fun createEmptyCulturalEvent(): CulturalEventMadridItem{
    return CulturalEventMadridItem(
        eventID = "",
        eventLocation = LatLng(0.0,0.0),
        eventTitle = "",
        eventDescription = "",
        eventCategory = "",
        eventAddress = "",
        eventDistrict = "",
        eventNeighborhood = "",
        eventDays = "",
        eventFrequency = "",
        eventInterval = 0,
        eventStart = "",
        eventEnd = "",
        eventExcludedDays = "",
        eventTime = "",
        eventPlace = "",
        eventHost = "",
        eventPrice = "",
        eventLink = ""
    )
}

class CulturalEventMadridItem(
    eventID: String,
    eventLocation: LatLng,
    eventTitle: String,
    eventDescription: String,
    eventCategory: String,
    eventAddress: String,
    eventDistrict: String,
    eventNeighborhood: String,
    eventDays: String,
    eventFrequency: String,
    eventInterval: Int,
    eventStart: String,
    eventEnd: String,
    eventExcludedDays: String,
    eventTime: String,
    eventPlace: String,
    eventHost: String,
    eventPrice: String,
    eventLink: String
): ClusterItem{
    private val location: LatLng
    private val title: String
    private val snippet: String
    /**
     * Extras
     */
    private val extraID: String
    private val extraDescription: String
    private val extraCategory: String
    private val extraAddress: String
    private val extraDistrict: String
    private val extraNeighborhood: String
    private val extraDays: String
    private val extraFrequency: String
    private val extraInterval: Int
    private val extraStart: String
    private val extraEnd: String
    private val extraExcludedDays: String
    private val extraTime: String
    private val extraHost: String
    private val extraPlace: String
    private val extraPrice: String
    private val extraLink: String

    override fun getPosition(): LatLng {
        return location
    }

    override fun getTitle(): String? {
        return title
    }

    override fun getSnippet(): String? {
        return snippet
    }

    /**
     * getters for extras
     */
    fun getExtraID(): String{
        return extraID
    }
    fun getExtraDescription(): String{
        return extraDescription
    }
    fun getExtraCategory(): String{
        return extraCategory
    }
    fun getExtraAddress(): String{
        return extraAddress
    }
    fun getExtraDistrict(): String{
        return extraDistrict
    }
    fun getExtraNeighborhood(): String{
        return extraNeighborhood
    }
    fun getExtraDays(): String{
        return extraDays
    }
    fun getExtraFrequency(): String{
        return extraFrequency
    }
    fun getExtraInterval(): Int{
        return extraInterval
    }
    fun getExtraStart(): String{
        return extraStart
    }
    fun getExtraEnd(): String{
        return extraEnd
    }
    fun getExtraExcludedDays(): String{
        return extraExcludedDays
    }
    fun getExtraTime(): String{
        return extraTime
    }
    fun getExtraHost(): String{
        return extraHost
    }
    fun getExtraPlace(): String{
        return extraPlace
    }
    fun getExtraPrice(): String{
        return extraPrice
    }
    fun getExtraLink(): String{
        return extraLink
    }

    init {
        location = eventLocation
        title = eventTitle
        snippet = eventPlace
        /**
         * extras
         */
        extraID = eventID
        extraDescription = eventDescription
        extraCategory = eventCategory
        extraAddress = eventAddress
        extraDistrict = eventDistrict
        extraNeighborhood = eventNeighborhood
        extraDays = eventDays
        extraFrequency = eventFrequency
        extraInterval = eventInterval
        extraStart = eventStart
        extraEnd = eventEnd
        extraExcludedDays = eventExcludedDays
        extraTime = eventTime
        extraHost = eventHost
        extraPlace = eventPlace
        extraPrice = eventPrice
        extraLink = eventLink

    }

}