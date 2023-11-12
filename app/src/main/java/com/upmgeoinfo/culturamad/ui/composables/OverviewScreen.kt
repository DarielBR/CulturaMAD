package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.navigation.AppScreens
import com.upmgeoinfo.culturamad.viewmodels.MainViewModel
import com.upmgeoinfo.culturamad.ui.composables.prefab.GeneralSearchBar
import com.upmgeoinfo.culturamad.ui.composables.prefab.NavForwardButton
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import com.upmgeoinfo.culturamad.viewmodels.main.model.CulturalEvent
import com.upmgeoinfo.culturamad.viewmodels.main.model.LAZY_COLUMN_TYPE

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OverviewScreen(
    viewModel: MainViewModel? = null,
    navController: NavHostController? = null
){
    viewModel?.hideBottomNavBar(false)
    LaunchedEffect(null) {
        viewModel?.updateStateReviews()//update with the last state saved in dbFi(all users interactions).
    }
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Spacer(modifier = Modifier.height(45.dp))

            GeneralSearchBar(
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.size(8.dp))

            val rowItems = listOf("NEARBY", "RATE", "ALL")
            LazyColumn(
                contentPadding = PaddingValues(bottom = 100.dp)
            ){
                items(rowItems){variant ->
                    OverviewRow(
                        viewModel = viewModel,
                        navController = navController,
                        variant = variant
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OverviewRow(
    viewModel: MainViewModel? = null,
    navController: NavHostController? = null,
    variant: String
){
    var nearBy by remember { mutableStateOf(emptyList<CulturalEvent>()) }
    var rate by remember { mutableStateOf(emptyList<CulturalEvent>()) }
    var allEvents by remember { mutableStateOf(emptyList<CulturalEvent>()) }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 2.dp, end = 2.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(
                id =
                    when(variant){
                        "NEARBY" -> R.string.ui_near_by
                        "RATE" -> R.string.ui_most_rated
                        else -> R.string.ui_all_events
                    }
            ),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
        NavForwardButton(text = stringResource(id = R.string.ui_see_all)) {
            viewModel?.changeLazyColumnType(
                newType = when(variant){
                    "NEARBY" -> LAZY_COLUMN_TYPE.NEAR_BY
                    "RATE" -> LAZY_COLUMN_TYPE.RATE
                    else -> LAZY_COLUMN_TYPE.ALL_EVENTS
                }
            )
            navController?.navigate(AppScreens.ColumnEventView.route)
        }
    }

    LazyRow(
        contentPadding = PaddingValues(
            horizontal = 4.dp
        ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
    ){
        nearBy = viewModel?.getNearByEvents(3) ?: emptyList()
        rate = viewModel?.getMostRatedEvents(30) ?: emptyList()
        allEvents = viewModel?.getEventsAndAds() ?: emptyList()
        items(
            when(variant){
                "NEARBY" -> nearBy
                "RATE" -> rate
                else -> allEvents
            }
        ){ item ->
            if (item.id == 0) {
                AdGridCard {}
            }else{
                EventGridCard(
                    viewModel = viewModel,
                    culturalEvent = item,
                    onClick = {
                        viewModel?.setCurrentItem(item.id.toString())
                        navController?.navigate(AppScreens.DetailViewScreen.route)
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun PreviewOverviewScree(){
    CulturaMADTheme {
        OverviewScreen()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun OverViewBarPreview(){
    CulturaMADTheme {
        Column{
            GeneralSearchBar(viewModel = null)
            GeneralSearchBar(viewModel = null)
        }
    }
}
