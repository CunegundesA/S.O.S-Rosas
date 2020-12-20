package com.example.sosrosas.View

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.sosrosas.R
import com.example.sosrosas.ViewModel.AgendaSharedPreferences
import kotlinx.android.synthetic.main.grid_view_calendar_days.view.*
import java.util.*

class GridViewCalendarAdapter : BaseAdapter {

    private var context : Context
    private var listDay : List<Date>
    private var mesAtual : Int
    private var anoAtual: Int
    private var mAgendaShared : AgendaSharedPreferences
    private var emailUser : String

    constructor(context: Context, listDay: List<Date>, mesAtual: Int, anoAtual: Int, emailUser : String){
        this.context = context
        this.listDay = listDay
        this.mesAtual = mesAtual
        this.anoAtual = anoAtual
        this.mAgendaShared = AgendaSharedPreferences(context)
        this.emailUser = emailUser
    }

    override fun getCount(): Int {
        return listDay.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(position: Int, view : View?, parent: ViewGroup?): View {
        val convertView: View = LayoutInflater.from(parent!!.context).inflate(R.layout.grid_view_calendar_days, parent, false)
        var listAgendaDays = mAgendaShared.getList(emailUser)
        val hoje = Date()

        // Monta as cores padrões do calendario, os dias normais de cinza e o dia atual de vermelho
        if(listDay.get(position).date == hoje.date && listDay.get(position).month == hoje.month && listDay.get(position).year == hoje.year){
            convertView.text_day.text = listDay.get(position).date.toString()
            convertView.text_day.setTextColor(Color.WHITE)
            convertView.text_day.background = convertView.resources.getDrawable(R.color.colorTextDefaultRed)
        }else if(listDay.get(position).month == (mesAtual - 1)
            || listDay.get(position).month == (mesAtual + 1)
            || listDay.get(position).year == (anoAtual - 1)
            || listDay.get(position).year == (anoAtual + 1) ){
            convertView.text_day.text = listDay.get(position).date.toString()
            convertView.text_day.isEnabled = false
            convertView.date_grid.isEnabled = false
            convertView.text_day.background = convertView.resources.getDrawable(R.color.grayColor)
        } else{
            convertView.text_day.text = listDay.get(position).date.toString()
        }

        if (listAgendaDays != null){
            for (x in 0 until listAgendaDays.size){
                val date = Date(listAgendaDays.get(x).toLong())
                if(date.date == listDay.get(position).date && date.month == listDay.get(position).month && date.year == listDay.get(position).year){
                    convertView.text_day.text = listDay.get(position).date.toString()
                    convertView.text_day.setTextColor(Color.WHITE)
                    convertView.text_day.background = convertView.resources.getDrawable(R.color.selectorRadioButton)
                }
            }
        }

        convertView.date_grid.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {

                var exist = false
                var agendaPosition = 0

                if (listAgendaDays != null){
                    for (x in 0 until listAgendaDays!!.size){
                        val date = Date(listAgendaDays!!.get(x).toLong())
                        if(date.date == listDay.get(position).date && date.month == listDay.get(position).month && date.year == listDay.get(position).year){
                            exist = true
                            agendaPosition = x
                        }
                    }
                }
                if(exist == false){
                    mAgendaShared.saveDate(listDay.get(position).time, emailUser)
                    convertView.text_day.text = listDay.get(position).date.toString()
                    convertView.text_day.setTextColor(Color.WHITE)
                    convertView.text_day.background = convertView.resources.getDrawable(R.color.selectorRadioButton)
                    listAgendaDays = mAgendaShared.getList(emailUser)
                }else{
                    mAgendaShared.removeDate(agendaPosition, emailUser)
                    listAgendaDays = mAgendaShared.getList(emailUser)

                    if(listDay.get(position).date == hoje.date && listDay.get(position).month == hoje.month && listDay.get(position).year == hoje.year){
                        convertView.text_day.text = listDay.get(position).date.toString()
                        convertView.text_day.setTextColor(Color.WHITE)
                        convertView.text_day.background = convertView.resources.getDrawable(R.color.colorTextDefaultRed)
                    }else {
                        convertView.text_day.text = listDay.get(position).date.toString()
                        convertView.text_day.setTextColor(convertView.resources.getColor(R.color.blackText))
                        convertView.text_day.background = convertView.resources.getDrawable(android.R.color.transparent)
                    }
                }
            }
        })

        return convertView
    }
}