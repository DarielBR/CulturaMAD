package com.upmgeoinfo.culturamad.ui.composables

/**
 * DEPRECATED (marked for delete)
 */
import android.content.res.Configuration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
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
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MapScreenWithClusterPreview(){
    CulturaMADTheme {
        MapScreenWithCluster()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@MapsComposeExperimentalApi
@Composable
fun MapScreenWithCluster(){
    val madrid = LatLng(40.4169087, -3.7035386)
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(madrid, 10f)
    }
    val context = LocalContext.current
    lateinit var clusterManager: ClusterManager<CulturalEventItem>
    var currentEventToShow by remember { mutableStateOf<CulturalEventMadrid>(
        CulturalEventMadrid(
            address = Address(Area(postalCode = "", streetAddress = "", locality = "", Id = ""), District(Id = "")),
            references = References(Id = ""),
            category = "",
            link = "",
            description = "",
            title = "",
            relation = Relation(Id = ""),
            recurrence = Recurrence(days = "", interval = 0, frequency = ""),
            uid = "",
            price = "",
            eventLocation = "",
            organization = Organization(accesibility = "", organizationName = ""),
            excludedDays = "",
            location = Location(latitude = 0.0, longitude = 0.0),
            aid = "",
            id = "",
            time = "",
            free = 1,
            dtend = "",
            dtstart = ""
        )
    ) }
    var openBottomSheet by remember { mutableStateOf(false) }
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    GoogleMap(
        cameraPositionState = cameraPositionState
    ){
        MapEffect() { map ->
            clusterManager = ClusterManager(context,map)
            map.setOnCameraIdleListener(clusterManager)
            map.setOnMarkerClickListener(clusterManager)

            addCulturalEventItems(clusterManager)
            clusterManager.setOnClusterItemInfoWindowClickListener(){
                val culturalEventsMadrid = MarkerData.dataList
                currentEventToShow = culturalEventsMadrid[0]
                for(event in culturalEventsMadrid){
                    if(event.location != null){
                        if(event.id == it.snippet){
                            currentEventToShow = event
                            openBottomSheet = true
                            break
                        }
                    }
                }
            }
        }
    }
    if(openBottomSheet){
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState
        ) {
            //EventCard(culturalEventMadrid = currentEventToShow)
            MockEventCard()
        }
    }
}

fun addCulturalEventItems(clusterManager: ClusterManager<CulturalEventItem>) {
    val culturalEventsMadrid = MarkerData.dataList

    for(event in culturalEventsMadrid){
        if(event.location != null){
            val culturalEventItem = CulturalEventItem(
                lat = event.location.latitude,
                lng = event.location.longitude,
                markerTitle = event.title,
                id = event.id
            )

            clusterManager.addItem(culturalEventItem)
        }
    }
}

fun addCulturalEventItemsStatic(clusterManager: ClusterManager<CulturalEventItem>) {
    // Set some lat/lng coordinates to start with.
    var lat = 40.4169087
    var lng = -3.7035386

    // Add ten cluster items in close proximity, for purposes of this example.
    for (i in 0..9) {
        val offset = i / 60.0
        lat += offset
        lng += offset
        val offsetItem =
            CulturalEventItem(lat, lng, "Title $i", "Snippet $i")
        clusterManager.addItem(offsetItem)
    }
}

/**
 * Cluster Implementation.
 */

class CulturalEventItem(
    lat: Double,
    lng: Double,
    markerTitle: String,
    id: String
): ClusterItem{

    private val position: LatLng
    private val title: String
    private val snippet: String

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String? {
        return title
    }

    override fun getSnippet(): String {
        return snippet
    }

    fun getZIndex(): Float{
        return 0f
    }

    init {
        position = LatLng(lat,lng)
        title = markerTitle
        snippet = id
    }

}

/*private fun addCulturalEventItems(){
    val culturalEventsMadrid = MarkerData.dataList

}*/
