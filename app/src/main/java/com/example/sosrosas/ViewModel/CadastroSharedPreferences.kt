package com.example.sosrosas.ViewModel

import android.content.Context
import com.example.sosrosas.Model.Usuario
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class CadastroSharedPreferences (context: Context)  {

    private val SharedPreferences = context.getSharedPreferences("S.O.S Rosas Usuario", Context.MODE_PRIVATE)

    fun saveUser(email: String, user: Usuario){
        val gson = Gson()
        val objectUser = gson.toJson(user)

        SharedPreferences.edit().putString(email, objectUser).apply()
    }

    fun getUser(email: String) : Usuario?{
        try {
            val gson = Gson()
            val obj = SharedPreferences.getString(email, "")
            var user = Usuario()

            user = gson.fromJson(obj, Usuario::class.java)

            return user
        }catch (e : NullPointerException){}

        return null
    }

}