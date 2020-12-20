package com.example.sosrosas.Fragment

import android.app.Activity
import android.content.Context
import android.location.SettingInjectorService
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sosrosas.Interfaces.CadastroListeners
import com.example.sosrosas.Interfaces.ViolentometroListeners
import com.example.sosrosas.R
import kotlinx.android.synthetic.main.activity_informacao_page2_violentometro1.*

class ViolentometroFragment1 : Fragment(), View.OnClickListener {

    private lateinit var mViolentometroNext: ViolentometroListeners

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root : ViewGroup = inflater.inflate(R.layout.activity_informacao_page2_violentometro1, container, false) as ViewGroup
        return root
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            mViolentometroNext = activity as ViolentometroListeners
        } catch (e: ClassCastException) {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getListeners()
    }

    open fun setListener(fragment: Fragment){
        try {
            mViolentometroNext = fragment as ViolentometroListeners
        } catch (e: ClassCastException) {
        }
    }

    private fun getListeners(){
        botao_comecar_perguntas.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        var id = view.id

        if(id == R.id.botao_comecar_perguntas){

            mViolentometroNext.goPageAsk()
        }
    }
}