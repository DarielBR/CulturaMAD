package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.datamodel.CulturalEventMadrid
import com.upmgeoinfo.culturamad.datamodel.MarkerData
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

@Composable
fun MockEventCard(
    //culturalEventMadrid: CulturalEventMadrid,
    modifier: Modifier = Modifier
){
    Card(
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
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
        MockEventCard(/*culturalEventMadrid = MarkerData.dataList[0]*/)
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

/**
 * Actual function called from the application
 */

@Composable
fun EventCard(
    culturalEventMadrid: CulturalEventMadrid,
    modifier: Modifier = Modifier
){
    Card(
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
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
                        text = culturalEventMadrid.title,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(bottom = 2.dp)
                    )
                    Text(//Address
                        text = culturalEventMadrid.address.area.streetAddress,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(top = 2.dp,bottom = 2.dp)
                    )
                    val isFree = (culturalEventMadrid.price == "")
                    Surface(
                        shape = MaterialTheme.shapes.extraSmall,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .padding(top = 2.dp)
                    ){
                        Text(//Price
                            text = if(isFree) "Free" else culturalEventMadrid.price,
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
                        text = culturalEventMadrid.description,
                        //text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
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
            if(culturalEventMadrid.recurrence != null){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val start = culturalEventMadrid.dtstart
                    val end = culturalEventMadrid.dtend
                    val excluded = culturalEventMadrid.excludedDays
                    val freqs = culturalEventMadrid.recurrence.days
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Text(//Dates
                            text = "Desde el $start, hasta el $end, excepto: $excluded .",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(//Freq
                            text = freqs,
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