package com.upmgeoinfo.culturamad.ui.composables.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable()
fun PermissionRequest(
    onResult: (Boolean) -> Unit
){
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ){isGranted ->
            if (isGranted){
                onResult.invoke(true)
            }else{
                onResult.invoke(false)
            }
        }
    val context = LocalContext.current

    Button(
        onClick = {
            when (PackageManager.PERMISSION_GRANTED){
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    //call functions that require permission
                }
                else -> {
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    ) {
        Text(text = "Check and request permission.")
    }
}