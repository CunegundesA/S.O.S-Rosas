package com.example.sosrosas.View

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.sosrosas.Model.ContatoAjuda
import com.example.sosrosas.Model.Usuario
import com.example.sosrosas.R
import com.example.sosrosas.ViewModel.ContatosViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_botao_do_panico.*
import java.util.ArrayList
import java.util.jar.Manifest

class BotaoDoPanicoActivity : AppCompatActivity(), View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener{

    private var listContatos = ArrayList<ContatoAjuda>()
    private val database = FirebaseDatabase.getInstance()
    private lateinit var data: DatabaseReference
    private var usuarioAtual = Usuario()
    private lateinit var childEventListener: ChildEventListener
    private val auth = FirebaseAuth.getInstance()
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var myLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_botao_do_panico)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        loadListContato()
        loadUserCurrent()
        getListeners()

        mGoogleApiClient = GoogleApiClient
            .Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        mGoogleApiClient.connect()
    }

    private fun getListeners(){
        button_voltar.setOnClickListener(this)
        botao_panico.setOnClickListener(this)
    }

    override fun onClick(view: View) {
       val id = view.id

        if(id == R.id.button_voltar){
            finish()
        }else if(id == R.id.botao_panico){
            sendSMS()
            ativarBotao()
        }
    }

    private fun ativarBotao(){
        val uri = Uri.parse( "tel:190")
        val intent = Intent(Intent.ACTION_CALL, uri)
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.CALL_PHONE), 1)
            getListeners()
        }else {
            startActivity(intent)
        }
    }

    private fun sendSMS(){
        for(x in listContatos){
            sendSMS(x)
        }
    }

    private fun sendSMS(contato: ContatoAjuda) {
        var numeroContato = "0"
        val sms = SmsManager.getDefault()
        val address = Geocoder(this).getFromLocation(myLocation.latitude, myLocation.longitude, 1)

        for (x in contato.celular) {
            if (x != '(' && x != ')' && x != '-') {
                numeroContato = numeroContato + x
            }
        }

        val msg = """O usuario ${usuarioAtual.nome} do aplicativo S.O.S Rosas esta em perigo!!!
                    |Dados de sua lozalização:
                    |Latitude: ${myLocation.latitude}
                    |Longitude: ${myLocation.longitude}
                    |Endereço: ${address.get(0).getAddressLine(0)}
                    |link: https://www.google.com.br/maps/place/${myLocation.latitude},${myLocation.longitude}""".trimMargin()

        val sendMSG = sms.divideMessage(msg)

        if (ActivityCompat.checkSelfPermission(applicationContext,
                android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(applicationContext,
                android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.SEND_SMS, android.Manifest.permission.READ_PHONE_STATE), 1)
        } else {
            sms.sendMultipartTextMessage(numeroContato, null, sendMSG, null, null)
        }
    }

    private fun loadListContato(){
        val user = auth.currentUser
        val uid = user?.uid
        data = database.getReference().child("BD_SOSRosas").child(uid.toString()).child("Contatos")

        childEventListener = object : ChildEventListener {

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val key = dataSnapshot.key
                val contatoAjuda = dataSnapshot.getValue<ContatoAjuda>()
                listContatos.add(contatoAjuda!!)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        data.addChildEventListener(childEventListener)
    }

    private fun loadUserCurrent() {
        val user = auth.currentUser
        val uid = user!!.uid
        val data2 =
            database.getReference().child("BD_SOSRosas").child(uid.toString()).child("Usuario")

        data2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usuarioAtual = snapshot.getValue<Usuario>() as Usuario
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onConnected(p0: Bundle?) {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        }
        myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

}