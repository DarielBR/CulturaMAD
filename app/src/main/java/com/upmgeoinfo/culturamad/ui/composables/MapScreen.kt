package com.upmgeoinfo.culturamad.ui.composables

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.upmgeoinfo.culturamad.datamodel.CulturalEventMadrid
import com.upmgeoinfo.culturamad.datamodel.MarkerData

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
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW),
        snippet = culturalEvent.description,
        onInfoWindowLongClick = {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TITLE, culturalEvent.title)
                putExtra(Intent.EXTRA_TEXT, culturalEvent.link)
            }
            context.startActivity(Intent.createChooser(intent,"Compartir")) },
        onInfoWindowClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(culturalEvent.link))
            context.startActivity(intent)
        }
    ){
        MarkerCard(culturalEvent = culturalEvent)
    }
}

/**
 * Recovers the data from all cultural events extrated from the web and creates a marker for each
 * valid cultural event that match with the search values.
 */
@Composable
fun RefreshMarkers(
    searchValue: String
) {
    val culturalEvents = MarkerData.dataList
    for (culturalEvent in culturalEvents) {
        if (culturalEvent.location != null
            && culturalEvent.category != null
            && culturalEvent.title != null
            && culturalEvent.dtstart != null
            && culturalEvent.address.district.Id != null
            && culturalEvent.description != null
        ) {
            if (searchValue == "") {
                CreateMarker(culturalEvent = culturalEvent)
            } else if (culturalEvent.category.contains(searchValue, true)
                || culturalEvent.title.contains(searchValue, true)
                || culturalEvent.dtstart.contains(searchValue, true)
                || culturalEvent.address.district.Id.contains(searchValue, true)
                || culturalEvent.description.contains(searchValue, true)
            ) {
                CreateMarker(culturalEvent = culturalEvent)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)//Necessary for using rememberPermissionState
@Composable
fun MapScreen(searchValue: String){
    val madrid = LatLng(40.4169087, -3.7035386)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(madrid, 10f)
    }
    val locationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    val myProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = locationPermissionState.status.isGranted
            )
        )
    }
    val myUiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false)) }

    GoogleMap(
        cameraPositionState = cameraPositionState,
        modifier = Modifier.fillMaxSize(),
        properties = myProperties,
        uiSettings = myUiSettings,
    ) {
        RefreshMarkers(searchValue = searchValue)
    }
}