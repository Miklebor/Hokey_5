package com.example.hokey_5

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MapKitFactory.setApiKey("39bb32a5-de2b-4402-ab96-a064b52fa1db")
        MapKitFactory.initialize(this)
        requestLocationPermission()

        val moscowCenter = Point(55.751574, 37.573856)
        val radius = 1000.0

        val searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        val searchSession = searchManager.submit(
            moscowCenter,
            radius,
            SearchOptions(),
            SearchListener()
        )
        val userPosition = Point(userLat, userLon)

        if (Circle(moscowCenter, radius.toFloat()).contains(userPosition)) {
            makeCall("+79995550583")
        }
        
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }
    fun makeCall(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phoneNumber")

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CALL_PHONE),
                1)
        } else {
            startActivity(callIntent)
        }
    }
    private fun requestLocationPermission(){
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )!= PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf( Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 0)
            return
        }
    }

}

class SearchListener : Session.SearchListener {
    override fun onSearchResponse(response: Response) {
        val results = response.collection.children

        for (result in results) {
            // Обработка найденных результатов
        }
    }

    override fun onSearchError(error: Error) {
        // Обработка ошибок
    }
}

