package services;

import model.Coefficients;
import model.StatisticsGVI;

import java.util.List;

public class OutputService {

    public static void showStatisticsGVI(List<StatisticsGVI> statistics) {
        statistics.forEach(System.out::println);
    }

    public static void showCoefficients(Coefficients coefficients) {
        System.out.println(coefficients.toString());
    }

    public static void showGeneralError(double generalError) {
        System.out.println("General error: " + generalError);
    }
}
