package OCE;

import OCEAlgorithms.KMeans;
import OCEAlgorithms.NaiveBayes;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author deni
 */
public class Main {

    public static void main(String[] args) throws Exception {

        // Read arff file and catch it's Instances
        DataSource dataSource = new DataSource("/home/deni/Documents/TCC/arffFiles/iris.arff");
        Instances dataOriginal = dataSource.getDataSet();

        // Count dataOriginal and attributes
        int numInstances = dataOriginal.numInstances();
        int numAttributes = dataOriginal.numAttributes();

        dataOriginal.setClassIndex(numAttributes - 1);
        int numClasses = dataOriginal.numClasses();

        NaiveBayes naiveDeni = new NaiveBayes();
        naiveDeni.setThreshold(0.0000000001);

        for (int classe = 0; classe < numClasses; classe++) {

            for (int fold = 0; fold < 10; fold++) {

                naiveDeni.setThreshold(0);

                Instances dataTrain = new Instances(dataOriginal, 0);
                Instances dataTest = new Instances(dataOriginal, 0);

                splitTrainTest(dataOriginal, dataTrain, dataTest, classe, fold);

                /*while (true) {

                    Integer[][] confusionMatrix = createConfusionMatrix();

                    naiveDeni.buildClassifier(dataTrain);
                    oneClassEvaluation(naiveDeni, dataTest, confusionMatrix, classe);

                    printConfusionMatrix(classe, fold, confusionMatrix);
                    printStatistics(confusionMatrix);

                    double threshold = naiveDeni.getThreshold();
                    double eta = 0.000000000001;
                    threshold += eta * ((confusionMatrix[0][1] - confusionMatrix[1][0]) / confusionMatrix[0][1]);
                    naiveDeni.setThreshold(threshold);
                    System.out.println(threshold);
                }*/
            }
        }

    }

    public static void splitTrainTest(Instances dataOriginal, Instances dataTrain, Instances dataTest, int classe, int fold) {

        Instances target = new Instances(dataOriginal, 0);
        Instances targetTest = new Instances(dataOriginal, 0);

        for (int inst = 0; inst < dataOriginal.numInstances(); inst++) {

            if (dataOriginal.instance(inst).classValue() == classe) {
                target.add(dataOriginal.instance(inst));
            } else {
                dataTest.add(dataOriginal.instance(inst));
            }
        }

        Instances dataTrainTarget = target.trainCV(10, fold);
        targetTest = target.testCV(10, fold);

        for (int inst = 0; inst < dataTrainTarget.numInstances(); inst++) {
            dataTrain.add(dataTrainTarget.instance(inst));
        }

        for (int inst = 0; inst < targetTest.numInstances(); inst++) {
            dataTest.add(targetTest.instance(inst));
        }
    }

    // Realiza os testes com o OneClassClassification
    public static void oneClassEvaluation(Classifier classifier, Instances dataTest, Integer[][] confusionMatrix, int classeTarget) {

        for (int inst = 0; inst < dataTest.numInstances(); inst++) {
            try {

                int classeReal = 0;

                // Verifica se a instancia eh da mesma classe de teste
                if (dataTest.instance(inst).classValue() != classeTarget) {
                    classeReal = 1;
                }

                // Captura resultado
                int resultado = (int) classifier.classifyInstance(dataTest.instance(inst));

                // Adiciona na matriz de confusao
                confusionMatrix[resultado][classeReal]++;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Cria uma matriz de confusao zerada
    public static Integer[][] createConfusionMatrix() {
        Integer[][] matriz = new Integer[2][2];

        for (int i = 0; i < 2; i++) {

            for (int j = 0; j < 2; j++) {
                matriz[i][j] = 0;
            }
        }

        return matriz;
    }

    // Apresenta a matriz de confusao
    public static void printConfusionMatrix(int classNumber, int foldNumber, Integer[][] confusionMatrix) {

        System.out.println("\nClass: " + classNumber);
        System.out.println("Fold: " + foldNumber);
        System.out.println("Matriz:");
        for (int i = 0; i < 2; i++) {
            for (int y = 0; y < 2; y++) {
                System.out.print("\t" + confusionMatrix[i][y] + "    ");
            }
            System.out.println();
        }
    }

    // Apresenta as estatisticas da matriz de confusao
    private static void printStatistics(Integer[][] confusionMatrix) {

        double vp = confusionMatrix[0][0];
        double fp = confusionMatrix[0][1];
        double fn = confusionMatrix[1][0];
        double vn = confusionMatrix[1][1];

        double accuracy = (vp + vn) / (vp + vn + fp + fn);
        double precision = (vp) / (vp + fp);
        double recall = (vp) / (vp + fn);

        if (Double.isNaN(precision)) {
            precision = 0;
        }

        if (Double.isNaN(recall)) {
            recall = 0;
        }

        System.out.println("\nAccuracy: " + accuracy);
        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);
        System.out.println("");
    }

}
