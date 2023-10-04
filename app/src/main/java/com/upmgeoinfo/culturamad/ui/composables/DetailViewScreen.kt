package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
import android.os.Build
import android.widget.Space
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.sharp.DateRange
import androidx.compose.material.icons.twotone.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.datamodel.CulturalEvent
import com.upmgeoinfo.culturamad.datamodel.utils.ScheduleParser
import com.upmgeoinfo.culturamad.ui.composables.prefab.CategoryTag
import com.upmgeoinfo.culturamad.ui.composables.prefab.PriceTag
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
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
                            .padding(
                                start = 8.dp,
                                end = 8.dp,
                                top = 16.dp,
                                bottom = 8.dp
                            )
                            .fillMaxWidth()
                    ) {
                        Column{
                            Text(
                                text = culturalEvent.title,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                overflow = TextOverflow.Clip
                            )
                            Text(
                                text = culturalEvent.place,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Light,
                                color = Color.White,
                                overflow = TextOverflow.Clip,
                                modifier = Modifier
                                    .padding(bottom = 12.dp)
                            )
                            CategoryTag(
                                category = culturalEvent.category,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ){
                Column(
                    modifier = Modifier
                        .padding(
                            start = 18.dp,
                            end = 18.dp,
                            top = 16.dp,
                            bottom = 8.dp
                        )
                        .fillMaxSize()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                        ){
                            Row{
                                Text(
                                    text = stringResource(id = R.string.ui_organized_by),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                    fontWeight = FontWeight.Light,
                                    modifier = Modifier.padding(end = 2.dp)
                                )

                                Text(
                                    text = culturalEvent.host,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .padding(start = 2.dp)
                                        .clickable { /*TODO: Intent to the host page*/ }
                                )
                            }
                        }
                        Column{
                            PriceTag(
                                price = culturalEvent.price
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                        }
                        Column(
                            modifier = Modifier
                                .padding(start = 4.dp)
                        ) {
                            val scheduleBlock = ScheduleParser(
                                culturalEvent = culturalEvent,
                                txtDateBlock1 = stringResource(id = R.string.txt_date_block_1),
                                txtDateBlock2 = stringResource(id = R.string.txt_date_block_2),
                                txtParseDays1 = stringResource(id = R.string.txt_parse_days_1),
                                txtParseDays2 = stringResource(id = R.string.txt_parse_days_2),
                                txtMO = stringResource(id = R.string.txt_MO),
                                txtTU = stringResource(id = R.string.txt_TU),
                                txtWE = stringResource(id = R.string.txt_WE),
                                txtTH = stringResource(id = R.string.txt_TH),
                                txtFR = stringResource(id = R.string.txt_FR),
                                txtSA = stringResource(id = R.string.txt_SA),
                                txtSU = stringResource(id = R.string.txt_SU),
                                txtDaysHoursBlock1 = stringResource(id = R.string.txt_day_hours_block_1),
                                txtDaysHoursBlock2 = stringResource(id = R.string.txt_day_hours_block_2),
                            )
                            //val myDate = LocalDateTime.parse(culturalEvent.dateStart, DateTimeFormatter.ISO_DATE_TIME)
                            //Text(text = "")
                            Text(text = scheduleBlock.showParsedSchedule())
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun DetailViewScreenPreview(){
    CulturaMADTheme {
        DetailViewScreen(culturalEvent = mockEvent)
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
    days = "SU,MO,TU,WE,TH,FR,SA",
    frequency = "Daily",
    interval = 1,
    dateStart = "2023-09-01 00:00:00.0",
    dateEnd = "2023-12-31 00:00:00.0",
    hours = "20:00",
    excludedDays = "2/9/2023;5/10/2023;10/10/2023",
    place = "Plaza del Sol",
    host = "Ayuntamiento de Madrid",
    price = "",
    link = "https://www.miradormadrid.com/placa-del-kilometro-cero/",
    bookmark = false,
    review = 0
)
