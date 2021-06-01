package com.kotlincodes.bottomnavigationview

import android.graphics.PointF
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CompositeIcon
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.RotationType
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider


class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    private var userLocationLayer: UserLocationLayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = view.findViewById(R.id.map_view)
        return view
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(context)
        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer!!.isVisible = true
        userLocationLayer!!.setHeadingEnabled(true)
        userLocationLayer!!.setObjectListener(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.map.move(
            CameraPosition(Point(0.0, 0.0), 17.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )

    }

    protected fun addMarker() {
        mapView.map.mapObjects.addPlacemark(Point(54.747976, 20.502257))
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        addMarker()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }


    private fun onObjectAdded (UserLocationView: userLocationView) {
        userLocationLayer!!.setAnchor(
            PointF(
                (mapView.width * 0.5).toFloat(),
                (mapView.height * 0.5).toFloat()),
            PointF((mapView.width * 0.5).toFloat(),
                (mapView.height * 0.83).toFloat())
                )
        userLocationView.getArrow().setIcon(ImageProvider.fromResource(this, R.drawable.user_arrow))

        val pinIcon: CompositeIcon = userLocationView.getPin().useCompositeIcon()

        pinIcon.setIcon(
            "icon",
            ImageProvider.fromResource(this, R.drawable.icon),
            IconStyle().setAnchor(PointF(0f, 0f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(0f)
                .setScale(1f)
        )

        pinIcon.setIcon(
            "pin",
            ImageProvider.fromResource(this, R.drawable.search_result),
            IconStyle().setAnchor(PointF(0.5f, 0.5f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(1f)
                .setScale(0.5f)
        )

    }
    fun onObjectRemoved(view: UserLocationView?) {}

    fun onObjectUpdated(view: UserLocationView?, event: ObjectEvent?) {}


}
