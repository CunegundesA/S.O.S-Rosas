package com.example.sosrosas.View

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.sosrosas.Api.ApiClient
import com.example.sosrosas.Common.Environment
import com.example.sosrosas.Interfaces.ApiInterface
import com.example.sosrosas.Interfaces.PrincipalListeners
import com.example.sosrosas.Model.Article
import com.example.sosrosas.Model.News
import com.example.sosrosas.Model.Usuario
import com.example.sosrosas.R
import com.example.sosrosas.ViewModel.CadastroSharedPreferences
import com.example.sosrosas.ViewModel.SharedPreferences
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.box_erro_conection_internet.*
import kotlinx.android.synthetic.main.box_erro_equal_password.*
import kotlinx.android.synthetic.main.box_main_violencia_fisica.*
import kotlinx.android.synthetic.main.box_main_violencia_moral.*
import kotlinx.android.synthetic.main.box_main_violencia_patrimonial.*
import kotlinx.android.synthetic.main.box_main_violencia_psicologica.*
import kotlinx.android.synthetic.main.box_main_violencia_sexual.*
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : Fragment(), View.OnClickListener,
    RecyclerViewNoticiasAdapter.NoticiasListerner {

    private var usuarioAtual = Usuario()
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private lateinit var mConfiguracaoNext: PrincipalListeners
    private val API_KEY = Environment.API_KEY
    private var listNoticias = ArrayList<Article>()
    private lateinit var mRecyclerViewNoticiasAdapter: RecyclerViewNoticiasAdapter
    private val fragmentAtual = this
    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var mCadastroShared: CadastroSharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root: ViewGroup =
            inflater.inflate(R.layout.activity_main, container, false) as ViewGroup

        root.recycler_view_noticias_main.isNestedScrollingEnabled = false
        root.recycler_view_noticias_main.itemAnimator = DefaultItemAnimator()

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
        mSharedPreferences = SharedPreferences(context!!)
        mCadastroShared = CadastroSharedPreferences(context!!)
        getListeners()
    }

    private fun getListeners() {
        configuracao_main.setOnClickListener(this)
        foto_main.setOnClickListener(this)
        card_violencia_fisica.setOnClickListener(this)
        card_violencia_psicologica.setOnClickListener(this)
        card_violencia_sexual.setOnClickListener(this)
        card_violencia_patrimonial.setOnClickListener(this)
        card_violencia_moral.setOnClickListener(this)
    }

    private fun verificationConnectionWithInternet(): Boolean {
        val conectInternet =
            activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conectInternet.activeNetworkInfo

        if (netInfo != null && netInfo.isConnected()) {
            return true
        } else {
            return false
        }
    }

    // Carrega os dados do usuario atual
    private fun loadUserCurrent() {
        val user = auth.currentUser
        val uid = user!!.uid
        val data =
            database.getReference().child("BD_SOSRosas").child(uid.toString()).child("Usuario")

        data.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.getValue() != null) {
                    usuarioAtual = snapshot.getValue<Usuario>() as Usuario
                }
                if (usuarioAtual != null) {
                    try {
                        text_nome_usuario.text = usuarioAtual!!.nomeUsuario

                        if (!usuarioAtual.email.isEmpty() && !usuarioAtual.senhaFalsa.isEmpty()) {
                            mSharedPreferences.savePasswordKeys(
                                usuarioAtual.email,
                                usuarioAtual.senhaFalsa
                            )
                        }

                        if (!usuarioAtual.email.isEmpty()) {
                            verificationPasswordsEquals(usuarioAtual.email)
                            mCadastroShared.saveUser(usuarioAtual.email, usuarioAtual)
                        }

                    } catch (e: NullPointerException) {
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun verificationPasswordsEquals(email: String) {
        val textEmail = email
        val textPassword = mSharedPreferences.getStoreString(email)

        if (textPassword != "" && textPassword != null) {
            auth.signInWithEmailAndPassword(textEmail, textPassword)
                .addOnCompleteListener(context!! as Activity, OnCompleteListener {
                    if (it.isSuccessful) {
                        val dialog = Dialog(context!!)
                        dialog.setContentView(R.layout.box_erro_equal_password)
                        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialog.button_ok_error_pass.setOnClickListener(object :
                            View.OnClickListener {
                            override fun onClick(p0: View?) {
                                mConfiguracaoNext.goConfiguration()
                                dialog.dismiss()
                            }

                        })
                        dialog.show()
                    }
                })
        }
    }

    // Carrega a foto do usuario atual
    private fun downloadPhotoUser() {
        progress_bar_user.visibility = View.VISIBLE
        val user = auth.currentUser
        val uid = user!!.uid

        val dataStorage =
            storage.getReference().child("Upload").child(uid).child("fotouser" + uid + ".jpg")
        dataStorage.downloadUrl.addOnCompleteListener(object : OnCompleteListener<Uri> {
            override fun onComplete(task: Task<Uri>) {

                if (task.isSuccessful) {
                    try {
                        val url = task.result.toString()

                        Glide.with(context!!).load(url).into(foto_main)
                        progress_bar_user.visibility = View.GONE
                    } catch (e: NullPointerException) {
                    }
                } else {
                    progress_bar_user.visibility = View.GONE
                }
            }
        })
    }

    //Carrega as noticias
    private fun loadNews() {
        val locale = Locale.getDefault()
        val country = locale.country.toLowerCase()
        val apiClient = ApiClient().getApiClient()!!.create(ApiInterface::class.java)

        val call: Call<News>
        call = apiClient.getNews(country, API_KEY)

        call.enqueue(object : retrofit2.Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                if (response.isSuccessful && response.body()!!.article != null) {
                    try {
                        if (!listNoticias.isEmpty()) {
                            listNoticias.clear()
                        }

                        listNoticias = response.body()!!.article as ArrayList<Article>

                        recycler_view_noticias_main.layoutManager = LinearLayoutManager(context)
                        mRecyclerViewNoticiasAdapter =
                            RecyclerViewNoticiasAdapter(context!!, listNoticias)
                        mRecyclerViewNoticiasAdapter.setListener(fragmentAtual)
                        recycler_view_noticias_main.adapter = mRecyclerViewNoticiasAdapter
                        mRecyclerViewNoticiasAdapter.notifyDataSetChanged()
                    }catch(e : NullPointerException){}
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                println(t.message)
            }

        })

    }

    override fun onClick(view: View) {
        val id = view.id

        if (id == R.id.configuracao_main) {
            mConfiguracaoNext.goConfiguration()
        } else if (id == R.id.foto_main) {
            mConfiguracaoNext.goPerfil()
        } else if (id == R.id.card_violencia_fisica) {
            val dialog = Dialog(context!!)
            dialog.setContentView(R.layout.box_main_violencia_fisica)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.close_card_violencia_fisica.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    dialog.dismiss()
                }

            })
            dialog.show()
        } else if (id == R.id.card_violencia_psicologica) {
            val dialog = Dialog(context!!)
            dialog.setContentView(R.layout.box_main_violencia_psicologica)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.close_card_violencia_psicologica.setOnClickListener(object :
                View.OnClickListener {
                override fun onClick(p0: View?) {
                    dialog.dismiss()
                }

            })
            dialog.show()
        } else if (id == R.id.card_violencia_sexual) {
            val dialog = Dialog(context!!)
            dialog.setContentView(R.layout.box_main_violencia_sexual)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.close_card_violencia_sexual.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    dialog.dismiss()
                }

            })
            dialog.show()
        } else if (id == R.id.card_violencia_patrimonial) {
            val dialog = Dialog(context!!)
            dialog.setContentView(R.layout.box_main_violencia_patrimonial)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.close_card_violencia_patrimonial.setOnClickListener(object :
                View.OnClickListener {
                override fun onClick(p0: View?) {
                    dialog.dismiss()
                }

            })
            dialog.show()
        } else if (id == R.id.card_violencia_moral) {
            val dialog = Dialog(context!!)
            dialog.setContentView(R.layout.box_main_violencia_moral)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.close_card_violencia_moral.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    dialog.dismiss()
                }

            })
            dialog.show()
        }

    }

    override fun onStart() {
        super.onStart()

        if (verificationConnectionWithInternet()) {
            try {
                loadUserCurrent()
                downloadPhotoUser()
                loadNews()
            } catch (e: NullPointerException) {
            }
        } else {
            val dialog = Dialog(context!!)
            dialog.setContentView(R.layout.box_erro_conection_internet)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.text_msm_erro2_internet.visibility = View.VISIBLE
            dialog.button_ok_error_conection_internet.setOnClickListener(object :
                View.OnClickListener {
                override fun onClick(p0: View?) {
                    dialog.dismiss()
                }

            })
            dialog.show()

            try {
                usuarioAtual = mCadastroShared.getUser(auth.currentUser!!.email.toString())!!

                if (usuarioAtual != null) {
                    try {
                        text_nome_usuario.text = usuarioAtual!!.nomeUsuario
                    } catch (e: NullPointerException) {
                    }
                }
            } catch (e: NullPointerException) {
            }

        }
    }

    // Leva direto para o link das noticias
    override fun goPagerBrowser(url: String) {
        val intent = Intent(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        startActivity(intent)
    }

}
