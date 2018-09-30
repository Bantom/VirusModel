package services;

import excel.ImportFromExcel;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class VirusModelService {

    public static StatisticsAndCoefficients executeVirusModel(int quantityOfPeople, int quantityOfExperiments, int quantityOfExperimantsForCoefficients, String dataFilename) {
        List<StatisticsAndCoefficients> bestResultsForCoefficients = new ArrayList<>();

        for (int exp = 0; exp < quantityOfExperiments; exp++) {
            Coefficients coefficients = InstantiationService.initCoefficients(quantityOfPeople);

            for (int coefExp = 0; coefExp < quantityOfExperimantsForCoefficients; coefExp++) {
                bestResultsForCoefficients.add(executeVirusModelForOneSeason(coefficients, dataFilename));
            }
        }

        return chooseResultWithMinimalError(bestResultsForCoefficients);
    }

    private static StatisticsAndCoefficients chooseResultWithMinimalError(List<StatisticsAndCoefficients> listStatistics) {
        return listStatistics.stream()
                .reduce((u1, u2) -> u1.getGeneralError() < u2.getGeneralError() ? u1 : u2)
                .orElse(null);
    }

    private static StatisticsAndCoefficients executeVirusModelForOneSeason(Coefficients coefficients, String dataFilename) {
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
            agents = Actions.vaccinatePeopleFromS(week, vectorS, agents);

            List<Integer> newVectorG = Actions.movePeopleFromStoG(vectorS, vectorG, matrixContacts, vectorRG);
            vectorS = Utils.vectorMinusVector(vectorS, newVectorG);

            VectorsDTO tmp = Actions.movePeopleFromStoI(vectorS, vectorI, matrixContacts, vectorRI, agents);
            List<Integer> newVectorI = tmp.getVectorI();
            agents = tmp.getAgents();
            vectorS = Utils.vectorMinusVector(vectorS, newVectorI);
            vectorS = Actions.movePeopleFromYtoS(vectorY, vectorS);
            vectorY = Actions.movePeopleFromItoY(vectorI, coefficients);
            vectorI = Utils.vectorMinusVector(vectorI, vectorY);

            vectorS = Actions.movePeopleFromOtoS(vectorO, vectorS);
            vectorO = Actions.movePeopleFromGtoO(vectorG, coefficients);
            vectorS = Actions.movePeopleFromGtoS(vectorG, vectorS);
            vectorS = Actions.movePeopleFromItoS(vectorI, vectorS);
            vectorI = newVectorI;
            vectorG = newVectorG;

            statisticsForSeason.add(new StatisticsGVI(StatisticsService.getStatisticsG(vectorG), StatisticsService.getStatisticsV(agents), StatisticsService.getStatisticsI(vectorI)));
        }
        double generalError = countGeneralError(dataFilename, statisticsForSeason);
        return new StatisticsAndCoefficients(statisticsForSeason, coefficients, generalError);
    }

    private static double countGeneralError(String fileName, List<StatisticsGVI> statistics) {
        List<StatisticsGVI> staticticsFromFile = ImportFromExcel.readSeasonsFromFile(fileName);
        double squareErrorI = 0;
        double squareErrorG = 0;
        double squareErrorV = 0;

        for (int i = 0; i < 33; i++) {
            squareErrorI += Math.pow(statistics.get(i).getI() - staticticsFromFile.get(i).getI(), 2);
            squareErrorG += Math.pow(statistics.get(i).getG() - staticticsFromFile.get(i).getG(), 2);
            squareErrorV += Math.pow(statistics.get(i).getV() - staticticsFromFile.get(i).getV(), 2);
        }

        squareErrorI = Math.sqrt(squareErrorI / (statistics.size() * (statistics.size() - 1)));
        squareErrorG = Math.sqrt(squareErrorG / (statistics.size() * (statistics.size() - 1)));
        squareErrorV = Math.sqrt(squareErrorV / (statistics.size() * (statistics.size() - 1)));

        return squareErrorI + squareErrorG + squareErrorV;
    }

    /**
     * https://statpsy.ru/pearson/primer-pearson/
     * от 0 до 0,3	    очень слабая
     * от 0,3 до 0,5	слабая
     * от 0, 5 до 0,7	средняя
     * от 0,7 до 0,9	высокая
     * от 0,9 до 1	    очень высокая
     *
     * @return correlation coefficient
     */
    public static double getCorrelationCoefficient(List<Double> countedData, List<Double> statisticsData) {
        double correlationCoefficient = 0;
        if (countedData.size() != statisticsData.size())
            throw new IllegalArgumentException("List counted data has another size as statistics data");

        double avgCountedData = countedData.stream().mapToDouble(Double::doubleValue).sum() / countedData.size();
        double avgStatisticsData = statisticsData.stream().mapToDouble(Double::doubleValue).sum() / statisticsData.size();

        List<Double> deviationCountedData = new ArrayList<>();
        List<Double> deviationStatisticsData = new ArrayList<>();
        for (int i = 0; i < countedData.size(); i++) {
            deviationCountedData.set(i, avgCountedData - countedData.get(i));
            deviationStatisticsData.set(i, avgStatisticsData - statisticsData.get(i));
        }

        List<Double> squareDeviationCountedData = new ArrayList<>();
        List<Double> squareDeviationStatisticsData = new ArrayList<>();
        for (int i = 0; i < countedData.size(); i++) {
            squareDeviationCountedData.set(i, Math.pow(deviationCountedData.get(i), 2));
            squareDeviationStatisticsData.set(i, Math.pow(deviationStatisticsData.get(i), 2));
        }

        List<Double> composition = new ArrayList<>();
        for (int i = 0; i < countedData.size(); i++) {
            composition.set(i, deviationCountedData.get(i) * deviationStatisticsData.get(i));
        }

        double sumSquareDeviationCountedData = squareDeviationCountedData.stream().mapToDouble(Double::doubleValue).sum();
        double sumSquareDeviationStatisticsData = squareDeviationStatisticsData.stream().mapToDouble(Double::doubleValue).sum();
        double sumComposition = composition.stream().mapToDouble(Double::doubleValue).sum();

        correlationCoefficient = sumComposition / Math.sqrt(sumSquareDeviationCountedData * sumSquareDeviationStatisticsData);

        return correlationCoefficient;
    }
}
