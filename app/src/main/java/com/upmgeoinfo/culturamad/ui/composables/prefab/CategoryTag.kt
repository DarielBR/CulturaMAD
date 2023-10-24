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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

@Composable
fun CategoryTag(
    category: String = "",
    color: Color = Color.White,
    fontSize: TextUnit = 12.sp
){
    Surface(
        shape = MaterialTheme.shapes.extraSmall,
        color = changeColorLuminosity(color = color, factor = 0.9f)
    ) {
        Text(
            text = if (category.contains("DanzaBaile"))
                stringResource(id = R.string.category_dance)
            else if (category.contains("Musica"))
                stringResource(id = R.string.category_music)
            else if (category.contains("Exposiciones"))
                stringResource(id = R.string.category_painting)
            else if (category.contains("TeatroPerformance"))
                stringResource(id = R.string.category_theatre)
            else category,
            color = changeColorLuminosity(color = color, factor = 0.5f),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            fontSize = fontSize,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun CatedoryTagPreview(){
    CulturaMADTheme {
        CategoryTag("MOCK")
    }
}

private fun changeColorLuminosity(color: Color, factor: Float): Color{
    val red = (color.red * factor).coerceIn(0f, 1f)
    val green = (color.green * factor).coerceIn(0f, 1f)
    val blue = (color.blue * factor).coerceIn(0f, 1f)
    return Color(red, green, blue, alpha = color.alpha)
}