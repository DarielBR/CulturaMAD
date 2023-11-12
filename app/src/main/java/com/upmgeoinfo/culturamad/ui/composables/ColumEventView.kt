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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import com.upmgeoinfo.culturamad.ui.composables.prefab.NavBackButton
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import com.upmgeoinfo.culturamad.viewmodels.MainViewModel
import com.upmgeoinfo.culturamad.viewmodels.main.model.CulturalEvent
import com.upmgeoinfo.culturamad.viewmodels.main.model.LAZY_COLUMN_TYPE

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ColumnEventView(
    viewModel: MainViewModel? = null,
    navController: NavHostController? = null
){
    var itemList by remember { mutableStateOf(emptyList<CulturalEvent>()) }
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Spacer(modifier = Modifier.height(45.dp))
            Row {
                NavBackButton(
                    color = MaterialTheme.colorScheme.primary,
                    onClick = { navController?.popBackStack() }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(
                        id =
                        when(viewModel?.state?.currentLazyColumType){
                            LAZY_COLUMN_TYPE.NEAR_BY -> R.string.ui_near_by
                            LAZY_COLUMN_TYPE.RATE -> R.string.ui_most_rated
                            LAZY_COLUMN_TYPE.ALL_EVENTS -> R.string.ui_all_events
                            else -> R.string.ui_favorite_events
                        }
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(
                        bottom = 100.dp,
                        start = 4.dp,
                        end = 4.dp
                    )
                ){
                    itemList = when(viewModel?.state?.currentLazyColumType){
                        LAZY_COLUMN_TYPE.ALL_EVENTS -> viewModel.getEventsAndAds()
                        LAZY_COLUMN_TYPE.RATE -> viewModel.getMostRatedEvents(30)
                        LAZY_COLUMN_TYPE.NEAR_BY -> viewModel.getNearByEvents(3)
                        else -> viewModel?.getFavoriteEvents() ?: emptyList()
                    }

                    items(itemList){item ->
                        if (item.id == 0){
                            AdGridCard {}
                        }else{
                            EventGridCard(
                                viewModel = viewModel,
                                culturalEvent = item,
                                width = 400,
                                onClick = {
                                    viewModel?.setCurrentItem(item.id.toString())
                                    navController?.navigate(AppScreens.DetailViewScreen.route)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ColumnEventViewPreview(){
    CulturaMADTheme {
        ColumnEventView()
    }
}