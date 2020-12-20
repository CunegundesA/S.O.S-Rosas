package com.example.sosrosas.Fragment


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sosrosas.Interfaces.DenunciasListeners
import com.example.sosrosas.Model.ContatoAjuda
import com.example.sosrosas.R
import com.example.sosrosas.View.RecyclerViewContatosAdapter
import com.example.sosrosas.ViewModel.ContatosViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_denuncia_page1_contato.*
import kotlinx.android.synthetic.main.activity_denuncia_page1_contato.view.*
import java.util.ArrayList

class ContatoFragment() : Fragment(), View.OnClickListener,
    RecyclerViewContatosAdapter.ContatosListerner {

    private lateinit var mDenunciasNext: DenunciasListeners
    private lateinit var mRecyclerViewContatosAdapter: RecyclerViewContatosAdapter
    private var listContatos = ArrayList<ContatoAjuda>()
    private val listKeys = ArrayList<String>()
    private lateinit var mContatosViewModel: ContatosViewModel
    private val database = FirebaseDatabase.getInstance()
    private lateinit var data: DatabaseReference
    private lateinit var childEventListener: ChildEventListener
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContatosViewModel = ContatosViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root: ViewGroup = inflater.inflate(R.layout.activity_denuncia_page1_contato,
            container,
            false) as ViewGroup
        root.recycler_view_contatos.layoutManager = LinearLayoutManager(context)
        mRecyclerViewContatosAdapter = RecyclerViewContatosAdapter(context!!, listContatos, this)
        mRecyclerViewContatosAdapter.setListener(this)
        root.recycler_view_contatos.adapter = mRecyclerViewContatosAdapter
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        botao_add_contato.setOnClickListener(this)
    }

    open fun setListener(fragment: Fragment) {
        try {
            mDenunciasNext = fragment as DenunciasListeners
        } catch (e: ClassCastException) {
        }
    }

    override fun onClick(view: View) {
        var id = view.id

        if (id == R.id.botao_add_contato) {
            mDenunciasNext.goPageAddContato()
        }
    }

    private fun listernerDataBase() {
        val user = auth.currentUser
        val uid = user?.uid
        data = database.getReference().child("BD_SOSRosas").child(uid.toString()).child("Contatos")

        childEventListener = object : ChildEventListener {

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val key = dataSnapshot.key
                listKeys.add(key!!)
                val contatoAjuda = dataSnapshot.getValue<ContatoAjuda>()
                contatoAjuda?.id = key!!
                listContatos.add(contatoAjuda!!)
                mRecyclerViewContatosAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                val key = dataSnapshot.key
                var index = listKeys.indexOf(key)
                val contatoAjuda = dataSnapshot.getValue<ContatoAjuda>()
                contatoAjuda?.id = key!!
                listContatos.set(index, contatoAjuda!!)
                mRecyclerViewContatosAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val key = dataSnapshot.key
                val index = listKeys.indexOf(key)
                listContatos.remove(listContatos.get(index))
                listKeys.remove(listKeys.get(index))
                mRecyclerViewContatosAdapter.notifyItemRemoved(index)
                mRecyclerViewContatosAdapter.notifyItemChanged(index, listContatos)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        data.addChildEventListener(childEventListener)

    }

    //Seleciona o contato e leva para sua pagina de informções
    override fun clicarContato(contatoAjuda: ContatoAjuda) {
        mDenunciasNext.goPageInformacoes(contatoAjuda)
    }

    //Exclui o contato
    override fun excluirContato(contatoAjuda: ContatoAjuda) {
        var alert = AlertDialog.Builder(context)
        alert.setTitle("Excluir este contato de emergência?")
        alert.setMessage("Deseja excluir ${contatoAjuda.nome} da sua lista de contatos de emergência?")
        alert.setPositiveButton("Sim", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                mContatosViewModel.remove(contatoAjuda.id)
            }

        })
        alert.setNegativeButton("Cancelar", null)
        alert.show()
    }

    override fun onStart() {
        super.onStart()
        listernerDataBase()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (childEventListener != null) {
            listContatos.clear()
            listKeys.clear()
            data.removeEventListener(childEventListener);
        }
    }

}