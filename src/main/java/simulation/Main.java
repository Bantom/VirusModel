package simulation;

import Tools.TimeLogging;
import excel.ExportToExcel;
import lombok.extern.slf4j.Slf4j;
import model.StatisticsAndCoefficients;
import simulation.services.OutputService;
import simulation.services.VirusModelService;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class Main {

    private static final int quantityOfPeople = 2000;
    private static final int quantityOfExperiments = 50;
    private static final int quantityOfExperimentsForCoefficients = 5;
    private static final String FILE_NAME = "Data.xlsx";

    private static TimeLogging timeLogging = new TimeLogging();
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("mm:ss:nnnnnnnnn");

    public static void main(String[] args) throws IOException {
        timeLogging.setFirst(LocalTime.now());
        StatisticsAndCoefficients result = VirusModelService.executeVirusModel(quantityOfPeople, quantityOfExperiments, quantityOfExperimentsForCoefficients, FILE_NAME);
        timeLogging.setSecond(LocalTime.now());
        log.info("Time : " + timeLogging.getDiff().format(dateFormat));
        ExportToExcel.writeToExcel(result, "coefficients");

        OutputService.showStatisticsGVI(result.getStatisticsGVI());
        OutputService.showCoefficients(result.getCoefficients());
        OutputService.showCorrelationCoefficients(result.getGcorCoef(), result.getVcorCoef(), result.getIcorCoef());
        OutputService.showGeneralError(result.getGeneralError());
    }
}
