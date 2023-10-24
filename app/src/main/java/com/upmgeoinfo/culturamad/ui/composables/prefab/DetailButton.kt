package com.upmgeoinfo.culturamad.ui.composables.prefab

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import com.upmgeoinfo.culturamad.ui.utils.IntentLauncher
import com.upmgeoinfo.culturamad.viewmodels.main.model.CulturalEvent

@Composable
fun DetailButton(
    icon: ImageVector? = null,
    enabled: Boolean = true,
    type: DetailButtonType = DetailButtonType.DEFAULT,
    context: Context? = null,
    culturalEvent: CulturalEvent = CulturalEvent(),
    modifier: Modifier? = Modifier,
    defaultOnClick: () -> Unit
){
    val intentLauncher = IntentLauncher(culturalEvent = culturalEvent, context = context)
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 1.dp,
        tonalElevation = 0.dp,
        modifier = Modifier
            .padding(2.dp)
    ) {
        IconButton(
            enabled = enabled,
            onClick = {
                when (type){
                    DetailButtonType.MAPS -> intentLauncher.mapsIntent()
                    DetailButtonType.SHARE -> intentLauncher.shareIntent()
                    DetailButtonType.SCHEDULE -> intentLauncher.scheduleIntent()
                    DetailButtonType.URL_LINK -> intentLauncher.browserUriIntent()
                    DetailButtonType.DEFAULT -> defaultOnClick
                    else -> defaultOnClick
                }
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent
            )
        ){
            Icon(
                imageVector = icon ?: Icons.Default.FavoriteBorder,
                contentDescription = ""
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DetailButtonPreview(){
    CulturaMADTheme {
        Row{
            DetailButton {} }
    }
}

enum class DetailButtonType{
    SHARE,
    DIRECTIONS,
    MAPS,
    SCHEDULE,
    URL_LINK,
    DEFAULT
}