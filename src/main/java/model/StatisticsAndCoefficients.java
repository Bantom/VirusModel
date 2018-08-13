package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StatisticsAndCoefficients {

    private List<StatisticsGVI> StatisticsGVI;
    private Coefficients coefficients;
    private double generalError;

}
