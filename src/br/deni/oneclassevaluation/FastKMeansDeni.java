package br.deni.oneclassevaluation;

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
public class FastKMeansDeni extends Classifier {

    public static void main(String[] args) throws Exception {
        FastKMeansDeni classifier = new FastKMeansDeni();
        classifier.buildClassifier(new DataSource("/home/deni/Desktop/arffFiles/CSTR.arff").getDataSet());
    }

    // K number
    private int k;

    // Define o maximo de iteracoes
    private int maximoIteracoes;

    // Define o threshold para classificar uma nova instancias
    private double threshold;

    // Instancias
    private HashMap<Integer, Double>[] instancias;

    // Vetor com os centroids e suas instancias
    private HashMap<Integer, ArrayList<Integer>> grupos;

    // Vetor com o numero de instancias de cada grupo
    private double coesaoGrupos[];
    private double coesaoGruposAnterior[];

    // Centroides de cada grupo
    private HashMap<Integer, Double>[] centroides;

    // Numero de atributos e instancias
    private int numAtributos;
    private int numInstancias;

    // Constructor
    public FastKMeansDeni() {
        super();
        this.k = 5;
        this.maximoIteracoes = 100;
        this.threshold = 0.1;
    }

    @Override
    public void buildClassifier(Instances dataTrain) throws Exception {

        // Inicializacao das variaveis do classificador
        numAtributos = dataTrain.numAttributes() - 1;
        numInstancias = dataTrain.numInstances();
        instancias = transformaInstancias(dataTrain);

        // Define os Grupos
        grupos = defineGrupos();
        coesaoGrupos = new double[k];
        coesaoGruposAnterior = new double[k];

        // Random the instances centroids
        Random random = new Random(0);
        for (int inst = 0; inst < numInstancias; inst++) {
            int randomK = random.nextInt(k);
            grupos.get(randomK).add(inst);
        }

        // Define os centroides de cada grupo
        defineCentroides();

        // Apresenta a primeira iteracao
        System.out.println("Iteracao 1 Concluida");
        for (int i = 0; i < k; i++) {
            System.out.println("Itens do Grupo " + i + ": " + grupos.get(i).size());
        }
        System.out.println(" ");

        boolean mesmaCoesao = false;
        int iteracoes = 0;

        // Enquanto a coesao nao se manter, continuar redefinindo os grupos
        while (!mesmaCoesao) {
            recalculaGrupos();
            defineCentroides();

            // Verifica se a coesao se manteve
            if (somatorio(coesaoGrupos) == somatorio(coesaoGruposAnterior)) {
                mesmaCoesao = true;
            }

            System.out.println("Iteracao " + (++iteracoes) + " Concluida");
            for (int i = 0; i < centroides.length; i++) {
                System.out.println("Itens do Grupo " + i + ": " + grupos.get(i).size());

            }
            System.out.println("Coesao: " + somatorio(coesaoGrupos) + "\n");

            if (iteracoes == this.maximoIteracoes) {
                System.out.println("Limite máximo de iteracoes!");
                System.exit(0);
            }

        }
    }

