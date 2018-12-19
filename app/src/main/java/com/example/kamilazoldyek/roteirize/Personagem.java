package com.example.kamilazoldyek.roteirize;

import java.util.UUID;

public class Personagem {

    private String nome_personagem;
    private String apelido_personagem;
    private String sexo_personagem;
    private String idade_personagem;
    private String datan_personagem;
    private String desc_personagem;
    private String livro_personagem;
    private String caminho_personagem;


    private String id_personagem;

    public Personagem(String nome_personagem, String id_personagem
                      ) {
        setNome_personagem(nome_personagem);
        setId_personagem(id_personagem);

    }

    public Personagem(String livro_personagem, String nome_personagem, String apelido_personagem, String sexo_personagem,
                      String idade_personagem, String datan_personagem,
                      String desc_personagem, String caminho_personagem) {
        setLivro_personagem(livro_personagem);
        setNome_personagem(nome_personagem);
        setApelido_personagem(apelido_personagem);
        setSexo_personagem(sexo_personagem);
        setIdade_personagem(idade_personagem);
        setDatan_personagem(datan_personagem);
        setDesc_personagem(desc_personagem);
        setCaminho_personagem(caminho_personagem);


        setId_personagem(UUID.randomUUID().toString());
    }

    public String getCaminho_personagem() {
        return caminho_personagem;
    }

    public void setCaminho_personagem(String caminho_personagem) {
        this.caminho_personagem = caminho_personagem;
    }

    public String getLivro_personagem() {
        return livro_personagem;
    }

    public void setLivro_personagem(String livro_personagem) {
        this.livro_personagem = livro_personagem;
    }

    public String getNome_personagem() {
        return nome_personagem;
    }

    public void setNome_personagem(String nome_personagem) {
        this.nome_personagem = nome_personagem;
    }
    public String getId_personagem() {
        return id_personagem;
    }

    public void setId_personagem(String id_personagem) {
        this.id_personagem = id_personagem;
    }

    public String getApelido_personagem() {
        return apelido_personagem;
    }

    public void setApelido_personagem(String apelido_personagem) {
        this.apelido_personagem = apelido_personagem;
    }

    public String getSexo_personagem() {
        return sexo_personagem;
    }

    public void setSexo_personagem(String sexo_personagem) {
        this.sexo_personagem = sexo_personagem;
    }

    public String getIdade_personagem() {
        return idade_personagem;
    }

    public void setIdade_personagem(String idade_personagem) {
        this.idade_personagem = idade_personagem;
    }

    public String getDatan_personagem() {
        return datan_personagem;
    }

    public void setDatan_personagem(String datan_personagem) {
        this.datan_personagem = datan_personagem;
    }

    public String getDesc_personagem() {
        return desc_personagem;
    }

    public void setDesc_personagem(String desc_personagem) {
        this.desc_personagem = desc_personagem;
    }
}
