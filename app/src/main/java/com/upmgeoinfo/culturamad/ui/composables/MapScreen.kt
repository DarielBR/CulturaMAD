package com.upmgeoinfo.culturamad.ui.composables

/**
 * DEPRECATED (marked for delete)
 */

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.datamodel.CulturalEventMadrid
import com.upmgeoinfo.culturamad.datamodel.MarkerData
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.rotate

/**
 * Creates a marker with the information taken form a cultural event.
 */
@Composable
fun CreateMarker(
    culturalEvent: CulturalEventMadrid
) {
    val context = LocalContext.current
    MarkerInfoWindow(
        state = MarkerState(
            position = LatLng(
                culturalEvent.location.latitude,
                culturalEvent.location.longitude
            )
        ),
        title = culturalEvent.title,
        draggable = false,
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
        //icon = BitmapDescriptorFactory.fromResource(R.drawable.cmad_starmarker),
        snippet = culturalEvent.description,
        onInfoWindowLongClick = {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TITLE, culturalEvent.title)
                putExtra(Intent.EXTRA_TEXT, culturalEvent.link)
            }
            context.startActivity(Intent.createChooser(intent, "Compartir"))
        },
        onInfoWindowClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(culturalEvent.link))
            context.startActivity(intent)
        }
    ) {
        MarkerCard(culturalEvent = culturalEvent)
    }
}

/**
 * Recovers the data from all cultural events extracted from the web and creates a marker for each
 * valid cultural event that match with the search values.
 */
@Composable
fun RefreshMarkers(
    searchValue: String,
    categoryDance: Boolean,
    categoryMusic: Boolean,
    categoryPainting: Boolean,
    categoryTheatre: Boolean
) {
    val culturalEvents = MarkerData.dataList
    for (culturalEvent in culturalEvents) {//First filter for valid culturalEvent
        if (culturalEvent.location != null
            && culturalEvent.address.district != null
            && culturalEvent.category != null
        ) {//Filter by categories if any.
            if (categoryDance && culturalEvent.category.contains("DanzaBaile")) {
                if (searchValue == "") {
                    CreateMarker(culturalEvent = culturalEvent)
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dtstart.contains(searchValue, true)
                    || culturalEvent.address.district.Id.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    CreateMarker(
                        culturalEvent = culturalEvent
                    )
                }
            } else if (categoryMusic && culturalEvent.category.contains("Musica")) {
                if (searchValue == "") {
                    CreateMarker(culturalEvent = culturalEvent)
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dtstart.contains(searchValue, true)
                    || culturalEvent.address.district.Id.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    CreateMarker(
                        culturalEvent = culturalEvent
                    )
                }
            } else if (categoryPainting && culturalEvent.category.contains("Exposiciones")) {
                if (searchValue == "") {
                    CreateMarker(culturalEvent = culturalEvent)
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dtstart.contains(searchValue, true)
                    || culturalEvent.address.district.Id.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    CreateMarker(
                        culturalEvent = culturalEvent
                    )
                }
            } else if (categoryTheatre && culturalEvent.category.contains("TeatroPerformance")) {
                if (searchValue == "") {
                    CreateMarker(culturalEvent = culturalEvent)
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dtstart.contains(searchValue, true)
                    || culturalEvent.address.district.Id.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    CreateMarker(
                        culturalEvent = culturalEvent
                    )
                }
            } else if (!categoryDance && !categoryMusic && !categoryPainting && !categoryTheatre) {
                if (searchValue == "") {
                    CreateMarker(culturalEvent = culturalEvent)
                } else if (culturalEvent.category.contains(searchValue, true)
                    || culturalEvent.title.contains(searchValue, true)
                    || culturalEvent.dtstart.contains(searchValue, true)
                    || culturalEvent.address.district.Id.contains(searchValue, true)
                    || culturalEvent.description.contains(searchValue, true)
                ) {
                    CreateMarker(
                        culturalEvent = culturalEvent
                    )
                }
            }
        }
    }
}

/**
 * Creates an instance of GoogleMap as a composable function. Also sets visual aspects of
 * the map and calls [RefreshMarkers] to generate an update collection of markers.
 */
@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)//Necessary for using rememberPermissionState
@Composable
fun MapScreen(
    fuseLocationClient: FusedLocationProviderClient,
    searchValue: String,
    categoryDance: Boolean,
    categoryMusic: Boolean,
    categoryPainting: Boolean,
    categoryTheatre: Boolean
) {
    /**
     * Creating location to move camera to when MapVire si created
     */
    val madrid = LatLng(40.4169087, -3.7035386)
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
     * creating the camera position variable to be passed to the MapProperties
     */
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(madrid, 10f)//usar madrid
    }
    /**
     *context and darkTheme will be used to configure the MapView and other logic ahead.
     */
    val context = LocalContext.current
    val darkTheme: Boolean = isSystemInDarkTheme()

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
     * Applying correct size to our window attending to the navigation mode set in the device
     */
    var isNavigationBarVisible by remember{ mutableStateOf(false) }
    val resources = context.resources
    val resourceId = resources.getIdentifier("config_navBarInteractionMode", "integer", "android")
    val interactionMode = resources.getInteger(resourceId)
    /**
     * 0-> 3 button mode
     * 1-> 2 button mode
     * 2-> gesture mode
     */
    isNavigationBarVisible = interactionMode < 2
    //gesture 84, buttons 168

    GoogleMap(
        cameraPositionState = cameraPositionState,
        modifier = Modifier
            .padding(bottom = if (!isNavigationBarVisible) 0.dp else 48.dp)
            .fillMaxSize(),
        properties = myProperties,
        uiSettings = myUiSettings,
    ) {
        RefreshMarkers(
            searchValue = searchValue,
            categoryDance = categoryDance,
            categoryMusic = categoryMusic,
            categoryPainting = categoryPainting,
            categoryTheatre = categoryTheatre
        )
    }
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
            drawableResource = R.drawable.cmad_mylocation,
            0.0f
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
            drawableResource = R.drawable.cmad_compass,
            0.0f
        )
    }
}

@Composable
fun MapButton(
    onClick: () -> Unit,
    drawableResource: Int,
    rotation: Float
) {
    Surface(
        elevation = 1.dp,
        shape = CircleShape,
        color = Color.Transparent,
        modifier = Modifier
            .padding(all = 4.dp)
            .alpha(0.8f)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
                .background(color = MaterialTheme.colorScheme.surface)
                .alpha(1f)
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.onSurface,
                painter = painterResource(id = drawableResource),
                contentDescription = null,
                modifier = Modifier
                    .rotate(rotation)
            )
        }
    }
}
