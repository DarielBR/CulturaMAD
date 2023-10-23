package com.upmgeoinfo.culturamad.ui.composables

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.provider.CalendarContract
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.viewmodels.main.model.CulturalEvent
import com.upmgeoinfo.culturamad.services.json_parse.utils.ScheduleParser
import com.upmgeoinfo.culturamad.ui.composables.prefab.CategoryTag
import com.upmgeoinfo.culturamad.ui.composables.prefab.DetailButton
import com.upmgeoinfo.culturamad.ui.composables.prefab.PriceTag
import com.upmgeoinfo.culturamad.ui.composables.prefab.RateTag
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import com.upmgeoinfo.culturamad.viewmodels.MainViewModel
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailViewScreen(
    viewModel: MainViewModel? = null,
    //culturalEvent: CulturalEvent,
    onNavBack: () -> Unit
    //TODO: Here will be listed the navigation lambdas
){
    val culturalEvent = viewModel?.getCurrentEvent() ?: mockEvent
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(//First Half
                modifier = Modifier
                    .fillMaxWidth()
                    //.height(200.dp)
                    .fillMaxHeight(0.25f)
            ) {
                Image(
                    painter =
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
                    Box(){
                        Row{
                            val context = LocalContext.current
                            DetailButton(
                                icon = Icons.Default.Share,
                                modifier = Modifier
                                    .padding(end = 5.dp)
                            ) {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TITLE, culturalEvent.title)
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        culturalEvent.link
                                    )
                                }
                                val title = context.resources.getString(R.string.event_share)
                                context.startActivity(Intent.createChooser(intent, title))
                            }
                            var favorite by remember { mutableStateOf(false) }
                            favorite = culturalEvent.favorite
                            DetailButton(
                                enabled = viewModel?.hasUser ?: false,
                                icon =
                                if (favorite) Icons.Default.Favorite
                                else Icons.Default.FavoriteBorder
                            ) {
                                favorite = !favorite
                                viewModel?.changeFavoriteState(
                                    culturalEvent = culturalEvent,
                                    favorite = favorite
                                )
                            }
                        }
                    }

                    /*IconButton(
                        onClick = { *//*TODO favorite action.*//* },
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
                    }*/
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
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ){
                                CategoryTag(
                                    category = culturalEvent.category,
                                    fontSize = 20.sp
                                )
                                RateTag(rate = culturalEvent.rate)
                            }
                        }
                    }
                }
            }
            Box(//Second Half
                modifier = Modifier
                    .fillMaxSize()
            ){
                Column(
                    modifier = Modifier
                        .padding(
                            //start = 18.dp,
                            //end = 18.dp,
                            top = 16.dp,
                            bottom = 8.dp
                        )
                        .fillMaxSize()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .padding(
                                start = 18.dp,
                                end = 18.dp
                            )
                            .fillMaxWidth()
                    ) {
                        Column(){
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
                            .padding(
                                top = 16.dp,
                                start = 18.dp,
                                end = 18.dp
                            )
                            .fillMaxWidth()
                    ) {
                        Column() {
                            val context = LocalContext.current
                            DetailButton(
                                icon = Icons.Default.DateRange,
                                onClick = {
                                    val startYear = culturalEvent.dateStart.subSequence(0..3).toString()
                                    val startMonth = culturalEvent.dateStart.subSequence(5..6).toString()
                                    val startDay = culturalEvent.dateStart.subSequence(8..9).toString()
                                    val hour: String
                                    val minutes: String
                                    if(culturalEvent.hours != ""){
                                        hour = culturalEvent.hours.subSequence(0..1).toString()
                                        minutes = culturalEvent.hours.subSequence(3..4).toString()
                                    }else{
                                        hour = "00"
                                        minutes = "00"
                                    }

                                    val calendar = Calendar.getInstance()
                                    calendar.set(Calendar.YEAR, startYear.toInt())
                                    calendar.set(Calendar.MONTH, startMonth.toInt())
                                    calendar.set(Calendar.DAY_OF_MONTH, startDay.toInt())
                                    if(culturalEvent.hours != ""){
                                        calendar.set(Calendar.HOUR_OF_DAY, hour.toInt())
                                        calendar.set(Calendar.MINUTE, minutes.toInt())
                                    }else{
                                        calendar.set(Calendar.HOUR_OF_DAY, "00".toInt())
                                        calendar.set(Calendar.MINUTE, "00".toInt())
                                    }

                                    val intent = Intent(Intent.ACTION_INSERT)
                                    intent.data = CalendarContract.Events.CONTENT_URI
                                    intent.putExtra(CalendarContract.Events.TITLE, culturalEvent.title)
                                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, culturalEvent.address)
                                    if(culturalEvent.hours != ""){
                                        intent.putExtra(
                                            CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                            calendar.timeInMillis
                                        )
                                        intent.putExtra(
                                            CalendarContract.EXTRA_EVENT_END_TIME,
                                            calendar.timeInMillis + 60 * 60 * 1000
                                        )
                                    }else {
                                        intent.putExtra(CalendarContract.Events.ALL_DAY, true)
                                    }

                                    context.startActivity(intent)
                                }
                            )
                            /*Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .padding(top = 2.dp, end = 4.dp)
                                    .clickable { *//*TODO intent to schedule*//* }
                            )*/
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
                            Text(
                                text = scheduleBlock.showParsedSchedule(),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    Row(//Details and review
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .fillMaxWidth()
                    ){
                        DetailsReviewTabRow(culturalEvent = culturalEvent)
                    }
                    Row(//final row
                        modifier = Modifier
                            //.padding(top = 24.dp)
                            .fillMaxWidth()
                    ){

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
        DetailViewScreen(){}
    }
}

private var mockEvent = CulturalEvent(
    id = 1111111,
    category = "Lugares de Interés",
    title = "Km 0 de Red de Carreteras",
    description = "This is a faux cultural event fort testing and developing purposes.\n" +
            "At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat.",
    latitude = "40.4169473",
    longitude = "-3.7035285",
    address = "Puerta del Sol, 7",
    district = "Centro",
    neighborhood = "Centro",
    days = "MO,TU,WE,TH,FR,SA,SU",
    frequency = "Daily",
    interval = 1,
    dateStart = "2023-01-01 00:00:00.0",
    dateEnd = "2023-12-31 00:00:00.0",
    hours = "",
    excludedDays = "2/5/2023",
    place = "Plaza del Sol",
    host = "Ayuntamiento de Madrid",
    price = "",
    link = "https://www.miradormadrid.com/placa-del-kilometro-cero/",
    favorite = false,
    rate = 0.0f,
    review = ""
)

@Composable
fun DetailsReviewTabRow(
    culturalEvent: CulturalEvent
){
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf(
        stringResource(id = R.string.ui_description),
        stringResource(id = R.string.ui_review)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TabRow(
            selectedTabIndex = tabIndex,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            for (index in tabs.indices){
                Tab(
                    text = { androidx.compose.material3.Text(
                        text = tabs[index],
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    ) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        when(tabIndex){
            0 -> DescriptionView(culturalEvent = culturalEvent)
            1 -> ReviewDetailTab(culturalEvent = culturalEvent)
        }
    }
}

@Composable
fun DescriptionView(culturalEvent: CulturalEvent){
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
        ){
            Row(
                modifier = Modifier
                    .padding(start = 18.dp, end = 18.dp, top = 16.dp)
            ) {
                Text(
                    text = culturalEvent.description,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .height(80.dp)
                        .verticalScroll(
                            state = rememberScrollState(),
                            enabled = true,
                            reverseScrolling = false
                        )
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
            ) {
                Minimap(
                    location = LatLng(
                        culturalEvent.latitude.toDouble(),
                        culturalEvent.longitude.toDouble()
                    ),
                    title = culturalEvent.title,
                    address = culturalEvent.address
                )
            }
        }
    }
}

@Composable
fun Minimap(
    location: LatLng,
    title: String,
    address: String,
){
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
    ){
        Column{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                val context = LocalContext.current
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(location, 17f)
                }
                val properties =
                    MapProperties(
                        mapType = MapType.NORMAL,
                        isMyLocationEnabled = false,
                        mapStyleOptions =
                        if (!isSystemInDarkTheme())
                            MapStyleOptions.loadRawResourceStyle(
                                context,
                                R.raw.map_style_silver
                            )
                        else
                            MapStyleOptions.loadRawResourceStyle(
                                context,
                                R.raw.map_style_dark
                            )
                    )
                val uiSettings =
                    MapUiSettings(
                        zoomControlsEnabled = false,
                        myLocationButtonEnabled = false,
                        compassEnabled = false,
                        mapToolbarEnabled = false
                    )

                GoogleMap(
                    onMapClick = { /*TODO: navigation to Cluster map view with new camera position*/ },
                    cameraPositionState = cameraPositionState,
                    uiSettings = uiSettings,
                    properties = properties
                ) {
                    Marker(
                        state = MarkerState(position = location),
                        title = title
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 12.dp, start = 18.dp, end = 18.dp, bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = address,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                DetailButton(
                    icon = Icons.Default.Person
                ) {

                }
            }
        }
    }
}

@Composable
fun ReviewDetailTab(
    culturalEvent: CulturalEvent
){
    Surface(
        modifier = Modifier
            .padding(top = 12.dp, start = 18.dp, end = 18.dp, bottom = 8.dp)
    ) {
        Text(
            text = "In this tab will be allocated all reviews stored about this cultural event",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
