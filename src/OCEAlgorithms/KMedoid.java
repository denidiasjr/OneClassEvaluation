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
public class KMedoid extends Classifier {

    public static void main(String[] args) throws Exception {
        KMedoid classifier = new KMedoid();
        classifier.buildClassifier(new DataSource("/home/deni/Desktop/arffFiles/CSTR.arff").getDataSet());
    }

    // K number
    private int k;

    // Define o maximo de iteracoes
    private int maximoIteracoes;

    // Define o threshold para classificar uma nova instancias
    private double threshold;

    // Instancias
    private Instances instancias;

    // Vetor com os centroids e suas instancias
    private HashMap<Integer, ArrayList<Integer>> grupos;

    // Vetor com o numero de instancias de cada grupo
    private double coesaoGrupos[];
    private double coesaoGruposAnterior[];

    // Centroides de cada grupo
    private int[] centroides;

    // Instancia vazia
    private Instance instanciaVazia;

    // Numero de atributos e instancias
    private int numAtributos;
    private int numInstancias;

    // Constructor
    public KMedoid() {
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
        
        // Define os Grupos
        grupos = defineGrupos();
        coesaoGrupos = new double[k];
        coesaoGruposAnterior = new double[k];
    }
    
    // Realiza a classificacao para uma nova instancia
    public double classifyInstance(Instance instancia) {
        return 0;
    }
    
    // Zera o HashMap para receber as instancias
    private HashMap<Integer, ArrayList<Integer>> defineGrupos() {

        HashMap<Integer, ArrayList<Integer>> hash = new HashMap<>();

        for (int i = 0; i < k; i++) {
            hash.put(i, new ArrayList<>());
        }

        return hash;
    }
}
