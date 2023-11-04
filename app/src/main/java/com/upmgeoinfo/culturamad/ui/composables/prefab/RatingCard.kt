package com.upmgeoinfo.culturamad.ui.composables.prefab

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import com.upmgeoinfo.culturamad.viewmodels.MainViewModel
import com.upmgeoinfo.culturamad.viewmodels.main.model.CulturalEvent

@Composable
fun RatingCard(
    culturalEvent: CulturalEvent? = null,
    viewModel: MainViewModel? = null
){
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                //verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 36.dp)
            ){
                Text(
                    text = stringResource(id = R.string.ui_how_was_your_experiance),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    //textAlign = TextAlign.Center
                )
            }
            Row {
                var filled1 by remember { mutableStateOf(false) }
                var filled2 by remember { mutableStateOf(false) }
                var filled3 by remember { mutableStateOf(false) }
                var filled4 by remember { mutableStateOf(false) }
                var filled5 by remember { mutableStateOf(false) }
                RatingStar(
                    filled = filled1
                ) {
                    filled1 = true
                    filled2 = false
                    filled3 = false
                    filled4 = false
                    filled5 = false
                    viewModel?.setEventRate(
                        culturalEvent = culturalEvent!!,
                        rate = 1.0f
                    )
                }
                RatingStar(
                    filled = filled2
                ) {
                    filled1 = true
                    filled2 = true
                    filled3 = false
                    filled4 = false
                    filled5 = false
                    viewModel?.setEventRate(
                        culturalEvent = culturalEvent!!,
                        rate = 2.0f
                    )
                }
                RatingStar(
                    filled = filled3
                ) {
                    filled1 = true
                    filled2 = true
                    filled3 = true
                    filled4 = false
                    filled5 = false
                    viewModel?.setEventRate(
                        culturalEvent = culturalEvent!!,
                        rate = 3.0f
                    )
                }
                RatingStar(
                    filled = filled4
                ) {
                    filled1 = true
                    filled2 = true
                    filled3 = true
                    filled4 = true
                    filled5 = false
                    viewModel?.setEventRate(
                        culturalEvent = culturalEvent!!,
                        rate = 4.0f
                    )
                }
                RatingStar(
                    filled = filled5
                ) {
                    filled1 = true
                    filled2 = true
                    filled3 = true
                    filled4 = true
                    filled5 = true
                    viewModel?.setEventRate(
                        culturalEvent = culturalEvent!!,
                        rate = 5.0f
                    )
                }
            }
        }
    }
}

@Composable
fun RatingStar(
    filled: Boolean = false,
    color: Color? = null,
    size: Int? = null,
    onClick: () -> Unit
){
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .padding(top = 16.dp, bottom = 36.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = color ?:
                if (filled) Color(0xFFF9ED69)
                else MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .size(size?.dp ?: 32.dp)
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RatingCardPreview(){
    CulturaMADTheme {
        Column(
            modifier = Modifier
                .padding(4.dp)
        ) {
            RatingCard()
        }
    }
}
