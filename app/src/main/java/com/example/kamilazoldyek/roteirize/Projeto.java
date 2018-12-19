package com.example.kamilazoldyek.roteirize;

import java.util.UUID;

public class Projeto {

    private String titulo_projeto;
    private String descricao_projeto;
    private String id_projeto;

    // TODO aqui tem que ter listas de personagens e varias coisas


    public Projeto(String titulo_projeto, String descricao_projeto){
//        this.titulo_projeto = titulo_projeto;
//        this.descricao_projeto = descricao_projeto;
        setTitulo_projeto(titulo_projeto);
        setDescricao_projeto(descricao_projeto);
        setId_projeto(UUID.randomUUID().toString());

    }

    public Projeto(String titulo_projeto, String descricao_projeto, String id_projeto){
//        this.titulo_projeto = titulo_projeto;
//        this.descricao_projeto = descricao_projeto;
        setTitulo_projeto(titulo_projeto);
        setDescricao_projeto(descricao_projeto);
        setId_projeto(id_projeto);

    }


    public String getId_projeto() {
        return id_projeto;
    }

    public void setId_projeto(String id_projeto) {
        this.id_projeto = id_projeto;
    }

    public String getTitulo_projeto() {
        return titulo_projeto;
    }

    public String getDescricao_projeto() {
        return descricao_projeto;
    }

    public void setTitulo_projeto(String titulo_projeto) {
        this.titulo_projeto = titulo_projeto;
    }

    public void setDescricao_projeto(String descricao_projeto) {
        this.descricao_projeto = descricao_projeto;
    }
}

