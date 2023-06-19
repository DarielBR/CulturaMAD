package com.upmgeoinfo.culturamad.ui.composables

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.room.Room
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.datamodel.CulturalEvent
import com.upmgeoinfo.culturamad.datamodel.CulturalEventMadrid
import com.upmgeoinfo.culturamad.datamodel.MainViewModel
import com.upmgeoinfo.culturamad.datamodel.MarkerData
import com.upmgeoinfo.culturamad.datamodel.database.CulturalEventDatabase
import com.upmgeoinfo.culturamad.datamodel.database.CulturalEventRepository
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class
)
@MapsComposeExperimentalApi
@Composable
fun ClusterMapScreen(
    fuseLocationClient: FusedLocationProviderClient,
    viewModel: MainViewModel
){
    CulturaMADTheme(){
        /**
         * Filter values
         */
        var searchValue by remember { mutableStateOf("") }
        var danceFilter by remember { mutableStateOf(false) }
        var musicFilter by remember { mutableStateOf(false) }
        var paintingFilter by remember { mutableStateOf(false) }
        var theatreFilter by remember { mutableStateOf(false) }

        /**
         * Keyboard Controller value to control keyboard behavior when using the search bar.
         */
        val keyboardController = LocalSoftwareKeyboardController.current
        /**
         * Obtaining Location, permission requests is done before [MapScreen] function os called.
         * thus, will not be controlled here. ([@suppressLint("MissingPermission")])
         */
        var myLocation by remember { mutableStateOf(LatLng(0.0,0.0)) }
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
         * State values to control camera animations from MyLocation and Compass buttons
         */
        var animateZoom by remember { mutableStateOf(false) }
        var animateBearing by remember { mutableStateOf(false) }
        /**
         *context and darkTheme will be used to configure the MapView and other logic ahead.
         */
        val context = LocalContext.current
        val darkTheme: Boolean = isSystemInDarkTheme()
        /**
         * Customizing the SystemBars
         */
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = !darkTheme
                // isNavigationBarContrastEnforced = true
            )
            systemUiController.setNavigationBarColor(
                color = Color.Transparent,
                darkIcons = !darkTheme
            )
        }
        /**
         * Map Properties and UISettings
         */
        val myProperties by remember {
            mutableStateOf(
                MapProperties(
                    mapType = MapType.NORMAL,
                    isMyLocationEnabled = locationPermissionState.status.isGranted,
                    mapStyleOptions = if (!darkTheme) MapStyleOptions.loadRawResourceStyle(
                        context,
                        R.raw.map_style_silver
                    )
                    else MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_dark)
                )
            )
        }
        val myUiSettings by remember {
            mutableStateOf(
                MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = false,
                    compassEnabled = false,
                    mapToolbarEnabled = false
                )
            )
        }
        /**
         * Cluster Manager
         */
        var clusterManager by remember {
            mutableStateOf<ClusterManager<CulturalEventMadridItem>?>(
                null
            )
        }
        var refreshClusterItems by remember { mutableStateOf(true) }
        /**
         * EventCard control values
         */
        var currentEventToShow by remember {
            mutableStateOf<CulturalEventMadridItem>(
                createEmptyCulturalEventItem()
            )
        }
        var openEventCard by remember { mutableStateOf(false) }
        /**
         * Applying correct size to our window attending to the navigation mode set in the device
         * * 0-> 3 button mode
         * 1-> 2 button mode
         * 2-> gesture mode
         *
         * gesture 84, buttons 168
         */
        var isNavigationBarVisible by remember { mutableStateOf(false) }
        val resources = context.resources
        val resourceId =
            resources.getIdentifier("config_navBarInteractionMode", "integer", "android")
        val interactionMode = resources.getInteger(resourceId)
        isNavigationBarVisible = interactionMode < 2
        /**
         * GoogleMap declaration
         */
        GoogleMap(
            onMapClick = {openEventCard = false},
            cameraPositionState = cameraPositionState,
            modifier = Modifier
                .padding(bottom = if (!isNavigationBarVisible) 0.dp else 48.dp)
                .fillMaxSize(),
            properties = myProperties,
            uiSettings = myUiSettings,
        ) {
            if (refreshClusterItems || animateZoom || animateBearing) {
                MapEffect() { map ->
                    if (clusterManager == null) clusterManager = ClusterManager(context, map)
                    map.setOnCameraIdleListener(clusterManager)
                    map.setOnMarkerClickListener(clusterManager)
                    /**
                     * Populating the ClusterItems list with the filtering options
                     */
                    /*val items = getCulturalEvents(
                        searchValue = searchValue,
                        categoryDance = danceFilter,
                        categoryMusic = musicFilter,
                        categoryPainting = paintingFilter,
                        categoryTheatre = theatreFilter
                    )*/
                    val items = getCulturalEventsFromDatabase(
                        viewModel = viewModel,
                        context = context,
                        searchValue = searchValue,
                        categoryDance = danceFilter,
                        categoryMusic = musicFilter,
                        categoryPainting = paintingFilter,
                        categoryTheatre = theatreFilter
                    )
                    val cmadRenderer = CMADClusterMarkerRenderer(context, map, clusterManager!!)
                    clusterManager?.renderer = cmadRenderer

                    clusterManager?.clearItems()
                    clusterManager?.addItems(items)
                    clusterManager?.cluster()
                    /**
                     * flag state change
                     */
                    refreshClusterItems = false

                    clusterManager?.setOnClusterItemClickListener {
                        currentEventToShow = it
                        viewModel.setCurrentItem(it.getExtraID())
                        openEventCard = true
                        return@setOnClusterItemClickListener false
                    }
                    /**
                     * Camera animations when buttons MyLocation and Compass are pressed.
                     */
                    if(animateZoom){
                        map.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(myLocation, 15f)
                        )
                        animateZoom = false
                    }
                    if(animateBearing){
                        val currentCameraLocation = LatLng(
                            cameraPositionState.position.target.latitude,
                            cameraPositionState.position.target.longitude
                        )
                        map.animateCamera(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition(
                                    currentCameraLocation,
                                    cameraPositionState.position.zoom,
                                    cameraPositionState.position.tilt,
                                    0.0f)
                            )
                        )
                        animateBearing = false
                    }
                }
            }
        }
        /**
         * Filtering Elements
         */
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {

            Spacer(
                modifier = Modifier
                    .height(45.dp)
            )
            /**
             * Declaring a SearchBar on top of the screen. A Composable function won't be used in
             * this case because si necessary to modify a variable external to the function's scope.
             * In particular the searchValue, will be used modified in the following declaration and
             * it will be used in other task furthermore.
             */
            Surface(
                elevation = 2.dp,
                shape = MaterialTheme.shapes.medium.copy(all = CornerSize(40)),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                TextField(
                    value = searchValue,
                    onValueChange = { newText: String ->
                        searchValue = newText
                        refreshClusterItems = true
                    },
                    shape = MaterialTheme.shapes.medium.copy(all = CornerSize(40)),
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                searchValue = ""
                                keyboardController?.hide()
                                refreshClusterItems = true
                            }
                        ) {
                            Icon(
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                painter = painterResource(id = R.drawable.cmad_close),
                                contentDescription = null
                            )
                        }

                    },
                    placeholder = {
                                    Text(
                                        stringResource(id = R.string.placeholder_search)
                                    )
                                  },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            /**
             * Declaring category filtering buttons
             */
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(categoriesPairsData) { item ->
                    when (stringResource(id = item.text)) {
                        "Danza" -> {
                            FilterItem(
                                filterStatus = danceFilter,
                                drawableResource = item.drawable,
                                stringResource = item.text,
                                onClick = {
                                    danceFilter = !danceFilter
                                    refreshClusterItems = true
                                }
                            )
                        }
                        "Música" -> {
                            FilterItem(
                                filterStatus = musicFilter,
                                drawableResource = item.drawable,
                                stringResource = item.text,
                                onClick = {
                                    musicFilter = !musicFilter
                                    refreshClusterItems = true
                                }
                            )
                        }
                        "Pintura" -> {
                            FilterItem(
                                filterStatus = paintingFilter,
                                drawableResource = item.drawable,
                                stringResource = item.text,
                                onClick = {
                                    paintingFilter = !paintingFilter
                                    refreshClusterItems = true
                                }
                            )
                        }
                        "Teatro" -> {
                            FilterItem(
                                filterStatus = theatreFilter,
                                drawableResource = item.drawable,
                                stringResource = item.text,
                                onClick = {
                                    theatreFilter = !theatreFilter
                                    refreshClusterItems = true
                                }
                            )
                        }
                    }
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
            MapButton(//MyLocation
                onClick = {
                    animateZoom = true
                },
                drawableResource = R.drawable.cmad_mylocation
            )
            MapButton(
                onClick = {
                    animateBearing = true
                },
                drawableResource = R.drawable.cmad_compass
            )
        }
        /**
         * EventCard declaration
         */
        EventCard(
            viewModel = viewModel,
            culturalEventMadridItem = currentEventToShow,
            closeClick = { openEventCard = false },
            visibility = openEventCard,
            navigationBarVisible = isNavigationBarVisible,
            myLocation = myLocation
        )
    }
}//CusterMapScreen function end

