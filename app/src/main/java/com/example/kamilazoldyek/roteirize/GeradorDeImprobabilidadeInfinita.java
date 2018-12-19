package com.example.kamilazoldyek.roteirize;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class GeradorDeImprobabilidadeInfinita {

    public static String HEADLINES[]={
            "Já escreveu hoje?",
            "Hora de escrever!",
            "Seu projeto está esperando!",
            "Não se esqueça de escrever!"
    };

    public static String TAGLINES[]={
            "Vai deixar seus leitores esperando?",
            "Um parágrafo já basta!",
            "Um parágrafo é melhor do que nada!",
            "Confira seu planejamento e comece!",
            "Não precisa de inspiração para escrever!",
            "Só mais um pouco para alcançar sua meta!",
            "Seu livro não vai se escrever sozinho!"
    };


    public static String titulos[]={
            "Enigma", "Enigma do Amanhã", "O vôo do pássaro","A vida de T. D. Brian","Marte não está morta",
            "Mundos Paralelos","Perdidos","O que se esconde atrás do espelho","Ame intensamente","Luar","Viver",
            "A Lenda dos Sete Cavaleiros","Os Mundos de Lucas","A Visão", "A Invasão", "As Sombras", "Vitórias Cotidianas", "Não Deixe de Lutar",
            "Cavaleiros de Júpiter", "Caminho Perdido", "Lutas do Amanhã"
    };

    public  static String nomes[] = {
            "Lucille", "Ana", "Miguel", "Lucas","Guilherme", "Lívia",
            "Carla", "Bianca","Erick", "Cristina","Lucca", "Luísa",
            "Liz", "Aisha","Grace", "Florence",
            "Maitee", "Olívia","Rayssa", "Levi","Pietro", "Deborah",
            "Louise", "Cesar","Isaac", "Jacob","Cecilia", "Alice",
            "Valentin", "Helen",
            "Icaro", "Henry","Antônio", "Ricardo","Richard",
            "Henrique","Gael", "Vincent","Benjamin", "Augusto","Dante",
            "Bruno","Felipe", "David", "Stern",
            "Peter","Christopher", "Jacquelin","Marina", "Eduarda",
            "Caio", "Gabriel","André", "Damara", "Amelia", "Ada", "Theo"


    };

    public  static String sobrenomes[] = {
            "Araújo", "Abreu", "Lima", "Duarte","Oak", "Smith",
            "Oswald", "de Lima","Braga", "Angelli","Fernandes",
            "Alves","Pereira", "Bernoulli",
            "de Souza", "Mathias","Figueiredo", "Cunha","Zandonadi",
            "Calliman","Zannitti", "Marques",
            "Nunes", "Ambrizio","Brioschi", "Laus","Trost", "Drachenberg",
            "Gaigher", "Olsen","Andersen", "Klein","Bordin", "de Moraes",
            "Vianna", "Welch",
            "Castro","Filletti", "Nielson","Hawking", "Marangon","Lupino", "Farias",
    };

    public  static String generos[] = {
            "Mulher", " Homem", "Mulher cisgênero", "Mulher transgênero", "Mulher", " Homem", "Homem cisgênero", "Homem Transgênero",
            "Não determinado", "Intersexo", "Mulher", " Homem", "Agênero", "Gênero neutro", "Mulher", " Homem"
    };

    public  static String caracteristicasA[] = {
            "Gosta de gatos", "Ama escrever", "Tem medo do desconhecido", "Cor preferida é azul","É gentil",
            "Possui sentidos aguçados","É sensível", "Gosta de chá","Gosta de café", "Costuma ser sensato às vezes",
            "Ama alguém em segredo", "Sabe tocar um instrumento", "Quer ser sempre melhor", "Possui filhos", "Não quer ter filhos", "Quer ter filhos",
            "Não gosta de gatos",  "Odeia livros", "Não tem medo do desconhecido", "Cor preferida é verde","É bruto e ríspido com todo mundo",
            "Gosta de arco e flecha","É insensível", "Não gosta de chá","Não gosta de café", "Irresponsável","É casado", "É divorciado","É viúvo",
            "É solteiro","É calmo e verdadeiro",
            "Diz que odeia todo mundo", "Antissocial", "Não liga pra opinião alheia", "Engajado em causas sociais", "Vegano","Já cometeu um crime"
    };

    public  static String caracteristicasB[] = {
            "Não gosta de fofocas", "Tem um vício", "Sofreu um trauma na infância", "É fechado emocionalmente",
            "Não é muito atraente", "Indeciso","É ansioso",
            "Presenciou um assassinato",
            "Gosta de fofocas", "Não tem vícios",   "Não é atraente", "Extremamente decidido",
            "É muito atraente", "Indeciso","É ansioso", "Tem uma história terrível para contar", "Gosta de boas histórias",
            "Presenciou um assassinato","Já foi preso", "Gosta de videogames", "Não gosta de videogames"
    };

    public  static String caracteristicasC[]={
            "Tem cicatrizes", "Não tem Cicatrizes", "Cabelo crespo, preto",
             "Pele negra","Pele parda", "Pele caucasiana", "Traços filipinos","Traços japoneses", "Traços chineses","Alienígena", "Possui uma necessidade especial",
            "Gordo","Magro", "Alto", "Baixo","Olhos azuis", "Olhos verdes", "Olhos castanhos","Olhos cinzentos", "Olhos pretos",
            "Cabelo preto","Cabelo ruivo","Cabelo loiro","Cabelo castanho","Cabelo verde","Cabelo azul","Cabelo roxo","Cabelo vermelho",
            "Cabelo arco-íris","Cabelo ondulado","Cabelo liso","Cabelo cacheado","Careca","Cabelo curto","Cabelo longo",
            "Cabelo de tamanho médio"
    };

    public  static String caracteristicasD[]={
            "Tem medo do escuro", "Tem medo de altura","Tem medo do oceano", "Tem medo do espaço", "Tem medo de descobrirem seu segredo",
            "Tem medo de perder alguém", "Tem medo de aranhas", "Claustrofóbico", "Tem agorafobia", "Tem síndrome do pânico", "Tem medo de insetos",
            "Tem medo de baratas", "Tem medo de ficar sozinho", "Tem medo de morrer pelo que é", "Tem medo de morrer"
    };

    public  static String lugares[]={
            "Cydonia, Marte", "Tokyo, Japão",  "Londres, Inglaterra",
            "Manhattan, New York", "Hell's Kitchen, New York", "Belo Horizonte, Brasil",
            "Curitiba, Brasil", "ISS - Estação Espacial Internacional", "Vilarejo perdido de Hausgurt",
            "Montanhas Sombrias do Norte", "Montanhas do Sul", "Floresta de Hilbert",
            "Condado de York", "Beco das baixadas", "Bar Fim do Mundo", "Terras Perdidas"

    };
    public  static String caracteristicasLugaresA[] = {
            "Costuma ser calmo quando as Lesmas Venusianas não estão atacando", "Depois do surto populacional em 2125, está insuportável morar lá",
            "Não é o lugar mais aconchegante do mundo, mas atende muito bem se você tiver dinheiro", "Cuidado com os nativos",
            "Os salários já foram melhores aqui", "Ótimos salários", "Perfeito para caçar alienígenas", "Fora os terremotos, é um bom lugar",
            "Confortável", "3 estrelas", "Lugarzinho meia-boca", "Lar da Ordem dos Magos Superiores de Eisen"

    };

    public  static String caracteristicasLugaresB[]={
                "Cuidado com os Kaesarianos que invadiram o lugar", "Tem parques lindos", "A floresta tem lobos, tome cuidado",
                "Eu não recomendaria andar à noite por lá", "Os mercenários são caros e arrogantes", "A vista é linda",
                "Só tem prédios", "Os trens são rápidos", "Tem poucos ônibus",
                "O próximo Entreposto Espacial fica a 30 anos-luz daqui", "Lugar cheio de gente ruim das ideias",
                "Se você vier de fora, ninguém vai confiar em você"
    };

    public  static String objetivos[]={
            "se casar", "ter filhos", "ser rico", "ser famoso", "ser um grande engenheiro","só sobreviver a mais um dia","ter um carro",
            "pagar suas contas","dormir em paz","se vingar do assassino de sua irmã","plantar verduras no quintal","escrever um livro",
            "tirar férias", "conseguir um emprego", "arrumar uma namorada", "comprar uma casa em Marte", "recuperar sua fortuna",
            "ser popular na escola"

    };

    public  static String problemas[]={
            "a invasão alienígena", "o fim do mundo", "o apocalipse zumbi", "a terceira guerra mundial", "a invasão dos bandidos a sua casa",
            "o assalto", "a invasão", "a pane nos equipamentos", "a luz acaba", "a água acaba", "a festa do vizinho", "a demissão em massa",
            "a nova ordem mundial", "o acidente de carro", "o ataque", "o desembarque dos Magos das Trevas nas terras de Lihen",
            "o fim do acordo de paz entre os reinos de Yuag e Rhak", "o saque à vila de Indak"

    };

    public  static String ehUm[]={
            "humano", "alienígena", "venusiano", "desempregado", "presidiário", "cara normal", "sujeito simples", "mercenário", "caçador",
            "milionário", "viciado em heroína", "amante dos animais", "político", "CEO de uma empresa de TI", "professor", "astronauta",
            "professor de línguas", "universitário", "estudante do ensino médio", "professor de história", "programador", "escritor", "artista",
            "músico", "desenhista", "fracassado", "elfo", "ferreiro", "açougueiro", "mago", "guerreiro", "Mago Supremo"

    };

    public static String historias[]={
            "Lugar assombrado pelo espírito de um antigo general",
            "Cidade natal da tia Ana",
            "Famoso entreposto comercial",
            "Houve uma grande batalha neste lugar",
            "Conhecido pelas flores",
            "Conhecido pela comida tradicional"
    };

    public static String importancia[]={
            "Aqui é onde os eventos principais acontecem",
            "Onde há a cena de batalha do capítulo 15",
            "Aqui é onde o protagonista pede a mão da namorada",
            "Onde acontece o acidente",
            "Casa do protagonista, a maioria das cenas se passam aqui",
            "Onde os mercenários se encontram para oferecer seus serviços"
    };




    public  static String fullNameGenerator() {

        int indexName = randBetween(0, nomes.length-1);
        int indexSurname = randBetween(0, sobrenomes.length-1);

        return nomes[indexName] + " " + sobrenomes[indexSurname];

    }


    public  static String birthDateGenerator(){
        String dataFormatada;
        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(1900, 2100);
        int day = randBetween(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR));

        gc.set(gc.YEAR, year);
        gc.set(gc.DAY_OF_YEAR, day);

        String birthlocal = placesGenerator();

        dataFormatada = gc.get(gc.DAY_OF_MONTH) + "/" + gc.get(gc.MONTH)+ "/" + gc.get(gc.YEAR)+ " em " + birthlocal;


        return dataFormatada;
    }

    public  static String personFeaturesGenerator() {
        int indexA = randBetween(0, caracteristicasA.length-1);
        int indexB = randBetween(0, caracteristicasB.length-1);
        int indexC = randBetween(0, caracteristicasC.length-1);
        int indexD = randBetween(0, caracteristicasD.length-1);

        return caracteristicasA[indexA] + ". " + caracteristicasB[indexB] + ". "+caracteristicasC[indexC] + ". " + caracteristicasD[indexD] + ". ";
    }

    public  static String ageGenerator(){
        int age = randBetween(0, 200);
        if (age>100){
            return age + " anos (sua espécie vive muito)";
        }
        return age + " anos";
    }

    public  static String ultimatePlotGenerator(){
        int protagonistName = randBetween(0, nomes.length-1);
        int villainName = randBetween(0, nomes.length-1);
        int friendName = randBetween(0, nomes.length-1);

        int isAName = randBetween(0, ehUm.length-1);

        int placeName = randBetween(0, lugares.length-1);

        int objective = randBetween(0, objetivos.length-1);
        int problem = randBetween(0, problemas.length-1);

        String protagonist = nomes[protagonistName];
        String friend = nomes[friendName];
        String villain = nomes[villainName];
        String isA = ehUm[isAName];
        String place = lugares[placeName];
        String obj = objetivos[objective];
        String prob = problemas[problem];



        String plot = protagonist + " é um " + isA + " que mora em "+ place + ". Ele deseja muito " + obj + ". Porém, "
                + prob +" acontece, causado por " + villain + ", que vai mudar a vida de " + protagonist + " para sempre. " +
                "Em sua jornada para deter os planos de " + villain + ", "
                + protagonist + " encontrou " + friend + ", que está disposto a ajudar. " +
                "Será que eles conseguirão impedir os planos de " + villain + "?";
        return plot;

    }

    public  static String placesGenerator() {
        int indexA = randBetween(0, lugares.length-1);

        return lugares[indexA];

    }

    public  static String historyGenerator() {
        int indexA = randBetween(0, historias.length-1);

        return historias[indexA] + ".";

    }

    public  static String importanceGenerator() {
        int indexA = randBetween(0, importancia.length-1);

        return importancia[indexA] + ".";

    }

    public  static String titleGenerator() {
        int indexA = randBetween(0, titulos.length-1);

        return titulos[indexA];

    }

    public  static String placeFeaturesGenerator() {
        int indexA = randBetween(0, caracteristicasLugaresA.length-1);
        int indexB = randBetween(0, caracteristicasLugaresB.length-1);

        return caracteristicasLugaresA[indexA] + ". " + caracteristicasLugaresB[indexB] + ". " ;
    }

    public static String genderGenerator() {
        int indexA = randBetween(0, generos.length-1);

        return generos[indexA];

    }

    public static String headlineGenerator() {
        int indexA = randBetween(0, HEADLINES.length-1);
        return HEADLINES[indexA];

    }

    public static String taglinesGenerator() {
        int indexA = randBetween(0, TAGLINES.length-1);
        return TAGLINES[indexA];
    }


    public static int randBetween(int start, int finish){
        return start+(int)Math.round(Math.random()*(finish-start));
    }




}
