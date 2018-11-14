package experiments.startingVaccination.services;

import excel.ImportFromExcel;
import experiments.startingVaccination.dto.StartingVaccinationDTO;
import model.*;
import simulation.services.Actions;
import simulation.services.InstantiationService;
import simulation.services.Utils;
import simulation.services.VirusModelService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static simulation.services.VirusModelService.checkVectorR;
import static simulation.services.VirusModelService.dynamicChangesMatrixContacts;

public class VirusModelServiceStartingVaccination {

    public static List<StartingVaccinationDTO> executeVirusModel(String dataFilename, String coefficientsFilename) {
        List<StartingVaccinationDTO> startingVaccinationList = new ArrayList<>();
        Coefficients coefficients = ImportFromExcel.readCoefficientsFromFile(coefficientsFilename);
        StatisticsAndCoefficients basicStatistics = VirusModelService.executeVirusModelForOneSeason(coefficients, dataFilename);
        int weekWithZeroValue = findWeekWithZeroValue(basicStatistics);

        for (int weekExtinction = 1; weekExtinction < weekWithZeroValue; weekExtinction++) {
            double percent  = executeVirusModelForOneSeason(coefficients, weekExtinction);
            startingVaccinationList.add(new StartingVaccinationDTO(
                    percent,
                    weekExtinction
            ));
        }
        return startingVaccinationList;
    }


    private static double executeVirusModelForOneSeason(Coefficients coefficients, int weekExtinction) {
        VectorsDTO vectors = InstantiationService.initVectors(coefficients);

        List<Agent> agents = vectors.getAgents();
        agents = Actions.makePeopleBecameUnsusceptible(agents, coefficients);
        List<Integer> vectorS = vectors.getVectorS();
        List<Integer> vectorI = vectors.getVectorI();
        List<Integer> vectorG = vectors.getVectorG();
        MatrixContacts matrixContacts = vectors.getMatrixContacts();
        List<Integer> vectorRG = vectors.getVectorRG();
        List<Integer> vectorRI = vectors.getVectorRI();
        List<Integer> vectorY = Utils.getVectorWithZeroValues(coefficients.getQuantityOfPeople());
        List<Integer> vectorO = Utils.getVectorWithZeroValues(coefficients.getQuantityOfPeople());

        for (int week = 0; week < 33; week++) {
            if (week == (weekExtinction - 1)) {
                return ActionsStartingVaccination.getPercentForStartingExtinction(agents, vectorRI, coefficients);
            }
            agents = Actions.vaccinatePeopleFromS(week, vectorS, agents);

            List<Integer> newVectorG = Collections.emptyList();
            if (coefficients.isAnotherIll()) {
                newVectorG = Actions.movePeopleFromStoG(vectorS, vectorG, matrixContacts, vectorRG);
                vectorS = Utils.vectorMinusVector(vectorS, newVectorG);
            }

            VectorsDTO tmp = Actions.movePeopleFromStoI(vectorS, vectorI, matrixContacts, vectorRI, agents);
            List<Integer> newVectorI = tmp.getVectorI();
            agents = tmp.getAgents();
            vectorS = Utils.vectorMinusVector(vectorS, newVectorI);
            vectorS = Actions.movePeopleFromYtoS(vectorY, vectorS);
            vectorY = Actions.movePeopleFromItoY(vectorI, coefficients);
            vectorI = Utils.vectorMinusVector(vectorI, vectorY);

            if (coefficients.isAnotherIll()) {
                vectorS = Actions.movePeopleFromOtoS(vectorO, vectorS);
                vectorO = Actions.movePeopleFromGtoO(vectorG, coefficients);
                vectorS = Actions.movePeopleFromGtoS(vectorG, vectorS);
                vectorG = newVectorG;
            }
            vectorS = Actions.movePeopleFromItoS(vectorI, vectorS);
            vectorI = newVectorI;

            matrixContacts = dynamicChangesMatrixContacts(matrixContacts);
            vectorRG = checkVectorR(vectorRG, matrixContacts);
            vectorRI = checkVectorR(vectorRI, matrixContacts);
        }
        return 0;
    }

    private static int findWeekWithZeroValue(StatisticsAndCoefficients statisticsAndCoefficients) {
        int weekNumber = 0;
        for (int i = 0; i < statisticsAndCoefficients.getStatisticsGVI().size(); i++) {
            if (statisticsAndCoefficients.getStatisticsGVI().get(i).getI() == 0) {
                weekNumber = i + 1;
                break;
            }
        }
        return weekNumber;
    }
}
