package com.example.sosrosas.View

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.sosrosas.Interfaces.PrincipalListeners
import com.example.sosrosas.Model.MaskEdit
import com.example.sosrosas.Model.Usuario
import com.example.sosrosas.R
import com.example.sosrosas.ViewModel.CadastroSharedPreferences
import com.example.sosrosas.ViewModel.CadastroViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_cadastro_page1.*
import kotlinx.android.synthetic.main.activity_cadastro_page1.view.*
import kotlinx.android.synthetic.main.activity_configuracoes.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_perfil.*
import kotlinx.android.synthetic.main.activity_perfil.view.*
import kotlinx.android.synthetic.main.box_altered_foto.*
import kotlinx.android.synthetic.main.box_main_violencia_fisica.*
import java.io.IOException
import java.net.URL
import java.util.*

class PerfilActivity : Fragment(), View.OnClickListener {

    private var usuarioAtual = Usuario()
    private lateinit var mConfiguracaoNext: PrincipalListeners
    private val mCadastroViewModel = CadastroViewModel.getIstance()
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var isEnableSaveUsuario = false
    private var isEnableEditNameUser = false
    private lateinit var imageUri: Uri
    private lateinit var dialog: Dialog
    private lateinit var mCadastroShared: CadastroSharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root: ViewGroup =
            inflater.inflate(R.layout.activity_perfil, container, false) as ViewGroup

        root.text_nome_usuario_perfil.isEnabled = false

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
        ativateEditTexts(false)
        mCadastroShared = CadastroSharedPreferences(context!!)
        dialog = Dialog(context!!)
        dialog.setContentView(R.layout.box_altered_foto)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        if (verificationConnectionWithInternet()) {
            try {
                loadUserCurrent()
                downloadPhotoUser()
            }catch(e : NullPointerException){}
        } else {

            try {
                usuarioAtual = mCadastroShared.getUser(auth.currentUser!!.email.toString())!!
                if (usuarioAtual != null) {
                    try {
                        text_nome_usuario_perfil.setText(usuarioAtual!!.nomeUsuario)
                        text_perfil_nome.setText(usuarioAtual.nome)
                        text_perfil_cpf.setText(usuarioAtual.cpf)
                        text_perfil_rg.setText(usuarioAtual.rg)
                        text_perfil_date.setText(usuarioAtual.dataNascimento)
                        text_perfil_celular.setText(usuarioAtual.celular)
                        text_perfil_cep.setText(usuarioAtual.cep)
                        text_perfil_endereco.setText(usuarioAtual.endereco)
                        text_perfil_bairro.setText(usuarioAtual.bairro)
                        text_perfil_numero.setText(usuarioAtual.numero)
                        text_perfil_estado.setText(usuarioAtual.estado)
                        text_perfil_cidade.setText(usuarioAtual.cidade)
                        text_perfil_celular.addTextChangedListener(
                            MaskEdit().mask(
                                text_perfil_celular,
                                MaskEdit().FORMAT_CELULAR
                            )
                        )
                        text_perfil_cpf.addTextChangedListener(
                            MaskEdit().mask(
                                text_perfil_cpf,
                                MaskEdit().FORMAT_CPF
                            )
                        )
                        text_perfil_date.addTextChangedListener(
                            MaskEdit().mask(
                                text_perfil_date,
                                MaskEdit().FORMAT_DATA
                            )
                        )
                    } catch (e: NullPointerException) {
                    }
                }
            } catch (e: NullPointerException) {
            }
        }
        getListeners()
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

