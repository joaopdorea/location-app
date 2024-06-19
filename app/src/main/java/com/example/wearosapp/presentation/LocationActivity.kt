package com.example.wearosapp.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.DataMap.TAG
import com.example.wearosapp.presentation.MyDatabaseHelper
import java.text.DateFormat
import java.util.Calendar


class LocationActivity : ComponentActivity() {

    private lateinit var fusedLocationProviderClient:FusedLocationProviderClient
    private lateinit var locationRequest:LocationRequest
    private var currentLatitude:String = ""
    private var currentLongitude:String = ""
    private var currentDistance:String = ""






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            Scaffold(
                timeText = {
                    TimeText(
                        timeTextStyle = TimeTextDefaults.timeTextStyle(
                            fontSize = 12.sp
                        )
                    )
                },
                vignette = {
                    Vignette(vignettePosition = VignettePosition.TopAndBottom)
                }
            ) {
                InfoComposable()
            }
        }



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        locationRequest = LocationRequest.Builder(PRIORITY_HIGH_ACCURACY, 100)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(2000)
            .setMaxUpdateDelayMillis(100)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult == null) {
                    return
                }
            }
        }




        LocationServices.getFusedLocationProviderClient(applicationContext).requestLocationUpdates(locationRequest, locationCallback, null)







        if (!hasGps()) {
            Log.d(TAG, "This hardware doesn't have GPS.")
            // Fall back to functionality that doesn't use location or
            // warn the user that location function isn't available.
        }else{


                Log.d(TAG, "Has GPS")

                val mainHandler = Handler(Looper.getMainLooper())

                mainHandler.post(object : Runnable {
                    override fun run() {
                        getLastLocation()
                        mainHandler.postDelayed(this, 60000)
                    }
                })

        }




    }

    private fun hasGps(): Boolean =
        packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)

    private fun getLastLocation() {


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            var locationTask: Task<Location> = fusedLocationProviderClient.lastLocation

            locationTask.addOnSuccessListener{
                if(it != null){


                    val calendar = Calendar.getInstance().time
                    val dateFormat = DateFormat.getDateInstance().format(calendar)
                    val timeFormat = DateFormat.getTimeInstance().format(calendar)

                    val currentDate = "$dateFormat $timeFormat"


                    currentLatitude = it.latitude.toString()
                    currentLongitude = it.longitude.toString()
                    currentDistance = (calculateDistance(it.latitude, -22.90, it.longitude, -43.17)/1000).toString()

                    Log.d(TAG, "Latitude is ${it.latitude}")
                    Log.d(TAG, "Longitude is ${it.longitude}")
                    Log.d(TAG, (calculateDistance(it.latitude, -22.90, it.longitude, -43.17)/1000).toString() + " km")

                    val myDB = MyDatabaseHelper(this)
                    myDB.addEvent(currentDate, currentLatitude, currentLongitude, currentDistance)
                }
                else{
                    Log.d(TAG, "Null location")
                }

            }

            locationTask.addOnFailureListener{
                    Log.d(TAG, "Location could not be traced...")
            }







        }

    }




    private fun calculateDistance(lat1:Double, lat2:Double, long1:Double, long2:Double): Float{

        val startLocation = Location("start").apply {
            latitude = lat1
            longitude = long1
        }

        val endLocation = Location("end").apply {
            latitude = lat2
            longitude = long2
        }

        return startLocation.distanceTo(endLocation)

    }


}


@Composable
fun InfoComposable() {
    var sliderPosition by remember {
        mutableStateOf(120f)
    }
    Column(
        modifier = Modifier
            .padding(top = 40.dp, start = 30.dp)
            .width(140.dp)
    ) {
        Text(text = "Your location was sent!", fontSize = 12.sp)
    }
}





