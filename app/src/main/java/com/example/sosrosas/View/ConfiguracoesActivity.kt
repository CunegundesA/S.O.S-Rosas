package com.example.sosrosas.View

import android.animation.ValueAnimator
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sosrosas.Fragment.AddContatoFragment
import com.example.sosrosas.Interfaces.PrincipalListeners
import com.example.sosrosas.Model.ContatoAjuda
import com.example.sosrosas.Model.Usuario
import com.example.sosrosas.R
import com.example.sosrosas.ViewModel.CadastroViewModel
import com.example.sosrosas.ViewModel.SharedPreferences
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_cadastro_page1.*
import kotlinx.android.synthetic.main.activity_configuracoes.*
import kotlinx.android.synthetic.main.activity_informacao_page1_informacoes.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.box_erro_conection_internet.*
import kotlinx.android.synthetic.main.box_erro_equal_password.*
import kotlinx.android.synthetic.main.box_sucess_send_email_password.*
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class ConfiguracoesActivity : Fragment(), View.OnClickListener {

    private var usuarioAtual = Usuario()
    private lateinit var mConfiguracaoNext : PrincipalListeners
    private lateinit var mLoginUsuarioAtual : CadastroViewModel
    private lateinit var mGoogleSignInClient : GoogleSignInClient
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private lateinit var mSharedPreferences: SharedPreferences
    private var isOpenBoxPassword  = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root: ViewGroup =
            inflater.inflate(R.layout.activity_configuracoes, container, false) as ViewGroup

            mLoginUsuarioAtual = CadastroViewModel()
            servicesGoogle()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSharedPreferences = SharedPreferences(context!!)
        verificateLoginWhitGoogleorFacebook()
        getListeners()
    }

    //Pega todos os ouvintes
    private fun getListeners(){
        img_utilizador_configuracoes.setOnClickListener(this)
        sair_conta.setOnClickListener(this)
        box_passwords.setOnClickListener(this)
        save_new_password.setOnClickListener(this)
        save_new_fake_password.setOnClickListener(this)
        lose_fake_password.setOnClickListener(this)
    }

    //Faz conexão com serviços do Google
    private fun servicesGoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(context!! , gso)

    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            mConfiguracaoNext = activity as PrincipalListeners
        } catch (e: ClassCastException) {
        }
    }

    override fun onClick(view: View) {
        val id = view.id

        if(id == R.id.img_utilizador_configuracoes){
            mConfiguracaoNext.goPerfil()
        }else if( id == R.id.sair_conta){

            val alert = AlertDialog.Builder(context)
            alert.setTitle("Deslogar da conta?")
            alert.setMessage("Deseja deslogar da conta atual?")
            alert.setPositiveButton("Sim", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {

                    if(usuarioAtual.senhaFalsa != "" && usuarioAtual.senhaFalsa != null) {
                        auth.signInWithEmailAndPassword(usuarioAtual.email, usuarioAtual.senhaFalsa)
                            .addOnCompleteListener(context as Activity, OnCompleteListener {
                                if (it.isSuccessful) {
                                    val dialog = Dialog(context!!)
                                    dialog.setContentView(R.layout.box_erro_equal_password)
                                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                    dialog.button_ok_error_pass.setOnClickListener(object :
                                        View.OnClickListener {
                                        override fun onClick(p0: View?) {
                                            dialog.dismiss()
                                        }
                                    })
                                    dialog.show()
                                } else {
                                    mLoginUsuarioAtual.singOut()
                                    mConfiguracaoNext.singOut()
                                    mGoogleSignInClient.signOut()
                                    LoginManager.getInstance().logOut()
                                }
                            })
                    }else{
                        mLoginUsuarioAtual.singOut()
                        mConfiguracaoNext.singOut()
                        mGoogleSignInClient.signOut()
                        LoginManager.getInstance().logOut()
                    }
                }
            })
            alert.setNegativeButton("Cancelar", null)
            alert.show()

        }else if( id == R.id.box_passwords){
            if (!isOpenBoxPassword) {
                val valueAnimator =
                    ValueAnimator.ofInt(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    box_senhas.getLayoutParams().height = value
                    box_senhas.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxPassword = true
            } else {
                val valueAnimator =
                    ValueAnimator.ofInt(LinearLayout.LayoutParams.WRAP_CONTENT, 0)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    box_senhas.getLayoutParams().height = value
                    box_senhas.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxPassword = false
            }
        } else if(id == R.id.save_new_password){
            if(verificationConnectionWithInternet()) {
                updatePasswordUserCurrent()
            }else{
                val dialog = Dialog(context!!)
                dialog.setContentView(R.layout.box_erro_conection_internet)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.text_msm_erro3_internet.visibility = View.VISIBLE
                dialog.button_ok_error_conection_internet.setOnClickListener(object: View.OnClickListener{
                    override fun onClick(p0: View?) {
                        dialog.dismiss()
                    }

                })
                dialog.show()
            }
        }else if(id == R.id.save_new_fake_password){
            if(verificationConnectionWithInternet()) {
                updateFakePassword()
            }else{
                val dialog = Dialog(context!!)
                dialog.setContentView(R.layout.box_erro_conection_internet)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.text_msm_erro3_internet.visibility = View.VISIBLE
                dialog.button_ok_error_conection_internet.setOnClickListener(object: View.OnClickListener{
                    override fun onClick(p0: View?) {
                        dialog.dismiss()
                    }

                })
                dialog.show()
            }
        }else if(id == R.id.lose_fake_password){
            val alert = AlertDialog.Builder(context)
            alert.setTitle("Esqueceu sua senha falsa?")
            alert.setMessage("Se sim será enviado uma mensagem para o email da sua conta do aplicativo.")
            alert.setPositiveButton("Sim", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {

                    if (verificationConnectionWithInternet()){
                        sendEmail(usuarioAtual)
                        val dialog = Dialog(context!!)
                        dialog.setContentView(R.layout.box_sucess_send_email_password)
                        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialog.text_email_password.text =
                            "Uma mensagem com a sua senha falsa foi enviada para o seu email. Por favor verifique sua caixa de email, caso a mensagem não tenha chegado tente o processo novamente."
                        dialog.button_ok_send_email.setOnClickListener(object :
                            View.OnClickListener {
                            override fun onClick(p0: View?) {
                                dialog.dismiss()
                            }

                        })
                        dialog.show()
                    }else{
                        val dialog = Dialog(context!!)
                        dialog.setContentView(R.layout.box_erro_conection_internet)
                        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialog.text_msm_erro1_internet.visibility = View.VISIBLE
                        dialog.button_ok_error_conection_internet.setOnClickListener(object: View.OnClickListener{
                            override fun onClick(p0: View?) {
                                dialog.dismiss()
                            }

                        })
                        dialog.show()
                    }
                }
            })
            alert.setNegativeButton("Cancelar", null)
            alert.show()
        }
    }

    //Faz o download da foto do perfil do usuario
    private fun downloadPhotoUser(){
        val user = auth.currentUser
        val uid = user!!.uid

        val dataStorage = storage.getReference().child("Upload").child(uid).child("fotouser" + uid +".jpg")
        dataStorage.downloadUrl.addOnCompleteListener(object : OnCompleteListener<Uri> {
            override fun onComplete(task: Task<Uri>) {
                if (task.isSuccessful) {
                    try {
                        val url = task.result.toString()
                        Glide.with(context!!).load(url).into(img_utilizador_configuracoes)
                    }catch (e : NullPointerException){}
                }
            }
        })
    }

    // Faz a atualização da senha do usario atual
    private fun updatePasswordUserCurrent(){
        val senhaAtual = text_current_password.text.toString()
        val senhaNova = text_new_password.text.toString()
        val confirmSenha = text_confirm_new_password.text.toString()

        if(!senhaAtual.isEmpty() && !senhaNova.isEmpty() && !confirmSenha.isEmpty()){
            if(senhaNova == confirmSenha){
                auth.signInWithEmailAndPassword(usuarioAtual.email, senhaAtual)
                    .addOnCompleteListener(context!! as Activity, OnCompleteListener {
                        if(it.isSuccessful) {
                            if(senhaNova != usuarioAtual.senhaFalsa) {
                                Thread(
                                    Runnable {
                                        mLoginUsuarioAtual.update(usuarioAtual)
                                        mLoginUsuarioAtual.updatePassword(senhaNova)
                                    }).start()
                                text_current_password.text.clear()
                                text_new_password.text.clear()
                                text_confirm_new_password.text.clear()
                            }else{
                                Toast.makeText(context!!,"A nova senha está igual a senha falsa, por favor deixe-a diferente.",Toast.LENGTH_LONG).show()
                            }

                        }else{
                            Toast.makeText(context!!,"Senha atual incorreta",Toast.LENGTH_SHORT).show()
                        }
                    })
            }else{
                Toast.makeText(context!!, "Erro na confirmação de senha, verifique se escreveu a confirmação de senha corretamente", Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(context!!, "Preencha todos os campos por favor!", Toast.LENGTH_SHORT).show()
        }
    }

    //Faz a ataulização da senha falsa
    private fun updateFakePassword(){
        val senhaFalsaAtual = text_current_fake_password.text.toString()
        val senhaFalsaNova = text_new_fake_password.text.toString()
        val confirmSenhaFalsa = text_confirm_new_fake_password.text.toString()

        if(!senhaFalsaAtual.isEmpty() && !senhaFalsaNova.isEmpty() && !confirmSenhaFalsa.isEmpty()){
            if(senhaFalsaNova == confirmSenhaFalsa){
                if(senhaFalsaAtual == usuarioAtual.senhaFalsa){
                    auth.signInWithEmailAndPassword(usuarioAtual.email, senhaFalsaNova)
                        .addOnCompleteListener(context!! as Activity, OnCompleteListener {
                            if(it.isSuccessful) {
                                Toast.makeText(context!!,"A nova senha falsa está igual a senha atual, por favor deixe-a diferente.",Toast.LENGTH_LONG).show()
                            }else{
                                usuarioAtual.senhaFalsa = senhaFalsaNova
                                Thread(
                                    Runnable {
                                        mLoginUsuarioAtual.update(usuarioAtual)
                                        mSharedPreferences.update(
                                            usuarioAtual.email,
                                            usuarioAtual.senhaFalsa
                                        )
                                    }).start()
                                text_current_fake_password.text.clear()
                                text_new_fake_password.text.clear()
                                text_confirm_new_fake_password.text.clear()
                            }
                        })
                }else{
                    Toast.makeText(context!!,"Senha falsa atual incorreta",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context!!, "Erro na confirmação de senha, verifique se escreveu a confirmação de senha corretamente", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(context!!, "Preencha todos os campos por favor!", Toast.LENGTH_SHORT).show()
        }
    }

    // Carrega os dados do usuario atual
    private fun loadUserCurrent(){
        val user = auth.currentUser
        val uid = user!!.uid
        val data = database.getReference().child("BD_SOSRosas").child(uid.toString()).child("Usuario")

        data.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                if(snapshot.getValue() != null) {
                    usuarioAtual = snapshot.getValue<Usuario>() as Usuario
                }
                if(usuarioAtual != null){
                    try {
                        text_nome_usuario.text = usuarioAtual!!.nomeUsuario
                    }catch (e:NullPointerException){
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun verificateLoginWhitGoogleorFacebook(){
        val accountGoogle = GoogleSignIn.getLastSignedInAccount(context!!)
        val accountFacebook = AccessToken.getCurrentAccessToken()

        if(accountGoogle != null || accountFacebook != null){
            box_passwords.visibility = View.GONE
        }

    }

    private fun verificationConnectionWithInternet() : Boolean{
        val conectInternet = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conectInternet.activeNetworkInfo

        if (netInfo != null && netInfo.isConnected()) {
            return true
        }else{
            return false
        }
    }

    private fun sendEmail(user: Usuario) {
        val props = Properties()
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.starttls.enable", "true")
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com")
        props.put("mail.smtp.host", "smtp.gmail.com")
        props.put("mail.smtp.port", "587")

        val session: Session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("s.o.srosassuporte@gmail.com", "juremacunegundes2020")
            }
        })

        val msg: Message = MimeMessage(session)
        msg.setFrom(InternetAddress("s.o.srosassuporte@gmail.com", false))
        msg.setHeader("Content-Type", "text/html")
        msg.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(user.email))
        msg.setSubject("Senha Falsa!")

        val html = """<!doctype html>
<html lang="pt-br">
    <head>
        <meta charset="utf-8">
        <meta name="author" content="Affonso Ruiz, Aline Alves">
        <meta name="description" content="Email de alteração de senha">
        <meta name="keywords" content="Project Stages">
        
<style>
    
    .header-email{
        display: block;
        color: white;
        background-color: #C00612;
        padding: 5px;
        padding-left: 20px;
        padding-right: 20px;
        padding-bottom: 10px;
        margin: 0px;
        font-family: Arial, sans-serif,serif;
        text-align: justify;
    }
    
    .header-email h3{
        text-align: center
    }
    
    .img-email{
        width: 100px;
        height: 100px;
        display: block;
        text-align: center;
        padding: 10px;
        margin: auto;
    }    
    
    .body-email{
        width: 100%;
        height: 100px;
        text-align: center;
        display: block;
    }
    
    .title-body-email{
        color: #C00612;
    }
    
    .text-body-email{
        font-size: 18px;
        font-style: oblique;
        color:black;
    }
    
</style>
    </head>
    <body>
       
        <div class="header-email">
            <img class="img-email" src="https://i.pinimg.com/originals/8b/4a/ff/8b4aff4893be75e9969690d29bd7ab56.png"/>
            
            <h3>Senha Falsa</h3>
            
            <p>Caro usuário ${user.nomeUsuario}, a sua senha falsa segue logo abaixo, mas aconselhamos que você altere a senha falsa no aplicativo para evitar qualquer problema de segurança. Caso não tenha sido você quem solicitou este email, aconselhamos que você altere as suas senhas do aplicativo, tanto a senha de Login quanto a sua senha falsa.</p>
            
        </div>
        
        <div class="body-email">
            
            <p class="title-body-email">Senha Falsa:</p>
            <p class="text-body-email">${user.senhaFalsa}</p>
            
        </div>
        
        <p>Atenciosamente,</p>
        <p>S.O.S Rosas</p>
        
    </body>
</html>"""

        msg.setContent(html, "text/html")
        msg.setSentDate(Date())
        ConfiguracoesActivity.sendMenseger().execute(msg)
    }

    private class sendMenseger() : AsyncTask<Message, String, String>() {

        override fun doInBackground(vararg message: Message?): String {
            Transport.send(message[0])
            return "Sucess"
        }

    }

    override fun onStart() {
        super.onStart()
        if(verificationConnectionWithInternet()) {
            try {
                loadUserCurrent()
                downloadPhotoUser()
            }catch(e : NullPointerException){}
        }
    }

}