package com.example.sosrosas.View

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toDrawable
import com.example.sosrosas.Model.Usuario
import com.example.sosrosas.R
import com.example.sosrosas.ViewModel.CadastroViewModel
import com.example.sosrosas.ViewModel.ContatosViewModel
import com.example.sosrosas.ViewModel.SharedPreferences
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.account.WorkAccount.getClient
import com.google.android.gms.auth.api.phone.SmsRetriever.getClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignIn.getClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.safetynet.SafetyNet.getClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.box_termos_de_uso.*
import java.io.IOException
import java.net.URL
import java.util.*
import kotlin.math.log

class LoginActivit : AppCompatActivity(), View.OnClickListener {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var mGoogleSignInClient : GoogleSignInClient
    private var mUsuario = Usuario()
    private lateinit var mCadastro : CadastroViewModel
    private lateinit var fotoUsuario : ImageView
    private lateinit var callBackManager: CallbackManager
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(supportActionBar != null){
            supportActionBar!!.hide()
        }

        //Pega as permissões, caso não tenham
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.SEND_SMS, android.Manifest.permission.CALL_PHONE, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.READ_PHONE_STATE,android.Manifest.permission.INTERNET), 1)
        }

        mCadastro = CadastroViewModel()

        mSharedPreferences = SharedPreferences(this)

        servicesGoogle()
        serviceFacebook()
        getListeners()
    }

    //Função para pegar os eventos de botões
    private fun getListeners(){
        button_enter.setOnClickListener(this)
        text_cadastro.setOnClickListener(this)
        img_gmail.setOnClickListener(this)
        img_facebook.setOnClickListener(this)
        lose_password.setOnClickListener(this)
        text_termos.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val id = view.id

        if(id == R.id.button_enter) {
            if (getLoginValidation()) {
                val textEmail = edit_text_email_login.text.toString()
                val textPassword = edit_text_password_login.text.toString()

                if(mSharedPreferences.validationFakePasswordUser(textEmail, textPassword)){
                    val bundle = Bundle()
                    bundle.putString("email", textEmail)
                    val intent = Intent(applicationContext, CalendarioActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    finish()
                }else {
                    login()
                }
            }
        }else if (id == R.id.text_cadastro) {
                    startActivity(Intent(applicationContext, CadastroActivity::class.java))
        }else if(id == R.id.img_gmail){
            loginGoogle()
        }else if(id == R.id.img_facebook){
            loginFacebook()
        }else if(id == R.id.lose_password){
            startActivity(Intent(applicationContext, RecuperarSenhaActivity::class.java))
        }else if(id == R.id.text_termos){
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.box_termos_de_uso)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.button_ok.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    dialog.dismiss()
                }

            })
            dialog.show()
        }
    }

    //Função que valida o login
    private fun getLoginValidation() : Boolean{
        val textEmail = edit_text_email_login.text.toString()
        val textPassword = edit_text_password_login.text.toString()
        if(!textEmail.isEmpty() && !textPassword.isEmpty()){
            return true
        }else {
            Toast.makeText(applicationContext,"Por favor, preencha todos os campos!", Toast.LENGTH_LONG).show()
            return false
        }
    }

    // Login apartir de email e senha autenticados no Firebase
    private fun login(){
        val textEmail = edit_text_email_login.text.toString()
        val textPassword = edit_text_password_login.text.toString()
        auth.signInWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(this, OnCompleteListener {
            if(it.isSuccessful){
                startActivity(Intent(applicationContext, PrincipalActivity::class.java))
                finish()
            }else{
                Toast.makeText(applicationContext,"Email/Senha invalidos!", Toast.LENGTH_LONG).show()
            }
        } )
    }

    //------------------------------ Google SingIn ------------------------------------//

    //Connect service Google
    private fun servicesGoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this , gso)

    }

    //Login com o Google
    private fun loginGoogle(){
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if(account == null){
            val intent = mGoogleSignInClient.signInIntent
            startActivityForResult(intent, 555)
        }else{
            startActivity(Intent(applicationContext, PrincipalActivity::class.java))
            finish()
        }
    }

    //Cadastro no Firebase pelo Google
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    fotoUsuario = ImageView(this)
                    val imageUri = auth.currentUser!!.photoUrl.toString()
                    val loadImage = LoadImage()
                    loadImage.loadImage(fotoUsuario)
                    loadImage.execute(imageUri)
                    mCadastro.savePhotoUser(BitmapDrawable(resources, loadImage.get()))
                    mUsuario.nomeUsuario = auth.currentUser!!.displayName.toString()
                    mUsuario.email = auth.currentUser!!.email.toString()
                    mUsuario.celular = auth.currentUser!!.phoneNumber.toString()
                    mCadastro.save(mUsuario)
                    startActivity(Intent(applicationContext, PrincipalActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao autenticar conta do Google.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //---------------------- Facebook SingIn -------------------------------//

    //Faz a conexão com o Facebook
    private fun serviceFacebook(){
        callBackManager = CallbackManager.Factory.create();
        
        LoginManager.getInstance().registerCallback(callBackManager,
            object : FacebookCallback<LoginResult>{
                override fun onSuccess(result: LoginResult?) {
                    firebaseAuthWithFacebook(result!!.accessToken)
                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {

                }

            })
    }

    //Faz login pelo Facebook do Usuario
    private fun loginFacebook(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
    }

    //Cadastro no Firebase pelo Facebook
    private fun firebaseAuthWithFacebook(idToken: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(idToken.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    fotoUsuario = ImageView(this)
                    val imageUri = auth.currentUser!!.photoUrl.toString()
                    val loadImage = LoadImage()
                    loadImage.loadImage(fotoUsuario)
                    loadImage.execute(imageUri)
                    mCadastro.savePhotoUser(BitmapDrawable(resources, loadImage.get()))
                    mUsuario.nomeUsuario = auth.currentUser!!.displayName.toString()
                    mUsuario.email = auth.currentUser!!.email.toString()
                    mUsuario.celular = auth.currentUser!!.phoneNumber.toString()
                    mCadastro.save(mUsuario)
                    startActivity(Intent(applicationContext, PrincipalActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao autenticar conta do Facebook.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callBackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 555){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e: ApiException){}
        }
    }

    //Pega a imagem apartir de uma URL
    private class LoadImage() : AsyncTask<String,Void, Bitmap>(){
        private lateinit var imageView : ImageView

        fun loadImage(imgResult: ImageView){
            imageView = imgResult
        }

        override fun doInBackground(vararg strings: String?): Bitmap {
            val urlLink = strings[0]
            println(urlLink)
            var bitmap : Bitmap? = null
            try {
                val inputStream = URL(urlLink).openStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
            }catch (e : IOException){}

            return bitmap!!
        }

        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
        }

    }
}