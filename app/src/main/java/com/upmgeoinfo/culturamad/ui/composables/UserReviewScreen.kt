package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upmgeoinfo.culturamad.ui.composables.prefab.NavBackButton
import com.upmgeoinfo.culturamad.ui.composables.prefab.RatingCard
import com.upmgeoinfo.culturamad.ui.composables.prefab.ReviewPromptCard
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import com.upmgeoinfo.culturamad.viewmodels.MainViewModel
import com.upmgeoinfo.culturamad.viewmodels.main.model.CulturalEvent

@Composable
fun UserReviewScreen(
    viewModel: MainViewModel? = null,
    culturalEvent: CulturalEvent? = null,
    onNavBack: () -> Unit
){
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Spacer(modifier = Modifier.height(45.dp))
            Row {
                NavBackButton(
                    color = MaterialTheme.colorScheme.primary,
                    onClick = onNavBack
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column{
                    RatingCard(
                        viewModel = viewModel,
                        culturalEvent = culturalEvent
                    )
                    ReviewPromptCard(
                        viewModel = viewModel,
                        culturalEvent = culturalEvent
                    ){}
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun UserReviewSrcreePreview(){
    CulturaMADTheme {
        UserReviewScreen{}
    }
}