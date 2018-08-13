package services;

import model.Agent;
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

    private static boolean isAgentHere(int isAgent) {
        return isAgent == 1;
    }

    private static double getPercentsStoV(int weekNumber) {
        return (1 / (0.13 * Math.exp((weekNumber + 0.1) * 0.4)) + Math.random()) / 1000;
    }
}
