package com.example.sosrosas.Fragment

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.telephony.SmsManager
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.sosrosas.Common.Environment
import com.example.sosrosas.Interfaces.CadastroListeners
import com.example.sosrosas.Interfaces.DenunciasListeners
import com.example.sosrosas.Model.ContatoAjuda
import com.example.sosrosas.Model.MaskEdit
import com.example.sosrosas.Model.Usuario
import com.example.sosrosas.R
import com.example.sosrosas.ViewModel.CadastroViewModel
import com.example.sosrosas.ViewModel.ContatosSharedPreferences
import com.example.sosrosas.ViewModel.ContatosViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_cadastro_page1.view.*
import kotlinx.android.synthetic.main.activity_denuncia_page1_addcontatos.*
import kotlinx.android.synthetic.main.activity_denuncia_page1_addcontatos.view.*
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class AddContatoFragment : Fragment(), View.OnClickListener {

    private lateinit var mDenunciasNext: DenunciasListeners
    private lateinit var mContatosViewModel: ContatosViewModel
    private lateinit var mContatos: ContatoAjuda
    private var usuarioAtual = Usuario()
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private lateinit var mContatosShared : ContatosSharedPreferences
    private var listContatos = ArrayList<ContatoAjuda>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val root: ViewGroup = inflater.inflate(R.layout.activity_denuncia_page1_addcontatos,
            container,
            false) as ViewGroup

        root.text_numero_contato.addTextChangedListener(MaskEdit().mask(root.text_numero_contato,
            MaskEdit().FORMAT_CELULAR))

        mContatosViewModel = ContatosViewModel.getInstance()
        mContatos = ContatoAjuda()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContatosShared = ContatosSharedPreferences(context!!)

        try{
            listContatos = mContatosShared.getListContatos(auth.currentUser!!.email.toString()) as ArrayList<ContatoAjuda>
        }catch (e : NullPointerException){
        }

        botao_adicionar_contato.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        loadUserCurrent()
    }

    open fun setListener(fragment: Fragment) {
        try {
            mDenunciasNext = fragment as DenunciasListeners
        } catch (e: ClassCastException) {
        }
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

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.botao_adicionar_contato) {
            val nome = text_nome_contato.text.toString()
            val numero = text_numero_contato.text.toString()
            val email = text_email_contato.text.toString()

            if (nome.isEmpty() == false && numero.isEmpty() == false || email.isEmpty() == false) {
                mContatos.nome = nome
                mContatos.celular = numero
                mContatos.emailContato = email
                mContatos.nomeLowerCase = nome.toString().toLowerCase()

                if(email.isEmpty() == false) {
                    if(verificationConnectionWithInternet()) {
                        sendEmail(mContatos)
                    }
                }

                if(numero.isEmpty() == false) {
                    sendSMS(mContatos)
                }

                Thread(Runnable {
                mContatosViewModel.save(mContatos)
                listContatos.add(mContatos)
                mContatosShared.saveListContatos(auth.currentUser!!.email.toString(), listContatos)}).start()
                text_nome_contato.text.clear()
                text_numero_contato.text.clear()
                text_email_contato.text.clear()

                mDenunciasNext.goPageContato()
            } else {

                if(nome.isEmpty()){text_nome_contato.setError("Informe o nome do contato")}
                if(numero.isEmpty()){text_numero_contato.setError("Informe o número do contato")}
                if(email.isEmpty()){text_email_contato.setError("Informe o email do contato")}

                Toast.makeText(context,
                    "Preencha o nome e os campos de celular e/ou email!",
                    Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun loadUserCurrent() {
        val user = auth.currentUser
        val uid = user!!.uid
        val data =
            database.getReference().child("BD_SOSRosas").child(uid.toString()).child("Usuario")

        data.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usuarioAtual = snapshot.getValue<Usuario>() as Usuario
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun sendEmail(contato: ContatoAjuda) {
        val props = Properties()
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.starttls.enable", "true")
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com")
        props.put("mail.smtp.host", "smtp.gmail.com")
        props.put("mail.smtp.port", "587")

        val session: Session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(Environment.USER_EMAIL, Environment.USER_PASSWORD)
            }
        })

        val msg: Message = MimeMessage(session)
        msg.setFrom(InternetAddress(Environment.USER_EMAIL, false))
        msg.setHeader("Content-Type", "text/html")
        msg.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(contato.emailContato))
        msg.setSubject("Contato de Emergência!")

        val html = """<!doctype html>
<html lang="pt-br">
    <head>
        <meta charset="utf-8">
        <meta name="author" content="Affonso Ruiz, Aline Alves, Carlos Felipe, Joyce Brandão">
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
    
    .header-email h3{
        text-align: center
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
            
            <h3>Contato de Emergência!</h3>
            
            <p>Você foi adicionado como contato de ermegência pelo o usuario ${usuarioAtual.nomeUsuario} para caso ele esteja em perigo e necessite de ajuda.</p>
            
        </div>
        
        <p>Atenciosamente,</p>
        <p>S.O.S Rosas</p>
        
    </body>
</html>"""

        msg.setContent(html, "text/html")
        msg.setSentDate(Date())
        sendMenseger().execute(msg)
    }

    private fun sendSMS(contato: ContatoAjuda) {
        var numeroContato = "0"
        val sms = SmsManager.getDefault()

        for (x in contato.celular) {
            if (x != '(' && x != ')' && x != '-') {
                numeroContato = numeroContato + x
            }
        }

        println(numeroContato)

        if (ActivityCompat.checkSelfPermission(context!!,
                android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(context!! as Activity,
                arrayOf(android.Manifest.permission.SEND_SMS), 1)
        } else {
            sms.sendTextMessage(numeroContato,
                null,
                "O usuario ${usuarioAtual.nomeUsuario} do aplicativo S.O.S Rosas te adicionou como contato de emergencia.",
                null,
                null)
        }
    }


    private class sendMenseger() : AsyncTask<Message, String, String>() {

        override fun doInBackground(vararg message: Message?): String {
            Transport.send(message[0])
            return "Sucess"
        }

    }


}