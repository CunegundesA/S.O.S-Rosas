package com.example.sosrosas.Model

import android.content.Context
import android.widget.Toast

class ErroNotification {

    fun getErroNotification(context: Context, resposta : String){
        if (resposta.contains("least 6 characters")){

            Toast.makeText(context,"Digite uma senha maior que 5 characters",Toast.LENGTH_LONG).show()

        }
        else if(resposta.contains("address is badly")){

            Toast.makeText(context,"E-mail inválido",Toast.LENGTH_LONG).show()

        }
        else if(resposta.contains("interrupted connection")){

            Toast.makeText(context,"Sem conexão com o Firebase",Toast.LENGTH_LONG).show()

        }else if(resposta.contains("password is invalid")){

            Toast.makeText(context,"senha invalida",Toast.LENGTH_LONG).show()

        } else if(resposta.contains("There is no user")){

            Toast.makeText(context,"Este e-mail não está cadastrado",Toast.LENGTH_LONG).show()

        }
        else if(resposta.contains("address is already")){

            Toast.makeText(context,"E-mail já existe cadastrado",Toast.LENGTH_LONG).show()

        }
        else if(resposta.contains("INVALID_EMAIL")){

            Toast.makeText(context,"E-mail inválido",Toast.LENGTH_LONG).show()

        }
        else if(resposta.contains("EMAIL_NOT_FOUND")){

            Toast.makeText(context,"E-mail não cadastrado ainda",Toast.LENGTH_LONG).show()

        } else{
            Toast.makeText(context,resposta, Toast.LENGTH_LONG).show()
        }
    }


}