private data class DrawableStringImagePair(
    @DrawableRes val drawable: Int,
    @StringRes val text: Int
)

private val categoriesPairsData = listOf(
    R.drawable.dance_image to R.string.category_dance,
    R.drawable.painting_image to R.string.category_painting,
    R.drawable.music_image to R.string.category_music,
    R.drawable.teatro_image to R.string.category_theatre
).map { DrawableStringImagePair(it.first, it.second) }

fun getCulturalEvents(
    searchValue: String,
    categoryDance: Boolean,
    categoryMusic: Boolean,
    categoryPainting: Boolean,
    categoryTheatre: Boolean
): List<CulturalEventMadridItem>{

    val culturalEventsMadrid = MarkerData.dataList
    val culturalEventItems = mutableListOf<CulturalEventMadridItem>()

    for (culturalEvent in culturalEventsMadrid) {//First filter for valid culturalEvent
        if (culturalEvent.location != null
            && culturalEvent.address.district != null
            && culturalEvent.category != null
            && culturalEvent.recurrence != null
            && culturalEvent.address.area != null
        ) {//Filter by categories if any.
            if (categoryDance && culturalEvent.category.contains("DanzaBaile")) {
                if (searchValue == "") {
                    culturalEventItems.add(createCulturalEventMadridItem(culturalEvent))
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dtstart.contains(searchValue, true)
                    || culturalEvent.address.district.Id.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    culturalEventItems.add(createCulturalEventMadridItem(culturalEvent))
                }
            } else if (categoryMusic && culturalEvent.category.contains("Musica")) {
                if (searchValue == "") {
                    culturalEventItems.add(createCulturalEventMadridItem(culturalEvent))
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dtstart.contains(searchValue, true)
                    || culturalEvent.address.district.Id.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    culturalEventItems.add(createCulturalEventMadridItem(culturalEvent))
                }
            } else if (categoryPainting && culturalEvent.category.contains("Exposiciones")) {
                if (searchValue == "") {
                    culturalEventItems.add(createCulturalEventMadridItem(culturalEvent))
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dtstart.contains(searchValue, true)
                    || culturalEvent.address.district.Id.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    culturalEventItems.add(createCulturalEventMadridItem(culturalEvent))
                }
            } else if (categoryTheatre && culturalEvent.category.contains("TeatroPerformance")) {
                if (searchValue == "") {
                    culturalEventItems.add(createCulturalEventMadridItem(culturalEvent))
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dtstart.contains(searchValue, true)
                    || culturalEvent.address.district.Id.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    culturalEventItems.add(createCulturalEventMadridItem(culturalEvent))
                }
            } else if (!categoryDance && !categoryMusic && !categoryPainting && !categoryTheatre) {
                if (searchValue == "") {
                    culturalEventItems.add(createCulturalEventMadridItem(culturalEvent))
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dtstart.contains(searchValue, true)
                    || culturalEvent.address.district.Id.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    culturalEventItems.add(createCulturalEventMadridItem(culturalEvent))
                }
            }
        }
    }
    return culturalEventItems.toList()
}

