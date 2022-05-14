package flight.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author FLIGHT
 * @creationDate 20.04.2022
 */
public class SystemModelingService {

    private final UserInterfaceService userInterfaceService;
    private final List<QueuingSystem> systems;
    private final List<QueuingSystem> collectionSystems;
    private QueuingSystem analysisSystem;
    private QueuingSystem dataStoreSystem;
    private Source source;

    public SystemModelingService() {
        this.systems = new ArrayList<>();
        this.collectionSystems = new ArrayList<>();
        userInterfaceService = new UserInterfaceService();
    }

    public void execute() {
        createModel();
        compute();
    }

    private void createModel() {
        int collectionSystemsCount = userInterfaceService.collectionSystems();
        List<Integer> serviceRateList = userInterfaceService.serviceRateList();
        createSystems(collectionSystemsCount, serviceRateList);
        createSource();
    }

    private void createSystems(int collectionSystemsCount, List<Integer> serviceRateList) {
        for (int i = 1; i <= collectionSystemsCount; i++) {
            collectionSystems.add(new QueuingSystem(i, Type.COLLECTION, serviceRateList.get(0), 0.9, 0.1));
        }

        analysisSystem = new QueuingSystem(Type.ANALYSIS, serviceRateList.size() > 1 ? serviceRateList.get(1) : serviceRateList.get(0), 0, 1);
        dataStoreSystem = new QueuingSystem(Type.DATASTORE, serviceRateList.size() > 2 ? serviceRateList.get(2) : serviceRateList.get(0), 0, 1);

        systems.addAll(collectionSystems);
        systems.add(analysisSystem);
        systems.add(dataStoreSystem);
    }

    private void createSource() {
        source = new Source(userInterfaceService.incomingIntensity(),
                createRouteProbabilitiesMap(userInterfaceService.probabilities()));
    }

    private Map<QueuingSystem, Double> createRouteProbabilitiesMap(List<Double> probabilities) {
        Map<QueuingSystem, Double> routeProbability = new HashMap<>();

        int index = 0;
        for (QueuingSystem system : collectionSystems) {
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
            if (userInterfaceService.isTableView())
                userInterfaceService.displayResultsTable(systems);
            else userInterfaceService.displayResultsList(systems);
        }
    }

    private void computeCollectionIncomingIntensity() {
        Map<QueuingSystem, Double> routeProbabilities = source.getRouteProbabilities();
        for (Map.Entry<QueuingSystem, Double> routeInfo : routeProbabilities.entrySet()) {
            QueuingSystem system = routeInfo.getKey();
            system.setArrivalRate(source.getIncomingIntensity() * routeInfo.getValue());
        }
    }

    private void computeAnalysisAndDataIncomingIntensity() {
        double intensity = 0.00;
        for (QueuingSystem collectionSystem : collectionSystems) {
            intensity += collectionSystem.getArrivalRate() * collectionSystem.getForwardProbability();
        }
        analysisSystem.setArrivalRate(intensity);
        dataStoreSystem.setArrivalRate(intensity);
    }

    private boolean stationaryModeExists() {
        for (QueuingSystem system : systems) {
            if (system.getArrivalRate() > system.getServiceRate()) {
                userInterfaceService.stationaryModeAlert();
                return false;
            }
        }
        return true;
    }

    private void computeQueuingSystems() {
        for (QueuingSystem system : systems) {
            computeNoRequestsProbability(system);
            computeRequestsNumberExpectedValue(system);
            computeDurationExpectedValue(system);
            computeRequestsInTheQueueExpectedValue(system);
            computeQueueTimeExpectedValue(system);
        }
    }

    private void computeNoRequestsProbability(QueuingSystem system) {
        double probability = 1 - (system.getArrivalRate() / system.getServiceRate());
        system.setNoRequestsProbability(probability);
    }

    private void computeRequestsNumberExpectedValue(QueuingSystem system) {
        double expectedValue = system.getArrivalRate() / (system.getServiceRate() - system.getArrivalRate());
        system.setRequestsNumberExpectedValue(expectedValue);
    }

    private void computeDurationExpectedValue(QueuingSystem system) {
        double duration = system.getRequestsNumberExpectedValue() / system.getArrivalRate();
        system.setDurationExpectedValue(duration);
    }

    private void computeRequestsInTheQueueExpectedValue(QueuingSystem system) {
        double requestsInTheQueue = (system.getArrivalRate() * system.getArrivalRate()) /
                (system.getServiceRate() * (system.getServiceRate() - system.getArrivalRate()));
        system.setRequestsInTheQueueExpectedValue(requestsInTheQueue);
    }

    private void computeQueueTimeExpectedValue(QueuingSystem system) {
        double expectedValue = system.getRequestsInTheQueueExpectedValue() / system.getArrivalRate();
        system.setQueueTimeExpectedValue(expectedValue);
    }
}
