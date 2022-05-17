package flight.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FLIGHT
 * @creationDate 20.04.2022
 */
public class Source {

    private double arrivalRate;
    private Map<QueuingSystem, Double> routeProbabilities;

    public Source(double arrivalRate, Map<QueuingSystem, Double> routeProbabilities) {
        this.arrivalRate = arrivalRate;
        this.routeProbabilities = routeProbabilities;
    }

    public Map<QueuingSystem, Double> getRouteProbabilities() {
        return routeProbabilities;
    }

    public void setRouteProbabilities(Map<QueuingSystem, Double> routeProbabilities) {
        this.routeProbabilities = routeProbabilities;
    }

    public double getArrivalRate() {
        return arrivalRate;
    }

    public void setArrivalRate(int arrivalRate) {
        this.arrivalRate = arrivalRate;
    }
}