private fun getCulturalEventsFromDatabase(
    viewModel: MainViewModel,
    context: Context,
    searchValue: String,
    categoryDance: Boolean,
    categoryMusic: Boolean,
    categoryPainting: Boolean,
    categoryTheatre: Boolean
): List<CulturalEventMadridItem>{
    /**
     * Accessing the database.
     */
    /*val database = Room.databaseBuilder(context, CulturalEventDatabase::class.java, "culturalEvents_db").build()
    val dao = database.dao
    val culturalEventRepository = CulturalEventRepository(dao)
    val viewModel= MainViewModel(culturalEventRepository)*/

    val state = viewModel.state

    val culturalEventsFromDB = state.items.toList()
    /**
     * function return
     */
    val culturalEventItems = mutableListOf<CulturalEventMadridItem>()

    for (culturalEvent in culturalEventsFromDB) {
            if (categoryDance && culturalEvent.category.contains("DanzaBaile")) {
                if (searchValue == "") {
                    culturalEventItems.add(transformCulturalEventToClusterItem(culturalEvent))
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dateStart.contains(searchValue, true)
                    || culturalEvent.address.contains(searchValue, true)
                    || culturalEvent.district.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    culturalEventItems.add(transformCulturalEventToClusterItem(culturalEvent))
                }
            } else if (categoryMusic && culturalEvent.category.contains("Musica")) {
                if (searchValue == "") {
                    culturalEventItems.add(transformCulturalEventToClusterItem(culturalEvent))
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dateStart.contains(searchValue, true)
                    || culturalEvent.address.contains(searchValue, true)
                    || culturalEvent.district.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    culturalEventItems.add(transformCulturalEventToClusterItem(culturalEvent))
                }
            } else if (categoryPainting && culturalEvent.category.contains("Exposiciones")) {
                if (searchValue == "") {
                    culturalEventItems.add(transformCulturalEventToClusterItem(culturalEvent))
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dateStart.contains(searchValue, true)
                    || culturalEvent.address.contains(searchValue, true)
                    || culturalEvent.district.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    culturalEventItems.add(transformCulturalEventToClusterItem(culturalEvent))
                }
            } else if (categoryTheatre && culturalEvent.category.contains("TeatroPerformance")) {
                if (searchValue == "") {
                    culturalEventItems.add(transformCulturalEventToClusterItem(culturalEvent))
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dateStart.contains(searchValue, true)
                    || culturalEvent.address.contains(searchValue, true)
                    || culturalEvent.district.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    culturalEventItems.add(transformCulturalEventToClusterItem(culturalEvent))
                }
            } else if (!categoryDance && !categoryMusic && !categoryPainting && !categoryTheatre) {
                if (searchValue == "") {
                    culturalEventItems.add(transformCulturalEventToClusterItem(culturalEvent))
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dateStart.contains(searchValue, true)
                    || culturalEvent.address.contains(searchValue, true)
                    || culturalEvent.district.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    culturalEventItems.add(transformCulturalEventToClusterItem(culturalEvent))
                }
            }
    }
    return culturalEventItems.toList()
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

fun transformCulturalEventToClusterItem(culturalEvent: CulturalEvent): CulturalEventMadridItem{
    return CulturalEventMadridItem(
        eventID = culturalEvent.id.toString(),
        eventLocation = LatLng(culturalEvent.latitude.toDouble(), culturalEvent.longitude.toDouble()),
        eventTitle = culturalEvent.title,
        eventDescription = culturalEvent.description,
        eventCategory = culturalEvent.category,
        eventAddress = culturalEvent.address,
        eventDistrict = culturalEvent.district,
        eventNeighborhood = culturalEvent.neighborhood,
        eventDays = culturalEvent.days,
        eventFrequency = culturalEvent.frequency,
        eventInterval = culturalEvent.interval.toInt(),
        eventStart = culturalEvent.dateStart,
        eventEnd = culturalEvent.dateEnd,
        eventExcludedDays = culturalEvent.excludedDays,
        eventTime = culturalEvent.hours,
        eventPlace = culturalEvent.place,
        eventHost = culturalEvent.host,
        eventPrice = culturalEvent.price,
        eventLink = culturalEvent.link
    )
}

fun createEmptyCulturalEventItem(): CulturalEventMadridItem{
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
    private val extraTitle: String
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
    fun getExtraTitle(): String{
        return extraTitle
    }
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
        title = ""
        snippet = ""
        /**
         * extras
         */
        extraTitle = eventTitle
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

class CMADClusterMarkerRenderer(
    context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<CulturalEventMadridItem>
    ): DefaultClusterRenderer<CulturalEventMadridItem>(context, map, clusterManager){
    val context = context
    override fun onBeforeClusterItemRendered(
        item: CulturalEventMadridItem,
        markerOptions: MarkerOptions
    ) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        markerOptions!!.icon(getBitmapDescriptorFromVector(context, R.drawable.cmad_vestor_circle_marker))
    }

    fun getBitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = VectorDrawableCompat.create(context.resources, vectorResId, null)

        return BitmapDescriptorFactory.fromBitmap(vectorDrawable!!.toBitmap())
    }

}