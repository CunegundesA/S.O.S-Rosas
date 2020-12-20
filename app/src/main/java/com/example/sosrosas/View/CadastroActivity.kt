package com.example.sosrosas.View

import android.app.DownloadManager
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.telephony.SmsManager
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.sosrosas.Fragment.CadastroFragment1
import com.example.sosrosas.Fragment.CadastroFragment2
import com.example.sosrosas.Fragment.CadastroFragment3
import com.example.sosrosas.Interfaces.CadastroListeners
import com.example.sosrosas.Model.ErroNotification
import com.example.sosrosas.Model.Usuario
import com.example.sosrosas.R
import com.example.sosrosas.ViewModel.CadastroViewModel
import com.example.sosrosas.ViewModel.SharedPreferences
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_cadastro.*
import kotlinx.android.synthetic.main.activity_cadastro_page1.*
import kotlinx.android.synthetic.main.activity_cadastro_page2.*
import kotlinx.android.synthetic.main.activity_cadastro_page3.*
import java.io.File
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class CadastroActivity : AppCompatActivity(), CadastroListeners, View.OnTouchListener {

    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var mAdapter: PagerAdapter
    private lateinit var mCadastro: CadastroViewModel
    private lateinit var mUsuario: Usuario
    private val auth = FirebaseAuth.getInstance()
    private var list: MutableList<Fragment> = ArrayList<Fragment>()
    private var page1 = CadastroFragment1()
    private var page2 = CadastroFragment2()
    private var page3 = CadastroFragment3()
    private var codigoConfirmacao = 0
    private lateinit var drawble: BitmapDrawable
    private var isImageSet = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        mCadastro = CadastroViewModel()
        mUsuario = Usuario()

        list.add(page1)
        list.add(page2)
        list.add(page3)
        mAdapter = SlidePageAdapter(supportFragmentManager, list)
        cadastro_pager.adapter = mAdapter

        cadastro_pager.setOnTouchListener(this)

        mSharedPreferences = SharedPreferences(this)
    }

    override fun goPage2(activedImage: Boolean) {
        isImageSet = activedImage
        drawble = image_cadastro_usuario.drawable as BitmapDrawable
        val name = text_name.text.toString()
        val nameUser = text_name_user.text.toString()
        val email: String = text_email.text.toString().trim()
        val password = text_password.text.toString()
        val fakePasseord = text_fake_password.text.toString()
        val cpf = text_cpf.text.toString()
        val rg = text_rg.text.toString()
        val celular = text_celular.text.toString()
        val dataNascimento = text_data_nascimento.text.toString()

        mUsuario.nome = name
        mUsuario.nomeUsuario = nameUser
        mUsuario.email = email
        mUsuario.senha = password
        mUsuario.senhaFalsa = fakePasseord
        mUsuario.cpf = cpf
        mUsuario.rg = rg
        mUsuario.dataNascimento = dataNascimento
        mUsuario.celular = celular

        cadastro_pager.setCurrentItem(1)
    }

    override fun goPage3() {
        val cep = text_cep.text.toString()
        val endereco = text_endereco.text.toString()
        val bairro = text_bairro.text.toString()
        val numero = text_numero.text.toString()
        val cidade = text_cidade.text.toString()
        val estado = text_estado.text.toString()

        mUsuario.cep = cep
        mUsuario.endereco = endereco
        mUsuario.bairro = bairro
        mUsuario.numero = numero
        mUsuario.cidade = cidade
        mUsuario.estado = estado

        sendEmail()
        cadastro_pager.setCurrentItem(2)
        cadastro_pager.setOnTouchListener(null)
    }

    override fun getNextPage() {
        val codigo = text_codigo.text.toString()

        if(codigoConfirmacao == codigo.toInt()) {
            mCadastro.createLoginUser(mUsuario)
            auth.signInWithEmailAndPassword(mUsuario.email, mUsuario.senha).addOnCompleteListener(this, OnCompleteListener {
                if(it.isSuccessful){
                    if(isImageSet) {
                        mCadastro.savePhotoUser(drawble)
                    }
                    mCadastro.save(mUsuario)
                    mSharedPreferences.savePasswordKeys(mUsuario.email, mUsuario.senhaFalsa)
                    startActivity(Intent(application, PosCadastroActivity::class.java))
                    finish()
                }else{
                    ErroNotification().getErroNotification(this,it.exception.toString())
                }
            } )
        }else{
            Toast.makeText(applicationContext, "Codigo invalido!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendEmail() {
        codigoConfirmacao = (100000..999999).random()
        val props = Properties()
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.starttls.enable", "true")
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com")
        props.put("mail.smtp.host", "smtp.gmail.com")
        props.put("mail.smtp.port", "587")

        val session: Session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("SEU_EMAIL", "SUA_SENHA")
            }
        })

        val msg: Message = MimeMessage(session)
        msg.setFrom(InternetAddress("EMAIL", false))
        msg.setHeader("Content-Type", "text/html")
        msg.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(mUsuario.email))
        msg.setSubject("Confirmação de email - S.O.S Rosas")

        val html = """<!doctype html>
<html lang="pt-br">
    <head>
        <meta charset="utf-8">
        <meta name="author" content="Affonso Ruiz e Aline Alves">
        <meta name="description" content="Email de alteração de senha">
        <meta name="keywords" content="Project Stages">
        
<style>
    
    .header-email{
        display: block;
        color: white;
        background-color: #C00612;
        padding: 5px;
        padding-left: 20px;
        padding-right: 20px;
        padding-bottom: 10px;
        margin: 0px;
        font-family: Arial, sans-serif,serif;
        text-align: justify;
    }
    
    .body-email{
        display: block;
        color: #C00612;
        margin: 0px;
        font-family: Arial, sans-serif,serif;
        border-bottom: 1px solid black;
        text-align: center;
    }
    
    .img-email{
        width: 100px;
        height: 100px;
        display: block;
        text-align: center;
        padding: 10px;
        margin: auto;
    }    
    
</style>
    </head>
    <body>
       
        <div class="header-email">
            <img class="img-email" src="https://i.pinimg.com/originals/8b/4a/ff/8b4aff4893be75e9969690d29bd7ab56.png"/>
            <p>Caro, ${mUsuario.nomeUsuario}</p>
            <p>
             Você está recebendo um código de validação para validar o acesso a sua conta pessoal no <b>S.O.S Rosas</b>. Se você não é ${mUsuario.nomeUsuario} recomendamos que você troque sua senha de email.
            </p>
            <p>Se foi você mesmo que solicitou a criação do código de validação do email digite o código de validação que segue abaixo no <b>S.O.S Rosas</b>.</p>
        </div>
        
        <div class="body-email">
        
            <h3>Código de validação:</h3>
            <h3>${codigoConfirmacao}</h3>
        </div>
        
        <p>Atenciosamente,</p>
        <p>S.O.S Rosas</p>
        
    </body>
</html>"""

        msg.setContent(html, "text/html")
        msg.setSentDate(Date())
        sendMenseger().execute(msg)
    }

    private class sendMenseger() : AsyncTask<Message, String, String>() {

        override fun doInBackground(vararg message: Message?): String {
            Transport.send(message[0])
            return "Sucess"
        }

    }

    override fun onTouch(p0: View, p1: MotionEvent): Boolean {
        cadastro_pager.setCurrentItem(cadastro_pager.currentItem)
        return true
    }
}


