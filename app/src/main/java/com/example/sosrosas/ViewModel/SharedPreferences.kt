package com.example.sosrosas.ViewModel

import android.content.Context

class SharedPreferences(context: Context) {

    private val SharedPreferences = context.getSharedPreferences("S.O.S Rosas", Context.MODE_PRIVATE)

    fun savePasswordKeys(key: String , value: String){
            SharedPreferences.edit().putString(key, value).apply()
    }

    fun validationFakePasswordUser(key: String, fakePassword: String) : Boolean{
       val password = SharedPreferences.getString(key, "")

        if(!password!!.isEmpty() && password != null){
            if(password == fakePassword) {
                return true
            }else{
                return false
            }
        }else{
            return false
        }
    }

    fun clearPasswords(){
        SharedPreferences.edit().clear()
    }

    fun getStoreString(key: String) : String {
        return SharedPreferences.getString(key, "") ?: ""
    }

    fun update(key: String, fakePassword: String){
        SharedPreferences.edit().putString(key, fakePassword).apply()
    }

}