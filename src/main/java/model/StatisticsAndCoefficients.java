package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StatisticsAndCoefficients {

    private List<StatisticsGVI> StatisticsGVI;
    private Coefficients coefficients;
    private double generalError;
    private double gcorCoef;
    private double vcorCoef;
    private double icorCoef;

    public StatisticsAndCoefficients(List<model.StatisticsGVI> statisticsGVI, Coefficients coefficients, double generalError) {
        StatisticsGVI = statisticsGVI;
        this.coefficients = coefficients;
        this.generalError = generalError;
    }
}
