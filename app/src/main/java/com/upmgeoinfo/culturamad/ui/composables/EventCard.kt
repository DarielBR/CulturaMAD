package com.upmgeoinfo.culturamad.ui.composables

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.datamodel.CulturalEventMadrid
import com.upmgeoinfo.culturamad.datamodel.MarkerData
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

@Composable
fun MockEventCard(
    modifier: Modifier = Modifier
){
    Card(
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp)
        ),
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.teatro_image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .size(75.dp)
                )
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(//Title
                        //text = culturalEventMadrid.title,
                        text = "Título",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(bottom = 2.dp)
                    )
                    Text(//Address
                        //text = culturalEventMadrid.address.area.streetAddress,
                        text = "Calle del Barrio No. 99. La Barriada.",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(top = 2.dp,bottom = 2.dp)
                    )
                    //val isFree = (culturalEventMadrid.price == "")
                    Surface(
                        shape = MaterialTheme.shapes.extraSmall,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .padding(top = 2.dp)
                    ){
                        Text(//Price
                            //text = if(isFree) "Free" else culturalEventMadrid.price,
                            text = "Gratis",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier
                                .padding(2.dp)
                        )
                    }
                }
            }
            val scrollState = rememberScrollState()
            LaunchedEffect(Unit) { scrollState.animateScrollTo(100) }
            Surface(
                tonalElevation = 3.dp,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .padding(8.dp)
            ){
                Column(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth()
                        .height(100.dp)
                        .verticalScroll(scrollState)
                ) {
                    Text(//Description
                        //text = culturalEventMadrid.description,
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify,
                        //overflow = TextOverflow.Ellipsis,
                        minLines = 4,
                        maxLines = 6,
                        modifier = Modifier
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                /*val start = culturalEventMadrid.dtstart
                val end = culturalEventMadrid.dtend
                val excluded = culturalEventMadrid.excludedDays
                val freqs = culturalEventMadrid.recurrence.days*/
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ){
                    Text(//Dates
                        //text = "Desde el $start, hasta el $end, excepto: $excluded .",
                        text = "desde el 31/02/1111 hasta el 01/13/1109",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(//Freq
                        //text = freqs,
                        text = "Los 8 dias de la octana",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                var favorite by remember { mutableStateOf(false) }
                ActionButton(//Bookmark
                    icon =  if(favorite) R.drawable.cmad_bookmark_true
                            else R.drawable.cmad_bookmark_false,
                    onClick = { favorite = !favorite }
                )
                ActionButton(//Share
                    icon = R.drawable.cmad_share,
                    onClick = { }
                )
                ActionButton(//go to
                    icon = R.drawable.cmad_link,
                    onClick = {}
                )
                ActionButton(
                    icon = R.drawable.cmad_calendar,
                    onClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MockEventCardPreview(){
    CulturaMADTheme {
        MockEventCard()
    }
}

@Composable
fun ActionButton(
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Surface(
        shape = MaterialTheme.shapes.small,
        shadowElevation = 2.dp,
        tonalElevation = 3.dp,
        modifier = Modifier
            .padding(8.dp)
    ) {
        IconButton(
            onClick = onClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier
                //.padding(8.dp)
        ) {
            Icon(
                painterResource(id = icon),
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ActionButtonPreview(){
    CulturaMADTheme {
        ActionButton(
            icon = R.drawable.cmad_link,
            onClick = {}
        )
    }
}

@Composable
fun CardButton(
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier
            .clip(CircleShape)
    ){
        IconButton(
            onClick = onClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier
            //.padding(8.dp)
        ) {
            Icon(
                painterResource(id = icon),
                contentDescription = null
            )
        }
    }
}

/**
 * Actual function called from the application
 */

@Composable
fun EventCard(
    culturalEventMadridItem: CulturalEventMadridItem,
    closeClick: () -> Unit,
    visibility: Boolean,
    navigationBarVisible: Boolean,
    myLocation: LatLng,
    modifier: Modifier = Modifier
){
    AnimatedVisibility(
        visible = visibility,
        enter = slideInVertically(
            animationSpec = tween(200),
            initialOffsetY = {1000}
        ) + fadeIn(),
        exit = fadeOut()
    ){
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = if (navigationBarVisible) 56.dp else 32.dp
                )
                .fillMaxSize()
        ) {
            androidx.compose.material.Card(
                shape = MaterialTheme.shapes.large,
                elevation = 2.dp,
                backgroundColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
            ) {
                Column(
                    modifier = Modifier
                ) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        CardButton(
                            icon = R.drawable.cmad_close,
                            onClick = closeClick
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = if (culturalEventMadridItem.getExtraCategory()
                                    .contains("DanzaBaile")
                            )
                                painterResource(id = R.drawable.dance_image)
                            else if (culturalEventMadridItem.getExtraCategory().contains("Musica"))
                                painterResource(id = R.drawable.music_image)
                            else if (culturalEventMadridItem.getExtraCategory()
                                    .contains("Exposiciones")
                            )
                                painterResource(id = R.drawable.painting_image)
                            else painterResource(id = R.drawable.teatro_image),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .size(75.dp)
                        )
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(//Title
                                text = culturalEventMadridItem.getExtraTitle()!!,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .padding(bottom = 2.dp)
                            )
                            Text(//Address
                                text = culturalEventMadridItem.getExtraAddress(),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .padding(top = 2.dp, bottom = 2.dp)
                            )
                            val isFree = (culturalEventMadridItem.getExtraPrice() == "")
                            Surface(
                                shape = MaterialTheme.shapes.extraSmall,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .padding(top = 2.dp)
                            ) {
                                Text(//Price
                                    text = if (isFree) "Free" else culturalEventMadridItem.getExtraPrice(),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier
                                        .padding(2.dp)
                                )
                            }
                        }
                    }
                    if(culturalEventMadridItem.getExtraDescription() != ""){
                        val scrollState = rememberScrollState()
                        LaunchedEffect(Unit) { scrollState.animateScrollTo(100) }
                        Surface(
                            tonalElevation = 3.dp,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .fillMaxWidth()
                                    .height(
                                        if (culturalEventMadridItem.getExtraDescription().length <= 54) 24.dp
                                        else if (culturalEventMadridItem.getExtraDescription().length in 55..110) 48.dp
                                        else if (culturalEventMadridItem.getExtraDescription().length in 111..165) 72.dp
                                        else 96.dp
                                    )
                                    .verticalScroll(scrollState)
                            ) {
                                Text(//Description
                                    text = culturalEventMadridItem.getExtraDescription(),
                                    //text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Justify,
                                    //overflow = TextOverflow.Ellipsis,
                                    /*maxLines = if(culturalEventMadridItem.getExtraDescription().length <= 54 ) 1
                                        else if (culturalEventMadridItem.getExtraDescription().length in 55..110) 2
                                        else if(culturalEventMadridItem.getExtraDescription().length in 111..165) 3
                                        else if(culturalEventMadridItem.getExtraDescription().length >= 166) 4
                                        else 6,*/
                                    //maxLines = 1,
                                    modifier = Modifier
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        val start = culturalEventMadridItem.getExtraStart().subSequence(0..9)
                        val end = culturalEventMadridItem.getExtraEnd().subSequence(0..9)
                        val schedule = LocalContext.current.getString(R.string.ui_schedule, start, end)
                        val hours = if(culturalEventMadridItem.getExtraTime() == "") ""
                            else LocalContext.current.getString(R.string.ui_hours, culturalEventMadridItem.getExtraTime())
                        val excluded = if(culturalEventMadridItem.getExtraExcludedDays() == "")""
                            else LocalContext.current.getString(R.string.ui_excluded_days, culturalEventMadridItem.getExtraExcludedDays())
                        val frequency = if(culturalEventMadridItem.getExtraFrequency() == "") ""
                            else if(culturalEventMadridItem.getExtraFrequency() == "WEEKLY")
                                LocalContext.current.getString(R.string.ui_frequency, LocalContext.current.getString(R.string.ui_weekly))
                        else LocalContext.current.getString(R.string.ui_frequency, culturalEventMadridItem.getExtraFrequency())
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Text(//Dates
                                text = schedule,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ){
                                if(hours != ""){
                                    Text(//Hours
                                        text = hours,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                if(excluded != ""){
                                    Text(//Excluded Days
                                        text = excluded,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                            if(frequency != ""){
                                Text(//frequency
                                    text = frequency,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        var favorite by remember { mutableStateOf(false) }
                        ActionButton(//Bookmark
                            icon = if (favorite) R.drawable.cmad_bookmark_true
                            else R.drawable.cmad_bookmark_false,
                            onClick = { favorite = !favorite }
                        )
                        val context = LocalContext.current
                        ActionButton(//Share
                            icon = R.drawable.cmad_share,
                            onClick = {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TITLE, culturalEventMadridItem.title)
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        culturalEventMadridItem.getExtraLink()
                                    )
                                }
                                context.startActivity(Intent.createChooser(intent, "Compartir"))
                            }
                        )
                        ActionButton(//link
                            icon = R.drawable.cmad_link,
                            onClick = {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(culturalEventMadridItem.getExtraLink())
                                )
                                context.startActivity(intent)
                            }
                        )
                        ActionButton(//calendar
                            icon = R.drawable.cmad_calendar,
                            onClick = {}
                        )
                        ActionButton(//directions
                            icon = R.drawable.cmad_directions,
                            onClick = {
                                val myLat = myLocation.latitude
                                val myLng = myLocation.longitude
                                val latitude = culturalEventMadridItem.position.latitude
                                val longitude = culturalEventMadridItem.position.longitude
                                val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        //Uri.parse("geo:$latitude,$longitude"
                                        Uri.parse("http://maps.google.com/maps?saddr=$myLat,$myLng&daddr=$latitude,$longitude")
                                    )
                                intent.setPackage("com.google.android.apps.maps")
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                context.startActivity(intent)
                            }
                        )
                        ActionButton(//open Maps
                            icon = R.drawable.cmad_maps,
                            onClick = {
                                val myLat = myLocation.latitude
                                val myLng = myLocation.longitude
                                val latitude = culturalEventMadridItem.position.latitude
                                val longitude = culturalEventMadridItem.position.longitude
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("geo:$latitude,$longitude")
                                    //Uri.parse("http://maps.google.com/maps?saddr=$myLat,$myLng&daddr=$latitude,$longitude")
                                )
                                intent.setPackage("com.google.android.apps.maps")
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}