package com.example.proximitysensor

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var proximitySensor: Sensor? = null
    private var accelerometerSensor: Sensor? = null
    private var imageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        for (sensor in deviceSensors) {
            Log.d("sensorsList", sensor.name)
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null
            && sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null
        ) {
            Log.d("isSensorFound", "Sensor found")
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        } else {
            Log.d("isSensorFound", "Sensor not found")
        }

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if (event?.sensor?.type == Sensor.TYPE_PROXIMITY) {
            val value = event.values[0]
            val maxRange = event.sensor.maximumRange

            if (value > 0) {
                val objectAnimatorX = ObjectAnimator.ofFloat(imageView, View.SCALE_X, 3f)
                val objectAnimatorY = ObjectAnimator.ofFloat(imageView, View.SCALE_Y, 3f)

                objectAnimatorX.duration = 1000
                objectAnimatorY.duration = 1000

                val animatorSet = AnimatorSet()
                animatorSet.play(objectAnimatorX).with(objectAnimatorY)
                animatorSet.start()
            } else {
                val objectAnimatorX = ObjectAnimator.ofFloat(imageView, View.SCALE_X, 0.2f)
                val objectAnimatorY = ObjectAnimator.ofFloat(imageView, View.SCALE_Y, 0.2f)

                objectAnimatorX.duration = 1000
                objectAnimatorY.duration = 1000

                val animatorSet = AnimatorSet()
                animatorSet.play(objectAnimatorX).with(objectAnimatorY)
                animatorSet.start()
            }
        }

        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            // Do something
        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // Do something here if sensor accuracy changes.
    }

}