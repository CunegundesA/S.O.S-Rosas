package com.example.sosrosas.View

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sosrosas.Interfaces.DenunciasListeners
import com.example.sosrosas.Model.ContatoAjuda
import com.example.sosrosas.R
import kotlinx.android.synthetic.main.recyclerview_model_contatosajuda.view.*


class RecyclerViewContatosAdapter : RecyclerView.Adapter<RecyclerViewContatosAdapter.ViewHolder>{

    private var context : Context
    private var listContatos: List<ContatoAjuda>
    private var contatosListener: ContatosListerner
    private lateinit var mContatosListerner: ContatosListerner

    constructor(
        context: Context,
        listContatos: List<ContatoAjuda>,
        contatosListener: ContatosListerner
    ){
        this.context = context
        this.listContatos = listContatos
        this.contatosListener = contatosListener
    }

    open fun setListener(fragment: Fragment){
        try {
            mContatosListerner = fragment as ContatosListerner
        } catch (e: ClassCastException) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_model_contatosajuda, parent, false)

        val holder = ViewHolder(view)

        view.tag = holder

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contatoAjuda = listContatos.get(position)

        holder.nome.text = contatoAjuda.nome
        holder.card.setOnClickListener { mContatosListerner.clicarContato(contatoAjuda) }
        holder.deleteButton.setOnClickListener{ mContatosListerner.excluirContato(contatoAjuda)}
    }

    override fun getItemCount(): Int {
        return listContatos.size
    }

    interface ContatosListerner{

        fun clicarContato(contatoAjuda: ContatoAjuda)

        fun excluirContato(contatoAjuda: ContatoAjuda)

    }

    class ViewHolder : RecyclerView.ViewHolder{

        var nome : TextView
        var card : CardView
        var deleteButton : Button

        constructor (itemView: View) : super(itemView){
            nome = itemView.text_contato_nome
            card = itemView.recycleview_contatosajuda
            deleteButton = itemView.button_excluir_contato
        }
    }

}