package com.upmgeoinfo.culturamad

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.upmgeoinfo.culturamad.navigation.AppNavigation
import com.upmgeoinfo.culturamad.ui.composables.MapScreen
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CulturaMADTheme {
                AppNavigation()
            }
        }
    }
}
/**
 * Handles proper internet permission requests for a UI built with Compose.
 * Compose do not have same States as XML layouts apps, therefore is necessary to call
 * [DisposableEffect] for proper placing in the onCreate state and cleansing after leaving this state.
 */
@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestInternetPermission() {
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.INTERNET)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    permissionState.launchPermissionRequest()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })

    when {
        permissionState.status.isGranted -> {
            Text(text = "Reading external permission is granted")
        }
        permissionState.status.shouldShowRationale -> {
            Column {
                Text(text = "Reading external permission is required by this app")
            }
        }
        !permissionState.status.isGranted && !permissionState.status.shouldShowRationale -> {
            Text(text = "Permission fully denied. Go to settings to enable")
        }
    }
}

/**
 * Handles proper location permission requests for a UI built with Compose.
 * Compose do not have same States as XML layouts apps, therefore is necessary to call
 * [DisposableEffect] for proper placing in the onCreate state and cleansing after leaving this state.
 */
@ExperimentalPermissionsApi
@Composable
fun RequestLocationPermission() {
    val permissionStates = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    permissionStates.launchMultiplePermissionRequest()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    )
    {
        permissionStates.permissions.forEach { it ->
            when (it.permission) {
                Manifest.permission.READ_EXTERNAL_STORAGE -> {
                    when {
                        it.status.isGranted -> {
                            /* Permission has been granted by the user.
                               You can use this permission to now acquire the location of the device.
                               You can perform some other tasks here.
                            */
                            Text(text = "Read Ext Storage permission has been granted")
                        }
                        it.status.shouldShowRationale -> {
                            /*Happens if a user denies the permission two times

                             */
                            Text(text = "Read Ext Storage permission is needed")
                        }
                        !it.status.isGranted && !it.status.shouldShowRationale -> {
                            /* If the permission is denied and the should not show rationale
                                You can only allow the permission manually through app settings
                             */
                            Text(text = "Navigate to settings and enable the Storage permission")

                        }
                    }
                }
                Manifest.permission.ACCESS_FINE_LOCATION -> {
                    when {
                        it.status.isGranted -> {
                            /* Permission has been granted by the user.
                               You can use this permission to now acquire the location of the device.
                               You can perform some other tasks here.
                            */
                            Text(text = "Location permission has been granted")
                        }
                        it.status.shouldShowRationale -> {
                            /*Happens if a user denies the permission two times

                             */
                            Text(text = "Location permission is needed")

                        }
                        !it.status.isGranted && !it.status.shouldShowRationale -> {
                            /* If the permission is denied and the should not show rationale
                                You can only allow the permission manually through app settings
                             */
                            Text(text = "Navigate to settings and enable the Location permission")

                        }
                    }
                }
            }
        }
    }
}

