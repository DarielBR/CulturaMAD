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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import com.upmgeoinfo.culturamad.viewmodels.MainViewModel

@Composable
fun RatingCard(
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
                RatingStar {}
                RatingStar {}
                RatingStar {}
                RatingStar {}
                RatingStar {}
            }
        }
    }
}

@Composable
fun RatingStar(
    filled: Boolean = false,
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
            tint =  if(filled) Color(0xFFF9ED69)
                    else MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .size(32.dp)
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
