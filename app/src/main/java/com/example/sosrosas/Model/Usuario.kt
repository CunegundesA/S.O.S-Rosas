package com.example.sosrosas.Model

import android.widget.EditText
import java.sql.Date

data class Usuario(
    var email: String = "",
    var senhaFalsa: String = "",
    var nome: String = "",
    var nomeUsuario: String = "",
    var cpf: String = "",
    var rg: String = "",
    var dataNascimento: String = "",
    var celular: String = "",
    var cep: String = "",
    var endereco: String = "",
    var bairro: String = "",
    var numero: String = "",
    var estado: String = "",
    var cidade: String = ""
)