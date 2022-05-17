package flight.model;

/**
 * @author FLIGHT
 * @creationDate 20.04.2022
 */
public class QueuingSystem {

    private long id;
    private Type type; // к какому звену относится СМО
    private double arrivalRate; // Интенсивность потока требований
    private double serviceRate; // Интенсивность обслуживания требований
    private double failureProbability; // Вероятность сбоя
    private double returnProbability;
    private double forwardProbability;
    private double noRequestsProbability; // Вероятность отсутствия требований
    private double requestsNumberExpectedValue; // Мат. ожидание числа требований
    private double durationExpectedValue; // Мат. ожидание длительности пребывания требований
    private double requestsInTheQueueExpectedValue; // Мат. ожидание числа требований в очереди
    private double queueTimeExpectedValue; // Мат. ожидание длительности требований в очереди

    public QueuingSystem(long id, Type type, double serviceRate, double failureProbability, double returnProbability, double forwardProbability) {
        this.id = id;
        this.type = type;
        this.serviceRate = serviceRate;
        this.failureProbability = failureProbability;
        this.returnProbability = returnProbability;
        this.forwardProbability = forwardProbability;
    }

    public QueuingSystem(Type type, double serviceRate,  double failureProbability, double returnProbability, double forwardProbability) {
        this.type = type;
        this.serviceRate = serviceRate;
        this.failureProbability = failureProbability;
        this.returnProbability = returnProbability;
        this.forwardProbability = forwardProbability;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getArrivalRate() {
        return arrivalRate;
    }

    public void setArrivalRate(double arrivalRate) {
        this.arrivalRate = arrivalRate;
    }

    public double getServiceRate() {
        return serviceRate;
    }

    public void setServiceRate(double serviceRate) {
        this.serviceRate = serviceRate;
    }

    public double getReturnProbability() {
        return returnProbability;
    }

    public void setReturnProbability(double returnProbability) {
        this.returnProbability = returnProbability;
    }

    public double getForwardProbability() {
        return forwardProbability;
    }

    public void setForwardProbability(double forwardProbability) {
        this.forwardProbability = forwardProbability;
    }

    public double getNoRequestsProbability() {
        return noRequestsProbability;
    }

    public void setNoRequestsProbability(double noRequestsProbability) {
        this.noRequestsProbability = noRequestsProbability;
    }

    public double getRequestsNumberExpectedValue() {
        return requestsNumberExpectedValue;
    }

    public void setRequestsNumberExpectedValue(double requestsNumberExpectedValue) {
        this.requestsNumberExpectedValue = requestsNumberExpectedValue;
    }

    public double getDurationExpectedValue() {
        return durationExpectedValue;
    }

    public void setDurationExpectedValue(double durationExpectedValue) {
        this.durationExpectedValue = durationExpectedValue;
    }

    public double getRequestsInTheQueueExpectedValue() {
        return requestsInTheQueueExpectedValue;
    }

    public void setRequestsInTheQueueExpectedValue(double requestsInTheQueueExpectedValue) {
        this.requestsInTheQueueExpectedValue = requestsInTheQueueExpectedValue;
    }

    public double getQueueTimeExpectedValue() {
        return queueTimeExpectedValue;
    }

    public void setQueueTimeExpectedValue(double queueTimeExpectedValue) {
        this.queueTimeExpectedValue = queueTimeExpectedValue;
    }

    public double getFailureProbability() {
        return failureProbability;
    }

    public void setFailureProbability(double failureProbability) {
        this.failureProbability = failureProbability;
    }
}
