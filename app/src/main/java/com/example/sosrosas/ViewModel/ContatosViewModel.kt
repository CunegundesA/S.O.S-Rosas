package com.example.sosrosas.ViewModel

import androidx.lifecycle.ViewModel
import com.example.sosrosas.Model.ContatoAjuda
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import java.util.*
import kotlin.collections.HashMap


class ContatosViewModel : ViewModel(){

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("BD_SOSRosas")

    companion object{
        fun getInstance() = ContatosViewModel()
        fun getInstanceList() = ContatosViewModel().listarContatos()
    }

    fun save(contatoAjuda: ContatoAjuda){
        val user = auth.currentUser
        val uid = user?.uid
        myRef.child(uid.toString()).child("Contatos").push().setValue(contatoAjuda)
    }

    fun update(key : String, contatoAjuda: ContatoAjuda){
        val user = auth.currentUser
        val uid = user?.uid
        val data = database.getReference().child("BD_SOSRosas").child(uid.toString()).child("Contatos")
        val mapUpdate = HashMap<String, ContatoAjuda>()

        mapUpdate.put(key, contatoAjuda)
        data.updateChildren(mapUpdate.toMap())
    }

    fun remove(key : String){
        val user = auth.currentUser
        val uid = user?.uid
        val data = database.getReference().child("BD_SOSRosas").child(uid.toString()).child("Contatos")

        data.child(key).removeValue()
    }

    fun listarContatos() : List<ContatoAjuda>{
        val list = ArrayList<ContatoAjuda>()
        val listKeys = ArrayList<String>()
        val user = auth.currentUser
        val uid = user?.uid
        val data = database.getReference().child("BD_SOSRosas").child(uid.toString()).child("Contatos")

        data.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val key = dataSnapshot.key
                listKeys.add(key!!)
                val contatoAjuda = dataSnapshot.getValue<ContatoAjuda>()
                contatoAjuda?.id = key!!
                list.add(contatoAjuda!!)
                println(contatoAjuda)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                val key = dataSnapshot.key
                var index = listKeys.indexOf(key)
                val contatoAjuda = dataSnapshot.getValue<ContatoAjuda>()
                contatoAjuda?.id = key!!
                list.set(index, contatoAjuda!!)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
               /* val key = dataSnapshot.key
                val index = listKeys.indexOf(key)
                list.remove(list.get(index))
                listKeys.remove(listKeys.get(index))*/
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        return list
    }

}

