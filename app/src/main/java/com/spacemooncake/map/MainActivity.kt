package com.spacemooncake.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

class MainActivity : AppCompatActivity() {

    private var mapView: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey("aa975882-803a-4ec7-b1f9-71d005d73177")
        MapKitFactory.initialize(this)

        setContentView(R.layout.activity_main)

        mapView = findViewById<MapView>(R.id.mapview)
        with(mapView) {
            this?.map?.move(
                CameraPosition(Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0F),
                null
            )
        }


    }

    override fun onStart(){
        super.onStart()
        mapView?.onStart()
        MapKitFactory.getInstance().onStart()

    }

    override fun onStop(){
        super.onStop()
        mapView?.onStart()
        MapKitFactory.getInstance().onStart()
    }

}