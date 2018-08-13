import model.StatisticsAndCoefficients;
import services.OutputService;
import services.VirusModelService;

public class Main {

    private static final int quantityOfPeople = 5000;
    private static final int quantityOfExperiments = 1;
    private static final int quantityOfExperimentsForCoefficients = 1;
    private static final String FILE_NAME = "Data.xlsx";


    public static void main(String[] args) {
        StatisticsAndCoefficients result = VirusModelService.executeVirusModel(quantityOfPeople, quantityOfExperiments, quantityOfExperimentsForCoefficients, FILE_NAME);

        OutputService.showStatisticsGVI(result.getStatisticsGVI());
        OutputService.showCoefficients(result.getCoefficients());
        OutputService.showGeneralError(result.getGeneralError());
    }
}
