package com.upmgeoinfo.culturamad.ui.composables.prefab

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import kotlin.random.Random

@Composable
fun ReviewCard(
    userID: String = "usuario.de@prueba.com",
    review: String = "Muy buena, me encantó, creo que vuelo a ir muy pronto. Hay mucho para ver y con uan sola visita no alcanza para verlo todo. Ir en familia es muy recomendable",
    rate: Float = 9.0f
){
    Card(
        shape = MaterialTheme.shapes.extraSmall,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
        ){
            Column {
                Row {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth(0.20f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = getRandomColor(),
                            modifier = Modifier
                                .padding(2.dp)
                                .size(48.dp)
                        )
                        //RateTag(rate = rate)
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = userID,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(4.dp)
                        )
                        Text(
                            text = review,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ReviewCardPreview(){
    CulturaMADTheme {
        Column(
            modifier = Modifier
            .padding(2.dp)
        ){
            ReviewCard()
        }
    }
}

private fun getRandomColor(): Color{
    val id = Random.nextInt(1,9)
    return listOfIconColors.colors.find { it.id == id }?.color ?: Color.White
}

data class UserIconColor(
    val color: Color,
    val id: Int
)

object listOfIconColors{
    val colors = listOf<UserIconColor>(
        UserIconColor(
            color = Color.Red.copy(alpha = 0.5f),
            id = 1
        ),
        UserIconColor(
            color = Color.Gray.copy(alpha = 0.5f),
            id = 2
        ),
        UserIconColor(
            color = Color.Yellow.copy(alpha = 0.5f),
            id = 3
        ),
        UserIconColor(
            color = Color.Green.copy(alpha = 0.5f),
            id = 4
        ),
        UserIconColor(
            color = Color.Blue.copy(alpha = 0.5f),
            id = 6
        ),
        UserIconColor(
            color = Color.Magenta.copy(alpha = 0.5f),
            id = 7
        ),
        UserIconColor(
            color = Color.Cyan.copy(alpha = 0.5f),
            id = 8
        )
    )
}