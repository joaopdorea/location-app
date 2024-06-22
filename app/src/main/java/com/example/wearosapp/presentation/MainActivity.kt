package com.example.wearosapp.presentation


import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*
import kotlin.math.abs


class MainActivity : ComponentActivity() {

    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel = viewModel<StopWatchViewModel>()
            val timerState by viewModel.timerState.collectAsStateWithLifecycle()
            val stopWatchText by viewModel.stopWatchText.collectAsStateWithLifecycle()
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
                SwitchComposable()
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
        val intent = Intent(this@MainActivity, SetTimerActivity::class.java)
        startActivity(intent)
    }
}


@Composable
fun SwitchComposable() {
    val checkedValue = remember { mutableStateOf(true) }

    Column(
            modifier = Modifier
                .padding(top = 40.dp, start = 40.dp)

    ) {


        Text( text = "Location Status")
        Switch(
            checked = checkedValue.value,
            onCheckedChange = {

                checkedValue.value = it

            }
        )
        Text( text = "${checkedValue.value}")
    }
}
