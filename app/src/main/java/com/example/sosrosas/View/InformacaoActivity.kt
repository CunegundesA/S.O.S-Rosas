package com.example.sosrosas.View

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sosrosas.Fragment.InformacoesFragment
import com.example.sosrosas.Fragment.ViolentometroFragment1
import com.example.sosrosas.Interfaces.PrincipalListeners
import com.example.sosrosas.Interfaces.ViolentometroListeners
import com.example.sosrosas.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_informacao.*
import kotlinx.android.synthetic.main.activity_informacao.view.*

class InformacaoActivity : Fragment(), View.OnClickListener, ViolentometroListeners{

    private var pageInformacoes = InformacoesFragment()
    private var pageVilometro1 = ViolentometroFragment1()
    private lateinit var mConfiguracaoNext : PrincipalListeners
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var isBackFragmentActived = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root: ViewGroup =
            inflater.inflate(R.layout.activity_informacao, container, false) as ViewGroup
            root.icon_info.typeface = Typeface.DEFAULT_BOLD
        return root
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            mConfiguracaoNext = activity as PrincipalListeners
        } catch (e: ClassCastException) {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageVilometro1.setListener(this)
        childFragmentManager.beginTransaction().replace(R.id.frame_informacao, pageInformacoes).commit()
        getListeners()
    }

    private fun getListeners() {
        icon_info.setOnClickListener(this)
        icon_violentometro.setOnClickListener(this)
        img_configuracao_informacao.setOnClickListener(this)
        img_utilizador_informacao.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val id = view.id

        if (id == R.id.icon_info) {
            isBackFragmentActived = false
            childFragmentManager.beginTransaction().replace(R.id.frame_informacao, pageInformacoes).commit()
            image_informacao.setImageResource(R.drawable.icon_infop)
            underline_informacoes.visibility = View.VISIBLE
            underline_violentometro.visibility = View.INVISIBLE
            icon_info.typeface = Typeface.DEFAULT_BOLD
            icon_violentometro.typeface = Typeface.DEFAULT
        } else if (id == R.id.icon_violentometro) {
            isBackFragmentActived = false
            image_informacao.setImageResource(R.drawable.icon_violentrometro)
            childFragmentManager.beginTransaction().replace(R.id.frame_informacao, pageVilometro1).commit()
            underline_informacoes.visibility = View.INVISIBLE
            underline_violentometro.visibility = View.VISIBLE
            icon_info.typeface = Typeface.DEFAULT
            icon_violentometro.typeface = Typeface.DEFAULT_BOLD
        } else if(id == R.id.img_configuracao_informacao){
            isBackFragmentActived = false
            mConfiguracaoNext.goConfiguration()
        } else if(id == R.id.img_utilizador_informacao){
            isBackFragmentActived = false
            mConfiguracaoNext.goPerfil()
        }
    }

    private fun downloadPhotoUser(){
        val user = auth.currentUser
        val uid = user!!.uid

        val dataStorage = storage.getReference().child("Upload").child(uid).child("fotouser" + uid +".jpg")
        dataStorage.downloadUrl.addOnCompleteListener(object : OnCompleteListener<Uri> {
            override fun onComplete(task: Task<Uri>) {

                if (task.isSuccessful) {
                    try {
                        val url = task.result.toString()

                        Glide.with(context!!).load(url).into(img_utilizador_informacao)
                    }catch (e : NullPointerException){}
                }else{

                }
            }
        })
    }

    override fun goPageAsk() {
        isBackFragmentActived = true
        val pagePerguntas = PerguntasActivity()
        pagePerguntas.setListener(this)
        childFragmentManager.beginTransaction().replace(R.id.frame_informacao, pagePerguntas).commit()
    }

    override fun goPage1() {
        isBackFragmentActived = false
        childFragmentManager.beginTransaction().replace(R.id.frame_informacao, pageInformacoes).commit()
    }

    override fun goPageResultado(pontos: Int) {
        isBackFragmentActived = true
        val pageResultado = ResultadoViolentometroActivity(pontos)
        childFragmentManager.beginTransaction().replace(R.id.frame_informacao, pageResultado).commit()
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

    fun goBackFragmentViolentometroPressed(){
        if(isBackFragmentActived){
            isBackFragmentActived = false
            childFragmentManager.beginTransaction().replace(R.id.frame_informacao, pageVilometro1).commit()
        }
    }

    fun isBackFragmentActived() : Boolean{
        return isBackFragmentActived
    }

    override fun onStart() {
        super.onStart()
        if(verificationConnectionWithInternet()) {
            try {
                downloadPhotoUser()
            }catch(e : NullPointerException){}
        }
    }

}


