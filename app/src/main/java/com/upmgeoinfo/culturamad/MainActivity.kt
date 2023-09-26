package com.upmgeoinfo.culturamad

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.upmgeoinfo.culturamad.datamodel.CulturalEvent
import com.upmgeoinfo.culturamad.datamodel.CulturalEventMadrid
import com.upmgeoinfo.culturamad.datamodel.MainViewModel
import com.upmgeoinfo.culturamad.datamodel.MarkerData
import com.upmgeoinfo.culturamad.datamodel.database.CulturalEventDatabase
import com.upmgeoinfo.culturamad.datamodel.database.CulturalEventRepository
import com.upmgeoinfo.culturamad.navigation.AlternateNavigation
import com.upmgeoinfo.culturamad.navigation.AppNavigation
import com.upmgeoinfo.culturamad.ui.composables.ClusterMapScreen
import com.upmgeoinfo.culturamad.ui.composables.FilterItem
import com.upmgeoinfo.culturamad.ui.composables.MapScreen
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import java.sql.Date
import java.sql.Time

class MainActivity : ComponentActivity() {
    private lateinit var fuseLocationClient: FusedLocationProviderClient
    private lateinit var culturalEvents: List<CulturalEventMadrid>
    //private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Initializing late-init variables
         */
        fuseLocationClient = LocationServices.getFusedLocationProviderClient(this)
        culturalEvents = MarkerData.dataList
        /**
         * Displaying content Edge to Edge
         */
        WindowCompat.setDecorFitsSystemWindows(window, false)
        /**
         * [IN DEVELOPMENT]
         * Database values
         */
        val database = Room.databaseBuilder(this, CulturalEventDatabase::class.java, "culturalEvents_db").build()
        val dao = database.dao
        val culturalEventRepository = CulturalEventRepository(dao)
        val viewModel= MainViewModel(culturalEventRepository)
        /*TODO:Validate internet access failure here, if an exception is thrown a message must be shown and continue with the state list.*/
        val dataFromUri = MarkerData.transformedDataList
        /**
         * Updating Database (UPSERT operation)
         */
        /*TODO: Run Test in the following code-block*/
        if(viewModel.state.items.isEmpty()){
            for(item in dataFromUri) viewModel.saveCulturalEvent(item)
        }
        else{
            for(item in dataFromUri){
                val databaseItem = viewModel.state.items.find { it.id == item.id }
                if(databaseItem != null) viewModel.updateCulturalEvent(item, databaseItem.bookmark, databaseItem.review)
                else viewModel.saveCulturalEvent(item)
            }
            for(databaseItem in viewModel.state.items){
                val item = dataFromUri.find { it.id == databaseItem.id }
                if(item == null) viewModel.deleteCulturalEvent(databaseItem)
            }
        }
        viewModel.refreshItems()
        /**
         * Firebase analytics
         * this is for testing purposes only, may be disposed in the future.
         */
        val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Firebase integration successful")
        firebaseAnalytics.logEvent("culturaMAD_Start", bundle)
        /**
         * Composable content
         */
        setContent {
            val navController = rememberNavController()
            CulturaMADTheme {
                //AppNavigation(fuseLocationClient, viewModel)
                AlternateNavigation(
                    navController = navController,
                    fusedLocationClient = fuseLocationClient,
                    viewModel = viewModel
                )
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
            Manifest.permission.ACCESS_COARSE_LOCATION,
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
        permissionStates.permissions.forEach {
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
@ExperimentalMaterial3Api
@OptIn(ExperimentalComposeUiApi::class)//Necessary for using [LocalSoftwareKeyboardController.current]. Necessary for using Text Field.
@MapsComposeExperimentalApi
@Composable
fun UIDeclaration(
    fuseLocationClient: FusedLocationProviderClient
){
    CulturaMADTheme {
        var searchValue by remember { mutableStateOf("") }
        var danceFilter by remember { mutableStateOf(false) }
        var musicFilter by remember { mutableStateOf(false) }
        var paintingFilter by remember { mutableStateOf(false) }
        var theatreFilter by remember { mutableStateOf(false) }

        val keyboardController = LocalSoftwareKeyboardController.current //necessary to close keyboard after a search is prompted.

        /**
         * Customizing the System Bar
         */
        var darkTheme by remember { mutableStateOf(false) }
        darkTheme = isSystemInDarkTheme()
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = !darkTheme
               // isNavigationBarContrastEnforced = true
            )
            systemUiController.setNavigationBarColor(
                color = Color.Transparent,
                darkIcons = !darkTheme
            )
        }
        /**
         * Composable with GoogleMap
         */
        MapScreen(fuseLocationClient, searchValue, danceFilter, musicFilter, paintingFilter, theatreFilter)
        //MapScreenWithCluster()
        /*ClusterMapScreen(
            fuseLocationClient = fuseLocationClient,
            searchValue = searchValue,
            categoryDance = danceFilter,
            categoryMusic = musicFilter,
            categoryPainting = paintingFilter,
            categoryTheatre = theatreFilter
        )*/

        Column {

            Spacer(modifier = Modifier.height(45.dp))

            /**
             * Declaring a SearchBar on top of the screen. A Composable function won't be used in
             * this case because is necessary to modify a variable external to the function's scope.
             * In particular the searchValue, will be used modified in the following declaration and
             * it will be used in other task furthermore.
             */
            Surface(
                elevation = 2.dp,
                shape = MaterialTheme.shapes.medium.copy(all = CornerSize(40)),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                TextField(
                    value = searchValue,
                    onValueChange = { newText: String ->
                        searchValue = newText
                    },
                    shape = MaterialTheme.shapes.medium.copy(all = CornerSize(40)),
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        ) },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                searchValue = ""
                                keyboardController?.hide()
                            }
                        ) {
                            Icon(
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                painter = painterResource(id = R.drawable.cmad_close),
                                contentDescription = null
                            )
                        }

                    },
                    placeholder = { Text(stringResource(id = R.string.placeholder_search)) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
                    modifier = Modifier
                        //.padding(start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            /**
             * Declaring category filtering buttons
             */
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ){
                items(categoriesData){ item->
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

/**
 * Will be called from the onCreate function. Works with NavController
 */
@ExperimentalMaterial3Api
@OptIn(ExperimentalPermissionsApi::class)
@MapsComposeExperimentalApi
@Composable
fun MainScreen(fuseLocationClient: FusedLocationProviderClient, viewModel: MainViewModel){
    //CulturaMADTheme {
        RequestInternetPermission()
        RequestLocationPermission()
        //UIDeclaration(fuseLocationClient)
        ClusterMapScreen(
            fuseLocationClient = fuseLocationClient,
            viewModel = viewModel
        )
    //}
}