package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import com.google.android.gms.maps.model.Circle
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

@Composable
fun CategoryChip(modifier: Modifier){
    var categoryDance by remember { mutableStateOf(false) }

    androidx.compose.material.Card(
        backgroundColor =   if(!categoryDance) MaterialTheme.colorScheme.surfaceVariant
                            else MaterialTheme.colorScheme.onSurfaceVariant,
        shape = MaterialTheme.shapes.large,
        elevation = 5.dp,
        modifier = Modifier
            .clickable { categoryDance = !categoryDance }
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
                    painter = painterResource(id = R.drawable.painting_image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                        if(!categoryDance) setToSaturation(2f)
                        else setToSaturation(0f)
                    }),
                    modifier = Modifier
                        .padding(4.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                )
                if(categoryDance){
                    androidx.compose.material.Icon(
                        Icons.Filled.Check,
                        null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Text(
                text = stringResource(R.string.category_dance),
                color = if(!categoryDance) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.surfaceVariant,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CategoryChipPreview(modifier: Modifier = Modifier){
    CulturaMADTheme {
        CategoryChip(modifier = modifier)
    }
}