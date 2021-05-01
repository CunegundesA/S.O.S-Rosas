package com.example.sosrosas.View

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.sosrosas.Fragment.AddContatoFragment
import com.example.sosrosas.Fragment.ContatoFragment
import com.example.sosrosas.Fragment.DenunciaFragment
import com.example.sosrosas.Fragment.InformacoesContatoFragment
import com.example.sosrosas.Interfaces.DenunciasListeners
import com.example.sosrosas.Interfaces.PrincipalListeners
import com.example.sosrosas.Model.ContatoAjuda
import com.example.sosrosas.R
import com.example.sosrosas.ViewModel.ContatosViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_denuncia.*
import kotlinx.android.synthetic.main.activity_denuncia.view.*
import kotlinx.android.synthetic.main.activity_denuncia_page1_addcontatos.*
import kotlinx.android.synthetic.main.activity_informacao.*
import java.util.ArrayList

class DenunciaActivity : Fragment(), DenunciasListeners, View.OnClickListener {

    private lateinit var pageContato: ContatoFragment
    private lateinit var pageAddContato: AddContatoFragment
    private lateinit var pageDenuncia: DenunciaFragment
    private lateinit var mContatosViewModel: ContatosViewModel
    private lateinit var mContatos: ContatoAjuda
    private lateinit var mConfiguracaoNext: PrincipalListeners
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var isBackFragmentActived = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContatosViewModel = ContatosViewModel()
        mContatos = ContatoAjuda()
        pageDenuncia = DenunciaFragment()
        pageAddContato = AddContatoFragment()
        pageContato = ContatoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root: ViewGroup =
            inflater.inflate(R.layout.activity_denuncia, container, false) as ViewGroup
       root.icon_megafone.typeface = Typeface.DEFAULT_BOLD

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
        pageContato.setListener(this)
        pageAddContato.setListener(this)
        childFragmentManager.beginTransaction().replace(R.id.frame_denuncia, pageDenuncia).commit()
        getListeners()
    }

    private fun getListeners() {
        icon_contatos.setOnClickListener(this)
        icon_megafone.setOnClickListener(this)
        img_configuracao_denuncia.setOnClickListener(this)
        img_utilizador_denuncia.setOnClickListener(this)
    }

    //Vai para a pagina de Add contato
    override fun goPageAddContato() {
        isBackFragmentActived = true
        childFragmentManager.beginTransaction().replace(R.id.frame_denuncia, pageAddContato).commit()
    }

    //Add um contato e vai para a pagina de contato
    override fun goPageContato() {
        isBackFragmentActived = false
        childFragmentManager.beginTransaction().replace(R.id.frame_denuncia, pageContato).commit()
    }

    // Sai da pagina de informacoes de contato e vai para a pagina de contato
    override fun goPageContatoExitInformacao() {
        isBackFragmentActived = false
        childFragmentManager.beginTransaction().replace(R.id.frame_denuncia, pageContato).commit()
    }

    //Vai para a pagina de informações do contato
    override fun goPageInformacoes(contatoAjuda: ContatoAjuda) {
        isBackFragmentActived = true
        val pageInformacoesContato = InformacoesContatoFragment(contatoAjuda)
        pageInformacoesContato.setListener(this)
        childFragmentManager.beginTransaction().replace(R.id.frame_denuncia, pageInformacoesContato)
            .commit()
    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.icon_contatos) {
            isBackFragmentActived = false
            childFragmentManager.beginTransaction().replace(R.id.frame_denuncia, pageContato)
                .commit()
           image_denuncia.setImageResource(R.drawable.contatosp)
            underline_contatos.visibility = View.VISIBLE
            underline_denuncia.visibility = View.INVISIBLE
            icon_contatos.typeface = Typeface.DEFAULT_BOLD
            icon_megafone.typeface = Typeface.DEFAULT
        } else if (id == R.id.icon_megafone) {
            isBackFragmentActived = false
            childFragmentManager.beginTransaction().replace(R.id.frame_denuncia, pageDenuncia)
                .commit()
            image_denuncia.setImageResource(R.drawable.icon_denunciap)
            underline_contatos.visibility = View.INVISIBLE
            underline_denuncia.visibility = View.VISIBLE
            icon_contatos.typeface = Typeface.DEFAULT
            icon_megafone.typeface = Typeface.DEFAULT_BOLD

        } else if (id == R.id.img_configuracao_denuncia) {
            isBackFragmentActived = false
            mConfiguracaoNext.goConfiguration()
        } else if (id == R.id.img_utilizador_denuncia){
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

                        Glide.with(context!!).load(url).into(img_utilizador_denuncia)
                    }catch(e : NullPointerException){}
                }
            }
        })
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

    fun goBackFragmentContatosPressed(){
        if(isBackFragmentActived){
            isBackFragmentActived = false
            childFragmentManager.beginTransaction().replace(R.id.frame_denuncia, pageContato).commit()
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