package services;

import model.Agent;
import model.Coefficients;
import model.MatrixContacts;
import model.VectorsDTO;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class InstantiationService {

    static Coefficients initCoefficients(int quantityOfPeople) {
        int minPeoples = (int) Math.round(quantityOfPeople * (Math.random() * (0.025 - 0.00000001) + 0.00000001));
        int maxPeoples = (int) Math.round(quantityOfPeople * (Math.random() * (0.05 - 0.025) + 0.025));
        double probability = Math.random() / 10;
        double complicationProbabilityY = Math.random() / 10;
        double complicationProbabilityO = Math.random() / 10;
        double susceptibleProbability = Math.random() / 10;
        return new Coefficients(quantityOfPeople, minPeoples, maxPeoples, 0, 8, probability, complicationProbabilityY, complicationProbabilityO, susceptibleProbability);
    }

    private static MatrixContacts initMatrixContacts(Coefficients coefficients) {
        Random random = new Random();
        RandomDataGenerator binomialDistribution = new RandomDataGenerator();
        MatrixContacts matrixContacts = new MatrixContacts(coefficients.getQuantityOfPeople());
        int avgPeopleValue;
        int columnDecrement = coefficients.getQuantityOfPeople() - 1;

        for (int row = coefficients.getQuantityOfPeople() - 1; row >= 0; row--) {
            int counter;
            avgPeopleValue = binomialDistribution.nextBinomial(coefficients.getQuantityOfPeople(), coefficients.getProbability());
            for (int column = columnDecrement; column >= 0; column--) {
                matrixContacts.matrix[column][row] = random.nextInt(2);
                matrixContacts.matrix[row][column] = matrixContacts.matrix[column][row] == 1 ? 1 : 0;

                counter = countQuantityOfConnectionsForPerson(row, matrixContacts);
                if (counter >= avgPeopleValue) {
                    break;
                }
            }
            columnDecrement--;
            matrixContacts.matrix[row][row] = 0;
        }
        return matrixContacts;
    }

    public static VectorsDTO initVectors(Coefficients coefficients) {
        List<Agent> agents = getAgents(coefficients.getQuantityOfPeople());
        List<Integer> vectorI = Utils.getVectorsWithRandomOneOrZeroValues(coefficients.getQuantityOfPeople());
        List<Integer> vectorS = Utils.vectorMinusVector(Utils.getVectorWithValuesOne(coefficients.getQuantityOfPeople()), vectorI);
        List<Integer> vectorG = Utils.getVectorWithZeroValues(coefficients.getQuantityOfPeople());
        if(coefficients.isAnotherIll()) {
            vectorG = initVectorG(vectorI);
            vectorI = Utils.vectorMinusVector(vectorI, vectorG);
        }
        agents = becameIllPeopleFromVectorI(vectorI, agents);
        MatrixContacts matrixContacts = initMatrixContacts(coefficients);
        List<Integer> vectorRG = getVectorWithRandomElement(matrixContacts, coefficients);
        List<Integer> vectorRI = getVectorWithRandomElement(matrixContacts, coefficients);

        return new VectorsDTO(agents, vectorS, vectorI, vectorG, vectorRG, vectorRI, matrixContacts);
    }

    private static List<Agent> becameIllPeopleFromVectorI(List<Integer> vectorI, List<Agent> agents) {
        if (vectorI.size() != agents.size()) throw new IllegalArgumentException("Sizes of vectors I and Agent are different.");

        for (int i = 0; i < vectorI.size(); i++) {
            if (vectorI.get(i) == 1) {
                agents.get(i).setIll(true);
            }
        }
        return agents;
    }

    private static List<Agent> getAgents(int quantityOfPeople) {
        return Stream
                .generate(Agent::new)
                .limit(quantityOfPeople)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    private static List<Integer> getVectorWithRandomElement(MatrixContacts matrixContacts, Coefficients coefficients) {
        RandomDataGenerator binomialDistribution = new RandomDataGenerator();
        boolean flag;
        int value;
        List<Integer> vectorWithRandomElements = new ArrayList<Integer>();
        List<Integer> vectorContacts = getQuantityOfContacts(matrixContacts);

        for (int i = 0; i < matrixContacts.getQuantityOfPeople(); i++) {
            flag = true;
            while (flag) {
                value = setMaxIllContactsInsteadOfBigNumbers(binomialDistribution.nextBinomial(vectorContacts.get(i), coefficients.getProbability()), coefficients.getMaxContactsBecameIll());
                if (value <= vectorContacts.get(i) && value <= coefficients.getMaxContactsBecameIll()) {
                    vectorWithRandomElements.add(value);
                    flag = false;
                }
            }
        }
        return vectorWithRandomElements;
    }

    private static List<Integer> getQuantityOfContacts(MatrixContacts matrixContacts) {
        List<Integer> result = new ArrayList<Integer>();

        for (int i = 0; i < matrixContacts.getQuantityOfPeople(); i++) {
            result.add(0);
            for (int j = 0; j < matrixContacts.getQuantityOfPeople(); j++) {
                result.set(i, result.get(i) + matrixContacts.matrix[i][j]);
            }
        }
        return result;
    }

    private static int countQuantityOfConnectionsForPerson(int row, MatrixContacts matrixContacts) {
        int counter = 0;
        for (int column = 0; column < matrixContacts.getQuantityOfPeople(); column++) {
            counter += matrixContacts.matrix[column][row];
        }
        return counter;
    }

    private static List<Integer> initVectorG(List<Integer> vectorI) {
        List<Integer> vectorG = Utils.cloneFromVector(vectorI);
        vectorG = Utils.vectorMinusVector(vectorG, Utils.getVectorsWithRandomOneOrZeroValues(vectorG.size()));
        vectorG = Utils.negativeElementsToZero(vectorG);
        return vectorG;
    }

    private static int setMaxIllContactsInsteadOfBigNumbers(int numbersBecameIll, int maxIllContacts) {
        return numbersBecameIll > maxIllContacts ? maxIllContacts : numbersBecameIll;
    }
}
