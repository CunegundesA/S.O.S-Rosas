package com.example.sosrosas.Model

class ContatoAjuda(
    var id : String = "",
    var emailContato: String = "",
    var nome: String = "",
    var nomeLowerCase: String = nome.toString().toLowerCase(),
    var celular: String = ""
)