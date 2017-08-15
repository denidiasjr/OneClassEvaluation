/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OCEAlgorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author deni
 */
public class KMeans extends Classifier {

    public static void main(String[] args) throws Exception {
        KMeans classifier = new KMeans();
        classifier.buildClassifier(new DataSource("/home/deni/Documents/TCC/arffFiles/CSTR.arff").getDataSet());
    }

    // K number
    private int k;

    // Define o maximo de iteracoes
    private int maximoIteracoes;

    // Define o threshold para classificar uma nova instancias
    private double threshold;

    // Vetor com os centroids e suas instancias
    private HashMap<Integer, ArrayList<Integer>> grupos;

    // Vetor com o numero de instancias de cada grupo
    private double coesaoGrupos[];
    private double coesaoGruposAnterior[];

    // Centroides de cada grupo
    private Instances centroides;

    // Convergencia entre a coesao dos grupos
    private double convergencia;

    // Iteracao atual
    private int iteracao;

    // Os a quais grupos as intancias pertenciam anteriormente
    private int[] gruposAnterior;

    // Captura a porcentagem de troca entre as instancias
    private double porcentagemTroca;

    // Constructor
    public KMeans() {
        super();
        this.k = 5;
        this.maximoIteracoes = 100;
        this.threshold = 0.1;
        this.convergencia = 0;
        this.iteracao = 0;
        this.porcentagemTroca = -1;
    }

    @Override
    public void buildClassifier(Instances dataTrain) throws Exception {

        // Inicializacao das variaveis do classificador
        int numAtributos = dataTrain.numAttributes() - 1;
        int numInstancias = dataTrain.numInstances();

        // Define os Grupos
        grupos = defineGrupos();
        gruposAnterior = new int[numInstancias];
        coesaoGrupos = new double[k];
        coesaoGruposAnterior = new double[k];

        // Random the instances centroids
        Random random = new Random(0);
        for (int inst = 0; inst < numInstancias; inst++) {
            int randomK = random.nextInt(k);
            grupos.get(randomK).add(inst);
        }

        // Define os centroides de cada grupo
        defineCentroides(dataTrain);

        // Apresenta a primeira iteracao
        imprimeIteracao();

        // Enquanto a coesao nao se manter, continuar redefinindo os grupos
        while (!confereConvergencia()) {
            recalculaGrupos(dataTrain);
            defineCentroides(dataTrain);
            imprimeIteracao();
            verificaTrocas();
        }
    }

    // Realiza a classificacao para uma nova instancia
    public double classifyInstance(Instance instancia) {

        int index = capturaGrupo(instancia);
        double cosseno = calculaCosseno(centroides.instance(index), instancia);
        return (cosseno >= this.threshold ? 0 : 1);
    }

    // Define o K means
    public void setK(int k) {
        if (this.k > 1) {
            this.k = k;
        }
    }

