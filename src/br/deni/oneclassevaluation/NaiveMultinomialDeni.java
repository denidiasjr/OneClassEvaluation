/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.deni.oneclassevaluation;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.Instance;

/**
 *
 * @author deni
 */
public class NaiveMultinomialDeni extends Classifier {

    private double[] probTerms;
    private double threshold;

    // Construtor
    public NaiveMultinomialDeni() {
        super();
    }

    // Constroi o classificador
    @Override
    public void buildClassifier(Instances dataTrain) throws Exception {

        // Inicializacao das variaveis do classificador
        int numAtributos = dataTrain.numAttributes() - 1;
        int numInstancias = dataTrain.numInstances();

        probTerms = new double[numAtributos];

        double somatorioTotal = 0;

        for (int atr = 0; atr < numAtributos; atr++) {

            double somatorioParcial = 0;

            for (int inst = 0; inst < numInstancias; inst++) {

                if (dataTrain.instance(inst).value(atr) > 5) {
                    somatorioParcial += dataTrain.instance(inst).value(atr);
                }

            }

            probTerms[atr] = somatorioParcial;
            somatorioTotal += somatorioParcial;
        }

        for (int i = 0; i < probTerms.length; i++) {
            probTerms[i] = probTerms[i] / somatorioTotal;
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

        for (int atr = 0; atr < numAtributos; atr++) {

            // Verifica se o valor da instancia eh maior que 0
            if (instance.value(atr) > 0 && probTerms[atr] > 0) {

                // Calcula a gaussiana para cada novo atributo
                acumulaConfianca *= instance.value(atr) * probTerms[atr];
            }
        }

        //System.out.println("asdjlsdja");
        if (instance.classValue() == 0) {
            //System.out.println("CLASS VALUE");
        }

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
