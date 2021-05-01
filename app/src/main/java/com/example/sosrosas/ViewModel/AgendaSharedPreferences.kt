package com.example.sosrosas.ViewModel

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class AgendaSharedPreferences(context: Context)  {

    private val arrayJson = JSONArray()
    private val SharedPreferences = context.getSharedPreferences("S.O.S Rosas Agenda", Context.MODE_PRIVATE)

    fun saveDate(time: Long, email: String){
        val obj = JSONObject()
        obj.put("Time", time.toString())
        arrayJson.put(obj)
        val listArrayDates = arrayJson.toString()
        SharedPreferences.edit().putString(email, listArrayDates).apply()
    }

    fun getList(email: String) : List<String>?{

        try {
            val listDates = JSONArray(SharedPreferences.getString(email, ""))
            val listAllDates = ArrayList<String>()

            for (x in 0 until listDates.length()) {
                val obj = listDates.getJSONObject(x)
                listAllDates.add(obj.getString("Time"))
            }

            return listAllDates
        }catch (e : JSONException){}

        return null
    }

    fun removeDate(index: Int, email: String){
        arrayJson.remove(index)
        val listArrayDates = arrayJson.toString()
        SharedPreferences.edit().putString(email, listArrayDates).apply()
    }
}