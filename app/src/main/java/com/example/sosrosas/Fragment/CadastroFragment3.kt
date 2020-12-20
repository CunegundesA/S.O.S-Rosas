package com.example.sosrosas.Fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sosrosas.Interfaces.CadastroListeners
import com.example.sosrosas.R
import kotlinx.android.synthetic.main.activity_cadastro_page3.*

class CadastroFragment3 : Fragment(), View.OnClickListener{

    private lateinit var mCadastroNext: CadastroListeners

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root : ViewGroup = inflater.inflate(R.layout.activity_cadastro_page3, container, false) as ViewGroup

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
        button_avanca3.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        var id = view.id
        if(id == R.id.button_avanca3){
            if(validationNextPage()) {
                mCadastroNext.getNextPage()
            }
        }
    }

    private fun validationNextPage():Boolean{
        val codigo = text_codigo.text.toString()

        if(codigo.isEmpty()){
            Toast.makeText(context, "Preencha o campo de codigo, por favor!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


}