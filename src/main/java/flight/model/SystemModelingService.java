package flight.model;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author FLIGHT
 * @creationDate 20.04.2022
 */
public class SystemModelingService {

    private final UserInterfaceService userInterfaceService;
    private QueuingNetwork queuingNetwork;

    public SystemModelingService() {
        userInterfaceService = new UserInterfaceService();
    }

    public void execute() {
        createModel();
        compute();
    }

    private void createModel() {
        createSystems(userInterfaceService.collectionSystems(),
                userInterfaceService.serviceRateList(),
                userInterfaceService.failureProbability());
        createSource();
    }

    private void createSystems(int collectionSystemsCount, List<Integer> serviceRateList, double failureProbability) {
        List<QueuingSystem> collectionSystems = new ArrayList<>();
//        for (int i = 1; i <= collectionSystemsCount; i++) {
//            collectionSystems.add(new QueuingSystem(i, Type.COLLECTION, serviceRateList.get(0), failureProbability,  0.9, 0.1));
//        }

        IntStream.range(1, collectionSystemsCount + 1).forEach(index -> collectionSystems.add(new QueuingSystem(index, Type.COLLECTION, serviceRateList.get(0), failureProbability, 0.9, 0.1)));

        QueuingSystem analysisSystem = new QueuingSystem(Type.ANALYSIS, serviceRateList.size() > 1 ? serviceRateList.get(1) : serviceRateList.get(0), failureProbability, 0, 1);
        QueuingSystem dataStoreSystem = new QueuingSystem(Type.DATASTORE, serviceRateList.size() > 2 ? serviceRateList.get(2) : serviceRateList.get(0), failureProbability, 0, 1);

        List<QueuingSystem> systems = new ArrayList<>(collectionSystems);
        systems.addAll(Arrays.asList(analysisSystem, dataStoreSystem));

        queuingNetwork = new QueuingNetwork(systems, collectionSystems, analysisSystem, dataStoreSystem, failureProbability);
    }

    private void createSource() {
        Source source = new Source(userInterfaceService.incomingIntensity(),
                createRouteProbabilitiesMap(userInterfaceService.probabilities(queuingNetwork.getCollectionSystems().size())));

        queuingNetwork.setSource(source);
        queuingNetwork.setArrivalRate(source.getArrivalRate());
    }

    private Map<QueuingSystem, Double> createRouteProbabilitiesMap(List<Double> probabilities) {
        Map<QueuingSystem, Double> routeProbability = new HashMap<>();

        int index = 0;
        for (QueuingSystem system : queuingNetwork.getCollectionSystems()) {
            if (index < probabilities.size())
                routeProbability.put(system, probabilities.get(index++));
            else
                routeProbability.put(system, probabilities.get(0));
        }
        return routeProbability;
    }


    private void compute() {
        computeCollectionIncomingIntensity();
        computeAnalysisAndDataIncomingIntensity();
        if (stationaryModeExists()) {
            computeQueuingSystems();
            computeQueuingNetwork();
            if (userInterfaceService.isTableView())
                userInterfaceService.displayResultsTable(queuingNetwork);
            else userInterfaceService.displayResultsList(queuingNetwork);
        }
    }

    private void computeCollectionIncomingIntensity() {
        Source source = queuingNetwork.getSource();
        Map<QueuingSystem, Double> routeProbabilities = source.getRouteProbabilities();
        for (Map.Entry<QueuingSystem, Double> routeInfo : routeProbabilities.entrySet()) {
            QueuingSystem system = routeInfo.getKey();
            system.setArrivalRate(source.getArrivalRate() * routeInfo.getValue());
        }
    }

    private void computeAnalysisAndDataIncomingIntensity() {
//        double intensity = 0.00;
//        for (QueuingSystem collectionSystem : queuingNetwork.getCollectionSystems()) {
//            intensity += collectionSystem.getArrivalRate() * collectionSystem.getForwardProbability();
//        }

        double intensity = queuingNetwork.getCollectionSystems().stream()
                .map(s -> s.getArrivalRate() * s.getForwardProbability())
                .mapToDouble(x -> x).sum();

        QueuingSystem analysisSystem = queuingNetwork.getAnalysisSystem();
        QueuingSystem dataStoreSystem = queuingNetwork.getDataStoreSystem();

        analysisSystem.setArrivalRate(intensity);
        dataStoreSystem.setArrivalRate(intensity);
    }

