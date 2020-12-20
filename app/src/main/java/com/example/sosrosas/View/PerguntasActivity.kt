package com.example.sosrosas.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import com.example.sosrosas.Interfaces.ViolentometroListeners
import com.example.sosrosas.R
import kotlinx.android.synthetic.main.activity_perguntas.*

class PerguntasActivity : Fragment(), View.OnClickListener {

    private lateinit var mViolentometroNext: ViolentometroListeners

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root: ViewGroup =
            inflater.inflate(R.layout.activity_perguntas, container, false) as ViewGroup

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getListeners()
    }

    open fun setListener(fragment: Fragment) {
        try {
            mViolentometroNext = fragment as ViolentometroListeners
        } catch (e: ClassCastException) {
        }
    }

    private fun getListeners() {
        enviar_resposta.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val id = view.id

        if (id == R.id.enviar_resposta) {
            if (getValidation()) {
                mViolentometroNext.goPageResultado(getResultQuestions())
            }
        }
    }

    private fun getResultQuestions(): Int {
        var pontos = 0

        if (sim1.isChecked) {
            pontos = pontos + 1
        }

        if (sim2.isChecked) {
            pontos = pontos + 1
        }

        if (sim3.isChecked) {
            pontos = pontos + 2
        }

        if (sim4.isChecked) {
            pontos = pontos + 2
        }

        if (sim5.isChecked) {
            pontos = pontos + 2
        }

        if (sim6.isChecked) {
            pontos = pontos + 2
        }

        if (sim7.isChecked) {
            pontos = pontos + 1
        }

        if (sim8.isChecked) {
            pontos = pontos + 2
        }

        if (sim9.isChecked) {
            pontos = pontos + 3
        }

        if (sim10.isChecked) {
            pontos = pontos + 3
        }

        if (sim11.isChecked) {
            pontos = pontos + 3
        }

        if (sim12.isChecked) {
            pontos = pontos + 2
        }

        if (sim13.isChecked) {
            pontos = pontos + 3
        }

        if (sim14.isChecked) {
            pontos = pontos + 2
        }

        if (sim15.isChecked) {
            pontos = pontos + 3
        }

        if (sim16.isChecked) {
            pontos = pontos + 2
        }

        if (sim17.isChecked) {
            pontos = pontos + 1
        }

        if (sim18.isChecked) {
            pontos = pontos + 2
        }

        if (sim19.isChecked) {
            pontos = pontos + 3
        }

        if (sim20.isChecked) {
            pontos = pontos + 3
        }

        if (sim21.isChecked) {
            pontos = pontos + 3
        }

        if (sim22.isChecked) {
            pontos = pontos + 2
        }

        if (sim23.isChecked) {
            pontos = pontos + 2
        }

        if (sim24.isChecked) {
            pontos = pontos + 2
        }

        if (sim25.isChecked) {
            pontos = pontos + 3
        }

        return pontos
    }

    private fun getValidation(): Boolean {
        if (!sim1.isChecked && !nao1.isChecked ||
            !sim2.isChecked && !nao2.isChecked ||
            !sim3.isChecked && !nao3.isChecked ||
            !sim4.isChecked && !nao4.isChecked ||
            !sim5.isChecked && !nao5.isChecked ||
            !sim6.isChecked && !nao6.isChecked ||
            !sim7.isChecked && !nao7.isChecked ||
            !sim8.isChecked && !nao8.isChecked ||
            !sim9.isChecked && !nao9.isChecked ||
            !sim10.isChecked && !nao10.isChecked ||
            !sim11.isChecked && !nao11.isChecked ||
            !sim12.isChecked && !nao12.isChecked ||
            !sim13.isChecked && !nao13.isChecked ||
            !sim14.isChecked && !nao14.isChecked ||
            !sim15.isChecked && !nao15.isChecked ||
            !sim16.isChecked && !nao16.isChecked ||
            !sim17.isChecked && !nao17.isChecked ||
            !sim18.isChecked && !nao18.isChecked ||
            !sim19.isChecked && !nao19.isChecked ||
            !sim20.isChecked && !nao20.isChecked ||
            !sim21.isChecked && !nao21.isChecked ||
            !sim22.isChecked && !nao22.isChecked ||
            !sim23.isChecked && !nao23.isChecked ||
            !sim24.isChecked && !nao24.isChecked ||
            !sim25.isChecked && !nao25.isChecked) {
            Toast.makeText(context, "Por favor, responda todas as perguntas.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

}