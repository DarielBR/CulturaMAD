package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MapScreenWithClusterPreview(){
    CulturaMADTheme {
        MapScreenWithCluster()
    }
}

@Composable
fun MapScreenWithCluster(){

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