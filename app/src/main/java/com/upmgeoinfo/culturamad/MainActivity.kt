package com.upmgeoinfo.culturamad

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.upmgeoinfo.culturamad.datamodel.CulturalEvent
import com.upmgeoinfo.culturamad.datamodel.MainViewModel
import com.upmgeoinfo.culturamad.datamodel.MarkerData
import com.upmgeoinfo.culturamad.datamodel.database.CulturalEventDatabase
import com.upmgeoinfo.culturamad.datamodel.database.CulturalEventRepository
import com.upmgeoinfo.culturamad.services.json_parse.api_model.Event
import com.upmgeoinfo.culturamad.services.json_parse.`interface`.ApiService
import com.upmgeoinfo.culturamad.services.json_parse.`interface`.GraphApiService
import com.upmgeoinfo.culturamad.services.json_parse.reposiroty.ApiEventsRepository
import com.upmgeoinfo.culturamad.ui.composables.ClusterMapScreen
import com.upmgeoinfo.culturamad.ui.composables.ScaffoldedScreen
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    private lateinit var fuseLocationClient: FusedLocationProviderClient
    //private lateinit var culturalEvents: List<CulturalEventMadrid>

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Initializing late-init variables
         */
        fuseLocationClient = LocationServices.getFusedLocationProviderClient(this)
        //culturalEvents = MarkerData.dataList
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
        /**
         * Creating retrofit client for the ViewModel
         */
        val retrofit = Retrofit.Builder()
            .baseUrl("https://datos.madrid.es/egob/catalogo/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        /**
         * Creating an GraphApiService instance for the ApiEventsRepository
         */
        val apiService = retrofit.create(GraphApiService::class.java)
        val apiEventsRepository = ApiEventsRepository(apiService)
        /**
         * Creating the ViewModel
         */
        val viewModel= MainViewModel(
            culturalEventRepository,
            apiEventsRepository
        )
        /**
         * Getting the Event list from the API Uri
         */
        //fetchDataFromAPI(viewModel, this)

        /*TODO:Validate internet access failure here, if an exception is thrown a message must be shown and continue with the state list.*/
        var dataFromUri = emptyList<CulturalEvent>()
        dataFromUri = MarkerData.transformedDataList
       /* try {
            val dataFromUri = MarkerData.transformedDataList
        }catch (e: Exception){
            Toast.makeText(this, "Error found -> " + e.localizedMessage, Toast.LENGTH_LONG).show()
        }*/
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
                if(databaseItem != null) viewModel.updateCulturalEvent(item, databaseItem.bookmark, databaseItem.review!!)
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

                RequestInternetPermission()
                RequestLocationPermission()

                ScaffoldedScreen(
                    fusedLocationClient = fuseLocationClient,
                    viewModel = viewModel
                )
            }
        }
    }

    private fun fetchDataFromAPI(viewModel: MainViewModel, context: Context){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://datos.madrid.es/egob/catalogo/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val dataCall = apiService.getEventsData()
        dataCall.enqueue(
            object: Callback<List<Event>>{
                override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                    if (response.isSuccessful){
                        val events = response.body()
                        if (events != null){
                            //TODO: write it down to the state
                            Toast.makeText(
                                context,
                                "Fetching data from API operation successful.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }else{
                        Toast.makeText(
                            context,
                            "Unknown error while fetching data from API.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Fetch data from API operation failure.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
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
    CulturaMADTheme {
        RequestInternetPermission()
        RequestLocationPermission()

        ClusterMapScreen(
            fusedLocationClient = fuseLocationClient,
            viewModel = viewModel
        )
    }
}