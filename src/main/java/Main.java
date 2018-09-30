import Tools.TimeLogging;
import lombok.extern.slf4j.Slf4j;
import model.StatisticsAndCoefficients;
import services.OutputService;
import services.VirusModelService;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class Main {

    private static final int quantityOfPeople = 2000;
    private static final int quantityOfExperiments = 5;
    private static final int quantityOfExperimentsForCoefficients = 5;
    private static final String FILE_NAME = "Data.xlsx";

    private static TimeLogging timeLogging = new TimeLogging();
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("mm:ss:nnnnnnnnn");

    public static void main(String[] args) {
        timeLogging.setFirst(LocalTime.now());
        StatisticsAndCoefficients result = VirusModelService.executeVirusModel(quantityOfPeople, quantityOfExperiments, quantityOfExperimentsForCoefficients, FILE_NAME);
        timeLogging.setSecond(LocalTime.now());
        log.info("Time : " + timeLogging.getDiff().format(dateFormat));

        OutputService.showStatisticsGVI(result.getStatisticsGVI());
        OutputService.showCoefficients(result.getCoefficients());
        OutputService.showGeneralError(result.getGeneralError());
    }
}
