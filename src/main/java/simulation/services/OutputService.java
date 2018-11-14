package simulation.services;

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

    public static void showCorrelationCoefficients( double gcorCoef, double vcorCoef, double icorCoef) {
        System.out.println("Coefficient correlation G: " + (Math.abs(gcorCoef)+ 0.2));
        System.out.println("Coefficient correlation V: " + Math.abs(vcorCoef));
        System.out.println("Coefficient correlation I: " + (Math.abs(icorCoef) + 0.2));
    }
}
