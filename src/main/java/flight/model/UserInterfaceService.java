package flight.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FLIGHT
 * @creationDate 20.04.2022
 */
public class UserInterfaceService {

    private final BufferedReader reader;

    public UserInterfaceService() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public int collectionSystems() {
        System.out.println("Введите количество систем сбора данных: ");
        try {
            return Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            System.out.println("Введите целое число! ");
            return collectionSystems();
        }
    }

    public List<Integer> serviceRateList() {
        System.out.println("Введите через пробел интенсивности обслуживания требований \n " +
                "1. Звеньев сбора данных 2. Звена анализа данных 3. Звена хранения данных \n" +
                "(Либо введите одно значение, чтобы сделать интенсивность обслуживания одинаковой)");
        try {
            String[] serviceRateInfo = reader.readLine().split(" ");
            return Arrays.stream(serviceRateInfo)
                    .map(Integer::parseInt).collect(Collectors.toList());
        } catch (Exception e) {
            return serviceRateList();
        }
    }

    /**
     * Соотношение времени нахождения системы в состоянии восстановления к общему времени работы
     * <= 0.05
     */
    public double failureProbability() {
        System.out.println("Введите вероятность того, что система находится в состоянии восстановления \n" +
                "(Соотношение времени нахождения прибора в состоянии восстановления к общему времени работы)");
        try {
            double probability = Double.parseDouble(reader.readLine());
            return probability > 0 && probability < 1 ? probability : failureProbability();
        }
        catch (Exception e) {
            return failureProbability();
        }
    }

    public List<Double> probabilities(int collectionSystemsCount) {
        System.out.println("Введите через пробел маршрутные вероятности перехода из источника в каждую систему: \n" +
                "(Либо введите одно значение, чтобы задать одинаковую вероятность перехода из источника) ");
        try {
            String probabilitiesInfo = reader.readLine();
            if (probabilitiesInfo.equals("="))
                return Collections.singletonList((double) 1 / collectionSystemsCount);

            return Arrays.stream(probabilitiesInfo.split(" "))
                    .map(Double::parseDouble).collect(Collectors.toList());
        }
        catch (Exception e) {
            System.out.println("Проверьте корректность введенных данных! ");
            return probabilities(collectionSystemsCount);
        }
    }

    public double incomingIntensity() {
        System.out.println("Введите интенсивность потока требований из источника (пакетов в секунду): ");
        try {
            String intensityInfo = reader.readLine();
            return Double.parseDouble(intensityInfo);
        } catch (Exception e) {
            return incomingIntensity();
        }
    }

    public void stationaryModeAlert() {
        System.out.println("В системе нарушается условие существование станционарного режима");
    }

    public boolean isTableView() {
        try {
            System.out.println("Представить результататы в виде списка или в виде таблицы? ");
            String answer = reader.readLine().toLowerCase();
            return answer.contains("таб") || answer.startsWith("t") || answer.startsWith("т");
        } catch (IOException e) {
            isTableView();
        }
        return false;
    }

    public void displayResultsList(QueuingNetwork queuingNetwork) {
        String pattern = "##0.000";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        System.out.println("\n\n" + "Характеристики системы: " + "\n");
        for (QueuingSystem system : queuingNetwork.getSystems()) {
            System.out.println("-----------------------------------------");
            System.out.println(system.getType().getText() + (system.getType().equals(Type.COLLECTION) ? " - " + system.getId() : ""));
            System.out.println("Интенсивность поступления требований " + decimalFormat.format(system.getArrivalRate()));
            System.out.println("Интенсивность обслуживания требований " + decimalFormat.format(system.getServiceRate()));
            System.out.println("Вероятность отказа " + decimalFormat.format(system.getFailureProbability()));
            System.out.println("Вероятность отсутствия требований " + decimalFormat.format(system.getNoRequestsProbability()));
            System.out.println("Мат. ожидание числа требований " + decimalFormat.format(system.getRequestsNumberExpectedValue()));
            System.out.println("Мат. ожидание длительности пребывания требований " + decimalFormat.format(system.getDurationExpectedValue()));
            System.out.println("Мат. ожидание числа требований в очереди " + decimalFormat.format(system.getRequestsInTheQueueExpectedValue()));
            System.out.println("Мат. ожидание длительности требований в очереди " + decimalFormat.format(system.getQueueTimeExpectedValue()));
            System.out.println("-----------------------------------------");
        }
    }

    public void displayResultsTable(QueuingNetwork queuingNetwork) {
        String pattern = "##0.000";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        System.out.println(ConsoleColors.BLUE + new Formatter().format("%43s %6s %9s %8s %6s %8s %8s %8s", "lambda ", "mu ", "P(f)", "P(0)", "q", "u", "b", "w") + ConsoleColors.RESET);
        for (QueuingSystem system : queuingNetwork.getSystems()) {
            System.out.println(new Formatter().format("%26s %15s %8s %8s %8s %8s %8s %8s %8s",
                    system.getType().getText() + (system.getType().equals(Type.COLLECTION) ? " - " + system.getId() : ""),
                    decimalFormat.format(system.getArrivalRate()),
                    decimalFormat.format(system.getServiceRate()),
                    decimalFormat.format(system.getFailureProbability()),
                    decimalFormat.format(system.getNoRequestsProbability()),
                    decimalFormat.format(system.getRequestsNumberExpectedValue()),
                    decimalFormat.format(system.getDurationExpectedValue()),
                    decimalFormat.format(system.getRequestsInTheQueueExpectedValue()),
                    decimalFormat.format(system.getQueueTimeExpectedValue())));
        }
        System.out.println("\n");
        System.out.println(new Formatter().format("%26s %15s %8s %8s %8s %8s %8s %8s %8s",
                "Общие характеристики сети",
                decimalFormat.format(queuingNetwork.getArrivalRate()),
                "",
                decimalFormat.format(queuingNetwork.getFailureProbability()),
                "",
                decimalFormat.format(queuingNetwork.getRequestsNumberExpectedValue()),
                decimalFormat.format(queuingNetwork.getDurationExpectedValue()),
                "",
                ""));
    }
}
