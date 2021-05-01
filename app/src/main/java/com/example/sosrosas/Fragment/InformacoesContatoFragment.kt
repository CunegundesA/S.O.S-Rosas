package com.example.sosrosas.Fragment

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sosrosas.Interfaces.DenunciasListeners
import com.example.sosrosas.Model.ContatoAjuda
import com.example.sosrosas.Model.MaskEdit
import com.example.sosrosas.R
import com.example.sosrosas.ViewModel.ContatosSharedPreferences
import com.example.sosrosas.ViewModel.ContatosViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_contato_emergencia.*
import kotlinx.android.synthetic.main.activity_contato_emergencia.view.*
import java.lang.NullPointerException

class InformacoesContatoFragment(contato: ContatoAjuda) : Fragment(), View.OnClickListener {

    private var contatoAjuda = contato
    private lateinit var mDenunciasNext: DenunciasListeners
    private lateinit var mContatosViewModel: ContatosViewModel
    private lateinit var mContatoShared: ContatosSharedPreferences
    private val auth = FirebaseAuth.getInstance()
    private var cont = 0
    private var listContatos = ArrayList<ContatoAjuda>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: ViewGroup =
            inflater.inflate(R.layout.activity_contato_emergencia, container, false) as ViewGroup

        root.title_nome_informacao_contato.text = contatoAjuda.nome
        root.text_nome_informacao_contato.text.append(contatoAjuda.nome)
        root.text_numero_informacao_contato.text.append(contatoAjuda.celular)
        root.text_email_informacao_contato.text.append(contatoAjuda.emailContato)

        root.text_nome_informacao_contato.isEnabled = false
        root.text_numero_informacao_contato.isEnabled = false
        root.text_email_informacao_contato.isEnabled = false

        root.text_numero_informacao_contato.addTextChangedListener(
            MaskEdit().mask(
                root.text_numero_informacao_contato,
                MaskEdit().FORMAT_CELULAR
            )
        )

        mContatosViewModel = ContatosViewModel.getInstance()
        cont = 0

        return root
    }

    open fun setListener(fragment: Fragment) {
        try {
            mDenunciasNext = fragment as DenunciasListeners
        } catch (e: ClassCastException) {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContatoShared = ContatosSharedPreferences(context!!)
        try {
            listContatos =
                mContatoShared.getListContatos(auth.currentUser!!.email.toString()) as ArrayList<ContatoAjuda>
        } catch (e: NullPointerException) {
        }
        botao_editar_contato.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val id = view.id

        if (id == R.id.botao_editar_contato && cont == 0) {
            text_nome_informacao_contato.isEnabled = true
            text_numero_informacao_contato.isEnabled = true
            text_email_informacao_contato.isEnabled = true
            cont++
            botao_editar_contato.text = "Salvar"
        } else if (id == R.id.botao_editar_contato && cont == 1) {
            val contatoAuxiliar = ContatoAjuda()
            val nome = text_nome_informacao_contato.text.toString()
            val numero = text_numero_informacao_contato.text.toString()
            val email = text_email_informacao_contato.text.toString()

            contatoAuxiliar.nome = nome
            contatoAuxiliar.celular = numero
            contatoAuxiliar.emailContato = email
            contatoAuxiliar.nomeLowerCase = nome.toString().toLowerCase()

            Thread(
                Runnable {
                    mContatosViewModel.update(contatoAjuda.id, contatoAuxiliar)

                    if (listContatos.size > 0) {
                        for (x in 0 until listContatos.size) {
                            if (listContatos.get(x).id == contatoAjuda.id ||
                                listContatos.get(x).nome == contatoAjuda.nome &&
                                listContatos.get(x).celular == contatoAjuda.celular &&
                                listContatos.get(x).emailContato == contatoAjuda.emailContato
                            ) {

                                listContatos.set(x, contatoAuxiliar)
                                mContatoShared.saveListContatos(
                                    auth.currentUser!!.email.toString(),
                                    listContatos
                                )
                            }
                        }
                    }
                }).start()
            mDenunciasNext.goPageContatoExitInformacao()
        }
    }
}