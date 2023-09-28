package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upmgeoinfo.culturamad.datamodel.MainViewModel
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

@Composable
fun OverviewScreen(){
    Surface(
        color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = "This is the Overview",
                style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 54.sp
            )
            Text(
                text = "Desc.: Here a grid with Events cards will be shown to the user, base in location (near by 1 Km), most visited. Advertisement. Filter tools.",
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
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

@Composable
fun OverviewSearchBar(
    viewModel: MainViewModel? = null
){
    OutlinedTextField(value = , onValueChange = )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun OverViewBarPreview(){
    CulturaMADTheme {
        OverviewSearchBar(viewModel = null)
    }
}