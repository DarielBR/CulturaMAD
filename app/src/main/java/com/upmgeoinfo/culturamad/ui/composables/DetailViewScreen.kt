package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.datamodel.CulturalEvent
import com.upmgeoinfo.culturamad.ui.composables.prefab.CategoryTag
import com.upmgeoinfo.culturamad.ui.composables.prefab.PriceTag

@Composable
fun DetailViewScreen(
    culturalEvent: CulturalEvent,
    //TODO: Here will be listed the navigation lambdas
){
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter =
                    if (culturalEvent!!.category.contains("DanzaBaile"))
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

                Spacer(modifier = Modifier.height(45.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = { /*TODO: Navigation to Details Screen*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        shape = MaterialTheme.shapes.extraSmall,
                        modifier = Modifier
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowLeft,
                                tint = Color.White,
                                contentDescription = null
                            )
                            Text(
                                text = stringResource(id = R.string.ui_nav_back),
                                color = Color.White,
                                fontWeight = FontWeight.Light,
                                fontSize = 14.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = { /*TODO favorite action.*/ },
                        modifier = Modifier
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White
                                .copy(alpha = 0.6f),
                            modifier = Modifier
                        ) {
                            Icon(//TODO: the appearance and behavior of this icon and the button must be subjected to user fav data
                                imageVector = Icons.Filled.FavoriteBorder,
                                tint = Color.White,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(4.dp)
                            )
                        }
                    }
                }
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .align(alignment = Alignment.BottomStart)
                        .fillMaxWidth()
                        //.height(150.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = culturalEvent.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        CategoryTag(
                            category = culturalEvent.category,
                            fontSize = 20.sp
                        )
                        PriceTag(
                            price = culturalEvent.price,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun DetailViewScreenPreview(){
    DetailViewScreen(culturalEvent = mockEvent)
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
    bookmark = false,
    review = 0
)