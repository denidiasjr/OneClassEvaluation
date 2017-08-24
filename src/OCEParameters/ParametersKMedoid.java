/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OCEParameters;

/**
 *
 * @author deni
 */
public class ParametersKMedoid extends Parameters {
    
    private int maxIteracoes;
    private int k;
    private int convergencia;
    private int porcentagemTroca;
    
    public ParametersKMedoid(){
        super();
        this.maxIteracoes = 100;
        this.convergencia = 0;
        this.k = 5;
        this.porcentagemTroca = -1;
    }

    public int getMaxIteracoes() {
        return maxIteracoes;
    }

    public void setMaxIteracoes(int maxIteracoes) {
        this.maxIteracoes = maxIteracoes;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getConvergencia() {
        return convergencia;
    }

    public void setConvergencia(int convergencia) {
        this.convergencia = convergencia;
    }

    public int getPorcentagemTroca() {
        return porcentagemTroca;
    }

    public void setPorcentagemTroca(int porcentagemTroca) {
        this.porcentagemTroca = porcentagemTroca;
    }
}
