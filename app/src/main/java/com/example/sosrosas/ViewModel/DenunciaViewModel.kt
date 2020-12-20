package com.example.sosrosas.ViewModel

import androidx.lifecycle.ViewModel
import com.example.sosrosas.Model.Denuncia
import com.example.sosrosas.Model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class DenunciaViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("BD_SOSRosas")

    fun save(denuncia: Denuncia){
        val user = auth.currentUser
        val uid = user!!.uid
        val data = Date()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        myRef.child(uid).child("Denuncias").child(dateFormat.format(data)).child(timeFormat.format(data)).setValue(denuncia)
    }

}