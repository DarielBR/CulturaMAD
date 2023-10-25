package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.upmgeoinfo.culturamad.ui.composables.prefab.DetailButtonType
import com.upmgeoinfo.culturamad.ui.composables.prefab.NavBackButton
import com.upmgeoinfo.culturamad.ui.composables.prefab.PriceTag
import com.upmgeoinfo.culturamad.ui.composables.prefab.RateTag
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import com.upmgeoinfo.culturamad.ui.utils.IntentLauncher
import com.upmgeoinfo.culturamad.viewmodels.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailViewScreen(
    viewModel: MainViewModel? = null,
    onNavBack: () -> Unit
){
    val culturalEvent = viewModel?.getCurrentEvent() ?: mockEvent
    val context = LocalContext.current
    val intentLauncher = IntentLauncher(culturalEvent, context)

    viewModel?.hideBottomNavBar(true)
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(//First Half
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.30f)
            ) {
                Box(modifier = Modifier.fillMaxSize()){
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
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 45.dp)
                    ) {
                        Column {
                            NavBackButton(
                                color = Color.White,
                                onClick = onNavBack
                            )
                        }
                        Column {
                            Row {
                                DetailButton(
                                    icon = Icons.Default.Share,
                                    culturalEvent = culturalEvent,
                                    context = context,
                                    type = DetailButtonType.SHARE,
                                    modifier = Modifier
                                        .padding(end = 5.dp)
                                ){}
                                //
                                var favorite by remember { mutableStateOf(false) }
                                favorite = viewModel?.state?.items?.find { it.id == culturalEvent.id }?.favorite ?: false
                                androidx.compose.material3.Surface(
                                    shape = MaterialTheme.shapes.small,
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shadowElevation = 1.dp,
                                    tonalElevation = 0.dp,
                                    modifier = Modifier
                                        .padding(2.dp)
                                ) {
                                    IconButton(
                                        enabled = viewModel?.hasUser ?: false,
                                        onClick = {
                                            favorite = !favorite
                                            viewModel?.changeFavoriteState(
                                                culturalEvent = culturalEvent,
                                                favorite = favorite
                                            )
                                        },
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = Color.Transparent
                                        )
                                    ) {
                                        androidx.compose.material3.Icon(
                                            imageVector =
                                                if (favorite) Icons.Default.Favorite
                                                else Icons.Default.FavoriteBorder,
                                            contentDescription = ""
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 4.dp, end = 4.dp)
                    ) {
                        Column{
                            Text(
                                text = culturalEvent.title,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 2,
                                modifier = Modifier
                                    .padding(bottom = 4.dp)
                            )
                            Text(
                                text = culturalEvent.place,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Light,
                                color = Color.White,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier
                                    .padding(bottom = 6.dp)
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 4.dp)
                            ){
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(0.75f)
                                ){
                                    CategoryTag(
                                        category = culturalEvent.category,
                                        fontSize = 20.sp
                                    )
                                }
                                Column{
                                    RateTag(
                                        rate = culturalEvent.averageRate
                                    )
                                }
                            }
                        }
                    }
                }
                /*
                Row(
                    //verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        //.align(alignment = Alignment.BottomStart)
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



                        }
                    }
                }*/
            }
            Row(//Second Half
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                        ) {
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
                    if (scheduleBlock.showParsedSchedule() != ""){
                        Row(
                            modifier = Modifier
                                .padding(
                                    top = 16.dp,
                                    start = 18.dp,
                                    end = 18.dp
                                )
                                .fillMaxWidth()
                        ) {
                            Column {
                                DetailButton(
                                    icon = Icons.Default.DateRange,
                                    type = DetailButtonType.SCHEDULE,
                                    culturalEvent = culturalEvent,
                                    context = context
                                ){}
                            }
                            Column(
                                modifier = Modifier
                                    .padding(start = 4.dp)
                            ) {
                                Text(
                                    text = scheduleBlock.showParsedSchedule(),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                start = 18.dp,
                                top = 8.dp,
                                end = 18.dp
                            )
                            .fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.cmad_link),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                                .padding(end = 4.dp)
                        )
                        Text(
                            text = culturalEvent.link,
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp,
                            overflow = TextOverflow.Clip,
                            maxLines = 1,
                            modifier = Modifier
                                .clickable { intentLauncher.browserUriIntent() }
                        )
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
        DetailViewScreen {}
    }
}

private var mockEvent = CulturalEvent(
    id = 1111111,
    category = "Lugares de Interés, Hostóricos, Culturales, Recreativos y de Ocio",
    title = "Km 0 de Red de Carreteras y Autovías Radiales de España y sus alrededores.",
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
    host = "Ayuntamiento de Madrid y toda la gente que va a la Plaza del Sol.",
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
    var tabIndex by remember { mutableIntStateOf(0) }
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
                Minimap(culturalEvent = culturalEvent)
            }
        }
    }
}

@Composable
fun Minimap(
    culturalEvent: CulturalEvent
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
                    .fillMaxHeight(0.7f)
            ) {
                var latitude = Double.MIN_VALUE
                var longitude = Double.MIN_VALUE
                if (culturalEvent.latitude.isNotBlank()) latitude = culturalEvent.latitude.toDouble()
                if (culturalEvent.longitude.isNotBlank()) longitude = culturalEvent.longitude.toDouble()
                val location = LatLng(latitude, longitude)
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
                val intentLauncher = IntentLauncher(
                    culturalEvent = culturalEvent,
                    context = context
                )
                GoogleMap(
                    onMapClick = {myLocation ->
                        //intentLauncher.directionsIntent(myLocation = myLocation)
                        intentLauncher.mapsIntent()
                    },
                    cameraPositionState = cameraPositionState,
                    uiSettings = uiSettings,
                    properties = properties
                ) {
                    Marker(
                        state = MarkerState(position = location),
                        title = culturalEvent.title
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 12.dp, start = 18.dp, end = 18.dp, bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = culturalEvent.address,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ReviewDetailTab(
    culturalEvent: CulturalEvent? = null
){
    Surface(
        modifier = Modifier
            .padding(top = 2.dp, bottom = 2.dp)
    ) {
        Column {

        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ReviewDetailTabPreview(){
    CulturaMADTheme {
        ReviewDetailTab()
    }
}
