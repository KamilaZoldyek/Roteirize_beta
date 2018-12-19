package com.example.kamilazoldyek.roteirize;

public class Cena {
    private String nome_cena;
    private String acao_cena;
    private String personagens_cena;
    private String local_cena;


    private String id_cena;
    private String caminho_cena;
    private String livro_cena;
    private String capitulo_cena;


    public Cena(String nome_cena,  String acao_cena,
                String local_cena,
                String personagens_cena,
                String caminho_cena,
                String livro_cena,
                String capitulo_cena) {

        setNome_cena(nome_cena);
        setAcao_cena(acao_cena);
        setPersonagens_cena(personagens_cena);
        setCaminho_cena(caminho_cena);
        setLocal_cena(local_cena);
        setLivro_cena(livro_cena);
        setCapitulo_cena(capitulo_cena);
    }

    public String getNome_cena() {
        return nome_cena;
    }

    public void setNome_cena(String nome_cena) {
        this.nome_cena = nome_cena;
    }

    public String getId_cena() {
        return id_cena;
    }

    public void setId_cena(String id_cena) {
        this.id_cena = id_cena;
    }

    public String getCaminho_cena() {
        return caminho_cena;
    }

    public void setCaminho_cena(String caminho_cena) {
        this.caminho_cena = caminho_cena;
    }

    public String getLivro_cena() {
        return livro_cena;
    }

    public void setLivro_cena(String livro_cena) {
        this.livro_cena = livro_cena;
    }

    public String getCapitulo_cena() {
        return capitulo_cena;
    }

    public String getAcao_cena() {
        return acao_cena;
    }

    public void setAcao_cena(String acao_cena) {
        this.acao_cena = acao_cena;
    }

    public String getPersonagens_cena() {
        return personagens_cena;
    }

    public void setPersonagens_cena(String personagens_cena) {
        this.personagens_cena = personagens_cena;
    }

    public String getLocal_cena() {
        return local_cena;
    }

    public void setLocal_cena(String local_cena) {
        this.local_cena = local_cena;
    }

    public void setCapitulo_cena(String capitulo_cena) {
        this.capitulo_cena = capitulo_cena;
    }
}
