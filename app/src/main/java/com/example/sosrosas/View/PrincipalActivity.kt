          package com.example.sosrosas.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.sosrosas.Interfaces.PrincipalListeners
import com.example.sosrosas.Interfaces.ViolentometroListeners
import com.example.sosrosas.R
import com.example.sosrosas.ViewModel.ContatosViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_principal.*

class PrincipalActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener, PrincipalListeners{

    private var pageMainActivity = MainActivity()
    private var pageInformacaoActivity = InformacaoActivity()
    private var pageBuscaActivity = BuscaActivity()
    private var pageDenunciaActivity = DenunciaActivity()
    private var pageConfiguracoesActivity = ConfiguracoesActivity()
    private var pagePerfilActivity = PerfilActivity()
    private var currentFragmentName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        currentFragmentName = "main"

        botao_navegacao_principal.setOnNavigationItemSelectedListener(this)
        supportFragmentManager.beginTransaction().replace(R.id.frame_principal, pageMainActivity).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
       var fragmentAtual : Fragment? = null

        when (item.itemId) {
            R.id.nav_home -> {
                currentFragmentName = "main"
                fragmentAtual = pageMainActivity
            }

            R.id.nav_informacao -> {
                currentFragmentName = "info"
                fragmentAtual = pageInformacaoActivity
            }

            R.id.nav_rosa -> {
                startActivity(Intent(applicationContext, BotaoDoPanicoActivity::class.java))
            }

            R.id.nav_busca -> {
                currentFragmentName = "busca"
                fragmentAtual = pageBuscaActivity
            }

            R.id.nav_denuncia -> {
                currentFragmentName = "denuncia"
                fragmentAtual = pageDenunciaActivity
            }

        }

        if(fragmentAtual != null) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_principal, fragmentAtual!!)
                .commit()
        }

        return true
    }

    override fun goConfiguration() {
        supportFragmentManager.beginTransaction().replace(R.id.frame_principal, pageConfiguracoesActivity)
            .commit()
    }

    override fun goPerfil() {
        supportFragmentManager.beginTransaction().replace(R.id.frame_principal, pagePerfilActivity)
            .commit()
    }

    override fun singOut() {
        startActivity(Intent(applicationContext, LoginActivit::class.java))
        finish()
    }

    override fun onBackPressed() {

        if(!pageDenunciaActivity.isBackFragmentActived() && !pageInformacaoActivity.isBackFragmentActived()) {
            super.onBackPressed()
        }

        if(currentFragmentName.equals("denuncia")){
            pageDenunciaActivity.goBackFragmentContatosPressed()
        }else if(currentFragmentName.equals("info")){
            pageInformacaoActivity.goBackFragmentViolentometroPressed()
        }

    }

}



