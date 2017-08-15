/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OCEAlgorithms;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.Instance;

/**
 *
 * @author deni
 */
public class NaiveBayes extends Classifier {

    private double[] mediaAtributos;
    private double[] desvioAtributos;
    private double threshold;

    // Construtor
    public NaiveBayes() {
        super();
    }

    // Constroi o classificador
    @Override
    public void buildClassifier(Instances dataTrain) throws Exception {

        // Inicializacao das variaveis do classificador
        int numClasses = dataTrain.numClasses();
        int numAtributos = dataTrain.numAttributes() - 1;
        int numInstancias = dataTrain.numInstances();

        mediaAtributos = new double[numAtributos];
        desvioAtributos = new double[numAtributos];

        for (int inst = 0; inst < numInstancias; inst++) {

            for (int atr = 0; atr < numAtributos; atr++) {
                double anterior = dataTrain.instance(inst).value(atr);
                dataTrain.instance(inst).setValue(atr, anterior + 1);
            }
        }

        // Percorre as instancias a fim de capturar a norma 
        for (int inst = 0; inst < numInstancias; inst++) {

            double norma = 0;

            // Captura a norma dos atributos
            for (int atr = 0; atr < numAtributos; atr++) {
                norma = norma + Math.pow(dataTrain.instance(inst).value(atr), 2);
            }

            norma = Math.sqrt(norma);

            // Define o novo valor do atributo
            for (int atr = 0; atr < numAtributos; atr++) {
                double anterior = dataTrain.instance(inst).value(atr);
                dataTrain.instance(inst).setValue(atr, anterior / norma);
            }
        }

        // Percorre os atributos
        for (int atr = 0; atr < numAtributos; atr++) {

            double somatorio = 0;

            // Percorre as instancias para calcular a media
            for (int inst = 0; inst < numInstancias; inst++) {

                // Verifica se aquela instancia pertence a classe desejada
                somatorio += dataTrain.instance(inst).value(atr);
            }

            // Calcula a media dos atributos
            double media = somatorio / numInstancias;
            mediaAtributos[atr] = media;
            somatorio = 0;

            // Percorre as instancias para calcular o desvio padrao
            for (int inst = 0; inst < numInstancias; inst++) {

                // Verifica se aquela instancia pertence a classe desejada
                somatorio += Math.pow(dataTrain.instance(inst).value(atr) - media, 2);
            }

            // Calcula o desvio padrao dos atributos
            desvioAtributos[atr] = Math.sqrt(somatorio / numInstancias);
        }
    }

    // Realiza a classificacao para uma nova instancia
    public double classifyInstance(Instance instance) {

        return (distributionInstance(instance) >= this.threshold ? 0 : 1);
    }

    // Calcula a distribuicao normal para determinada instancia
    public double distributionInstance(Instance instance) {

        int numAtributos = instance.numAttributes() - 1;

        // Percorre os atributos para calcular a probabilidade
        double acumulaConfianca = 1;

        // Captura a norma dos atributos
        double norma = 0;
        
        for (int atr = 0; atr < numAtributos; atr++) {
            norma = norma + Math.pow(instance.value(atr), 2);
        }

        norma = Math.sqrt(norma);

        // Define o novo valor do atributo
        for (int atr = 0; atr < numAtributos; atr++) {
            double anterior = instance.value(atr);
            instance.setValue(atr, anterior / norma);
        }

        for (int atr = 0; atr < numAtributos; atr++) {

            // Calcula a gaussiana para cada novo atributo
            if (desvioAtributos[atr] == 0) {
                continue;
            }

            if (instance.value(atr) == 0) {
                continue;
            }

            double formula1 = 1 / (Math.sqrt(2 * Math.PI) * Math.pow(desvioAtributos[atr], 2));
            double formula2 = -Math.pow((instance.value(atr) - mediaAtributos[atr]) / (2 * desvioAtributos[atr]), 2);

            if (formula1 == 0 || formula2 == 0) {
                System.out.println("igual a 0");
            }

            acumulaConfianca *= formula1 * Math.pow(Math.E, formula2);
        }

        System.out.println("Confianca: " + acumulaConfianca);

        // Armazena a confianca da classe
        return acumulaConfianca;
    }

    public double getThreshold() {
        return this.threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
