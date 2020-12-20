package com.example.sosrosas.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.sosrosas.Interfaces.PrincipalListeners
import com.example.sosrosas.Interfaces.ViolentometroListeners
import com.example.sosrosas.R
import com.example.sosrosas.ViewModel.ContatosViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_informacao.*
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.activity_resultado_violentometro.*
import java.util.ArrayList

class PrincipalActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener, PrincipalListeners {

    private var pageMainActivity = MainActivity()
    private var pageInformacaoActivity = InformacaoActivity()
    private var pageBuscaActivity = BuscaActivity()
    private var pageDenunciaActivity = DenunciaActivity()
    private var pageConfiguracoesActivity = ConfiguracoesActivity()
    private var pagePerfilActivity = PerfilActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        botao_navegacao_principal.setOnNavigationItemSelectedListener(this)
        supportFragmentManager.beginTransaction().replace(R.id.frame_principal, pageMainActivity).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
       var fragmentAtual : Fragment? = null

        when (item.itemId) {
            R.id.nav_home -> {
                fragmentAtual = pageMainActivity
            }

            R.id.nav_informacao -> {
                fragmentAtual = pageInformacaoActivity
            }

            R.id.nav_rosa -> {
                startActivity(Intent(applicationContext, BotaoDoPanicoActivity::class.java))
            }

            R.id.nav_busca -> {
                fragmentAtual = pageBuscaActivity
            }

            R.id.nav_denuncia -> {
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
}



