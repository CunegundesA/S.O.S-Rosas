package com.example.sosrosas.ViewModel

import android.content.Context
import com.example.sosrosas.Model.ContatoAjuda
import com.example.sosrosas.Model.Usuario
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class ContatosSharedPreferences (context: Context)  {

    private val arrayJson = JSONArray()
    private val SharedPreferences = context.getSharedPreferences("S.O.S Rosas Contatos", Context.MODE_PRIVATE)

    fun saveContatos(email: String, contato: ContatoAjuda){
        val gson = Gson()
        val objectContato = gson.toJson(contato)
        arrayJson.put(objectContato)
        val listArrayDates = arrayJson.toString()
        SharedPreferences.edit().putString(email, listArrayDates).apply()
    }

    fun getListContatos(email: String) : List<ContatoAjuda>?{

        try {
            val listContatos = JSONArray(SharedPreferences.getString(email, ""))
            val listAllContatos = ArrayList<ContatoAjuda>()

            for (x in 0 until listContatos.length()) {
                val obj = listContatos.getString(x)
                val gson = Gson()
                var contato = ContatoAjuda()

                contato = gson.fromJson(obj, ContatoAjuda::class.java)
                listAllContatos.add(contato)
            }

            return listAllContatos
        }catch (e : JSONException){}

        return null
    }

    fun saveListContatos(email: String, list : List<ContatoAjuda>){
       if(arrayJson.length() > 0) {
           for (x in 0 until arrayJson.length()) {
               arrayJson.remove(x)
           }
       }

        for(y in list){
            val gson = Gson()
            val objectContato = gson.toJson(y)
            arrayJson.put(objectContato)
        }

        val listArrayDates = arrayJson.toString()
        SharedPreferences.edit().putString(email, listArrayDates).apply()

    }

    fun updateContato(email: String, contato: ContatoAjuda){
        val listContatos = JSONArray(SharedPreferences.getString(email, ""))
        val listAllContatos = ArrayList<ContatoAjuda>()

        for (x in 0 until listContatos.length()) {
            val obj = listContatos.getString(x)
            val gson = Gson()
            var contato = ContatoAjuda()

            contato = gson.fromJson(obj, ContatoAjuda::class.java)
            listAllContatos.add(contato)
        }

        for(y in 0 until listAllContatos.size){
            if(listAllContatos.get(y).id == contato.id){
                arrayJson.remove(y)
                val gson = Gson()
                val objectContato = gson.toJson(contato)
                arrayJson.put(objectContato)
            }
        }

        val listArrayDates = arrayJson.toString()
        SharedPreferences.edit().putString(email, listArrayDates).apply()
    }

    fun removeContato(email: String, contato: ContatoAjuda){
        val listContatos = JSONArray(SharedPreferences.getString(email, ""))
        val listAllContatos = ArrayList<ContatoAjuda>()

        for (x in 0 until listContatos.length()) {
            val obj = listContatos.getString(x)
            val gson = Gson()
            var contato = ContatoAjuda()

            contato = gson.fromJson(obj, ContatoAjuda::class.java)
            listAllContatos.add(contato)
        }

        for(y in 0 until listAllContatos.size){
            if(listAllContatos.get(y).id == contato.id){
                arrayJson.remove(y)
            }
        }

        val listArrayDates = arrayJson.toString()
        SharedPreferences.edit().putString(email, listArrayDates).apply()

    }
}