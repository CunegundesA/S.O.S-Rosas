package com.example.sosrosas.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.sosrosas.R
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_abertura.*

class AberturaActivity : AppCompatActivity(), Runnable {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var thread: Thread
    private lateinit var handler: Handler
    private lateinit var userDefault: SharedPreferences
    private lateinit var mGoogleSignInClient : GoogleSignInClient
    private var firstTimeAcess: Boolean = false
   // private lateinit var userPass: com.example.sosrosas.ViewModel.SharedPreferences
    private var i: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abertura)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        //userPass = com.example.sosrosas.ViewModel.SharedPreferences(this)
        userDefault = this.getSharedPreferences("UserDefault", Context.MODE_PRIVATE)


        if (!userDefault.getBoolean("First", true)) {
            firstTimeAcess = true
            userDefault.edit().putBoolean("First", true).commit()
        }else{
            firstTimeAcess = false
        }

        servicesGoogle()

        handler = Handler()
        thread = Thread(this)
        thread.start()
    }

    //Faz conexão com serviços do Google
    private fun servicesGoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(applicationContext , gso)

    }

    override fun run() {
        i = 1
        try {
            while (i <= 150) {
                Thread.sleep(15)
                handler.post(object : Runnable {
                    override fun run() {
                        i++
                        progress_opening.progress = i
                    }
                })
            }

           Firebase.database.setPersistenceEnabled(true)

            if (firstTimeAcess) {
                auth.signOut()
                mGoogleSignInClient.signOut()
                LoginManager.getInstance().logOut()
                startActivity(Intent(applicationContext, LoginActivit::class.java))
                finish()
            } else {
                val user = auth.currentUser
                if (user == null) {
                    startActivity(Intent(applicationContext, LoginActivit::class.java))
                    finish()
                } else {
                    startActivity(Intent(applicationContext, PrincipalActivity::class.java))
                    finish()
                }
           }
        } catch (e: InterruptedException) {}
    }
}