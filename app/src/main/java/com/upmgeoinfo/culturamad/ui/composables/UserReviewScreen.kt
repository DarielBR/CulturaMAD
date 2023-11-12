package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.ui.composables.prefab.NavBackButton
import com.upmgeoinfo.culturamad.ui.composables.prefab.RateStarTag
import com.upmgeoinfo.culturamad.ui.composables.prefab.RatingCard
import com.upmgeoinfo.culturamad.ui.composables.prefab.ReviewPromptCard
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import com.upmgeoinfo.culturamad.viewmodels.MainViewModel

@Composable
fun UserReviewScreen(
    viewModel: MainViewModel? = null,
    onNavBack: () -> Unit
){
    val culturalEvent = viewModel?.getCurrentEvent()
    var reviewValue by remember { mutableStateOf("") }
    var rateValue by remember { mutableDoubleStateOf(0.0) }
    if (viewModel?.hasUser == true){
        /*rateValue = viewModel.getUserEventReview(
            userID = viewModel.loginUiState.currentUserMail,
            eventID = culturalEvent?.id!!
        ).rate.toDouble()*/
        rateValue = viewModel.calculateAverageRate(culturalEvent?.id!!.toString())
    }

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
                    if (culturalEvent?.rate == 0.0){
                        RatingCard{ rate -> rateValue = rate }
                    }else {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp, bottom = 16.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.ui_your_rating_for_this_event),
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                )
                                RateStarTag(rate = rateValue)
                            }
                        }
                    }
                    ReviewPromptCard(
                        viewModel = viewModel,
                        culturalEvent = culturalEvent
                    ){review -> reviewValue = review }
                    Button(
                        onClick = {
                            viewModel?.setEventReview(
                                culturalEvent = culturalEvent!!,
                                review = reviewValue
                            )
                            viewModel?.setEventRate(
                                culturalEvent = culturalEvent!!,
                                rate = rateValue
                            )
                            viewModel?.updateEventsReviewState()
                            onNavBack.invoke()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                            .height(48.dp)
                    ) {
                        Text(text = stringResource(R.string.ui_publish_review))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun UserReviewScreenPreview(){
    CulturaMADTheme {
        UserReviewScreen{}
    }
}