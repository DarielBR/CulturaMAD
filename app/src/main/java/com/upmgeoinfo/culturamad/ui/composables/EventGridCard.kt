package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.maps.model.Circle
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.viewmodels.main.model.CulturalEvent
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import com.upmgeoinfo.culturamad.viewmodels.MainViewModel
import kotlin.math.roundToInt

@Composable
fun EventGridCard(
    viewModel: MainViewModel? = null,
    culturalEvent: CulturalEvent,
    onClick: () -> Unit,
){
    viewModel?.refreshDeviceLocation()
    androidx.compose.material.Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .padding(8.dp)
            .size(width = 300.dp, height = 250.dp)//240-150
            .clickable { onClick.invoke() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ){
            Row(//Upper half
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    //.size(width = 240.dp, height = 75.dp)
            ) {
                Box(//Box containing the upper half
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Image(painter =
                        if (culturalEvent.category.contains("DanzaBaile"))
                            painterResource(id = R.drawable.dance_image)
                        else if (culturalEvent.category.contains("Musica"))
                            painterResource(id = R.drawable.music_image)
                        else if (culturalEvent.category.contains("Exposiciones"))
                            painterResource(id = R.drawable.painting_image)
                        else painterResource(id = R.drawable.teatro_image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                    )

                    Box(//For category label
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .padding(2.dp)
                                .alpha(0.8f)
                        ) {
                            Text(
                                text =
                                if (culturalEvent.category.contains("DanzaBaile"))
                                    stringResource(id = R.string.category_dance)
                                else if (culturalEvent.category.contains("Musica"))
                                    stringResource(id = R.string.category_music)
                                else if (culturalEvent.category.contains("Exposiciones"))
                                    stringResource(id = R.string.category_painting)
                                else if (culturalEvent.category.contains("TeatroPerformance"))
                                    stringResource(id = R.string.category_theatre)
                                else culturalEvent.category,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                fontSize = 8.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .padding(4.dp)
                            )
                        }
                    }

                    Box(//For favorite button
                        modifier = Modifier
                            .align(alignment = Alignment.TopEnd)
                    ){
                        var favorite by remember { mutableStateOf(false) }
                        favorite = viewModel?.state?.items?.find { it.id == culturalEvent.id }?.favorite ?: false
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier
                                .padding(2.dp)
                                .alpha(0.8f)
                                .clickable {
                                    favorite = !favorite
                                    viewModel?.changeFavoriteState(
                                        culturalEvent = culturalEvent,
                                        favorite = favorite
                                    )
                                }

                        ) {
                            androidx.compose.material3.Icon(
                                imageVector =
                                if (favorite) Icons.Default.Favorite
                                else Icons.Default.FavoriteBorder,
                                contentDescription = "",
                                tint = Color.Gray,
                                modifier = Modifier
                                    .padding(1.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.BottomStart)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(4.dp)
                                .align(alignment = Alignment.BottomStart)
                        ){
                            Text(//Title
                                text = culturalEvent.title,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White
                            )
                            Text(//Place
                                text = culturalEvent.place,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            Row(//Lower half
                modifier = Modifier
                    .size(width = 240.dp, height = 75.dp)
            ) {
                Box(//Box containing the lowe half
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                    ){
                        val color = if(culturalEvent.price == "") Color.Green
                        else MaterialTheme.colorScheme.surfaceVariant
                        Surface(
                            shape = MaterialTheme.shapes.extraSmall,
                            color = color,
                            modifier = Modifier
                                .alpha(if (culturalEvent.price == "") 0.5f else 1.0f)
                                .padding(4.dp)
                        ) {
                            Text(
                                text = if(culturalEvent.price == "") "GRATIS"
                                else "€ " + culturalEvent.price,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier
                                    .padding(2.dp)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.BottomStart)
                            .padding(start = 6.dp)
                    ){
                        var distanceToPoint: Double? by remember { mutableStateOf(null) }
                        distanceToPoint = viewModel?.calculateDistanceOverEarth(
                            latitude = culturalEvent.latitude.toDouble(),
                            longitude = culturalEvent.longitude.toDouble()
                        )
                        Text(
                            text =
                                if (distanceToPoint != null)
                                    culturalEvent.address + " | " + (distanceToPoint!! * 10).roundToInt().toDouble()/10  + "Km"
                                else culturalEvent.address,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .padding(6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdGridCard(
    onClick: () -> Unit
){
    androidx.compose.material.Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .padding(8.dp)
            .size(width = 300.dp, height = 250.dp)//240-150
            .clickable { onClick.invoke() }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            AndroidView(
                modifier = Modifier
                    .fillMaxSize(),
                factory = { context ->
                    AdView(context).apply {
                        setAdSize(AdSize.MEDIUM_RECTANGLE)
                        adUnitId = "ca-app-pub-3940256099942544/6300978111"
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )
        }
    }
}

private var mockEvent = CulturalEvent(
    id = 1111111,
    category = "MOCK",
    title = "Mock Cultural Event",
    description = "This is a faux cultural event fort testing and developing purposes.",
    latitude = "40.4169473",
    longitude = "-3.7035285",
    address = "Puerta del Sol, 7",
    district = "Centro",
    neighborhood = "Centro",
    days = "SU, MO, TU, WE, TH, FR, SA",
    frequency = "Daily",
    interval = 1,
    dateStart = "2023-09-01 00:00:00.0",
    dateEnd = "2023-12-31 00:00:00.0",
    hours = "All day",
    excludedDays = "",
    place = "Plaza del Sol",
    host = "Ayuntamiento de Madrid",
    price = "",
    link = "https://www.miradormadrid.com/placa-del-kilometro-cero/",
    favorite = false,
    rate = 0.0f,
    review = ""
)

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EventGridCardPreview(){
    CulturaMADTheme {
        Column{
            Column(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
            ){
                EventGridCard(
                    culturalEvent = mockEvent,
                    onClick = {}
                )
                //AdGridCard{}

            }
        }
    }
}

