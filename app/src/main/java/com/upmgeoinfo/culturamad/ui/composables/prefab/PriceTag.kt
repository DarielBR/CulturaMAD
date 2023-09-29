package com.upmgeoinfo.culturamad.ui.composables.prefab

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upmgeoinfo.culturamad.R.*
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

@Composable
fun PriceTag(
    price: String = "",
    //color: Color? = null,
    fontSize: TextUnit = 12.sp
){
    Surface(
        shape = MaterialTheme.shapes.extraSmall,
        color = if (price == "") changeColorLuminosity(Color.Green, 0.9f)
                else changeColorLuminosity(Color.White, 0.8f)
    ) {
        Text(
            text = if (price == "") stringResource(id = string.event_free) else "€ " +price,
            color = Color(0xFF018786),
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            modifier = Modifier
                .padding(2.dp)
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun PriceTagPreview(){
    CulturaMADTheme {
        PriceTag(price = "")
    }
}

private fun changeColorLuminosity(color: Color, factor: Float): Color{
    val red = (color.red * factor).coerceIn(0f, 1f)
    val green = (color.green * factor).coerceIn(0f, 1f)
    val blue = (color.blue * factor).coerceIn(0f, 1f)
    return Color(red, green, blue, alpha = color.alpha)
}