package experiments.startingVaccination;

import Tools.TimeLogging;
import experiments.startingVaccination.dto.StartingVaccinationDTO;
import experiments.startingVaccination.services.VirusModelServiceStartingVaccination;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class Main {
    private static final String FILE_NAME = "Data.xlsx";
    private static final String coefficientsFilename = "coefficients.xlsx";

    private static TimeLogging timeLogging = new TimeLogging();
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("mm:ss:nnnnnnnnn");

    public static void main(String[] args) {
        timeLogging.setFirst(LocalTime.now());
        List<StartingVaccinationDTO> result = VirusModelServiceStartingVaccination.executeVirusModel(FILE_NAME, coefficientsFilename);
        timeLogging.setSecond(LocalTime.now());
        log.info("Time : " + timeLogging.getDiff().format(dateFormat));

        double beforePercentValue = 0;
        for (StartingVaccinationDTO dto: result) {
            System.out.println("On week " + dto.getWeekNumber() + " needs to vaccinate " + dto.getPercent() + "% of people for starting extinction.");
            if (beforePercentValue == dto.getPercent()){
                break;
            }
            beforePercentValue = dto.getPercent();
        }
    }
}
