package com.example.sosrosas.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.sosrosas.R
import kotlinx.android.synthetic.main.activity_calendario.*
import java.util.*

class CalendarioActivity: AppCompatActivity(), View.OnClickListener {

    private lateinit var emailUserCurrent : String
    private var listDays = ArrayList<Date>()
    private lateinit var hoje: Date
    private var mesAtual: Int = 0
    private var anoAtual: Int = 0
    private val meses = arrayOf(
        "Janeiro",
        "Fevereiro",
        "Março",
        "Abril",
        "Maio",
        "Junho",
        "Julho",
        "Agosto",
        "Setembro",
        "Outubro",
        "Novembro",
        "Dezembro"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        hoje = Date()
        mesAtual = hoje.month
        anoAtual = hoje.year

        val extras = intent.extras

        if(extras != null && extras.containsKey("email")) {
            emailUserCurrent = extras.getSerializable("email").toString()
        }

        showCalendar(mesAtual, anoAtual)

        getListeners()
    }

    // Pega todos os ouvintes
    private fun getListeners(){
        mes_anterior.setOnClickListener(this)
        mes_posterior.setOnClickListener(this)
        button_voltar_agenda.setOnClickListener(this)
    }

    //Mostra o calendario
    private fun showCalendar(mes: Int, ano: Int) {
        val primeiroDiadaSemana = Date(ano, mes, 1).getDay();
        val primeiroDiadaSemanadoProximoMes = Date(ano, mes + 1, 1).getDay();
        val diasNoAno = 32 - Date(ano, mes, 32).getDate();
        val anoCalendar = GregorianCalendar.getInstance()
        anoCalendar.set(ano,mes,1)

        text_mes_ano.text = "${meses[mes]}, ${2000 + (ano - 100)}"

        var data = 1

        for (x in 0 until 6) {
            for (y in 0 until 7) {
                if (x == 0 && y < primeiroDiadaSemana) {
                    val diasNoAnoDoMesPassado = 32 - Date(ano, mes - 1, 32).getDate();
                    val celulaTexto = diasNoAnoDoMesPassado - (primeiroDiadaSemana - (y + 1))
                    listDays.add(Date(ano,mes-1,celulaTexto))
                } else if (data > diasNoAno) {
                    break
                } else {
                    if (hoje.date == data && hoje.month == mes && hoje.year == ano) {
                        val celulaTexto = data
                        listDays.add(Date(ano, mes , celulaTexto))
                        data++
                    } else {
                        val celulaTexto = data
                        listDays.add(Date(ano, mes , celulaTexto))
                        data++
                    }
                }

                if (x >= 4 && data > diasNoAno) {
                    if (primeiroDiadaSemanadoProximoMes == 0) {
                        break
                    } else {
                        var diaDoproxMes = 1
                                    while (diaDoproxMes < (8 - primeiroDiadaSemanadoProximoMes)) {
                            val celulaTexto = diaDoproxMes
                            listDays.add(Date(ano, mes + 1 , celulaTexto))
                            diaDoproxMes++;
                        }
                    }
                }
            }
        }

        val gridAdapter = GridViewCalendarAdapter(this, listDays, mes, ano, emailUserCurrent)
        calendar_grid.adapter = gridAdapter
    }

    override fun onClick(view: View) {
        val id = view.id
        if(id == R.id.mes_anterior){
            listDays.clear()
            anoAtual = if(mesAtual == 0) { anoAtual - 1} else{anoAtual}
            mesAtual = if(mesAtual == 0){11} else {mesAtual - 1}
            showCalendar(mesAtual,anoAtual)
        }else if(id == R.id.mes_posterior){
            listDays.clear()
            anoAtual = if(mesAtual == 11) { anoAtual + 1 } else { anoAtual}
            mesAtual = (mesAtual + 1) % 12
            showCalendar(mesAtual,anoAtual)
        }else if(id == R.id.button_voltar_agenda){
            startActivity(Intent(applicationContext, LoginActivit::class.java))
            finish()
        }
    }

}

