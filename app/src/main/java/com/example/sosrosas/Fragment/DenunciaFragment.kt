package com.example.sosrosas.Fragment

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sosrosas.Model.ContatoAjuda
import com.example.sosrosas.Model.Denuncia
import com.example.sosrosas.R
import com.example.sosrosas.ViewModel.DenunciaViewModel
import kotlinx.android.synthetic.main.activity_denuncia_page1_denuncia.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class DenunciaFragment : Fragment(), View.OnClickListener {

    private lateinit var mDenunciaViewModel: DenunciaViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root : ViewGroup = inflater.inflate(R.layout.activity_denuncia_page1_denuncia, container, false) as ViewGroup

        mDenunciaViewModel = DenunciaViewModel()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getListeners()
    }

    private fun getListeners(){
        button_send_denuncia.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val id = view.id

        if(id == R.id.button_send_denuncia){
             sendDenuncia()
        }
    }

    private fun sendDenuncia(){
        val remetente = text_remetente.text.toString()
        val textDenuncia = text_write_denuncia.text.toString()
        val dataTime = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        val data = dateFormat.format(dataTime) + " - " + timeFormat.format(dataTime)

        if(!remetente.isEmpty() && !textDenuncia.isEmpty()) {
            mDenunciaViewModel.save(Denuncia(data, remetente, textDenuncia))
            sendEmail(Denuncia(data, remetente, textDenuncia))

            text_remetente.setText("Anônimo")
            text_write_denuncia.text.clear()
        }else{
            Toast.makeText(context!!,"Preencha todos os campos por favor!",Toast.LENGTH_SHORT).show()
        }

    }

    private fun sendEmail(denuncia : Denuncia) {
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
            InternetAddress.parse("s.o.srosassuporte@gmail.com"))
        msg.setSubject("Denuncia - ${denuncia.nome} !")

        val text = """
            Remetente: ${denuncia.nome}
            Data: ${denuncia.data}
            Denuncia: ${denuncia.denuncia}
        """.trimIndent()

        msg.setContent(text, "text/plain")
        msg.setSentDate(Date())
        DenunciaFragment.sendMenseger().execute(msg)
    }

    private class sendMenseger() : AsyncTask<Message, String, String>() {
        override fun doInBackground(vararg message: Message?): String {
            Transport.send(message[0])
            return "Sucess"
        }
    }

}