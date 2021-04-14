package com.cilledyr.myfriendsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private var cox = ""
    private var coy = ""
    private lateinit var mMap: GoogleMap
    private val friendRepo = FriendRepository.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        cox = intent.getStringExtra("coordinateX").toString()
        coy = intent.getStringExtra("coordinateY").toString()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker .
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


            val coordinates = LatLng(cox.toDouble(), coy.toDouble())
            mMap.addMarker(MarkerOptions().position(coordinates).title("Friend Last Known location"))
        val zoomLevel = 16.0f //This goes up to 21

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates,zoomLevel))
        }
    }
