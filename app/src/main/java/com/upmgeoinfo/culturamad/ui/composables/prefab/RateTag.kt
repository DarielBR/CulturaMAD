package com.upmgeoinfo.culturamad.ui.composables.prefab

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

@Composable
fun RateTag(
    rate: Float? = 0.0f
){
    androidx.compose.material3.Surface(
        shape = MaterialTheme.shapes.extraSmall,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .padding(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .padding(1.dp)
        ) {
            Text(
                text = rate.toString(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Text(
                text = "/10",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun RateStarTag(
    rate: Float? = 1f,
    color: Color? = null
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(4.dp)
        ){
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = color ?: MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = " | ",
                color = color ?: MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(bottom = 3.dp)
            )
            Text(
                text = rate.toString(),
                color = color ?: MaterialTheme.colorScheme.onSurface,
                fontSize = 22.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RateTagReview(){
    CulturaMADTheme {
        Column(
            modifier = Modifier
                .padding(4.dp)
        ) {
            RateTag()
            RateStarTag()
        }
    }
}