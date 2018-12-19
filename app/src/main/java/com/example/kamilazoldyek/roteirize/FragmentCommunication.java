package com.example.kamilazoldyek.roteirize;

public interface FragmentCommunication {

        void respostaNotas(int position, String titulo, String corpo, String id);
        void respostaProjetos(int position, String titulo);
        void respostaCapitulos(int position, String nome_capitulo, String titulo_projeto, String escrito);
        void respostaPersonagens(int position,
                                 String titulo,
                                 String nome,
                                 String apelido,
                                 String sexo,
                                 String idade,
                                 String data,
                                 String descrição,
                                 String path);

        void respostaCenario(int position,
                                 String titulo,
                                 String nome,
                                 String descricao,
                                 String historia,
                                 String importancia,
                                 String path);

        void respostaCena(int position,
                          String nome_cena,
                          String acao,
                          String local,
                          String personagens,
                          String nome_capitulo,
                          String titulo_projeto,
                          String path);


}
