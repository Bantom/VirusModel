package experiments.vaccinationIntensity;

import experiments.vaccinationIntensity.dto.ServiceIntensityDTO;
import experiments.vaccinationIntensity.services.VirusModelServiceIntensity;
import simulation.services.OutputService;
import Tools.TimeLogging;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class Main {
    private static final int quantityOfSpans = 10;
    private static final int maxIntencity = 5;
    private static final String FILE_NAME = "Data.xlsx";
    private static final String coefficientsFilename = "coefficients.xlsx";

    private static TimeLogging timeLogging = new TimeLogging();
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("mm:ss:nnnnnnnnn");

    public static void main(String[] args) {
        timeLogging.setFirst(LocalTime.now());
        List<ServiceIntensityDTO> result = VirusModelServiceIntensity.executeVirusModel(quantityOfSpans, maxIntencity, FILE_NAME, coefficientsFilename);
        timeLogging.setSecond(LocalTime.now());
        log.info("Time : " + timeLogging.getDiff().format(dateFormat));

        for (ServiceIntensityDTO dto: result) {
            System.out.println("---------------" + dto.getIntensity() +"---------------");
            System.out.println("Week number: " + dto.getWeekNumber());
            OutputService.showCorrelationCoefficients(dto.getGcorCoef(), dto.getVcorCoef(), dto.getIcorCoef());
            OutputService.showGeneralError(dto.getGeneralError());
        }
    }
}
