package com.example.kamilazoldyek.roteirize;

public class Capitulo {

    private String titulo_capitulo;

    private String projeto_capitulo;
    private String caminho_capitulo;
    private String id_cenario;


    public Capitulo(String titulo_capitulo, String projeto_capitulo, String caminho_capitulo) {
        setTitulo_capitulo(titulo_capitulo);
        setProjeto_capitulo(projeto_capitulo);
        setCaminho_capitulo(caminho_capitulo);
    }

    public String getTitulo_capitulo() {
        return titulo_capitulo;
    }

    public void setTitulo_capitulo(String titulo_capitulo) {
        this.titulo_capitulo = titulo_capitulo;
    }

    public String getProjeto_capitulo() {
        return projeto_capitulo;
    }

    public void setProjeto_capitulo(String projeto_capitulo) {
        this.projeto_capitulo = projeto_capitulo;
    }

    public String getCaminho_capitulo() {
        return caminho_capitulo;
    }

    public void setCaminho_capitulo(String caminho_capitulo) {
        this.caminho_capitulo = caminho_capitulo;
    }

    public String getId_cenario() {
        return id_cenario;
    }

    public void setId_cenario(String id_cenario) {
        this.id_cenario = id_cenario;
    }
}
