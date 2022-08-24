package com.spacemooncake.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.params.BlackLevelPattern
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider


class MainActivity : AppCompatActivity(),
    UserLocationObjectListener {


    private lateinit var userLocationLayer: UserLocationLayer
    private lateinit var mapView: MapView

    private var routeStartLocation = Point(0.0, 0.0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(mapApiKey)
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.map_view)
        checkPermission()

    }

    private fun checkPermission() {
        val permissionLocation =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                requestPermissionLocation
            )
        } else {
            onMapReady()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            requestPermissionLocation -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onMapReady()
                }
                return
            }
        }
    }


    private fun onMapReady() {
        val map = MapKitFactory.getInstance()
        userLocationLayer = map.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.setAnchor(
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.5).toFloat()),
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.83).toFloat())
        )

        userLocationLayer.setObjectListener(this)

        mapView.map.move(
            CameraPosition(
                routeStartLocation,
                16f,
                0f,
                0f
            )
        )

    }

    private fun addPlaceMarks(point: Point) {
        val point1 = Point(point.latitude + 0.0024, point.longitude - 0.0040)
        val point2 = Point(point.latitude + 0.0008, point.longitude + 0.0007)
        val point3 = Point(point.latitude - 0.0007, point.longitude - 0.0014)

        mapView.map.mapObjects.addPlacemark(
            point1,
            ImageProvider.fromResource(this, R.drawable.mark5)
        )

        mapView.map.mapObjects.addPlacemark(
            point2,
            ImageProvider.fromResource(this, R.drawable.mark5)
        )

        mapView.map.mapObjects.addPlacemark(
            point3,
            ImageProvider.fromResource(this, R.drawable.mark5)
        )


        mapView.map.mapObjects.addPlacemark(
            point1,
            ImageProvider.fromBitmap(drawSimpleBitmap("Место №1"))
        )

        mapView.map.mapObjects.addPlacemark(
            point2,
            ImageProvider.fromBitmap(drawSimpleBitmap("Место №2"))
        )

        mapView.map.mapObjects.addPlacemark(
            point3,
            ImageProvider.fromBitmap(drawSimpleBitmap("Место №3"))
        )

    }


    fun  drawSimpleBitmap( number : String) : Bitmap {
        val picSize = 10000
        val bitmap = Bitmap.createBitmap(picSize, picSize, Bitmap.Config.ARGB_8888);
        val canvas = Canvas(bitmap)
        // отрисовка плейсмарка
        val paint = Paint();
        paint.color = 80000000;
        paint.style = Paint.Style.FILL;
        canvas.drawCircle((picSize / 2).toFloat(), (picSize / 2).toFloat(), (picSize / 2).toFloat(), paint);
        // отрисовка текста
        paint.color = Color.BLACK
        paint.isAntiAlias = true;
        paint.textSize = 25F;
        paint.isFakeBoldText = true
        paint.textAlign = Paint.Align.LEFT
        canvas.drawText(number, (picSize / 2).toFloat(),
            picSize / 2 - ((paint.descent() + paint.ascent()) / 2), paint);
        return bitmap;
    }


    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()

    }

    override fun onStop() {
        super.onStop()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    companion object {
        const val mapApiKey = "aa975882-803a-4ec7-b1f9-71d005d73177"
        const val requestPermissionLocation = 1
    }

    override fun onObjectAdded(view: UserLocationView) {}

    override fun onObjectRemoved(p0: UserLocationView) {}

    override fun onObjectUpdated(view: UserLocationView, p1: ObjectEvent) {
        val point = userLocationLayer.cameraPosition()!!.target
        println("lat: ${point.latitude} long: ${point.longitude}")
        addPlaceMarks(point)
    }

}