    // Realiza a classificacao para uma nova instancia
    public double classifyInstance(Instance instancia) {

        HashMap<Integer, Double> instanciaHash = transformaInstancia(instancia);

        int index = capturaGrupo(instanciaHash);
        double cosseno = calculaCosseno(centroides[index], instanciaHash);
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

    // Zera o HashMap para receber as instancias
    private HashMap<Integer, ArrayList<Integer>> defineGrupos() {

        HashMap<Integer, ArrayList<Integer>> hash = new HashMap<>();

        for (int i = 0; i < k; i++) {
            hash.put(i, new ArrayList<>());
        }

        return hash;
    }

    // Calcula a distancia do cosseno entre duas instancias
    private double calculaCosseno(HashMap<Integer, Double> a, HashMap<Integer, Double> b) {

        double normaA = calculaNorma(a);
        double normaB = calculaNorma(b);

        double somador = 0;

        Iterator iter = a.entrySet().iterator();

        // Percorre os atributos do documento A
        while (iter.hasNext()) {
            Map.Entry itemA = (Map.Entry) iter.next();

            // Verifica se o atributo de A está contido em B e soma se estiver
            if (b.containsKey(itemA.getKey())) {
                somador += ((double) itemA.getValue() * b.get(itemA.getKey()));
            }

        }

        return somador / (normaA * normaB);
    }

    // TODO verificar o problema com a norma para o documento A
    // Calcula a norma de uma determinada instancia
    private double calculaNorma(HashMap<Integer, Double> instancia) {

        double somador = 0;

        Iterator iter = instancia.entrySet().iterator();

        // Percorre os atributos do documento A
        while (iter.hasNext()) {
            Map.Entry item = (Map.Entry) iter.next();

            somador += Math.pow(instancia.get(item.getKey()), 2);
        }

        return Math.sqrt(somador);
    }

    // Define a configuração dos primeiros centroides
    private void defineCentroides() {

        double somatorio = 0;
        double novoValor = 0;

        centroides = new HashMap[k];

        // Percorre os grupos e calcula a distancia de cada item
        for (int grp = 0; grp < k; grp++) {

            int numInstancias = grupos.get(grp).size();
            centroides[grp] = new HashMap<>();

            // Percorre os itens do grupo
            for (int inst : grupos.get(grp)) {

                Iterator iter = instancias[inst].entrySet().iterator();

                // Percorre as iteracoes e adiciona o somatorio dos atributos no centroide
                while (iter.hasNext()) {
                    Map.Entry entrada = (Map.Entry) iter.next();

                    // TODO
                    // Verificar no grp = 0 a transicao da instancia 13 pra 14 (43 => 62 em instancias)
                    if (centroides[grp].containsKey((int) entrada.getKey())) {
                        novoValor = centroides[grp].get((int) entrada.getKey()) + (double) entrada.getValue();
                    } else {
                        novoValor = (double) entrada.getValue();
                    }

                    centroides[grp].put((int) entrada.getKey(), novoValor);
                    //somatorio += novoValor;
                }
                //somatorio = 0;
            }

            Iterator iterCentroide = centroides[grp].entrySet().iterator();

            // Calcula o centroide
            while (iterCentroide.hasNext()) {
                Map.Entry entradaCentroide = (Map.Entry) iterCentroide.next();

                double valorCentroide = (double) entradaCentroide.getValue() / numInstancias;
                centroides[grp].put((int) entradaCentroide.getKey(), valorCentroide);
            }

            //System.out.println("Somatorio " + grp + ": " + somatorio);

        }
    }

    // Recalcula os grupos criados pelos centroides
    private void recalculaGrupos() {

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
            int grupo = capturaGrupo(instancias[inst]);
            grupos.get(grupo).add(inst);
        }

    }

    // Captura grupo da instancia atual
    private int capturaGrupo(HashMap<Integer, Double> instancia) {

        int index = 0;
        double maiorCosseno = 0;

        for (int cent = 0; cent < centroides.length; cent++) {

            double cosseno = calculaCosseno(centroides[cent], instancia);

            if (cosseno > maiorCosseno) {
                index = cent;
                maiorCosseno = cosseno;
            }
        }

        // Reaiza somatorio de coesao
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

    // Transforma as instancias de Instances para um vetor HashMap
    private HashMap<Integer, Double>[] transformaInstancias(Instances dataTrain) {

        HashMap<Integer, Double>[] instancias = new HashMap[numInstancias];

        // ATRIBUTO 1725 DA INSTANCIA 62 TA ZERANDO E DEVERIA SER 1.0
        for (int inst = 0; inst < numInstancias; inst++) {
            instancias[inst] = transformaInstancia(dataTrain.instance(inst));

        }

        return instancias;
    }

    // Transforma uma instancia de Instance para um HashMap
    private HashMap<Integer, Double> transformaInstancia(Instance instancia) {

        HashMap<Integer, Double> novaInstancia = new HashMap<>();

        for (int atr = 0; atr < numAtributos; atr++) {

            if (instancia.value(atr) > 0) {
                novaInstancia.put(atr, instancia.value(atr));
            }
        }

        return novaInstancia;
    }
}
