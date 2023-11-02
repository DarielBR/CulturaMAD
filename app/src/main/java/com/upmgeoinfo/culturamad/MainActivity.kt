package com.upmgeoinfo.culturamad

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.room.Room
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.analytics.FirebaseAnalytics
import com.upmgeoinfo.culturamad.services.authentication.AuthenticationRepository
import com.upmgeoinfo.culturamad.services.firestoredb.FirestoredbRepository
import com.upmgeoinfo.culturamad.viewmodels.MainViewModel
import com.upmgeoinfo.culturamad.services.room.CulturalEventDatabase
import com.upmgeoinfo.culturamad.services.room.CulturalEventRepository
import com.upmgeoinfo.culturamad.services.json_parse.reposiroty.ApiEventsRepository
import com.upmgeoinfo.culturamad.ui.composables.ScaffoldedScreen
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Initializing late-init variables
         *
         * fuseLocationClient provides access to device location.
         */
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        /**
         * Displaying content Edge to Edge
         */
        WindowCompat.setDecorFitsSystemWindows(window, false)
        /**
         * Creating repository instances to build the application view model
         */
        val database = Room.databaseBuilder(this, CulturalEventDatabase::class.java, "culturalEvents_db").build()
        val dao = database.dao
        val culturalEventRepository = CulturalEventRepository(dao)
        val apiEventsRepository = ApiEventsRepository()
        val authenticationRepository = AuthenticationRepository()
        val firestoredbRepository = FirestoredbRepository()
        /**
         * Creating the ViewModel and the authenticationViewModel
         */
        val viewModel= MainViewModel(
            apiEventsRepository = apiEventsRepository,
            culturalEventRepository = culturalEventRepository,
            firestoredbRepository = firestoredbRepository,
            authenticationRepository = authenticationRepository,
            fusedLocationProviderClient = fusedLocationClient,
            context = this
        )
        //TODO: comment this line when AuthenticationViewModel has been merged with MainViewModel
        //val authenticationViewModel = AuthenticationViewModel(authenticationRepository)
        /**
         * Firebase analytics
         * this is for testing purposes only, may be disposed in the future.
         */
        val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Firebase integration successful")
        firebaseAnalytics.logEvent("culturaMAD_Start", bundle)
        /**
         * Initializing AdMob
         */
        MobileAds.initialize(this){}
        /*MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder().setTestDeviceIds(listOf("ABCDEF012345")).build()
        )*/
        /**
         * Composable content
         */
        setContent {
            //val navController = rememberNavController()
            CulturaMADTheme {
                /**
                 * Requesting user permissions
                 */
                RequestInternetPermission()
                RequestLocationPermission()
                /**
                 * Composable content
                 */
                ScaffoldedScreen(
                    fusedLocationClient = fusedLocationClient,
                    viewModel = viewModel,
                    //TODO: comment this line when AuthenticationViewModel has been merged with MainViewModel
                    //authenticationViewModel = authenticationViewModel
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

/*private data class DrawableStringPair(
    @DrawableRes val drawable: Int,
    @StringRes val text: Int
)

private val categoriesData = listOf(
    R.drawable.dance_image to R.string.category_dance,
    R.drawable.painting_image to R.string.category_painting,
    R.drawable.music_image to R.string.category_music,
    R.drawable.teatro_image to R.string.category_theatre
).map { DrawableStringPair(it.first, it.second) }*/

/**
 * Will be called from the onCreate function. Works with NavController
 */
/*
@ExperimentalMaterial3Api
@OptIn(ExperimentalPermissionsApi::class)
@MapsComposeExperimentalApi
@Composable
fun MainScreen(fuseLocationClient: FusedLocationProviderClient, viewModel: MainViewModel){
    CulturaMADTheme {
        RequestInternetPermission()
        RequestLocationPermission()

        ClusterMapScreen(
            fusedLocationClient = fuseLocationClient,
            viewModel = viewModel
        )
    }
}*/
