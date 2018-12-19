package com.example.kamilazoldyek.roteirize;

import java.util.UUID;

public class Nota {

    private String titulo_nota;
    private String corpo_nota;
    private String id_nota;

    public Nota(String titulo_nota, String corpo_nota) {

//        this.titulo_nota = titulo_nota;
//        this.corpo_nota = corpo_nota;
//        this.id_nota = getId_nota();
        setTitulo_nota(titulo_nota);
        setCorpo_nota(corpo_nota);
        setId_nota(UUID.randomUUID().toString());

    }

    public Nota(String titulo_nota, String corpo_nota, String id) {

        setTitulo_nota(titulo_nota);
        setCorpo_nota(corpo_nota);
        setId_nota(id);
    }




    public void updateNote(String content){
        setCorpo_nota(content);
    }

    public String toStringFormat(){
        return String.format("[Nota #%s] %s", id_nota, titulo_nota);
    }

    public String getId_nota() {
        return id_nota;
    }

    public void setId_nota(String id_nota) {
        this.id_nota = id_nota;
    }

    public String getTitulo_nota() {
        return titulo_nota;
    }

    public void setTitulo_nota(String titulo_nota) {
        this.titulo_nota = titulo_nota;
    }

    public String getCorpo_nota() {
        return corpo_nota;
    }

    public void setCorpo_nota(String corpo_nota) {
        this.corpo_nota = corpo_nota;
    }
}
