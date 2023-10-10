package com.upmgeoinfo.culturamad.ui.composables.prefab

import android.content.res.Configuration
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

@Composable
fun UserTextField(
    value: String = "",
    isError: Boolean? = null,
    onValueChange: () -> Unit
) {

}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun UserTextFieldPreview(){
    CulturaMADTheme {
        UserTextField(){}
    }
}