/**
 * Main UI declarative function.
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)//Necessary for using [LocalSoftwareKeyboardController.current]. Necessary for using Text Field.
@Composable
fun UIDeclaration(
    modifier: Modifier = Modifier
){
    CulturaMADTheme {
        var searchValue by remember { mutableStateOf("") }
        var categoryDance by remember { mutableStateOf(false) }
        var categoryMusic by remember { mutableStateOf(false) }
        var categoryPainting by remember { mutableStateOf(false) }
        var categoryTheatre by remember { mutableStateOf(false) }

        val keyboardController = LocalSoftwareKeyboardController.current //necessary to close keyboard after a search is prompted.
        
        MapScreen(searchValue, categoryDance, categoryMusic, categoryPainting, categoryTheatre)
        
        Column {

            Spacer(modifier = Modifier.height(8.dp))

            /**
             * Declaring a SearchBar on top of the screen. A Composable function won't be used in
             * this case because si necessary to modify a variable external to the function's scope.
             * In particular the searchValue, will be used modified in the following declaration and
             * it will be used in other task furthermore.
             */
            TextField(
                value = searchValue, 
                onValueChange = {newText: String ->
                    searchValue = newText
                },
                shape = MaterialTheme.shapes.medium.copy(all = CornerSize(50)),
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                placeholder = { Text(stringResource(id = R.string.placeholder_search))},
                colors = TextFieldDefaults.colors(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions( onSearch = { keyboardController?.hide() }),
                modifier = Modifier
                    .padding(start = 60.dp, end = 60.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(modifier = Modifier.height(8.dp))

            /**
             * Declaring category filtering buttons
             */

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp)
            ){
                androidx.compose.material.Card(
                    backgroundColor =   if(!categoryDance) MaterialTheme.colorScheme.surfaceVariant
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = MaterialTheme.shapes.large,
                    elevation = 5.dp,
                    modifier = Modifier
                        .padding(start = 2.dp, end = 2.dp)
                        .clickable {
                            categoryDance = !categoryDance
                            categoryMusic = false
                            categoryPainting = false
                            categoryTheatre = false
                        }
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
                                painter = painterResource(id = R.drawable.dance_image),
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
                                Icon(
                                    Icons.Filled.Check,
                                    null,
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                        androidx.compose.material.Text(
                            text = stringResource(R.string.category_dance),
                            color = if (!categoryDance) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.surfaceVariant,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .padding(end = 8.dp)
                        )
                    }
                }
                androidx.compose.material.Card(
                    backgroundColor =   if(!categoryMusic) MaterialTheme.colorScheme.surfaceVariant
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = MaterialTheme.shapes.large,
                    elevation = 5.dp,
                    modifier = Modifier
                        .padding(start = 2.dp, end = 2.dp)
                        .clickable {
                            categoryDance = false
                            categoryMusic = !categoryMusic
                            categoryPainting = false
                            categoryTheatre = false
                        }
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
                                painter = painterResource(id = R.drawable.music_image),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                    if(!categoryMusic) setToSaturation(2f)
                                    else setToSaturation(0f)
                                }),
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(30.dp)
                                    .clip(CircleShape)
                            )
                            if(categoryMusic){
                                androidx.compose.material.Icon(
                                    Icons.Filled.Check,
                                    null,
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                        androidx.compose.material.Text(
                            text = stringResource(R.string.category_music),
                            color = if (!categoryMusic) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.surfaceVariant,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }
                androidx.compose.material.Card(
                    backgroundColor =   if(!categoryPainting) MaterialTheme.colorScheme.surfaceVariant
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = MaterialTheme.shapes.large,
                    elevation = 5.dp,
                    modifier = Modifier
                        .padding(start = 2.dp, end = 2.dp)
                        .clickable {
                            categoryDance = false
                            categoryMusic = false
                            categoryPainting = !categoryPainting
                            categoryTheatre = false
                        }
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
                                    if(!categoryPainting) setToSaturation(2f)
                                    else setToSaturation(0f)
                                }),
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(30.dp)
                                    .clip(CircleShape)
                            )
                            if(categoryPainting){
                                androidx.compose.material.Icon(
                                    Icons.Filled.Check,
                                    null,
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                        androidx.compose.material.Text(
                            text = stringResource(R.string.category_painting),
                            color = if (!categoryPainting) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.surfaceVariant,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }
                androidx.compose.material.Card(
                    backgroundColor =   if(!categoryTheatre) MaterialTheme.colorScheme.surfaceVariant
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = MaterialTheme.shapes.large,
                    elevation = 5.dp,
                    modifier = Modifier
                        .padding(start = 2.dp, end = 2.dp)
                        .clickable {
                            categoryDance = false
                            categoryMusic = false
                            categoryPainting = false
                            categoryTheatre = !categoryTheatre
                        }
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
                                painter = painterResource(id = R.drawable.teatro_image),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                    if(!categoryTheatre) setToSaturation(2f)
                                    else setToSaturation(0f)
                                }),
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(30.dp)
                                    .clip(CircleShape)
                            )
                            if(categoryTheatre){
                                androidx.compose.material.Icon(
                                    Icons.Filled.Check,
                                    null,
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                        androidx.compose.material.Text(
                            text = stringResource(R.string.category_theatre),
                            color = if (!categoryTheatre) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.surfaceVariant,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Will be called from the onCreate function. Works with NavController
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(modifier: Modifier){
    CulturaMADTheme {
        RequestInternetPermission()
        RequestLocationPermission()
        UIDeclaration()
    }
}