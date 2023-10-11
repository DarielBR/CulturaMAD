package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.datamodel.CulturalEvent
import com.upmgeoinfo.culturamad.viewmodels.main.MainViewModel
import com.upmgeoinfo.culturamad.ui.composables.prefab.GeneralSearchBar
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

@Composable
fun OverviewScreen(
    viewModel: MainViewModel? = null
){
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

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.ui_recommended),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )

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
                    ){
                        Text(
                            text = stringResource(id = R.string.ui_see_all),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                    }
                }
            }

            //Spacer(modifier = Modifier.size(8.dp))

            LazyRow(
                contentPadding = PaddingValues(
                    vertical = 4.dp,
                    horizontal = 4.dp
                ),
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                var itemsList = viewModel?.state?.items?.toList() ?: emptyList()
                if (viewModel?.state?.searchValue != ""){
                    val newItemList = emptyList<CulturalEvent>().toMutableList()
                    val searchValue = viewModel?.state?.searchValue
                    for(item in itemsList){
                        if (item.title.contains(searchValue!!, ignoreCase = true)){
                            newItemList.add(item)
                        }
                    }
                    itemsList = newItemList.toList()
                }
                items(itemsList){ item ->
                    EventGridCard(culturalEvent = item, onClick = {})
                }
            }
            Text(
                text = "This is the Overview",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 54.sp
            )
            Text(
                text = "Desc.: Here a grid with Events cards will be shown to the user, base in location (near by 1 Km), most visited. Advertisement. Filter tools.",
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 5,
                fontSize = 20.sp
            )
        }
    }
}

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
            //OverviewSearchBar2(viewModel = null)
        }
    }
}
