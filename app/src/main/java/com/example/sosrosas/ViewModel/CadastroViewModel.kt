package com.example.sosrosas.ViewModel


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.icu.text.NumberFormat.Field.SIGN
import android.transition.Fade.IN
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.sosrosas.Model.Article
import com.example.sosrosas.Model.ErroNotification
import com.example.sosrosas.Model.Usuario
import com.example.sosrosas.R
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.box_erro_equal_password.*
import kotlinx.android.synthetic.main.box_main_violencia_fisica.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.security.spec.PSSParameterSpec.DEFAULT
import java.util.*

class CadastroViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("BD_SOSRosas")
    private val storage = FirebaseStorage.getInstance()

    companion object{
        fun getIstance() = CadastroViewModel()
    }

    //Cria um login de usuario
    fun createLoginUser(usuario: Usuario, passwordUser: String){
        auth.createUserWithEmailAndPassword(usuario.email, passwordUser)
    }

    //Desloga um usuario cadastrado por email e senha
    fun singOut(){
        auth.signOut()
    }

    // Salva um novo usuario
    fun save(usuario: Usuario){
        val user = auth.currentUser
        val uid = user!!.uid
        myRef.child(uid).child("Usuario").setValue(usuario)
    }

    //Salva a foto de perfil do  usuario
    fun savePhotoUser(bitmapDrawable: BitmapDrawable){
        val user = auth.currentUser
        val uid = user!!.uid
        val storageReference = storage.getReference().child("Upload").child(uid)
        val arquivoReference = storageReference.child("fotouser" + uid +".jpg")
        val bitmap = bitmapDrawable.bitmap
        val byte = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,byte)

        arquivoReference.putBytes(byte.toByteArray())

    }

    //Remove a foto de perfil do usuario
    fun removePhotoUser(){
        val user = auth.currentUser
        val uid = user!!.uid
        val storageReference = storage.getReference().child("Upload").child(uid).child("fotouser" + uid +".jpg")

        storageReference.delete()
    }

    //Atauliza as informaçõies do usuario.
    fun update(usuario: Usuario){
        val user = auth.currentUser
        val uid = user?.uid
        val data = database.getReference().child("BD_SOSRosas").child(uid.toString()).child("Usuario")

        data.setValue(usuario)
    }

    //Atualiza a senha do usuario
    fun updatePassword(senha: String){
        auth.currentUser!!.updatePassword(senha)
    }

    //Pega o usuario atual
    fun getUsuarioAtual() : Usuario{
        var usuarioAtual = Usuario()
        val user = auth.currentUser
        val uid = user!!.uid
        val data = database.getReference().child("BD_SOSRosas").child(uid.toString()).child("Usuario")

        data.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                usuarioAtual = snapshot.getValue<Usuario>() as Usuario
                println("The user current is ${usuarioAtual.nomeUsuario}")
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        println("The last user is ${usuarioAtual.nomeUsuario}")
        return usuarioAtual
    }

}