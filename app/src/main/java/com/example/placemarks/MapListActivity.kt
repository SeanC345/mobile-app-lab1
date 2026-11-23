package com.example.placemarks

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.CopyrightOverlay

class MapListActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    private lateinit var txtTitle: TextView
    private lateinit var txtDescription: TextView
    private lateinit var imgPlace: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_list)

        // Initialise views
        mapView = findViewById(R.id.mapList)
        txtTitle = findViewById(R.id.mapTitleText)
        txtDescription = findViewById(R.id.mapDescText)
        imgPlace = findViewById(R.id.mapImageView)

        findViewById<Button>(R.id.mapBackButton).setOnClickListener {
            finish()
        }


        // Configure OpenStreetMap
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        // Show map attribution
        mapView.overlays.add(CopyrightOverlay(this))

        // Load placemarks
        loadMarkers()
    }

    private fun loadMarkers() {

        val placemarks = AppData.placedMarks.findAll()

        if (placemarks.isEmpty()) {
            txtTitle.text = "No Placemarks"
            txtDescription.text = "Add a placemark to display it on the map."
            imgPlace.setImageDrawable(null)
            return
        }

        placemarks.forEach { placemark ->

            val position = GeoPoint(
                placemark.x,
                placemark.y
            )

            val marker = Marker(mapView)

            marker.position = position
            marker.title = placemark.title
            marker.snippet = placemark.desc

            marker.setAnchor(
                Marker.ANCHOR_CENTER,
                Marker.ANCHOR_BOTTOM
            )

            marker.setOnMarkerClickListener { _, _ ->

                displayPlace(placemark)

                true
            }

            mapView.overlays.add(marker)
        }

        // Centre map on first placemark
        placemarks.firstOrNull()?.let { first ->

            mapView.controller.setZoom(12.0)

            mapView.controller.setCenter(
                GeoPoint(first.x, first.y)
            )
        }

        mapView.invalidate()
    }

    private fun displayPlace(placemark: PlacemarkModel) {

        txtTitle.text = placemark.title

        txtDescription.text = placemark.desc

        if (placemark.imageUri != null) {
            imgPlace.setImageURI(
                Uri.parse(placemark.imageUri)
            )
        } else {
            imgPlace.setImageDrawable(null)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        mapView.onDetach()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()

        mapView.tileProvider.clearTileCache()
        mapView.invalidate()
    }
}