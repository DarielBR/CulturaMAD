@file:OptIn(ExperimentalMaterialApi::class)

package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

@Composable
fun FilterItem(
    filterStatus: Boolean,
    drawableResource: Int,
    stringResource: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    androidx.compose.material.Card(
        onClick = onClick,
        backgroundColor =   if(!filterStatus) MaterialTheme.colorScheme.surface
        else MaterialTheme.colorScheme.onSurface,
        shape = MaterialTheme.shapes.large,
        elevation = 2.dp,
        modifier = Modifier
            .padding(start = 2.dp, end = 2.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
            ){
                Image(
                    painter = painterResource(id = drawableResource),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                        if(!filterStatus) setToSaturation(2f)
                        else setToSaturation(0f)
                    }),
                    modifier = Modifier
                        .padding(4.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                )
                if(filterStatus){
                    Icon(
                        Icons.Filled.Check,
                        null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Text(
                text = stringResource(id = stringResource),
                color = if (!filterStatus) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.surface,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(end = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FilterItemPreview(){
    var danceFilter by remember { mutableStateOf(false) }
    CulturaMADTheme {
        FilterItem(
            filterStatus = danceFilter,
            drawableResource = R.drawable.dance_image,
            stringResource = R.string.category_dance,
            onClick = { danceFilter = !danceFilter }
        )
    }
}


@Composable
fun FilterItems(
    modifier: Modifier = Modifier
){
    var danceFilter by remember{ mutableStateOf(false) }
    var musicFilter by remember{ mutableStateOf(false) }
    var paintingFilter by remember{ mutableStateOf(false) }
    var theatreFilter by remember{ mutableStateOf(false) }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ){
        items(categoriesData){item->
            when(stringResource(id = item.text)) {
                "Danza" -> {
                    FilterItem(
                        filterStatus = danceFilter,
                        drawableResource = item.drawable,
                        stringResource = item.text,
                        onClick = { danceFilter = !danceFilter }
                    )
                }
                "Música" -> {
                    FilterItem(
                        filterStatus = musicFilter,
                        drawableResource = item.drawable,
                        stringResource = item.text,
                        onClick = { musicFilter = !musicFilter }
                    )
                }
                "Pintura" -> {
                    FilterItem(
                        filterStatus = paintingFilter,
                        drawableResource = item.drawable,
                        stringResource = item.text,
                        onClick = { paintingFilter = !paintingFilter }
                    )
                }
                "Teatro" -> {
                    FilterItem(
                        filterStatus = theatreFilter,
                        drawableResource = item.drawable,
                        stringResource = item.text,
                        onClick = { theatreFilter = !theatreFilter }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FilterItemsPreview(){
    CulturaMADTheme {
        FilterItems()
    }
}

private data class DrawableStringPair(
    @DrawableRes val drawable: Int,
    @StringRes val text: Int
)

private val categoriesData = listOf(
    R.drawable.dance_image to R.string.category_dance,
    R.drawable.painting_image to R.string.category_painting,
    R.drawable.music_image to R.string.category_music,
    R.drawable.teatro_image to R.string.category_theatre
).map { DrawableStringPair(it.first, it.second) }