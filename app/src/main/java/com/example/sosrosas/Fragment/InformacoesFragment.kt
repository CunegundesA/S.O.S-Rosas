package com.example.sosrosas.Fragment

import android.animation.ValueAnimator
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.sosrosas.R
import kotlinx.android.synthetic.main.activity_informacao_page1_informacoes.*
import kotlinx.android.synthetic.main.activity_informacao_page1_informacoes.view.*


class InformacoesFragment() : Fragment(),View.OnClickListener {

    private var isOpenBoxViolenciaFisica = false
    private var isOpenBoxViolenciaPsicologica= false
    private var isOpenBoxViolenciaSexual = false
    private var isOpenBoxViolenciaPatrimonial = false
    private var isOpenBoxViolenciaMoral = false
    private var isOpenBoxMito01 = false
    private var isOpenBoxMito02 = false
    private var isOpenBoxMito03 = false
    private var isOpenBoxMito04 = false
    private var isOpenBoxMito05 = false
    private var isOpenBoxMito06 = false
    private var isOpenBoxMito07 = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root : ViewGroup = inflater.inflate(R.layout.activity_informacao_page1_informacoes,
            container,
            false) as ViewGroup


        root.text_ciclo_violencia.setText(Html.fromHtml(getString(R.string.text_ciclo_violencia)))
        root.text_informacoes_denuncia.setText(Html.fromHtml(getString(R.string.text_denuncia)))


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getListeners()
    }

    private fun getListeners(){
        box_violencia_fisica.setOnClickListener(this)
        box_violencia_psicologica.setOnClickListener(this)
        box_violencia_sexual.setOnClickListener(this)
        box_violencia_patrimonial.setOnClickListener(this)
        box_violencia_moral.setOnClickListener(this)
        box_mito01.setOnClickListener(this)
        box_mito02.setOnClickListener(this)
        box_mito03.setOnClickListener(this)
        box_mito04.setOnClickListener(this)
        box_mito05.setOnClickListener(this)
        box_mito06.setOnClickListener(this)
        box_mito07.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val id = view.id

        //Violencia fisica
        if(id == R.id.box_violencia_fisica){
            if(!isOpenBoxViolenciaFisica) {

                seta_violencia_fisica.rotationX = 180f

                val valueAnimator = ValueAnimator.ofInt(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_violencia_fisica.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_violencia_fisica.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxViolenciaFisica = true
            }else{
                seta_violencia_fisica.rotationX = 360f

                val valueAnimator = ValueAnimator.ofInt(LinearLayout.LayoutParams.WRAP_CONTENT, 0)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_violencia_fisica.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_violencia_fisica.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxViolenciaFisica = false
            }
        }
        //Violencia psicologica
        else if(id == R.id.box_violencia_psicologica){

            if(!isOpenBoxViolenciaPsicologica){

                seta_violencia_psicologica.rotationX = 180f

                val valueAnimator = ValueAnimator.ofInt(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_violencia_psicologica.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_violencia_psicologica.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxViolenciaPsicologica = true
            }else{
                seta_violencia_psicologica.rotationX = 360f

                val valueAnimator = ValueAnimator.ofInt(LinearLayout.LayoutParams.WRAP_CONTENT, 0)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_violencia_psicologica.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_violencia_psicologica.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxViolenciaPsicologica = false

            }
        }
        //Violencia sexual
        else if(id == R.id.box_violencia_sexual){
            if(!isOpenBoxViolenciaSexual) {

                seta_violencia_sexual.rotationX = 180f

                val valueAnimator = ValueAnimator.ofInt(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_violencia_sexual.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_violencia_sexual.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxViolenciaSexual = true
            }else{

                seta_violencia_sexual.rotationX = 360f

                val valueAnimator = ValueAnimator.ofInt(LinearLayout.LayoutParams.WRAP_CONTENT, 0)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_violencia_sexual.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_violencia_sexual.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxViolenciaSexual = false
            }
        }
        //Violencia patrimonial
        else if(id == R.id.box_violencia_patrimonial) {

            if (!isOpenBoxViolenciaPatrimonial) {

                seta_violencia_patrimonial.rotationX = 180f

                val valueAnimator =
                    ValueAnimator.ofInt(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_violencia_patrimonial.getLayoutParams().height =
                        value * animation.animatedFraction.toInt()
                    text_box_violencia_patrimonial.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxViolenciaPatrimonial = true
            } else {

                seta_violencia_patrimonial.rotationX = 360f

                val valueAnimator =
                    ValueAnimator.ofInt(LinearLayout.LayoutParams.WRAP_CONTENT, 0)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_violencia_patrimonial.getLayoutParams().height =
                        value * animation.animatedFraction.toInt()
                    text_box_violencia_patrimonial.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxViolenciaPatrimonial = false
            }
        }
        //Violencia moral
        else if(id == R.id.box_violencia_moral) {

            if (!isOpenBoxViolenciaMoral) {

                seta_violencia_moral.rotationX = 180f

                val valueAnimator =
                    ValueAnimator.ofInt(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_violencia_moral.getLayoutParams().height =
                        value * animation.animatedFraction.toInt()
                    text_box_violencia_moral.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxViolenciaMoral = true
            } else {

                seta_violencia_moral.rotationX = 360f

                val valueAnimator =
                    ValueAnimator.ofInt(LinearLayout.LayoutParams.WRAP_CONTENT, 0)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_violencia_moral.getLayoutParams().height =
                        value * animation.animatedFraction.toInt()
                    text_box_violencia_moral.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxViolenciaMoral = false
            }
        }
        //Mito 01
        else if(id == R.id.box_mito01) {

            if (!isOpenBoxMito01) {

                seta_mito01.rotationX = 180f

                val valueAnimator =
                    ValueAnimator.ofInt(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_mito01.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_mito01.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxMito01= true
            } else {

                seta_mito01.rotationX = 360f

                val valueAnimator =
                    ValueAnimator.ofInt(LinearLayout.LayoutParams.WRAP_CONTENT, 0)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_mito01.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_mito01.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxMito01 = false
            }
        }
        //Mito 02
        else if(id == R.id.box_mito02) {

            if (!isOpenBoxMito02) {

                seta_mito02.rotationX = 180f

                val valueAnimator =
                    ValueAnimator.ofInt(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_mito02.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_mito02.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxMito02= true
            } else {

                seta_mito02.rotationX = 360f

                val valueAnimator =
                    ValueAnimator.ofInt(LinearLayout.LayoutParams.WRAP_CONTENT, 0)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_mito02.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_mito02.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxMito02 = false
            }
        }
        //Mito 03
        else if(id == R.id.box_mito03) {

            if (!isOpenBoxMito03) {

                seta_mito03.rotationX = 180f

                val valueAnimator =
                    ValueAnimator.ofInt(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_mito03.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_mito03.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxMito03= true
            } else {

                seta_mito03.rotationX = 360f

                val valueAnimator =
                    ValueAnimator.ofInt(LinearLayout.LayoutParams.WRAP_CONTENT, 0)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_mito03.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_mito03.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxMito03 = false
            }
        }
        //Mito 04
        else if(id == R.id.box_mito04) {
            if (!isOpenBoxMito04) {

                seta_mito04.rotationX = 180f

                val valueAnimator =
                    ValueAnimator.ofInt(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_mito04.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_mito04.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxMito04= true
            } else {

                seta_mito04.rotationX = 360f

                val valueAnimator =
                    ValueAnimator.ofInt(LinearLayout.LayoutParams.WRAP_CONTENT, 0)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_mito04.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_mito04.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxMito04 = false
            }
        }
        //Mito 05
        else if(id == R.id.box_mito05) {

            if (!isOpenBoxMito05) {

                seta_mito05.rotationX = 180f

                val valueAnimator =
                    ValueAnimator.ofInt(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_mito05.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_mito05.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxMito05= true
            } else {

                seta_mito05.rotationX = 360f

                val valueAnimator =
                    ValueAnimator.ofInt(LinearLayout.LayoutParams.WRAP_CONTENT, 0)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_mito05.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_mito05.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxMito05 = false
            }
        }
        //Mito 06
        else if(id == R.id.box_mito06) {
            if (!isOpenBoxMito06) {

                seta_mito06.rotationX = 180f

                val valueAnimator =
                    ValueAnimator.ofInt(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_mito06.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_mito06.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxMito06= true
            } else {

                seta_mito06.rotationX = 360f

                val valueAnimator =
                    ValueAnimator.ofInt(LinearLayout.LayoutParams.WRAP_CONTENT, 0)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_mito06.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_mito06.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxMito06 = false
            }
        }
        //Mito 07
        else if(id == R.id.box_mito07) {

            if (!isOpenBoxMito07) {

                seta_mito07.rotationX = 180f

                val valueAnimator =
                    ValueAnimator.ofInt(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_mito07.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_mito07.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxMito07= true
            } else {

                seta_mito07.rotationX = 360f

                val valueAnimator =
                    ValueAnimator.ofInt(LinearLayout.LayoutParams.WRAP_CONTENT, 0)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    text_box_mito07.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    text_box_mito07.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxMito07 = false
            }
        }
    }
}