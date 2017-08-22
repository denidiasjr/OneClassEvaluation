/*
 * Class with algorithm parameters
 *
 * Deni Dias da Silva Junior
 * @author denidiasjr
 */
package OCEParameters;

import java.io.Serializable;

public class Parameters implements Serializable {
    
    private double[] threshold;

    // Getters and Setters
    public double[] getThreshold() {
        return threshold;
    }

    public void setThreshold(double[] threshold) {
        this.threshold = threshold;
    }

}
