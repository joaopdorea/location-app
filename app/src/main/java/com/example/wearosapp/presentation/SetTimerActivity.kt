package com.example.wearosapp.presentation

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.Role.Companion.Switch


import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.wear.compose.material.*
import com.example.wearosapp.presentation.theme.TimerState
import java.sql.Time
import java.text.DateFormat
import java.util.Calendar
import kotlin.math.abs

class SetTimerActivity : ComponentActivity() {

    private lateinit var gestureDetector: GestureDetector

    var sliderPosition = 2f
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
                SliderComposable()
            }
            gestureDetector = GestureDetector(this, GestureListener())



        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }


    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPETHRESHOLD = 50
        private val SWIPEVELOCITYTHRESHOLD = 50

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            val diffX = e2.x - e1.x
            val diffY = e2.y - e1.y
            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > SWIPETHRESHOLD && abs(velocityX) > SWIPEVELOCITYTHRESHOLD) {
                    if (diffX < 0) {
                        onSwipeLeft()
                    }
                    return true
                }
            }
            return false
        }
    }

    private fun onSwipeLeft() {
        val intent = Intent(this@SetTimerActivity, LocationActivity::class.java)
        startActivity(intent)

    }
}

@Composable
fun SliderComposable(){
    var sliderPosition by remember {
        mutableStateOf(1f)
    }

    Column (modifier = Modifier
        .padding(top = 40.dp, start = 30.dp)
        .width(140.dp)
    ){
        Text(text = "Interval time")
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 1f..30f

        )
        Text(text = "${"%.0f".format(sliderPosition)} minutes")
    }

}
