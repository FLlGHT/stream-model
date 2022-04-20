package flight.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
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
        } catch (IOException e) {
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
        } catch (IOException e) {
            return serviceRateList();
        }
    }

    public List<Double> probabilities() {
        System.out.println("Введите через пробел маршрутные вероятности перехода из источника в каждую систему: \n" +
                "(Либо введите одно значение, чтобы задать одинаковую вероятность перехода из источника) ");
        try {
            String probabilitiesInfo = reader.readLine();
            return Arrays.stream(probabilitiesInfo.split(" "))
                    .map(Double::parseDouble).collect(Collectors.toList());
        }
        catch (IOException e) {
            System.out.println("Проверьте корректность введенных данных! ");
            return probabilities();
        }
    }

    public int incomingIntensity() {
        System.out.println("Введите интенсивность потока требований из источника (пакетов в секунду): ");
        try {
            String intensityInfo = reader.readLine();
            return Integer.parseInt(intensityInfo);
        } catch (IOException e) {
            return incomingIntensity();
        }
    }

    public void stationaryModeAlert() {
        System.out.println("В системе нарушается условие существование станционарного режима");
    }

    public void displayResults(List<QueuingSystem> systems) {
        String pattern = "##0.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        System.out.println("Характеристики системы: " + "\n");
        for (QueuingSystem system : systems) {
            System.out.println("-----------------------------------------");
            System.out.println(system.getType().getText() + (system.getType().equals(Type.COLLECTION) ? " - " + system.getId() : ""));
            System.out.println("Интенсивность поступления требований " + decimalFormat.format(system.getArrivalRate()));
            System.out.println("Интенсивность обслуживания требований " + decimalFormat.format(system.getServiceRate()));
            System.out.println("Вероятность отсутствия требований " + decimalFormat.format(system.getNoRequestsProbability()));
            System.out.println("Мат. ожидание числа требований " + decimalFormat.format(system.getRequestsNumberExpectedValue()));
            System.out.println("Мат. ожидание длительности пребывания требований " + decimalFormat.format(system.getDurationExpectedValue()));
            System.out.println("Мат. ожидание числа требований в очереди " + decimalFormat.format(system.getRequestsInTheQueueExpectedValue()));
            System.out.println("Мат. ожидание длительности требований в очереди " + decimalFormat.format(system.getQueueTimeExpectedValue()));
            System.out.println("-----------------------------------------");
        }
    }
}
