package com.example.kamilazoldyek.roteirize;

import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class ResumoFragment extends Fragment {


    private TextView projeto_nomeTV, projeto_descricaoTV,
            personagensTV,cenariosTV, capitulosCenasTV, metaTV;
    private  LinearLayout pdflayout, resumoLayout;
    private File diretorioProjeto, diretorioPersonagens,
    diretorioCenarios, diretorioCapitulos;
    private String tituloProjeto, descricaoProjeto, spinnerProjeto;
    private int metaProjeto;
    private Context context;
    private LocalData localData;
    private NestedScrollView resumoScrollView;
    private ProjetosListaItemOpcoesFragment projetosListaItemOpcoesFragment;


    public ResumoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tituloProjeto = getArguments().getString("TITULO");
        descricaoProjeto = getArguments().getString("DESCRICAO");
        metaProjeto = getArguments().getInt("META");
        spinnerProjeto = getArguments().getString("SPINNER");


        context = getContext();
        localData = new LocalData(context);
        projetosListaItemOpcoesFragment = new ProjetosListaItemOpcoesFragment();

        diretorioProjeto = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS + "/"
                + tituloProjeto);
        diretorioPersonagens = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS + "/"
                + tituloProjeto + Constantes.PERSONAGENS);
        diretorioCapitulos = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS + "/"
                + tituloProjeto + Constantes.CAPITULOS);
        diretorioCenarios = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS + "/"
                + tituloProjeto + Constantes.CENARIOS);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.resumo_fragment, container, false);
        getActivity().setTitle("Resumo - "+ tituloProjeto);



        projeto_nomeTV = v.findViewById(R.id.resuno_nome_projeto);
        projeto_descricaoTV = v.findViewById(R.id.resumo_descricao);
        metaTV = v.findViewById(R.id.resumo_meta);
        personagensTV = v.findViewById(R.id.resumo_personagens);
        cenariosTV = v.findViewById(R.id.resumo_cenarios);
        capitulosCenasTV = v.findViewById(R.id.resumo_cenas);

        pdflayout = v.findViewById(R.id.resumo_pdf_layout);
        resumoLayout = v.findViewById(R.id.resumoLayout);
        resumoScrollView = v.findViewById(R.id.resumoScrollView);

        projeto_nomeTV.setText(tituloProjeto);
        projeto_descricaoTV.setText(descricaoProjeto);
        metaTV.setText("Meta do projeto: "+metaProjeto+" "+spinnerProjeto);
        personagensTV.setText(carregarPersonagens());
        cenariosTV.setText(carregarCenarios());
        capitulosCenasTV.setText(carregarCenas());

        pdflayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    salvaPDF();
                }else{
                    salvaPDFMarshmallow();
                }
            }
        });


        return v;
    }

    private String carregarPersonagens(){

        File[] files = diretorioPersonagens.listFiles();
        String theFile;
        String content = "";
        StringBuilder conteudoTotal = new StringBuilder();

        if (diretorioPersonagens.exists()) {
            if (diretorioPersonagens.length() > 0) {
                for (int f = 0; f < files.length; f++) {
                    theFile = files[f].getName();
                    File file = new File(diretorioPersonagens, theFile);
                    if(file.exists()){
                        StringBuilder sb = new StringBuilder();
                        try {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String line;
                            while ((line=br.readLine()) != null) {

                                sb.append(line);
                                sb.append("\n");
                            }
                            content = sb.toString();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "file not found", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "ioexception", Toast.LENGTH_SHORT).show();
                        }
                    }

                    conteudoTotal.append("\n");
                    conteudoTotal.append(" › "+formataTitulo(theFile));
                    conteudoTotal.append("\n\n");
                    conteudoTotal.append(formataPersonagem(content));

                }
            } else if (diretorioPersonagens.length()==0){
                //add traços;
                conteudoTotal.append("Ainda não há personagens para este projeto");
            }
        }
        return conteudoTotal.toString();

    }
    private String carregarCenarios(){
        File[] files = diretorioCenarios.listFiles();
        String theFile;
        String content = "";
        StringBuilder conteudoTotal = new StringBuilder();

        if (diretorioCenarios.exists()) {
            if (diretorioCenarios.length() > 0) {
                for (int f = 0; f < files.length; f++) {
                    theFile = files[f].getName();
                    File file = new File(diretorioCenarios, theFile);
                    if(file.exists()){
                        StringBuilder sb = new StringBuilder();
                        try {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String line;
                            while ((line=br.readLine()) != null) {

                                sb.append(line);
                                sb.append("\n");
                            }
                            content = sb.toString();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "file not found", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "ioexception", Toast.LENGTH_SHORT).show();
                        }
                    }

                    conteudoTotal.append("\n");
                    conteudoTotal.append(" › "+formataTitulo(theFile));
                    conteudoTotal.append("\n\n");
                    conteudoTotal.append(formataCenario(content));

                }
            } else if (diretorioCenarios.length()==0){
                //add traços;
                conteudoTotal.append("Ainda não há cenários para este projeto");
            }
        }
        return conteudoTotal.toString();


    }
    private String carregarCenas(){

        File[] files = diretorioCapitulos.listFiles();

        String nomeCena, nomeCapitulo, diretorioCapitulosPATH;
        String content = "";
        String parcial = "";
        diretorioCapitulosPATH = diretorioCapitulos.getAbsolutePath();


        StringBuilder conteudoTotal = new StringBuilder();
        StringBuilder conteudoParcial = new StringBuilder();

        if (diretorioCapitulos.exists()) {

            if (diretorioCapitulos.length() > 0) {

                for (int g = 0; g < files.length; g++){
                    nomeCapitulo = files[g].getName();

                    File scenesDirectory = new File(diretorioCapitulosPATH+"/"
                            +nomeCapitulo+"/"+Constantes.CENAS);

                    File[] scenes = scenesDirectory.listFiles();

                    for (int f = 0; f < scenes.length; f++) {
                        nomeCena = scenes[f].getName();

                        File file = new File(scenesDirectory, nomeCena);

                        if(file.exists()){
                            StringBuilder sb = new StringBuilder();
                            try {
                                BufferedReader br = new BufferedReader(new FileReader(file));
                                String line;
                                while ((line=br.readLine()) != null) {
                                    sb.append(line);
                                    sb.append("\n");
                                }
                                content = sb.toString();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "file not found", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "ioexception", Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (content.equalsIgnoreCase("\n")) {
                            //add traços;
                            conteudoTotal.append("Ainda não há cenas para este capítulo");
                        }


                        conteudoParcial.append("\n");
                        conteudoParcial.append(" › "+formataTitulo(nomeCena));
                        conteudoParcial.append("\n");
                        conteudoParcial.append(formataCena(content));
                        parcial = conteudoParcial.toString();

                    }
                    conteudoTotal.append("\n");
                    conteudoTotal.append(" › "+nomeCapitulo + "\n");
                    conteudoTotal.append(parcial);

                }


            } else if (diretorioCapitulos.length()==0){
                //add traços;
                conteudoTotal.append("Ainda não há capítulos para este projeto");
            }
        }
        return conteudoTotal.toString();

    }
    private String formataTitulo(String titulo){
        StringTokenizer tokens = new StringTokenizer(titulo, "«");
        String first = tokens.nextToken();
        return first;
    }

    private String formataPersonagem(String content){
        StringTokenizer tokens = new StringTokenizer(content, "«");
        String um = tokens.nextToken();
        String dois = tokens.nextToken();
        String tres = tokens.nextToken();
        String quatro = tokens.nextToken();
        String cinco = tokens.nextToken();
        String seis = tokens.nextToken();
        String sete = tokens.nextToken();
        String oito = tokens.nextToken();
        String nove = tokens.nextToken();
        String dez = tokens.nextToken();
        String onze = tokens.nextToken();
        String doze = tokens.nextToken();
        String resultado = "▪ Nome: "+dois
                +"\n" + "▪ Apelido: "+quatro
                +"\n" + "▪ Gênero: "+seis
                +"\n" + "▪ Idade: "+oito
                +"\n" + "▪ Data e local de nascimento: "+dez
                +"\n" + "▪ Descrição: "+doze
                +"\n";

        return resultado;
    }
    private String formataCena(String content){
        StringTokenizer tokens = new StringTokenizer(content, "«");
        String um = tokens.nextToken();
        String dois = tokens.nextToken();
        String tres = tokens.nextToken();
        String quatro = tokens.nextToken();
        String cinco = tokens.nextToken();
        String seis = tokens.nextToken();

        String resultado = "▪ O que acontece nessa cena: "+dois
                +"\n" + "▪ Cenário: "+quatro
                +"\n" + "▪ Personagens: "+seis
                +"\n";

        return resultado;
    }
    private String formataCenario(String content){
        StringTokenizer tokens = new StringTokenizer(content, "«");

        String um = tokens.nextToken();
        String dois = tokens.nextToken();
        String tres = tokens.nextToken();
        String quatro = tokens.nextToken();
        String cinco = tokens.nextToken();
        String seis = tokens.nextToken();
        String sete = tokens.nextToken();
        String oito = tokens.nextToken();




        String resultado = "▪ Descrição do lugar: "+quatro
                +"\n" + "▪ História do lugar: "+seis
                +"\n" + "▪ Importância desse lugar para o projeto: "+oito
                +"\n";

        return resultado;
    }

    public void salvaPDFMarshmallow(){
        PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo pageInfo;
        pageInfo = new PdfDocument.PageInfo.Builder(resumoScrollView.getChildAt(0).getWidth(), resumoScrollView.getChildAt(0).getHeight(), 1).create();

        PdfDocument.Page page = document.startPage(pageInfo);

        resumoLayout.draw(page.getCanvas());

        document.finishPage(page);

         File text_file = new File(diretorioProjeto, tituloProjeto + ".pdf");

        try {
            FileOutputStream fos = new FileOutputStream(text_file);
            document.writeTo(fos);
            document.close();
            fos.close();
        } catch (IOException kamis2) {
            kamis2.printStackTrace();
            Log.e("KAMIS", "ops", kamis2);
        }
        Toast.makeText(getContext(), "Salvo em " + text_file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        try {

            Intent mShareIntent = new Intent(Intent.ACTION_SEND);
            mShareIntent.setType("application/pdf");

                mShareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + text_file.getAbsolutePath()));
                mShareIntent.putExtra(Intent.EXTRA_SUBJECT, "Compartilhando " + tituloProjeto);
                mShareIntent.putExtra(Intent.EXTRA_TEXT, "Compartilhando " + tituloProjeto);

            startActivity(Intent.createChooser(mShareIntent, "Compartilhar"));
        } catch (Exception kamis) {
            kamis.printStackTrace();
            Log.e("KAMIS", "deu ruim", kamis);
        }

    }

    public void salvaPDF() {

        PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo pageInfo;
        pageInfo = new PdfDocument.PageInfo.Builder(resumoScrollView.getChildAt(0).getWidth(), resumoScrollView.getChildAt(0).getHeight(), 1).create();

        PdfDocument.Page page = document.startPage(pageInfo);

        resumoLayout.draw(page.getCanvas());

        document.finishPage(page);

//        projeto_descricaoTV.draw(page.getCanvas());
//        metaTV.draw(page.getCanvas());
//        personagensTV.draw(page.getCanvas());
//        cenariosTV.draw(page.getCanvas());
//        capitulosCenasTV.draw(page.getCanvas());
//        document.finishPage(page);


        // File text_file = new File(diretorioProjeto, tituloProjeto + ".pdf");
        File path = new File(getContext().getFilesDir(), "Roteirize");
        path.mkdirs();
        File text_file = new File(path, tituloProjeto+".pdf");

        try {
            FileOutputStream fos = new FileOutputStream(text_file);
            document.writeTo(fos);
            document.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getContext(), "Salvo em " + text_file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        try {

            Uri contentUri = FileProvider.getUriForFile(context, "com.example.kamilazoldyek.roteirize.provider", text_file);

            Intent mShareIntent = new Intent(Intent.ACTION_SEND);
            mShareIntent.setType("application/pdf");

                mShareIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                mShareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mShareIntent.putExtra(Intent.EXTRA_SUBJECT, "Compartilhando " + tituloProjeto);
                mShareIntent.putExtra(Intent.EXTRA_TEXT, "Compartilhando " + tituloProjeto);
                mShareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
//                Toast.makeText(getContext(), contentUri.getPath(), Toast.LENGTH_LONG).show();

            startActivity(Intent.createChooser(mShareIntent, "Compartilhar"));
        } catch (Exception kamis) {
            kamis.printStackTrace();
            Log.e("KAMIS", "deu ruim", kamis);
        }
    }
}
