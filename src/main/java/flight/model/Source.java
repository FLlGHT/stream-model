package flight.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FLIGHT
 * @creationDate 20.04.2022
 */
public class Source {

    private int incomingIntensity;
    private Map<QueuingSystem, Double> routeProbabilities;

    public Source() {
        routeProbabilities = new HashMap<>();
    }

    public Source(int incomingIntensity, Map<QueuingSystem, Double> routeProbabilities) {
        this.incomingIntensity = incomingIntensity;
        this.routeProbabilities = routeProbabilities;
    }

    public Map<QueuingSystem, Double> getRouteProbabilities() {
        return routeProbabilities;
    }

    public void setRouteProbabilities(Map<QueuingSystem, Double> routeProbabilities) {
        this.routeProbabilities = routeProbabilities;
    }

    public int getIncomingIntensity() {
        return incomingIntensity;
    }

    public void setIncomingIntensity(int incomingIntensity) {
        this.incomingIntensity = incomingIntensity;
    }
}
