package com.upmgeoinfo.culturamad.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

@Preview(showBackground = true)
@Composable
fun TestPreview(){
    CulturaMADTheme {
        Test()
    }
}

@Composable
fun Test() {
    var visible by remember { mutableStateOf(true) }
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(){
                Button(
                    elevation = ButtonDefaults.elevation(2.dp),
                    onClick = { visible = !visible }
                ) {
                    Text(text = if (visible) "Invisible" else "Visible")
                }
            }
            TestCard(visible = visible)
        }
    }
}

@Composable
fun TestCard(
    visible: Boolean
){
    AnimatedVisibility(
        visible = visible
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxWidth()
        ){
            Card(
                elevation = 2.dp,
                //backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .padding(/*top = 32.dp,*/ start = 8.dp, end = 8.dp, bottom = 8.dp)
                //.fillMaxSize()
            ) {
                Text(
                    text = "Texto de prueba",
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
        }
    }
}
