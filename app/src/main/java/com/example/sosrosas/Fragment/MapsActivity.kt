package com.example.sosrosas.Fragment

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.media.Image
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.sosrosas.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.internal.ImagesContract
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.box_erro_location.*
import kotlinx.android.synthetic.main.box_main_violencia_fisica.*

class MapsActivity : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private lateinit var mMap: GoogleMap
    private lateinit var mLocation: Location
    private lateinit var myLocation: Location
    private lateinit var mLocationManager: LocationManager
    private lateinit var mGoogleApiClient: GoogleApiClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root: ViewGroup =
            inflater.inflate(R.layout.activity_maps, container, false) as ViewGroup

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mGoogleApiClient = GoogleApiClient
            .Builder(context!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        mGoogleApiClient.connect()
        return root
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mLocationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        val provider = mLocationManager.getBestProvider(criteria, true)
        println(provider)
        mLocation = Location(provider)

        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.setMinZoomPreference(11.0f)
        if (ActivityCompat.checkSelfPermission(context!!,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(context as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        }
        mMap.isMyLocationEnabled = true

        // Hospitais
        val hospital28Agosto = LatLng(-3.101703, -60.014122)
        mMap.addMarker(MarkerOptions().position(hospital28Agosto)
            .title("Hospital e Pronto Socorro 28 de Agosto")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_hospital30x40r)))

        val hospitalDelphina = LatLng(-2.997890, -60.029874)
        mMap.addMarker(MarkerOptions().position(hospitalDelphina)
            .title("Hospital Delphina Rinaldi Abdel Aziz")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_hospital30x40r)))

        val hospitalJoaoLucio = LatLng(-3.073721, -59.962384)
        mMap.addMarker(MarkerOptions().position(hospitalJoaoLucio)
            .title("Hospital e Pronto Socorro Dr. João Lúcio Pereira Machado")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_hospital30x40r)))

        val hospitalBezerraAraujo = LatLng(-3.038509, -59.941333)
        mMap.addMarker(MarkerOptions().position(hospitalBezerraAraujo)
            .title("Hospital e Pronto Socorro Dr Aristóteles Platão Bezerra de Araújo")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_hospital30x40r)))

        //DECCM
        val deccmAnexoNorte = LatLng(-3.017140, -59.938902)
        mMap.addMarker(MarkerOptions().position(deccmAnexoNorte)
            .title("DECCM - Delegacia Especializada Em Crimes Contra A Mulher/Anexo Norte")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_deccm30x40)))

        val deccm = LatLng(-3.088099, -60.018686)
        mMap.addMarker(MarkerOptions().position(deccm)
            .title("DECCM - Delegacia da Mulher")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_deccm30x40)))

        val deccmAnexoSul = LatLng(-3.147606, -60.005539)
        mMap.addMarker(MarkerOptions().position(deccmAnexoSul)
            .title("DECCM - Delegacia Especializada Em Crimes Contra A Mulher/Anexo Sul")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_deccm30x40)))

        //DIPs
        val dip1 = LatLng(-3.118445, -60.011530)
        mMap.addMarker(MarkerOptions().position(dip1)
            .title("1° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip2 = LatLng(-3.147606, -60.005539)
        mMap.addMarker(MarkerOptions().position(dip2)
            .title("2° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip3 = LatLng(-3.118486, -59.994947)
        mMap.addMarker(MarkerOptions().position(dip3)
            .title("3° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip4 = LatLng(-3.074215, -59.926509)
        mMap.addMarker(MarkerOptions().position(dip4)
            .title("4° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip5 = LatLng(-3.119399, -60.047426)
        mMap.addMarker(MarkerOptions().position(dip5)
            .title("5° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip6 = LatLng(-3.031731, -59.986813)
        mMap.addMarker(MarkerOptions().position(dip6)
            .title("6° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip7 = LatLng(-3.138416, -59.992352)
        mMap.addMarker(MarkerOptions().position(dip7)
            .title("7° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip8 = LatLng(-3.103487, -60.063302)
        mMap.addMarker(MarkerOptions().position(dip8)
            .title("8° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip9 = LatLng(-3.072660, -59.947955)
        mMap.addMarker(MarkerOptions().position(dip9)
            .title("9° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip10 = LatLng(-3.071835, -60.045656)
        mMap.addMarker(MarkerOptions().position(dip10)
            .title("10° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip11 = LatLng(-3.090408, -59.981504)
        mMap.addMarker(MarkerOptions().position(dip11)
            .title("11° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip12 = LatLng(-3.071024, -60.022697)
        mMap.addMarker(MarkerOptions().position(dip12)
            .title("12° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip13 = LatLng(-3.017135, -59.938886)
        mMap.addMarker(MarkerOptions().position(dip13)
            .title("13° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip14 = LatLng(-3.050255, -59.945047)
        mMap.addMarker(MarkerOptions().position(dip14)
            .title("14° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip15 = LatLng(-3.000238, -59.987189)
        mMap.addMarker(MarkerOptions().position(dip15)
            .title("15° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip17 = LatLng(-3.053726, -60.040604)
        mMap.addMarker(MarkerOptions().position(dip17)
            .title("17° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip18 = LatLng(-3.025094, -60.011880)
        mMap.addMarker(MarkerOptions().position(dip18)
            .title("18° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip19 = LatLng(-3.086215, -60.067817)
        mMap.addMarker(MarkerOptions().position(dip19)
            .title("19° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip20 = LatLng(-3.005997, -60.037734)
        mMap.addMarker(MarkerOptions().position(dip20)
            .title("20° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip21 = LatLng(-3.112576, -60.034632)
        mMap.addMarker(MarkerOptions().position(dip21)
            .title("21° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip22 = LatLng(-3.111442, -60.018303)
        mMap.addMarker(MarkerOptions().position(dip22)
            .title("22° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip23 = LatLng(-3.070740, -59.996390)
        mMap.addMarker(MarkerOptions().position(dip23)
            .title("23° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip24 = LatLng(-3.137956, -60.016430)
        mMap.addMarker(MarkerOptions().position(dip24)
            .title("24° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip25 = LatLng(-3.090042, -59.943231)
        mMap.addMarker(MarkerOptions().position(dip25)
            .title("25° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip26 = LatLng(-2.986117, -60.017442)
        mMap.addMarker(MarkerOptions().position(dip26)
            .title("26° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip27 = LatLng(-3.044350, -59.959082)
        mMap.addMarker(MarkerOptions().position(dip27)
            .title("27° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip28 = LatLng(-3.084011, -59.892211)
        mMap.addMarker(MarkerOptions().position(dip28)
            .title("28° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip29 = LatLng(-3.125592, -59.936070)
        mMap.addMarker(MarkerOptions().position(dip29)
            .title("29° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

        val dip30 = LatLng(-3.031503, -59.922117)
        mMap.addMarker(MarkerOptions().position(dip30)
            .title("30° DIP - Distrito Integrado de Polícia")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_policia30x40)))

    }

    override fun onConnected(p0: Bundle?) {
        if (ActivityCompat.checkSelfPermission(context!!,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(context as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        }

        val isLocationActive = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if(isLocationActive) {
            try {
                myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
                val myLatLng = LatLng(myLocation.latitude, myLocation.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng))
            }catch (e: NullPointerException){
                val dialog = Dialog(context!!)
                dialog.setContentView(R.layout.box_erro_location)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.text_msm_erro1.visibility = View.VISIBLE
                dialog.button_ok_error_location.setOnClickListener(object: View.OnClickListener{
                    override fun onClick(p0: View?) {
                        dialog.dismiss()
                    }
                })
                dialog.show()
                val myLatLng = LatLng(-3.0443101,-60.1071934)
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng))
            }
        }else{
            val dialog = Dialog(context!!)
            dialog.setContentView(R.layout.box_erro_location)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.button_ok_error_location.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    dialog.dismiss()
                }
            })
            dialog.show()
            val myLatLng = LatLng(-3.0443101,-60.1071934)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng))
        }
    }

    //Hospitais
    fun goHospital28deAgosto(){
        val hospital28Agosto = LatLng(-3.101703, -60.014122)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hospital28Agosto))
    }

    fun goHospitalDelphina(){
        val hospitalDelphina = LatLng(-2.997890, -60.029874)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hospitalDelphina))
    }

    fun goHospitalJoaoLucio(){
        val hospitalJoaoLucio = LatLng(-3.073721, -59.962384)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hospitalJoaoLucio))
    }

    fun goHospitalBezerraAraujo(){
        val hospitalBezerraAraujo = LatLng(-3.038509, -59.941333)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hospitalBezerraAraujo))
    }

    //DECCM
    fun goDECCMNorte(){
        val deccmAnexoNorte = LatLng(-3.017140, -59.938902)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(deccmAnexoNorte))
    }

    fun goDECCM(){
        val deccm = LatLng(-3.088099, -60.018686)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(deccm))
    }

    fun goDECCMSul(){
        val deccmAnexoSul = LatLng(-3.147606, -60.005539)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(deccmAnexoSul))
    }

    //DPIs
    fun goDIP1(){
        val dip1 = LatLng(-3.118445, -60.011530)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip1))
    }

    fun goDIP2(){
        val dip2 = LatLng(-3.147606, -60.005539)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip2))
    }

    fun goDIP3(){
        val dip3 = LatLng(-3.118486, -59.994947)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip3))
    }

    fun goDIP4(){
        val dip4 = LatLng(-3.074215, -59.926509)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip4))
    }

    fun goDIP5(){
        val dip5 = LatLng(-3.119399, -60.047426)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip5))
    }

    fun goDIP6(){
        val dip6 = LatLng(-3.031731, -59.986813)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip6))
    }

    fun goDIP7(){
        val dip7 = LatLng(-3.138416, -59.992352)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip7))
    }

    fun goDIP8(){
        val dip8 = LatLng(-3.103487, -60.063302)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip8))
    }

    fun goDIP9(){
        val dip9 = LatLng(-3.072660, -59.947955)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip9))
    }

    fun goDIP10(){
        val dip10 = LatLng(-3.071835, -60.045656)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip10))
    }

    fun goDIP11(){
        val dip11 = LatLng(-3.090408, -59.981504)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip11))
    }

    fun goDIP12(){
        val dip12 = LatLng(-3.071024, -60.022697)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip12))
    }

    fun goDIP13(){
        val dip13 = LatLng(-3.017135, -59.938886)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip13))
    }

    fun goDIP14(){
        val dip14 = LatLng(-3.050255, -59.945047)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip14))
    }

    fun goDIP15(){
        val dip15 = LatLng(-3.000238, -59.987189)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip15))
    }

    fun goDIP17(){
        val dip17 = LatLng(-3.053726, -60.040604)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip17))
    }

    fun goDIP18(){
        val dip18 = LatLng(-3.025094, -60.011880)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip18))
    }

    fun goDIP19(){
        val dip19 = LatLng(-3.086215, -60.067817)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip19))
    }

    fun goDIP20(){
        val dip20 = LatLng(-3.005997, -60.037734)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip20))
    }

    fun goDIP21(){
        val dip21 = LatLng(-3.112576, -60.034632)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip21))
    }

    fun goDIP22(){
        val dip22 = LatLng(-3.111442, -60.018303)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip22))
    }

    fun goDIP23(){
        val dip23 = LatLng(-3.070740, -59.996390)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip23))
    }

    fun goDIP24(){
        val dip24 = LatLng(-3.137956, -60.016430)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip24))
    }

    fun goDIP25(){
        val dip25 = LatLng(-3.090042, -59.943231)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip25))
    }

    fun goDIP26(){
        val dip26 = LatLng(-2.986117, -60.017442)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip26))
    }

    fun goDIP27(){
        val dip27 = LatLng(-3.044350, -59.959082)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip27))
    }

    fun goDIP28(){
        val dip28 = LatLng(-3.084011, -59.892211)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip28))
    }

    fun goDIP29(){
        val dip29 = LatLng(-3.125592, -59.936070)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip29))
    }

    fun goDIP30(){
        val dip30 = LatLng(-3.031503, -59.922117)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dip30))
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }
}