package com.example.sosrosas.View

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sosrosas.Fragment.MapsActivity
import com.example.sosrosas.Interfaces.PrincipalListeners
import com.example.sosrosas.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_busca.*
import kotlinx.android.synthetic.main.activity_denuncia.*
import kotlinx.android.synthetic.main.activity_informacao.*
import kotlinx.android.synthetic.main.activity_informacao_page1_informacoes.*

class BuscaActivity : Fragment(), View.OnClickListener {

    private var pageMap = MapsActivity()
    private lateinit var mConfiguracaoNext : PrincipalListeners
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var isOpenBoxHospital = false
    private var isOpenBoxDECCM = false
    private var isOpenBoxDip = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root: ViewGroup =
            inflater.inflate(R.layout.activity_busca, container, false) as ViewGroup
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
        childFragmentManager.beginTransaction().replace(R.id.frame_map, pageMap).commit()
        getListeners()
    }

    private fun getListeners(){
        img_configuracao_busca.setOnClickListener(this)
        img_utilizador_busca.setOnClickListener(this)
        open_box_hospital.setOnClickListener(this)
        hospital_01.setOnClickListener(this)
        hospital_02.setOnClickListener(this)
        hospital_03.setOnClickListener(this)
        hospital_04.setOnClickListener(this)
        open_box_deccm.setOnClickListener(this)
        deccm_01.setOnClickListener(this)
        deccm_02.setOnClickListener(this)
        deccm_03.setOnClickListener(this)
        open_box_dip.setOnClickListener(this)
        dip_01.setOnClickListener(this)
        dip_02.setOnClickListener(this)
        dip_03.setOnClickListener(this)
        dip_04.setOnClickListener(this)
        dip_05.setOnClickListener(this)
        dip_06.setOnClickListener(this)
        dip_07.setOnClickListener(this)
        dip_08.setOnClickListener(this)
        dip_09.setOnClickListener(this)
        dip_10.setOnClickListener(this)
        dip_11.setOnClickListener(this)
        dip_12.setOnClickListener(this)
        dip_13.setOnClickListener(this)
        dip_14.setOnClickListener(this)
        dip_15.setOnClickListener(this)
        dip_17.setOnClickListener(this)
        dip_18.setOnClickListener(this)
        dip_19.setOnClickListener(this)
        dip_20.setOnClickListener(this)
        dip_21.setOnClickListener(this)
        dip_22.setOnClickListener(this)
        dip_23.setOnClickListener(this)
        dip_24.setOnClickListener(this)
        dip_25.setOnClickListener(this)
        dip_26.setOnClickListener(this)
        dip_27.setOnClickListener(this)
        dip_28.setOnClickListener(this)
        dip_29.setOnClickListener(this)
        dip_30.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val id = view.id

        if(id == R.id.img_configuracao_busca){
            mConfiguracaoNext.goConfiguration()
        } else if(id == R.id.img_utilizador_busca){
            mConfiguracaoNext.goPerfil()
        }else if(id == R.id.open_box_hospital){
            if(!isOpenBoxHospital){
                seta_hospital.rotationX = 180f
                val valueAnimator = ValueAnimator.ofInt(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    box_hospital.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    box_hospital.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxHospital = true
            }else{
                seta_hospital.rotationX = 360f
                val valueAnimator = ValueAnimator.ofInt(LinearLayout.LayoutParams.WRAP_CONTENT, 0)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    box_hospital.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    box_hospital.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxHospital = false
            }
        }else if(id == R.id.hospital_01){
            pageMap.goHospital28deAgosto()
        }else if(id == R.id.hospital_02){
            pageMap.goHospitalDelphina()
        }else if(id == R.id.hospital_03){
            pageMap.goHospitalJoaoLucio()
        }else if(id == R.id.hospital_04){
            pageMap.goHospitalBezerraAraujo()
        }else if(id == R.id.open_box_deccm){
            if(!isOpenBoxDECCM){
                seta_deccm.rotationX = 180f
                val valueAnimator = ValueAnimator.ofInt(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    box_deccm.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    box_deccm.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxDECCM = true
            }else{
                seta_deccm.rotationX = 360f
                val valueAnimator = ValueAnimator.ofInt(LinearLayout.LayoutParams.WRAP_CONTENT, 0)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    box_deccm.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    box_deccm.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxDECCM = false
            }
        }else if(id == R.id.deccm_01){
            pageMap.goDECCMNorte()
        }else if(id == R.id.deccm_02){
            pageMap.goDECCM()
        }else if(id == R.id.deccm_03){
            pageMap.goDECCMSul()
        }else if(id == R.id.open_box_dip){
            if(!isOpenBoxDip){
                seta_dip.rotationX = 180f
                val valueAnimator = ValueAnimator.ofInt(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    box_dip.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    box_dip.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxDip = true
            }else{
                seta_dip.rotationX = 360f
                val valueAnimator = ValueAnimator.ofInt(LinearLayout.LayoutParams.WRAP_CONTENT, 0)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    box_dip.getLayoutParams().height = value * animation.animatedFraction.toInt()
                    box_dip.requestLayout()
                }
                valueAnimator.start()
                isOpenBoxDip = false
            }
        }else if(id == R.id.dip_01){
            pageMap.goDIP1()
        }else if(id == R.id.dip_02){
            pageMap.goDIP2()
        }else if(id == R.id.dip_03){
            pageMap.goDIP3()
        }else if(id == R.id.dip_04){
            pageMap.goDIP4()
        }else if(id == R.id.dip_05){
            pageMap.goDIP5()
        }else if(id == R.id.dip_06){
            pageMap.goDIP6()
        }else if(id == R.id.dip_07){
            pageMap.goDIP7()
        }else if(id == R.id.dip_08){
            pageMap.goDIP8()
        }else if(id == R.id.dip_09){
            pageMap.goDIP9()
        }else if(id == R.id.dip_10){
            pageMap.goDIP10()
        }else if(id == R.id.dip_11){
            pageMap.goDIP11()
        }else if(id == R.id.dip_12){
            pageMap.goDIP12()
        }else if(id == R.id.dip_13){
            pageMap.goDIP13()
        }else if(id == R.id.dip_14){
            pageMap.goDIP14()
        }else if(id == R.id.dip_15){
            pageMap.goDIP15()
        }else if(id == R.id.dip_17){
            pageMap.goDIP17()
        }else if(id == R.id.dip_18){
            pageMap.goDIP18()
        }else if(id == R.id.dip_19){
            pageMap.goDIP19()
        }else if(id == R.id.dip_20){
            pageMap.goDIP20()
        }else if(id == R.id.dip_21){
            pageMap.goDIP21()
        }else if(id == R.id.dip_22){
            pageMap.goDIP22()
        }else if(id == R.id.dip_23){
            pageMap.goDIP23()
        }else if(id == R.id.dip_24){
            pageMap.goDIP24()
        }else if(id == R.id.dip_25){
            pageMap.goDIP25()
        }else if(id == R.id.dip_26){
            pageMap.goDIP26()
        }else if(id == R.id.dip_27){
            pageMap.goDIP27()
        }else if(id == R.id.dip_28){
            pageMap.goDIP28()
        }else if(id == R.id.dip_29){
            pageMap.goDIP29()
        }else if(id == R.id.dip_30){
            pageMap.goDIP30()
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
                        Glide.with(context!!).load(url).into(img_utilizador_busca)
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

    override fun onStart() {
        super.onStart()
        if(verificationConnectionWithInternet()) {
            try {
                downloadPhotoUser()
            }catch (e : NullPointerException){}
        }
    }

}