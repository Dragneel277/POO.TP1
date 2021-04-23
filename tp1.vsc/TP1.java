import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.lang.Math;
import java.text.DecimalFormat;

public class TP1 {

    private static MyCommand interC;
    static final int MAX_ALUNOS = 35;
    private static int alunosLidos = 0;
    private static int notaMax = 0;
    private static int notaMin = 0;
    private static int notaAvg = 0;

    private static String[] nomeAlunos = new String[MAX_ALUNOS];
    private static int[] notasAlunos = new int[MAX_ALUNOS];

    public static void main(String[] args) {
        boolean querSair = false;

        interC = new MyCommand();

        do {
            interC.limparEcra();
            interC.showPrompt();
            String[] cmdEscrito = interC.lerComando();
            ArrayList<String> cmd = interC.validarComando(cmdEscrito);

            if (cmd == null) {
                interC.showMsg("Comando inválido. Digite help para ajuda");

            } else {
                if (cmd.get(0).equalsIgnoreCase("carregar")) {
                    alunosLidos = loadData(nomeAlunos, "turmaLeit.txt");
                    int notA = loadData(notasAlunos);
                    if (alunosLidos != notA) {
                        System.out.println("alunos = " + alunosLidos);
                        System.out.println("notaA = " + notA);
                        interC.showMsg("Erro carregando dados");
                    }

                    else

                        interC.showMsg("Dados carregados OK!");
                } else if (cmd.get(0).equalsIgnoreCase("listar")) {
                    mostrarAlunos();

                } else if (cmd.get(0).equalsIgnoreCase("paginar")) {
                    String input = JOptionPane.showInputDialog("Nũmeros estudantes por pãgina :");
                    int numeroU = Integer.parseInt(input);
                    mostrarAlunos(numeroU);

                } else if (cmd.get(0).equalsIgnoreCase("mostrarp")) {
                    mostrarPauta();

                } else if (cmd.get(0).equalsIgnoreCase("mostrarr")) {
                    mostraResumo();

                } else if (cmd.get(0).equalsIgnoreCase("top")) {
                    mostrarTop();

                } else if (cmd.get(0).equalsIgnoreCase("pesquisarnome")) {
                    String nomePesq = JOptionPane.showInputDialog("O que procura  :");
                    pesquisar(nomePesq);

                } else if (cmd.get(0).equalsIgnoreCase("pesquisarnota")) {
                    String vaPesq = JOptionPane.showInputDialog("O que procura  :");
                    int notaPesq = Integer.parseInt(vaPesq);
                    pesquisar(notaPesq);
                } else if (cmd.get(0).equalsIgnoreCase("help")) {
                    interC.showHelp();

                } else if (cmd.get(0).equalsIgnoreCase("terminar")) {
                    querSair = true;
                }
            }

        } while (!querSair);

    }

