package com.example.sosrosas.Fragment

import android.app.Activity
import android.app.Dialog
import android.app.DownloadManager
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sosrosas.Interfaces.CadastroListeners
import com.example.sosrosas.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_cadastro_page2.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.box_main_violencia_fisica.*
import kotlinx.android.synthetic.main.box_termos_de_uso.*
import java.io.File

class CadastroFragment2 : Fragment(), View.OnClickListener {

    private lateinit var mCadastroNext: CadastroListeners

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root : ViewGroup = inflater.inflate(R.layout.activity_cadastro_page2, container, false) as ViewGroup

        return root
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            mCadastroNext = activity as CadastroListeners
        } catch (e: ClassCastException) {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getListeners()
    }


    private fun getListeners(){
        button_avanca2.setOnClickListener(this)
        text_termos_uso.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val id = view.id
        if(id == R.id.button_avanca2){
            if(validation()) {
                mCadastroNext.goPage3()
            }
        }else if(id == R.id.text_termos_uso){
            val dialog = Dialog(context!!)
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

    private fun validation():Boolean{
        val cep = text_cep.text.toString()
        val endereco = text_endereco.text.toString()
        val bairro = text_bairro.text.toString()
        val numero = text_numero.text.toString()
        val cidade = text_cidade.text.toString()
        val estado = text_estado.text.toString()
        val termos = check_termos.isChecked

        if(cep != "" && endereco != "" && bairro != "" && numero != "" && cidade != "" && estado != ""){

            if(termos) {
                return true
            }

            Toast.makeText(context, "Aceite os termos de uso para prosseguir", Toast.LENGTH_SHORT).show()
            return false
        }

        if(cep.length < 8){
            Toast.makeText(context, "CEP invalido!", Toast.LENGTH_SHORT).show()
            return false
        }

        Toast.makeText(context, "Preencha todos os campos, por favor!", Toast.LENGTH_SHORT).show()
        return false
    }


}