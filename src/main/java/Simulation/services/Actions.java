package Simulation.services;

import model.Agent;
import model.Coefficients;
import model.MatrixContacts;
import model.VectorsDTO;

import java.util.List;


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
                    if (matrixContacts.matrix[i][j] == 1 && vectorS.get(j) == 1 && counterIll <= r0) {
                        newVectorG.set(j, 1);
                        counterIll++;
                    }
                }
            }
        }
        return newVectorG;
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
                    if (matrixContacts.matrix[i][j] == 1 && vectorS.get(j) == 1 && counterIll <= r0 && !agents.get(j).isVaccinated() && !agents.get(j).isIll() && agents.get(j).isSusceptible()) {
                        newVectorI.set(j, 1);
                        agents.get(j).setIll(true);
                        counterIll++;
                    }
                }
            }
        }
        return new VectorsDTO(agents, newVectorI);
    }

    public static List<Integer> movePeopleFromItoY(List<Integer> vectorI, Coefficients coefficients) {
        List<Integer> vectorY = Utils.getVectorWithZeroValues(vectorI.size());
        int quantityComplicatedPeople = (int) Math.round(Utils.countPeopleFromVector(vectorI) * coefficients.getComplicationProbabilityY());
        for (int i = 0; i < vectorI.size(); i++) {
            if (quantityComplicatedPeople != 0) {
                if (vectorI.get(i) == 1) {
                    vectorY.set(i,1);
                    vectorI.set(i,0);
                    quantityComplicatedPeople--;
                }
            } else {
                break;
            }
        }
        return vectorY;
    }

    private static boolean isAgentHere(int isAgent) {
        return isAgent == 1;
    }

    private static double getPercentsStoV(int weekNumber) {
        return (1 / (0.13 * Math.exp((weekNumber + 0.1) * 0.4)) + Math.random()) / 1000;
    }

    public static List<Integer> movePeopleFromYtoS(List<Integer> vectorY, List<Integer> vectorS) {
        return Utils.vectorPlusVector(vectorS, vectorY);
    }

    public static List<Integer> movePeopleFromOtoS(List<Integer> vectorO, List<Integer> vectorS) {
        return Utils.vectorPlusVector(vectorS, vectorO);
    }

    public static List<Integer> movePeopleFromGtoO(List<Integer> vectorG, Coefficients coefficients) {
        List<Integer> vectorO = Utils.getVectorWithZeroValues(vectorG.size());
        int quantityComplicatedPeople = (int) Math.round(Utils.countPeopleFromVector(vectorG) * coefficients.getComplicationProbabilityO());
        for (int i = 0; i < vectorG.size(); i++) {
            if (quantityComplicatedPeople != 0) {
                if (vectorG.get(i) == 1) {
                    vectorO.set(i,1);
                    vectorG.set(i,0);
                    quantityComplicatedPeople--;
                }
            } else {
                break;
            }
        }
        return vectorO;
    }

    public static List<Agent> makePeopleBecameUnsusceptible(List<Agent> agents, Coefficients coefficients) {
        int quantityUnsusceptiblePeople = (int) Math.round(coefficients.getQuantityOfPeople() * coefficients.getSusceptibleProbability());
        int counterUnsusceptiblePeople = 0;
        while (quantityUnsusceptiblePeople > counterUnsusceptiblePeople) {
            for (int i = 0; i < agents.size(); i++) {
                double randomValue = Math.random();
                if (quantityUnsusceptiblePeople == counterUnsusceptiblePeople) break;
                if (agents.get(i).isSusceptible() && randomValue > 0.5) {
                    agents.get(i).setSusceptible(false);
                    counterUnsusceptiblePeople++;
                }
            }
        }
        return agents;
    }
}
