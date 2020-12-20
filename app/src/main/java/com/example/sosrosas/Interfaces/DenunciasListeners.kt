package com.example.sosrosas.Interfaces

import com.example.sosrosas.Model.ContatoAjuda

interface DenunciasListeners {
    fun goPageAddContato()
    fun goPageContato()
    fun goPageContatoExitInformacao()
    fun goPageInformacoes(contatoAjuda: ContatoAjuda)
}