    private fun getListeners() {
        configuracao_perfil.setOnClickListener(this)
        botao_editar_perfil.setOnClickListener(this)
        button_editar_nome_usuario.setOnClickListener(this)
        foto_perfil.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val id = view.id

        if (id == R.id.configuracao_perfil) {
            mConfiguracaoNext.goConfiguration()
        } else if (id == R.id.botao_editar_perfil) {

            if (isEnableSaveUsuario == false) {
                ativateEditTexts(true)
                isEnableSaveUsuario = true
                botao_editar_perfil.text = "Salvar"
            } else if (isEnableSaveUsuario == true) {
                val nome = text_perfil_nome.text.toString()
                val cpf = text_perfil_cpf.text.toString()
                val rg = text_perfil_rg.text.toString()
                val data = text_perfil_date.text.toString()
                val celular = text_perfil_celular.text.toString()
                val cep = text_perfil_cep.text.toString()
                val endereco = text_perfil_endereco.text.toString()
                val bairro = text_perfil_bairro.text.toString()
                val numero = text_perfil_numero.text.toString()
                val estado = text_perfil_estado.text.toString()
                val cidade = text_perfil_cidade.text.toString()

                usuarioAtual.nome = nome
                usuarioAtual.cpf = cpf
                usuarioAtual.rg = rg
                usuarioAtual.dataNascimento = data
                usuarioAtual.celular = celular
                usuarioAtual.cep = cep
                usuarioAtual.endereco = endereco
                usuarioAtual.bairro = bairro
                usuarioAtual.numero = numero
                usuarioAtual.estado = estado
                usuarioAtual.cidade = cidade

                Thread(Runnable {
                    mCadastroViewModel.update(usuarioAtual)
                    mCadastroShared.saveUser(usuarioAtual.email, usuarioAtual)
                }).start()
                ativateEditTexts(false)
                isEnableSaveUsuario = false
                botao_editar_perfil.text = "Editar"
            }
        } else if (id == R.id.button_editar_nome_usuario) {
            if (isEnableEditNameUser == false) {
                button_editar_nome_usuario.setImageResource(R.drawable.icon_info)
                isEnableEditNameUser = true
                text_nome_usuario_perfil.isEnabled = true
            } else if (isEnableEditNameUser == true) {
                val nomeUsuario = text_nome_usuario_perfil.text.toString()

                usuarioAtual.nomeUsuario = nomeUsuario

                Thread(Runnable {
                    mCadastroViewModel.update(usuarioAtual)
                }).start()
                button_editar_nome_usuario.setImageResource(R.drawable.editar)
                isEnableEditNameUser = false
                text_nome_usuario_perfil.isEnabled = false
            }
        } else if (id == R.id.foto_perfil) {
            dialog.box_add_photo.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                    startActivityForResult(intent, 0)
                    dialog.dismiss()
                }

            })
            dialog.box_remove_photo.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    Thread(Runnable {
                        mCadastroViewModel.removePhotoUser()
                    }).start()
                    foto_perfil.setImageResource(R.drawable.do_utilizador)
                    dialog.box_remove_photo.visibility = View.GONE
                    dialog.dismiss()
                }

            })
            dialog.show()
        }
    }

    //Faz o download da foto do perfil do usuario
    private fun downloadPhotoUser() {
        val user = auth.currentUser
        val uid = user!!.uid

        val dataStorage =
            storage.getReference().child("Upload").child(uid).child("fotouser" + uid + ".jpg")
        dataStorage.downloadUrl.addOnCompleteListener(object : OnCompleteListener<Uri> {
            override fun onComplete(task: Task<Uri>) {

                if (task.isSuccessful) {
                    try {
                        val url = task.result.toString()

                        Glide.with(context!!).asBitmap().load(url).into(foto_perfil)
                        dialog.box_remove_photo.visibility = View.VISIBLE
                    }catch (e : NullPointerException){}
                } else {
                    dialog.box_remove_photo.visibility = View.GONE
                }
            }
        })
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
                        text_nome_usuario_perfil.setText(usuarioAtual!!.nomeUsuario)
                        text_perfil_nome.setText(usuarioAtual.nome)
                        text_perfil_cpf.setText(usuarioAtual.cpf)
                        text_perfil_rg.setText(usuarioAtual.rg)
                        text_perfil_date.setText(usuarioAtual.dataNascimento)
                        text_perfil_celular.setText(usuarioAtual.celular)
                        text_perfil_cep.setText(usuarioAtual.cep)
                        text_perfil_endereco.setText(usuarioAtual.endereco)
                        text_perfil_bairro.setText(usuarioAtual.bairro)
                        text_perfil_numero.setText(usuarioAtual.numero)
                        text_perfil_estado.setText(usuarioAtual.estado)
                        text_perfil_cidade.setText(usuarioAtual.cidade)
                        text_perfil_celular.addTextChangedListener(
                            MaskEdit().mask(
                                text_perfil_celular,
                                MaskEdit().FORMAT_CELULAR
                            )
                        )
                        text_perfil_cpf.addTextChangedListener(
                            MaskEdit().mask(
                                text_perfil_cpf,
                                MaskEdit().FORMAT_CPF
                            )
                        )
                        text_perfil_date.addTextChangedListener(
                            MaskEdit().mask(
                                text_perfil_date,
                                MaskEdit().FORMAT_DATA
                            )
                        )
                    } catch (e: NullPointerException) {
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    //Hbilita todos os campos para o usuario atualizalos
    private fun ativateEditTexts(ativar: Boolean) {
        text_perfil_nome.isEnabled = ativar
        text_perfil_cpf.isEnabled = ativar
        text_perfil_rg.isEnabled = ativar
        text_perfil_date.isEnabled = ativar
        text_perfil_celular.isEnabled = ativar
        text_perfil_cep.isEnabled = ativar
        text_perfil_endereco.isEnabled = ativar
        text_perfil_bairro.isEnabled = ativar
        text_perfil_numero.isEnabled = ativar
        text_perfil_estado.isEnabled = ativar
        text_perfil_cidade.isEnabled = ativar
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                if (data != null) {
                    imageUri = data.data!!

                    Glide.with(context!!).asBitmap().load(imageUri).listener(object :
                        RequestListener<Bitmap> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            isFirstResource: Boolean,
                        ): Boolean {
                            return true
                        }

                        override fun onResourceReady(
                            resource: Bitmap?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean,
                        ): Boolean {
                            dialog.box_remove_photo.visibility = View.VISIBLE
                            Thread(Runnable {
                                mCadastroViewModel.savePhotoUser(
                                    BitmapDrawable(
                                        resources,
                                        resource
                                    )
                                )
                            }).start()
                                return false
                        }

                    }).into(foto_perfil)

                }
            }
        }
    }
}