    private boolean stationaryModeExists() {
        boolean stationaryModeExists = queuingNetwork.getSystems().stream().allMatch(system -> system.getArrivalRate() <= system.getServiceRate());
        if (!stationaryModeExists)
                userInterfaceService.stationaryModeAlert();

        return stationaryModeExists;
    }

    private void computeQueuingSystems() {
        for (QueuingSystem system : queuingNetwork.getSystems()) {
            computeNoRequestsProbability(system);
            computeRequestsNumberExpectedValue(system);
            computeDurationExpectedValue(system);
            computeRequestsInTheQueueExpectedValue(system);
            computeQueueTimeExpectedValue(system);
        }
    }

    private void computeNoRequestsProbability(QueuingSystem system) {
        double failureFreeProbability = 1 - system.getFailureProbability();
        double probability = 1 - (system.getArrivalRate() / (system.getServiceRate() * failureFreeProbability));
        system.setNoRequestsProbability(probability);
    }

    private void computeRequestsNumberExpectedValue(QueuingSystem system) {
        double failureFreeProbability = 1 - system.getFailureProbability();
        double expectedValue = system.getArrivalRate() / ((system.getServiceRate() * failureFreeProbability) - system.getArrivalRate());
        system.setRequestsNumberExpectedValue(expectedValue);
    }

    private void computeDurationExpectedValue(QueuingSystem system) {
        double duration = system.getRequestsNumberExpectedValue() / system.getArrivalRate();
        system.setDurationExpectedValue(duration);
    }

    private void computeRequestsInTheQueueExpectedValue(QueuingSystem system) {
        double failureFreeProbability = 1 - system.getFailureProbability();
        double serviceRateWithFailures = system.getServiceRate() * failureFreeProbability;
        double requestsInTheQueue = (system.getArrivalRate() * system.getArrivalRate()) /
                (serviceRateWithFailures * (serviceRateWithFailures - system.getArrivalRate()));
        system.setRequestsInTheQueueExpectedValue(requestsInTheQueue);
    }

    private void computeQueueTimeExpectedValue(QueuingSystem system) {
        double expectedValue = system.getRequestsInTheQueueExpectedValue() / system.getArrivalRate();
        system.setQueueTimeExpectedValue(expectedValue);
    }

    private void computeRequestsNumberExpectedValue() {
        QueuingSystem analysisSystem = queuingNetwork.getAnalysisSystem();
        QueuingSystem dataStoreSystem = queuingNetwork.getDataStoreSystem();

        double collectionAverageExpectedValue = queuingNetwork.getCollectionSystems()
                .stream()
                .mapToDouble(QueuingSystem::getRequestsNumberExpectedValue)
                .sum();
        queuingNetwork.setRequestsNumberExpectedValue(collectionAverageExpectedValue +
                analysisSystem.getRequestsNumberExpectedValue() + dataStoreSystem.getRequestsNumberExpectedValue());
    }

    private void computeDurationExpectedValue() {
        QueuingSystem analysisSystem = queuingNetwork.getAnalysisSystem();
        QueuingSystem dataStoreSystem = queuingNetwork.getDataStoreSystem();

        double collectionAverageExpectedValue = queuingNetwork.getCollectionSystems()
                .stream()
                .mapToDouble(QueuingSystem::getDurationExpectedValue)
                .average().orElse(Double.NaN);
        queuingNetwork.setDurationExpectedValue(collectionAverageExpectedValue +
                analysisSystem.getDurationExpectedValue() + dataStoreSystem.getDurationExpectedValue());
    }

    private void computeQueuingNetwork() {
        computeDurationExpectedValue();
        computeRequestsNumberExpectedValue();
    }
}
