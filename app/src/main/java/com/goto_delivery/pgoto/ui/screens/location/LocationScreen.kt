package com.goto_delivery.pgoto.ui.screens.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.location.Location
import android.location.LocationManager
import android.os.Build.VERSION.SDK_INT
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.goto_delivery.pgoto.R
import com.goto_delivery.pgoto.ui.theme.GotoTheme
import com.goto_delivery.pgoto.ui.utils.UiEvent
import com.goto_delivery.pgoto.ui.utils.WindowInfo
import com.goto_delivery.pgoto.ui.utils.rememberWindowInfo
import kotlinx.coroutines.flow.collect

@ExperimentalPermissionsApi
@ExperimentalMaterial3Api
@Composable
fun TurnOnLocationScreen(
    viewModel: LocationViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigate: (UiEvent.Navigate) -> Unit
) {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .componentRegistry {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder(context))
            } else {
                add(GifDecoder())
            }
        }
        .build()

    val windowInfo = rememberWindowInfo()

    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    LaunchedEffect(key1 = locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted)
            checkGpsStatus(context)
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }
                is UiEvent.AlertDialog -> {
                    if (!locationPermissionsState.allPermissionsGranted)
                        locationPermissionsState.launchMultiplePermissionRequest()
                    
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.address)) }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 15.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = if (isSystemInDarkTheme()) R.drawable.location_dark else R.drawable.location_light,
                            builder = {
                                crossfade(true)
                            },
                            imageLoader = imageLoader
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(
                            when (windowInfo.screenWidthInfo) {
                                is WindowInfo.WindowType.Compact -> 300.dp
                                is WindowInfo.WindowType.Medium -> 500.dp
                                else -> 600.dp
                            }
                        )
                    )
                }

                Button(
                    onClick = { viewModel.onEvent(LocationEvents.OnRequestPermission) }
                ) {
                    Text(text = stringResource(id = R.string.use_location))
                }

                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = stringResource(id = R.string.add_by_yourself))
                }
            }

            Column(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = buildAnnotatedString {

                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)) {
                            append(stringResource(id = R.string.we_recommend_that) + " ")
                        }
                        append(stringResource(id = R.string.the_location))

                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)) {
                            append(" " + stringResource(id = R.string.for_faster_results))
                        }
                    },
                    textAlign = TextAlign.Center
                )
            }

        }

    }
}

@SuppressLint("MissingPermission")
private fun checkGpsStatus(context: Context) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    if (gpsStatus) {
        Log.d("location", "gps on")
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.d("location", location.toString())
            }
    } else {
        Log.d("location", "gps off")
        gpsStatus(context = context)
    }
}

fun gpsStatus(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

    context.startActivity(intent)
}

@ExperimentalPermissionsApi
@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
private fun LocationPreviewLight() {
    GotoTheme {
        TurnOnLocationScreen(
            onNavigate = {}
        )
    }
}

@ExperimentalPermissionsApi
@ExperimentalMaterial3Api
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun LocationPreviewDark() {
    GotoTheme {
        TurnOnLocationScreen(
            onNavigate = {}
        )
    }
}