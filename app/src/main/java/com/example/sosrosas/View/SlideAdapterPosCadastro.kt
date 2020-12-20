package com.example.sosrosas.View

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.getSystemService
import androidx.viewpager.widget.PagerAdapter
import com.example.sosrosas.R
import kotlinx.android.synthetic.main.activity_informacao.view.*
import kotlinx.android.synthetic.main.viewpager_slide_pos_cadastro.view.*
import java.util.*
import kotlin.coroutines.coroutineContext

class SlideAdapterPosCadastro : PagerAdapter {

    private val context: Context
    private val images = arrayOf(
        R.drawable.logo_sem_nome,
        R.drawable.icon_home_red,
        R.drawable.icon_info_red,
        R.drawable.botao_panico,
        R.drawable.icon_busca_red,
        R.drawable.icon_denuncia_red)

    private val titles = arrayOf(
        "Seja Bem-Vinda ao S.O.S Rosas!",
        "MENU",
        "INFORMAÇÕES",
        "BOTÃO DO PÂNICO",
        "BUSCA",
        "DENÚNCIA"
    )

    private val text = arrayOf(
        """Neste aplicativo você irá encontrar informações sobre os tipos de violência gênero, e as etapas a seguir para denunciá-lo. 
            |Nós convidamos você a compartilhar esse aplicativo com as mulheres ao seu redor. Leia as etapas a seguir para aprender a usar este aplicativo.""".trimMargin(),
        "Neste ícone de menu você encontrará as principais notícias no seu feed, também poderá acessar seu perfil e as configurações do aplicativo.",
        "Neste ícone de informções você encontará informações sobre os tipos de violência contra a mulher, Mitos vs Realidade, etc. Aqui também você encontrará nosso Violentômetro onde algumas perguntas serão feitas para indentificar relações em condições de violência.",
        "Neste botão do pânico ao clicar pela primeira vez, vai ser solicitado que você adicione seus contatos de emergência, após ser adicionado os contatos o botão ao ser clicado enviará um S.O.S(um pedido de ajuda) aos contatos adicionados e ligará para o 190.",
        "Neste ícone de busca você poderá realizar buscas por departamentos de polícia e hospitais masi próximos.",
        "Neste ícone de denúncia você encontrará seus contatos de emergência e poderá realiar denúncias.")

    constructor(context: Context){
        this.context = context
    }

    override fun getCount(): Int {
        return titles.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val root:View = layoutInflater.inflate(R.layout.viewpager_slide_pos_cadastro, null)

        root.image_slide.setImageResource(images.get(position))
        root.title_slide.text = titles.get(position)
        root.text_slide.text = text.get(position)

        container.addView(root)

        return root
    }

    override fun destroyItem(container: ViewGroup, position: Int, o: Any) {
        container.removeView(o as View)
    }
}