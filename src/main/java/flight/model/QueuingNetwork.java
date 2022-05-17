package flight.model;

import java.util.List;

/**
 * @author FLIGHT
 * @creationDate 17.05.2022
 */
public class QueuingNetwork {

    private List<QueuingSystem> systems;
    private List<QueuingSystem> collectionSystems;
    private QueuingSystem analysisSystem;
    private QueuingSystem dataStoreSystem;
    private Source source;

    private double failureProbability; // Вероятность сбоя
    private double arrivalRate; // Интенсивность потока требований
    private double durationExpectedValue; // Мат. ожидание длительности пребывания требований
    private double requestsNumberExpectedValue; // Мат. ожидание числа требований

    public QueuingNetwork() {

    }

    public QueuingNetwork(List<QueuingSystem> systems, List<QueuingSystem> collectionSystems, QueuingSystem analysisSystem, QueuingSystem dataStoreSystem, double failureProbability) {
        this.systems = systems;
        this.collectionSystems = collectionSystems;
        this.analysisSystem = analysisSystem;
        this.dataStoreSystem = dataStoreSystem;
        this.failureProbability = failureProbability;
    }

    public List<QueuingSystem> getSystems() {
        return systems;
    }

    public void setSystems(List<QueuingSystem> systems) {
        this.systems = systems;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public double getFailureProbability() {
        return failureProbability;
    }

    public void setFailureProbability(double failureProbability) {
        this.failureProbability = failureProbability;
    }

    public double getArrivalRate() {
        return arrivalRate;
    }

    public void setArrivalRate(double arrivalRate) {
        this.arrivalRate = arrivalRate;
    }

    public double getDurationExpectedValue() {
        return durationExpectedValue;
    }

    public void setDurationExpectedValue(double durationExpectedValue) {
        this.durationExpectedValue = durationExpectedValue;
    }

    public double getRequestsNumberExpectedValue() {
        return requestsNumberExpectedValue;
    }

    public void setRequestsNumberExpectedValue(double requestsNumberExpectedValue) {
        this.requestsNumberExpectedValue = requestsNumberExpectedValue;
    }

    public List<QueuingSystem> getCollectionSystems() {
        return collectionSystems;
    }

    public void setCollectionSystems(List<QueuingSystem> collectionSystems) {
        this.collectionSystems = collectionSystems;
    }

    public QueuingSystem getAnalysisSystem() {
        return analysisSystem;
    }

    public void setAnalysisSystem(QueuingSystem analysisSystem) {
        this.analysisSystem = analysisSystem;
    }

    public QueuingSystem getDataStoreSystem() {
        return dataStoreSystem;
    }

    public void setDataStoreSystem(QueuingSystem dataStoreSystem) {
        this.dataStoreSystem = dataStoreSystem;
    }
}
