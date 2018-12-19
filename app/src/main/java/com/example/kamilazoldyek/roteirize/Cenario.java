package com.example.kamilazoldyek.roteirize;

public class Cenario {

    private String nome_cenario;
    private String descricao_cenario;
    private String historia_cenario;
    private String importancia_cenario;

    private String id_cenario;
    private String caminho_cenario;
    private String projeto_cenario;

    public Cenario(String nome_cenario, String descricao_cenario,
                   String historia_cenario, String importancia_cenario,
                   String caminho_cenario, String projeto_cenario) {
        setNome_cenario(nome_cenario);
        setCaminho_cenario(caminho_cenario);
        setDescricao_cenario(descricao_cenario);
        setHistoria_cenario(historia_cenario);
        setImportancia_cenario(importancia_cenario);
        setProjeto_cenario(projeto_cenario);
    }


    public String getNome_cenario() {
        return nome_cenario;
    }

    public void setNome_cenario(String nome_cenario) {
        this.nome_cenario = nome_cenario;
    }

    public String getDescricao_cenario() {
        return descricao_cenario;
    }

    public void setDescricao_cenario(String descricao_cenario) {
        this.descricao_cenario = descricao_cenario;
    }

    public String getHistoria_cenario() {
        return historia_cenario;
    }

    public void setHistoria_cenario(String historia_cenario) {
        this.historia_cenario = historia_cenario;
    }

    public String getImportancia_cenario() {
        return importancia_cenario;
    }

    public void setImportancia_cenario(String importancia_cenario) {
        this.importancia_cenario = importancia_cenario;
    }

    public String getId_cenario() {
        return id_cenario;
    }

    public void setId_cenario(String id_cenario) {
        this.id_cenario = id_cenario;
    }

    public String getCaminho_cenario() {
        return caminho_cenario;
    }

    public void setCaminho_cenario(String caminho_cenario) {
        this.caminho_cenario = caminho_cenario;
    }

    public String getProjeto_cenario() {
        return projeto_cenario;
    }

    public void setProjeto_cenario(String projeto_cenario) {
        this.projeto_cenario = projeto_cenario;
    }
}