    /**
     * Método implementado por Prof. Não devem alterar. Este método recebe como
     * parâmetros um array e um ficheiro Lẽ cada linha do ficheiro e guarda no
     * array. Retorna o número de linhas que forma lidas do ficheiro.
     * 
     * @param lAlunos
     * @param nomeFicheiro
     * @return quantos nomes foram lidos do ficheiro -1 se não possível ler ficheiro
     */
    public static int loadData(String[] lAlunos, String nomeFicheiro) {
        Scanner in = null;
        File inputFile = new File(nomeFicheiro);
        // PrintWriter out = new PrintWriter(outputFileName);
        try {
            in = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int i = 0;
        while (in.hasNextLine()) {
            String nomeAl = in.nextLine();
            if ((nomeAl != null) && !(nomeAl.isBlank()) && !(nomeAl.isEmpty())) {
                lAlunos[i] = nomeAl;
                i++;
            }

        }
        in.close();
        return i;
    }

    /**
     * Método implementado por Prof. Não devem alterar. Este método recebe como
     * parâmetros um array de inteiros e vai gerar aleatoriamente valores inteiros
     * entre 0 e 20 que representam a nota de cada aluno.
     * 
     * @param lNotas
     * @return how much name was read from the files -1 if was not able to read the
     *         file
     */
    public static int loadData(int[] lNotas) {
        Random rand = new Random();
        int cont = 0;
        for (cont = 0; cont < alunosLidos; cont++) {
            int randomNum = rand.nextInt(20) + 1;
            notasAlunos[cont] = randomNum;
        }
        return cont;
    }

    /**
     * Método a ser implementando no TP1. O método deverá listar todos os nomes dos
     * alunos guardados no array nomesAlunos. O método deverá verificar se já foi
     * carregado os dados para o array. Se sim mostra os nomes dos alunos. Senão
     * deve mostrar a mensagem "Não há dados"
     * 
     * @param
     * @return
     */
    public static void mostrarAlunos() {
        if (nomeAlunos[0] == null) {
            interC.showMsg("Nao ha dados");/**
                                            * verificar se o array esta vazio, caso o primeiro elemento estiver logo
                                            * assumimos que o resto do array tb vai estar assim fazemos return para sair
                                            * do ciclo
                                            */
            return;
        }

        int cod = 0;
        System.out.println("Codigo" + " " + "Nome estudante");
        for (String aluno : nomeAlunos) { /** Percorrer o array utilizando o for */

            if (aluno == null) // if null jump
                continue;
            System.out.print(cod++ + " " + aluno + "\n");/** cod equivale ao numero de alunos +1 */
        }
        interC.showMsg("Enter para sair");
    }

    /**
     * Método a ser implementando no TP1 O método deverá listar todos os nomes dos
     * alunos guardados no array nomesAlunos. O método deverá verificar se já foi
     * carregado os dados para o array. Se sim mostra os nomes dos alunos. Senão
     * deve mostrar a mensagem "Não há dados". Neste método os dados não são
     * mostrados todos de uma só vez. Devem ser apresentados até encher a tela.
     * Vamos supor que 10 nomes enchem a tela. Então deverá ser apresentados de 10
     * em 10. Esse número que indica quantos nomes enchem a tela é um parâmetro do
     * método.
     * 
     * @param tela é um inteiro que indica quantos alunos são mostrados.
     */
    public static void mostrarAlunos(int tela) {
        boolean mudarPagina = false;
        int pagina = 0;
        if (nomeAlunos[0] == null) {/* ver se o txt esta carregado */
            interC.showMsg("Não há dados");/* Caso nao estiver carregado mostrar a mssgn */
            return;

        }
        int cont = 0, cod = 1;
        int jump = (alunosLidos / tela) + 1;
        /**
         * variavel que armazena o limite do primeiro loop, este que é responsavel pela
         * paginaçao este loop servira como paginas
         */
        for (int j = 1; j <= jump; j++) {
            System.out.println("Codigo" + "  " + "Nome estudante");
            System.out.println();
            for (int i = cont; i < ((j * tela) < nomeAlunos.length ? j * tela : (j) * tela - 1); i++) {
                if (nomeAlunos[i] == null)
                    break;

                System.out.print(cod++ + " " + nomeAlunos[i] + "\n");

            }

            cont += tela;/**
                          * o contador é constantemente atualizado com o valor da tela assim guardando o
                          * valor a começar na seguinte pagina.
                          */

            interC.showMsg("Enter para continuar...");

        }

    }

    /**
     * Método a ser implementando no TP1. O método deverá percorrer o array de
     * notas, calcular o valor da média aritmética de notas, a nota máximo e a nota
     * mínima. Os valores calculados devem ser guaraddos na variáveis notaAVG
     * (média), notaMax (nota máxima) e notaMin(nota mínima) Devem validar se o
     * array de notas tem elementos. Se estiver vazio devem somente apresentar a
     * mensagem "Não há dados"
     */
    private static void calcularMaxMinAvg() {
        if (notasAlunos[0] == 0) {
            interC.showMsg("Nao ha dados");
            return;
        }
        int total = 0, max = 0, min = 0;
        for (int Naluno = 0; Naluno < notasAlunos.length; Naluno++) {
            if (notasAlunos[Naluno] > max) { // comparar a nota do aluno no Naluno com o atual maximo
                max = notasAlunos[Naluno]; // se encontrar algo > que max ,max toma o valor novo

            } else if (notasAlunos[Naluno] < min) {
                min = notasAlunos[Naluno];//// comparar a nota do aluno no Naluno com o atual maximo
                                          //// se encontrar algo < que min ,min toma o valor novo
            }
            total += notasAlunos[Naluno];//
        }
        notaAvg = total / notasAlunos.length;
        notaMax = max;
        notaMin = min;

        System.out.println("a nota mais alta na turma é --------> " + notaMax);
        System.out.println("a nota mais baixa na turma é --------> " + notaMin);
        System.out.println("a media da turma é de --------> " + notaAvg);

    }

    /**
     * Método a ser implementando no TP1. O método deverá apresentar um resumo da
     * avaliação; Nota máxima, Nota mínima, Nota média. Número de alunos com nota
     * superior a média e número de alunos com nota inferior a média. a mensagem
     * "Não há dados"
     */
    public static void mostraResumo() {
        if (nomeAlunos[0] == null) {
            interC.showMsg("Nao ha dados ...");
            return;
        }

        int Acmedia = 0, Abmedia = 0;
        System.out.println(alunosLidos + " alunos presentes");
        System.out.println("____________________________________________________");

        calcularMaxMinAvg();
        System.out.println("____________________________________________________");

        for (int i = 0; i < notasAlunos.length; i++) {
            if (notasAlunos[i] > notaAvg) { // comparar as notas dos alunos com a media previamente calculada
                Acmedia++; // faz +1 ao numero de alunos acima da media
            } else
                Abmedia++; // se nao for maior que a media logo incrementa em abaixo da media

        }
        System.out.print("a turma contem " + Acmedia + " alunos com nota superior a media \n");
        System.out.print("a turma contem " + Abmedia + " alunos com nota inferior a media \n");
        System.out.print("____________________________________________________\n");

        interC.showMsg("Enter para continuar ...");
    }

    /**
     * Método a ser implementando no TP1. O método deverá apresentar o nome dos três
     * alunos que têm as melhores notas.
     */

    public static void mostrarTop() {

        if (notasAlunos[0] == 0) {
            interC.showMsg("Nao ha dados");
            return;

        }
        /** Variaves */
        String[] alunoTop = new String[3]; // array que guarda o top de alunos no qual aceita apenas 3 nomes
        int[] notasTop = new int[3]; // array que guarda o top de notas dos alunos no qual aceita apenas 3 notas

        int Mnota = -1;
        int index = 0;

        int[] copyNum = new int[notasAlunos.length]; // cria o array de copia

        for (int i = 0; i < copyNum.length; i++) {// copia o array para nao alterar no original
            copyNum[i] = notasAlunos[i];
        }

        for (int j = 0; j < 3; j++) { // for que da tres voltas armazenando os 3 valores top

            for (int indice = 0; indice < copyNum.length; indice++) {// ciclo no qual percorre o array
                if (copyNum[indice] > Mnota) { //// identificar o valor maximo do array
                    Mnota = copyNum[indice]; // guarda em Mnota
                    index = indice; // guarda o indice em index
                }
            }

            notasTop[j] = Mnota; // notas top na posiçao j recebe o valor de Mnota
            alunoTop[j] = nomeAlunos[index]; // aluno top na posicao J recebe o nome do aluno correspondente a nota
            Mnota = -1; // reseta o valor da nota maior para pode receber um novo
            copyNum[index] = -1; // faz reset do index da copia
        } // fim do primeiro for

        for (int i = 0; i < 3; i++) {
            System.out.println(alunoTop[i] + " " + notasTop[i]);
        }
        interC.showMsg("Enter para continuar ...");

    }

    /**
     * Método a ser implementando no TP1. Apresentar a pauta com nomes dos alunos e
     * á frente cada nome a respectiva nota obtida.
     */
    public static void mostrarPauta() {
        if (notasAlunos[0] == 0) {
            interC.showMsg("Nao ha dados");
            return;

        }
        int prox = (alunosLidos / 10) + 1;
        int cont = 0, cod = 1;
        for (int j = 1; j <= prox; j++) {
            System.out.println("codigo" + "   " + "Nome estudante" + "   " + "Nota");
            for (int i = cont; i < ((j * 10) < nomeAlunos.length ? j * 10 : (j) * 10 - 1); i++) {
                if (nomeAlunos[i] == null)
                    break;
                System.out.println(cod++ + "    " +"    "+nomeAlunos[i] + "      " + notasAlunos[i]);

            }

            cont += 10;// para apresentar apenas 10 por pagina
            interC.showMsg("Prima enter para ver os proximos 10 alunos");
        }

    }

    /**
     * Método a ser implementando no TP1 Apresentar para um aluno específico em que
     * o nome é dado como parâmetro a nota de avaliação
     * 
     * @param nome é uma string contendo o nome do aluno que queremos apresentar a
     *             sua nota
     * @return
     */
    public static void mostrarDetalhesAluno(String nome) {
        interC.showMsg("A ser implementado ...");

    }

    /**
     * Método a ser implementando no TP1 O método deverá pedir um nome e pesquisar o
     * array de nomes. Caso existir ou caso existem nomes parecidos apresentar a
     * lista de nomes. Nomes parecidos são nomes que iniciam com as mesmas duas ou
     * três primeiras letras. Ou apelidos iguais.
     */
    public static void pesquisar(String nome) {
        if (nomeAlunos[0] == null) {
            interC.showMsg("Nao ha dados ...");
            return;
        }
        for (String aluno : nomeAlunos) {

            if (aluno == null)
                continue;
            if ((aluno.toLowerCase()).contains(nome.toLowerCase())) {// Testar se foi introduzido o apelido
                                                                     // ou o primeiro nome

                System.out.println(aluno);// Mostrar o(s) nome(s)/apelido(s) pesquisados
            }

        }

        interC.showMsg("Enter para continuar ...");

    }

    /**
     * Método a ser implementando no TP1 O método deverá pedir um nome e pesquisar o
     * array de nomes. Caso existir ou caso existem nomes parecidos apresentar a
     * lista de nomes. Nomes parecidos são nomes que iniciam com as mesmas duas ou
     * três primeiras letras. Ou apelidos iguais.
     */
    public static void pesquisar(int nota) {
        if (notasAlunos[0] == 0) {
            interC.showMsg("Nao ha dados ...");
            return;
        }

        System.out.println("Nome estudante" + "     " + "Notas");
        for (int i = 0; i < notasAlunos.length; i++) {
            if (nota == notasAlunos[i]) {
                System.out.println(nomeAlunos[i] + "  " + notasAlunos[i]);
            }
        }

        interC.showMsg("Enter para continuar ...");

    }

    private String[] searchByName(String nome) {
        return null;
    }

}
