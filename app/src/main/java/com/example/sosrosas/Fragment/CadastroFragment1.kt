package com.example.sosrosas.Fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.sosrosas.Interfaces.CadastroListeners
import com.example.sosrosas.Model.MaskEdit
import com.example.sosrosas.R
import kotlinx.android.synthetic.main.activity_cadastro_page1.*
import kotlinx.android.synthetic.main.activity_cadastro_page1.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.box_info_fake_password.*
import kotlinx.android.synthetic.main.box_termos_de_uso.*

class CadastroFragment1() : Fragment(), View.OnClickListener {

    private lateinit var mCadastroNext: CadastroListeners
    private lateinit var imageUri : Uri
    private var isSetImage = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root: ViewGroup =
            inflater.inflate(R.layout.activity_cadastro_page1, container, false) as ViewGroup

        root.text_celular.addTextChangedListener(MaskEdit().mask(root.text_celular, MaskEdit().FORMAT_CELULAR))
        root.text_cpf.addTextChangedListener(MaskEdit().mask(root.text_cpf, MaskEdit().FORMAT_CPF))
        root.text_data_nascimento.addTextChangedListener(MaskEdit().mask(root.text_data_nascimento, MaskEdit().FORMAT_DATA))

        return root
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            mCadastroNext = activity as CadastroListeners
        } catch (e: ClassCastException) {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getListeners()
    }

    // Pega todos os ouvintes
    private fun getListeners() {
        button_avanca.setOnClickListener(this)
        image_cadastro_usuario.setOnClickListener(this)
        button_remove_image.setOnClickListener(this)
        info_fake_pass.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.button_avanca) {
            if (validation()) {
                if(validationCaracteres()) {
                    mCadastroNext.goPage2(isSetImage)
                }
            }
        }else if(id == R.id.image_cadastro_usuario){
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)

            startActivityForResult(intent, 0)
        }else if(id == R.id.button_remove_image){
            isSetImage = false
            image_cadastro_usuario.setImageResource(R.drawable.utilizador_alterar)
            button_remove_image.visibility = View.GONE
        }else if(id == R.id.info_fake_pass){
            val dialog = Dialog(context!!)
            dialog.setContentView(R.layout.box_info_fake_password)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.button_ok_info_fake_pass.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    dialog.dismiss()
                }

            })
            dialog.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK){
            if(requestCode == 0){
                if(data != null){
                    imageUri = data.data!!
                    Glide.with(context!!).asBitmap().load(imageUri).listener(object : RequestListener<Bitmap>{
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
                            isSetImage = true
                            button_remove_image.visibility = View.VISIBLE
                            return false
                        }

                    }).into(image_cadastro_usuario)
                }
            }
        }
    }

    //Valida caso todos os campos estejam preenchidos
    private fun validation(): Boolean {
        val name = text_name.text.toString()
        val nameUser = text_name_user.text.toString()
        val email: String = text_email.text.toString().trim()
        val password = text_password.text.toString()
        val confirmPassword = text_confirm_password.text.toString()
        val senhaFalsa = text_fake_password.text.toString()
        val cpf = text_cpf.text.toString()
        val rg = text_rg.text.toString()
        val celular = text_celular.text.toString()

        if (name != "" && nameUser != "" && email != "" && cpf != "" && rg != "" && celular != "" && password != "" && confirmPassword != "") {
            if(password == confirmPassword) {
                if(password != senhaFalsa){
                    return true
                }else{
                    Toast.makeText(context, "Erro!, a senha e a senha falsa são iguais, por favor deixe-as diferente.", Toast.LENGTH_LONG).show()
                    return false
                }
            }else{
                Toast.makeText(context, "Erro na confirmação de senha!", Toast.LENGTH_SHORT).show()
                return false
            }
        }else {
            Toast.makeText(context, "Preencha todos os campos, por favor", Toast.LENGTH_SHORT)
                .show()
            return false
        }
    }

    //Valida caso a senha tenha 8 ou mais caracteres
    private fun validationCaracteres(): Boolean{
        val password = text_password.text.toString()
        val senhaFalsa = text_fake_password.text.toString()
        if(password.length < 8 || senhaFalsa.length < 8){
            Toast.makeText(context, "A senha e a senha falsa devem ter pelo menos 8 caracteres!", Toast.LENGTH_SHORT).show()
            return false
        }else{
            return true
        }
    }

}