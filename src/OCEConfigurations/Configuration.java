/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OCEConfigurations;

import OCEParameters.ParametersKMeans;
import OCEParameters.ParametersKMedoid;
import OCEParameters.ParametersNaiveBayes;
import OCEParameters.ParametersMultinomialNaiveBayes;
import java.io.Serializable;

/**
 *
 * @author deni
 */
public class Configuration implements Serializable {
    
    private int numReps;
    private int numFolds;
    
    private String dirInput;
    private String dirOutput;
    
    private boolean NB;
    private boolean MNB;
    private boolean KME;
    private boolean KMED;
    
    private ParametersNaiveBayes parametersNB;
    private ParametersMultinomialNaiveBayes parametersMNB;
    private ParametersKMeans parametersKME;
    private ParametersKMedoid parametersKMED;
    
    
    public Configuration(){
        setNumReps(10);
        setNumFolds(10);
        setDirInput("");
        setDirOutput("");
        setNB(false);
        setMNB(false);
        setKME(false);
        setKMED(false);
        setParametersNB(new ParametersNaiveBayes());
        setParametersMNB(new ParametersMultinomialNaiveBayes());
        setParametersKME(new ParametersKMeans());
        setParametersKMED(new ParametersKMedoid());
    }

    public int getNumReps() {
        return numReps;
    }

    public void setNumReps(int numReps) {
        this.numReps = numReps;
    }

    public int getNumFolds() {
        return numFolds;
    }

    public void setNumFolds(int numFolds) {
        this.numFolds = numFolds;
    }

    public String getDirInput() {
        return dirInput;
    }

    public void setDirInput(String dirInput) {
        this.dirInput = dirInput;
    }

    public String getDirOutput() {
        return dirOutput;
    }

    public void setDirOutput(String dirOutput) {
        this.dirOutput = dirOutput;
    }

    public boolean isNB() {
        return NB;
    }

    public void setNB(boolean NB) {
        this.NB = NB;
    }

    public boolean isMNB() {
        return MNB;
    }

    public void setMNB(boolean MNB) {
        this.MNB = MNB;
    }

    public boolean isKME() {
        return KME;
    }

    public void setKME(boolean KME) {
        this.KME = KME;
    }

    public boolean isKMED() {
        return KMED;
    }

    public void setKMED(boolean KMED) {
        this.KMED = KMED;
    }

    public ParametersNaiveBayes getParametersNB() {
        return parametersNB;
    }

    public void setParametersNB(ParametersNaiveBayes parametersNB) {
        this.parametersNB = parametersNB;
    }

    public ParametersMultinomialNaiveBayes getParametersMNB() {
        return parametersMNB;
    }

    public void setParametersMNB(ParametersMultinomialNaiveBayes parametersMNB) {
        this.parametersMNB = parametersMNB;
    }

    public ParametersKMeans getParametersKME() {
        return parametersKME;
    }

    public void setParametersKME(ParametersKMeans parametersKME) {
        this.parametersKME = parametersKME;
    }

    public ParametersKMedoid getParametersKMED(){
        return parametersKMED;
    }
    
    public void setParametersKMED(ParametersKMedoid parametersKMedoid) {
        this.parametersKMED = parametersKMedoid;
    }
}
