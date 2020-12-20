package com.example.sosrosas.View

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sosrosas.R
import kotlinx.android.synthetic.main.activity_resultado_violentometro.*

class ResultadoViolentometroActivity(pontos: Int) : Fragment() {

    private val result = pontos

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root: ViewGroup =
            inflater.inflate(R.layout.activity_resultado_violentometro, container, false) as ViewGroup

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showResult()
    }

    private fun showResult(){

        if(result == 0){
            text_title_result.text = "Não foi indentificado um possível relacionamento abusivo."
            text_title_result.setTextColor(Color.parseColor("#16AE05"))
        } else if(result > 0 && result <= 6){
            img_violentometro.setImageResource(R.drawable.violentometro_offcima)
            text_title_result.text = "CUIDADO!\n A violência está presente"
            text_title_result.setTextColor(Color.parseColor("#EDD706"))
            text_result.text = """Procure ajuda psicológica
                |Caso a violência persista, não hesite em procurar ajuda!
                |Sugerimos que você se informe melhor sobre seus direitos e os tipos de violência em informações ou ligue para o 180 (Central de atendimento à mulher)
            """.trimMargin()

        }else if(result >= 7 && result <= 12){
            img_violentometro.setImageResource(R.drawable.violentometro_offmeio)
            text_title_result.text = "REAJA!\n Denuncie e peça ajuda"
            text_title_result.setTextColor(Color.parseColor("#ED6A06"))
            text_result.text = """Procure orientação policial e psicológica
                |Não se cale! Você não está sozinha(o)!
                |Sugerimos que você busque por ajuda, o mais rapido possivel e se informe sobre como proseguir em caso de violência em informações.
                |A violência precisa ser interropida.
                | Reaja!
            """.trimMargin()

        } else if(result > 12){
            img_violentometro.setImageResource(R.drawable.violentometro_offbaixo)
            text_title_result.text = "ALERTA!\n Sua vida esta em perigo"
            text_title_result.setTextColor(Color.parseColor("#C00612"))
            text_result.text = """Procure orientação policial urgente.
                |Não espere que algo de pior aconteça, sua vida está em perigo.
                |Não se cale ou hesite em realizar uma denúncia! Você tem leis ao seu favor, realize a denúncia e acabe com este ciclo de violência.
                | Não se cale!
            """.trimMargin()
        }

    }

}