    public void setMaximoIteracoes(int maximoIteracoes) {
        if (this.maximoIteracoes > 1) {
            this.maximoIteracoes = maximoIteracoes;
        }
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public void setConvergencia(double convergencia) {
        this.convergencia = convergencia;
    }

    public void setPorcentagem(double porcentagemTroca) {
        this.porcentagemTroca = porcentagemTroca;
    }

    // Zera o HashMap para receber as instancias
    private HashMap<Integer, ArrayList<Integer>> defineGrupos() {

        HashMap<Integer, ArrayList<Integer>> hash = new HashMap<>();

        for (int i = 0; i < k; i++) {
            hash.put(i, new ArrayList<>());
        }

        return hash;
    }

    // Calcula a distancia do cosseno entre duas instancias
    private double calculaCosseno(Instance a, Instance b) {

        double normaA = calculaNorma(a);
        double normaB = calculaNorma(b);

        double somador = 0;

        for (int atr = 0; atr < a.numAttributes(); atr++) {
            somador += (a.value(atr) * b.value(atr));
        }

        return somador / (normaA * normaB);
    }

    // TODO Fazer calculo do cosseno utilizando vetor de double
    private double calculaCosseno(double[] cosseno, Instance documento) {
        return 0.0;
    }

    // Calcula a norma de uma determinada instancia
    private double calculaNorma(Instance instancia) {

        double somador = 0;

        for (int atr = 0; atr < instancia.numAttributes(); atr++) {
            somador += Math.pow(instancia.value(atr), 2);
        }

        return Math.sqrt(somador);
    }

    // TODO Calcular norma para estrutura de vetor
    private double calculaNorma(double[] norma) {
        return 0.0;
    }

    // Define a configuração dos primeiros centroides
    private void defineCentroides(Instances dataTrain) {

        double novoValor = 0;

        int numAtributos = dataTrain.numAttributes();

        centroides = new Instances(dataTrain, 0);

        // Percorre os grupos e calcula a distancia de cada item
        for (int grp = 0; grp < k; grp++) {

            int numInstancias = grupos.get(grp).size();
            Instance centroide = capturaInstanciaZerada(dataTrain.instance(0));

            // Percorre os atributos e soma os valores das suas instancias
            for (int atr = 0; atr < numAtributos; atr++) {

                for (int inst : grupos.get(grp)) {

                    // Captura novo valor e acrescenta no centroide
                    novoValor = dataTrain.instance(inst).value(atr) / numInstancias;
                    centroide.setValue(atr, novoValor + centroide.value(atr));
                }
            }

            // Atribui centroide ao vetor de centroides
            centroides.add(centroide);
        }
    }

    // Recalcula os grupos criados pelos centroides
    private void recalculaGrupos(Instances dataTrain) {

        int numInstancias = dataTrain.numInstances();

        // Captura as trocas entre os documentos
        Iterator iter = grupos.keySet().iterator();
        while (iter.hasNext()) {
            int grupo = (int) iter.next();

            // Percorre as instancias dos grupos
            for (int instancia : grupos.get(grupo)) {
                gruposAnterior[instancia] = grupo;
            }
        }

        // TODO neste momento devemos cadastrar as instancias dos documentos
        grupos = defineGrupos();

        // Percorre a coesao anterior
        for (int i = 0; i < k; i++) {
            coesaoGruposAnterior[i] = coesaoGrupos[i];
        }

        // Zera o vetor
        for (int i = 0; i < k; i++) {
            coesaoGrupos[i] = 0;
        }

        // Captura o grupo de cada instancia
        for (int inst = 0; inst < numInstancias; inst++) {
            int grupo = capturaGrupo(dataTrain.instance(inst));
            grupos.get(grupo).add(inst);
        }

    }

    // Verifica frequencia de trocas entre os documentos
    private void verificaTrocas() {

        int numTrocas = 0;

        // Captura as trocas entre os documentos
        Iterator iter = grupos.keySet().iterator();
        while (iter.hasNext()) {
            int grupo = (int) iter.next();

            // Percorre as instancias dos grupos
            for (int instancia : grupos.get(grupo)) {

                if (gruposAnterior[instancia] != grupo) {
                    numTrocas++;
                }
            }
        }

        double porcentagem = (numTrocas * 100) / gruposAnterior.length;

        // Verificar se a porcentagem de troca esta menor que a definida
        if (porcentagem <= porcentagemTroca) {
            System.exit(0);
        }

    }

    // Gera uma instancia zerada a partir de outra instancia
    private Instance capturaInstanciaZerada(Instance instancia) {
        Instance novaInstancia = new Instance(instancia);
        for (int atr = 0; atr < instancia.numAttributes(); atr++) {
            novaInstancia.setValue(atr, 0);
        }
        return novaInstancia;
    }

    // Captura grupo da instancia atual
    private int capturaGrupo(Instance instancia) {

        int index = 0;
        double maiorCosseno = 0;

        for (int cent = 0; cent < centroides.numInstances(); cent++) {

            double cosseno = calculaCosseno(centroides.instance(cent), instancia);

            if (cosseno > maiorCosseno) {
                index = cent;
                maiorCosseno = cosseno;
            }
        }

        // Realiza somatorio de coesao
        coesaoGrupos[index] += maiorCosseno;

        return index;
    }

    private double somatorio(double[] vetor) {

        double somatorio = 0;

        for (int i = 0; i < vetor.length; i++) {
            somatorio += vetor[i];
        }

        return somatorio;
    }

    // Verifica a convergencia para continuar executando o algoritmo
    private boolean confereConvergencia() {

        boolean mesmaCoesao = false;

        for (int i = 0; i < k; i++) {

            // Compara a coesao grupo a grupo
            if (((coesaoGrupos[i] - coesaoGruposAnterior[i] >= 0) && (iteracao > 1)) && ((coesaoGrupos[i] - coesaoGruposAnterior[i]) <= convergencia)) {
                mesmaCoesao = true;
            }
        }

        return mesmaCoesao;
    }

    // Imprime os dados da iteracao atual
    private void imprimeIteracao() {

        System.out.println("Iteracao " + (++iteracao) + " Concluida");
        for (int i = 0; i < centroides.numInstances(); i++) {
            System.out.println("Itens do Grupo " + i + ": " + grupos.get(i).size());

        }
        System.out.println("Coesao: " + somatorio(coesaoGrupos) + "\n");

        if (iteracao == this.maximoIteracoes) {
            System.out.println("Limite máximo de iteracoes!");
            System.exit(0);
        }
    }
}
