package Experiments.vaccinationIntensity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.Coefficients;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceIntensityDTO {
    private double generalError;
    private double gcorCoef;
    private double vcorCoef;
    private double icorCoef;
    int weekNumber;
    double intensity;
}
