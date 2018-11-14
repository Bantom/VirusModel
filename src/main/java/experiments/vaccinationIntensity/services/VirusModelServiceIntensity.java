package experiments.vaccinationIntensity.services;

import experiments.vaccinationIntensity.dto.ServiceIntensityDTO;
import simulation.services.*;
import excel.ImportFromExcel;
import model.*;

import java.util.*;
import java.util.stream.Collectors;

import static simulation.services.VirusModelService.*;

public class VirusModelServiceIntensity {
    public static List<ServiceIntensityDTO> executeVirusModel(int quantityOfSpans, double maxIntensity, String dataFilename, String coefficientsFilename) {
        List<ServiceIntensityDTO> intensityList = new ArrayList<>();
        double spanLength = (maxIntensity - 1) / quantityOfSpans;
        Coefficients coefficients = ImportFromExcel.readCoefficientsFromFile(coefficientsFilename);
        double intensity = 1;
        for (int exp = 0; exp < quantityOfSpans; exp++) {
            StatisticsAndCoefficients statisticsAndCoefficients = executeVirusModelForOneSeason(coefficients, dataFilename, intensity);
            setCorrelationCoefficients(statisticsAndCoefficients, dataFilename);
            int weekWithZeroValue = findWeekWithZeroValue(statisticsAndCoefficients);
            if (exp != 0) {
                weekWithZeroValue = intensityList.get(exp - 1).getWeekNumber() < weekWithZeroValue ? intensityList.get(exp - 1).getWeekNumber() : weekWithZeroValue;
            }
            intensityList.add(new ServiceIntensityDTO(
                    statisticsAndCoefficients.getGeneralError(),
                    statisticsAndCoefficients.getGcorCoef(),
                    statisticsAndCoefficients.getVcorCoef(),
                    statisticsAndCoefficients.getIcorCoef(),
                    weekWithZeroValue,
                    intensity
            ));
            intensity += spanLength;
        }

        return intensityList;
    }


    private static StatisticsAndCoefficients executeVirusModelForOneSeason(Coefficients coefficients, String dataFilename, double intensity) {
        List<StatisticsGVI> statisticsForSeason = new ArrayList<>();

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
            agents = ActionsIntensity.vaccinatePeopleFromS(week, vectorS, agents, intensity);

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

            statisticsForSeason.add(new StatisticsGVI(StatisticsService.getStatisticsG(vectorG), StatisticsService.getStatisticsV(agents), StatisticsService.getStatisticsI(vectorI)));
        }
        double generalError = countGeneralError(dataFilename, statisticsForSeason);
        return new StatisticsAndCoefficients(statisticsForSeason, coefficients, generalError);
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

    private static void setCorrelationCoefficients(StatisticsAndCoefficients statistics, String fileName) {
        List<StatisticsGVI> staticticsFromFile = ImportFromExcel.readSeasonsFromFile(fileName);
        List<Double> gStatistics = staticticsFromFile.stream().map(StatisticsGVI::getG).collect(Collectors.toList());
        List<Double> vStatistics = staticticsFromFile.stream().map(StatisticsGVI::getV).collect(Collectors.toList());
        List<Double> iStatistics = staticticsFromFile.stream().map(StatisticsGVI::getI).collect(Collectors.toList());

        List<StatisticsGVI> statisticsGVI = statistics.getStatisticsGVI();
        List<Double> g = statisticsGVI.stream().map(StatisticsGVI::getG).collect(Collectors.toList());
        List<Double> v = statisticsGVI.stream().map(StatisticsGVI::getV).collect(Collectors.toList());
        List<Double> i = statisticsGVI.stream().map(StatisticsGVI::getI).collect(Collectors.toList());

        double gcorCoef = getCorrelationCoefficient(g, gStatistics);
        double vcorCoef = getCorrelationCoefficient(v, vStatistics);
        double icorCoef = getCorrelationCoefficient(i, iStatistics);
        statistics.setGcorCoef(gcorCoef);
        statistics.setVcorCoef(vcorCoef);
        statistics.setIcorCoef(icorCoef);
    }
}
