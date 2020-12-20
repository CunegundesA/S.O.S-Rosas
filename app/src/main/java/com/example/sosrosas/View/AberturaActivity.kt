package com.example.sosrosas.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.sosrosas.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_abertura.*

class AberturaActivity : AppCompatActivity(), Runnable {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var thread : Thread
    private lateinit var handler : Handler
    private var i : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abertura)

        if(supportActionBar != null){
            supportActionBar!!.hide()
        }

        handler = Handler()
        thread = Thread(this)
        thread.start()
    }

    override fun run() {
        i = 1

        try{

            while (i <=150){
                Thread.sleep(15)
                handler.post(object : Runnable{
                    override fun run() {
                        i++
                        progress_opening.progress = i
                    }
                })
            }
            val user = auth.currentUser

            if(user == null) {
                startActivity(Intent(applicationContext, LoginActivit::class.java))
                finish()
            }else{
                startActivity(Intent(applicationContext, PrincipalActivity::class.java))
                finish()
            }

        }catch(e : InterruptedException){

        }
    }
}