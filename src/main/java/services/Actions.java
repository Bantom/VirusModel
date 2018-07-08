package services;

import excel.ImportFromExcel;
import model.*;
import org.apache.poi.ss.formula.functions.T;
import services.Utils;

import java.util.List;
import java.util.OptionalDouble;

import static java.lang.Math.abs;


public class Actions {

    public static List<Agent> vaccinatePeopleFromS(int weekNumber, List<Integer> vectorS, List<Agent> agents) {
        int counterOfVaccinated = 0;
        int peopleQuantityForVaccination = (int) Math.round(vectorS.size() * getPercentsStoV(weekNumber));

        for (int i = 0; i < vectorS.size(); i++) {
            if (
                    vectorS.get(i) == 1
                    && counterOfVaccinated <= peopleQuantityForVaccination
                    && !agents.get(i).isVaccinated()
                    && !agents.get(i).isIll()
                    ) {
                agents.get(i).setVaccinated(true);
                counterOfVaccinated++;
            }
        }
        return agents;
    }

    public static List<Integer> movePeopleFromStoG(List<Integer> vectorS, List<Integer> vectorG, MatrixContacts matrixContacts, List<Integer> vectorRG) {
        int r0;
        int counterIll;
        List<Integer> newVectorG = Utils.getVectorWithZeroValues(matrixContacts.getQuantityOfPeople());
        for (int i = 0; i < vectorG.size(); i++) {
            if (isAgentHere(vectorG.get(i))) {
                r0 = negativeValueToZero(vectorRG.get(i) - 2);
                counterIll = 0;

                for (int j = 0; j < matrixContacts.getQuantityOfPeople(); j++) {
                    if (matrixContacts.matrix[i][j] == 1 && vectorS.get(j) == 1 && counterIll <= r0 ) {
                        newVectorG.set(j, 1);
                        counterIll++;
                    }
                }
            }
        }
        return newVectorG;
    }

    public static void showCoefficients(Coefficients coefficients, List<Integer> vectorRG, List<Integer> vectorRI) {
        System.out.println(coefficients.toString());
        System.out.println("avg RG: " + getAverageValueR0(vectorRG));
        System.out.println("avg RI: " + getAverageValueR0(vectorRI));
        System.out.println("RG: " + vectorRG);
        System.out.println("RI: " + vectorRI);
    }

    private static double getAverageValueR0(List<Integer> vectorR0) {
        OptionalDouble average = vectorR0
                .stream()
                .mapToDouble(a -> a)
                .average();
        return average.isPresent() ? average.getAsDouble() : 0;
    }

    private static int negativeValueToZero(int value) {
        return value < 0 ? 0 : value;
    }

    public static List<Integer> movePeopleFromGtoS(List<Integer> vectorG, List<Integer> vectorS) {
        return Utils.vectorPlusVector(vectorS, vectorG);
    }

    public static List<Integer> movePeopleFromItoS(List<Integer> vectorI, List<Integer> vectorS) {
        return Utils.vectorPlusVector(vectorS, vectorI);
    }

    public static VectorsDTO movePeopleFromStoI(List<Integer> vectorS, List<Integer> vectorI, MatrixContacts matrixContacts, List<Integer> vectorRI, List<Agent> agents) {
        int r0;
        int counterIll;
        List<Integer> newVectorI = Utils.getVectorWithZeroValues(matrixContacts.getQuantityOfPeople());
        for (int i = 0; i < vectorI.size(); i++) {
            if (isAgentHere(vectorI.get(i))) {
                r0 = vectorRI.get(i);
                counterIll = 0;

                for (int j = 0; j < matrixContacts.getQuantityOfPeople(); j++) {
                    if (matrixContacts.matrix[i][j] == 1 && vectorS.get(j) == 1 && counterIll <= r0 && !agents.get(j).isVaccinated() && !agents.get(j).isIll()) {
                        newVectorI.set(j, 1);
                        agents.get(j).setIll(true);
                        counterIll++;
                    }
                }
            }
        }
        return new VectorsDTO(agents, newVectorI);
    }

    public static double getStatisticsG(List<Integer> vectorG) {
        return (double) getPeopleQuantity(vectorG) / vectorG.size();
    }

    public static double getStatisticsI(List<Integer> vectorI) {
        return (double) getPeopleQuantity(vectorI) / vectorI.size();
    }

    public static double getStatisticsV(List<Agent> agents) {
        int counter = 0;
        for (Agent agent : agents) {
            counter += agent.isVaccinated() ? 1 : 0;
        }
        return (double) counter/agents.size();
    }

    public static void showStatisticsGVI(List<statisticsGVI> statistics) {
        statistics.forEach(System.out::println);
    }

    public static void showErrors(String fileName, List<statisticsGVI> statistics) {
        List<statisticsGVI> staticticsFromFile = ImportFromExcel.readSeasonsFromFile(fileName);
        double squareErrorI = 0;
        double squareErrorG = 0;
        double squareErrorV = 0;

        for (int i = 0; i < 33; i++) {
            squareErrorI += Math.pow(statistics.get(i).getI() - staticticsFromFile.get(i).getI(),2);
            squareErrorG += Math.pow(statistics.get(i).getG() - staticticsFromFile.get(i).getG(),2);
            squareErrorV += Math.pow(statistics.get(i).getV() - staticticsFromFile.get(i).getV(),2);
        }

        squareErrorI = Math.sqrt(squareErrorI/(statistics.size()*(statistics.size() - 1)));
        squareErrorG = Math.sqrt(squareErrorG/(statistics.size()*(statistics.size() - 1)));
        squareErrorV = Math.sqrt(squareErrorV/(statistics.size()*(statistics.size() - 1)));

        System.out.println("SquareErrorI: " + squareErrorI);
        System.out.println("SquareErrorG: " + squareErrorG);
        System.out.println("SquareErrorV: " + squareErrorV);
    }

    private static int getPeopleQuantity(List<Integer> vector) {
        int counter = 0;
        for (Integer aVector : vector) {
            counter += aVector;
        }
        return counter;
    }

    private static boolean isAgentHere(int isAgent) {
        return isAgent == 1;
    }

    private static double getPercentsStoV(int weekNumber) {
        return (1 / (0.13 * Math.exp((weekNumber + 0.1) * 0.4)) + Math.random()) / 1000;
    }
}
