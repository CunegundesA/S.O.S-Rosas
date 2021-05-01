package com.example.sosrosas.View

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.sosrosas.Model.ErroNotification
import com.example.sosrosas.R
import com.example.sosrosas.ViewModel.CadastroViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_recuperar_senha.*
import kotlinx.android.synthetic.main.box_erro_conection_internet.*
import kotlinx.android.synthetic.main.box_sucess_send_email_password.*
import kotlinx.android.synthetic.main.box_termos_de_uso.*
import java.lang.Exception

class RecuperarSenhaActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mCadastroUsuario : CadastroViewModel
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_senha)

        if(supportActionBar != null){
            supportActionBar!!.hide()
        }

        mCadastroUsuario = CadastroViewModel.getIstance()
        getListeners()
    }

    private fun getListeners(){
        button_recuperar_senha.setOnClickListener(this)
    }

    private fun verificationConnectionWithInternet() : Boolean{
        val conectInternet = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conectInternet.activeNetworkInfo

        if (netInfo != null && netInfo.isConnected()) {
            return true
        }else{
            return false
        }
    }

    override fun onClick(view: View) {
        val id = view.id
        if(id == R.id.button_recuperar_senha){
            if(sendValidation()){
                if(verificationConnectionWithInternet()) {
                    val textEmail = text_email_recuperar_senha.text.toString().trim()
                    sendEmailRecoverPassword(this, textEmail)
                }else{
                    val dialog = Dialog(applicationContext)
                    dialog.setContentView(R.layout.box_erro_conection_internet)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.button_ok_error_conection_internet.setOnClickListener(object: View.OnClickListener{
                        override fun onClick(p0: View?) {
                            dialog.dismiss()
                        }

                    })
                    dialog.show()
                }
            }
        }
    }

    private fun sendEmailRecoverPassword(context : Context, email : String){
        auth.sendPasswordResetEmail(email).addOnSuccessListener(object: OnSuccessListener<Void> {
            override fun onSuccess(p0: Void?) {
                val dialog = Dialog(context)
                dialog.setContentView(R.layout.box_sucess_send_email_password)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.button_ok_send_email.setOnClickListener(object: View.OnClickListener{
                    override fun onClick(p0: View?) {
                        finish()
                        dialog.dismiss()
                    }

                })
                dialog.show()
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                println(e.toString())
                ErroNotification().getErroNotification(context, e.toString())
            }

        })
    }

    private fun sendValidation() : Boolean{
        val textEmail = text_email_recuperar_senha.text.toString()

        if(!textEmail.isEmpty()){
            return true
        }else{

            if(textEmail.isEmpty()){ text_email_recuperar_senha.setError("Preencha este campo com o seu email!") }
            Toast.makeText(applicationContext, "Preencha o campo de email por favor!", Toast.LENGTH_SHORT).show()
            return false
        }
    }

}