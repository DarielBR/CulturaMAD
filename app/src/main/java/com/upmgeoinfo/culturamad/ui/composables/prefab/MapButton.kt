package com.upmgeoinfo.culturamad.ui.composables.prefab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun MapButton(
    onClick: () -> Unit,
    drawableResource: Int,
    rotation: Float
) {
    androidx.compose.material.Card(
        elevation = 1.dp,
        shape = CircleShape,
        backgroundColor = Color.Transparent,
        modifier = Modifier
            .padding(4.dp)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .clip(CircleShape)
                .size(45.dp)
                .background(color = MaterialTheme.colorScheme.surface)
            //.alpha(0.6f)
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.onSurface,
                painter = painterResource(id = drawableResource),
                contentDescription = null,
                modifier = Modifier
                    .rotate(rotation)
            )
        }

    }
}