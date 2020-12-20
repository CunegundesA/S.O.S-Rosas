package com.example.sosrosas.View

import android.animation.ValueAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sosrosas.Interfaces.PrincipalListeners
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
                    mLoginUsuarioAtual.singOut()
                    mConfiguracaoNext.singOut()
                    mGoogleSignInClient.signOut()
                    LoginManager.getInstance().logOut()
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
            updatePasswordUserCurrent()
        }else if(id == R.id.save_new_fake_password){
            updateFakePassword()
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
                    val url = task.result.toString()

                    Glide.with(context!!).load(url).into(img_utilizador_configuracoes)

                }else{

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
                if(senhaAtual == usuarioAtual.senha) {
                    if(senhaNova != usuarioAtual.senhaFalsa) {
                        usuarioAtual.senha = senhaNova
                        mLoginUsuarioAtual.update(usuarioAtual)
                        mLoginUsuarioAtual.updatePassword(senhaNova)
                        text_current_password.text.clear()
                        text_new_password.text.clear()
                        text_confirm_new_password.text.clear()
                    }else{
                        Toast.makeText(context!!,"A nova senha está igual a senha falsa, por favor deixe-a diferente.",Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(context!!,"Senha atual incorreta",Toast.LENGTH_SHORT).show()
                }
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
                    if(senhaFalsaAtual != usuarioAtual.senha) {
                        usuarioAtual.senhaFalsa = senhaFalsaNova
                        mLoginUsuarioAtual.update(usuarioAtual)
                        mSharedPreferences.update(usuarioAtual.email, usuarioAtual.senhaFalsa)
                        text_current_fake_password.text.clear()
                        text_new_fake_password.text.clear()
                        text_confirm_new_fake_password.text.clear()
                    }else{
                        Toast.makeText(context!!,"A nova senha falsa está igual a senha atual, por favor deixe-a diferente.",Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(context!!,"Senha atual incorreta",Toast.LENGTH_SHORT).show()
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

    override fun onStart() {
        super.onStart()
        loadUserCurrent()
        downloadPhotoUser()
    }

}