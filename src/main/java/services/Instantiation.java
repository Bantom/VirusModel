package services;

import model.Agent;
import model.Coefficients;
import model.MatrixContacts;
import model.VectorsDTO;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;
import services.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Instantiation {

    public static Coefficients initCoefficients(int quantityOfPeople) {
        int minPeoples = (int) Math.round(quantityOfPeople * (Math.random() * (0.025 - 0.00000001) + 0.00000001));
        int maxPeoples = (int) Math.round(quantityOfPeople * (Math.random() * (0.05 - 0.025) + 0.025));
        double probability = Math.random()/10;
        return new Coefficients(quantityOfPeople, minPeoples, maxPeoples, 0, 5, probability);
    }

    public static MatrixContacts initMatrixContacts(Coefficients coefficients) {
        Random random = new Random();
        RandomDataGenerator binomialDistribution = new RandomDataGenerator();
        MatrixContacts matrixContacts = new MatrixContacts(coefficients.getQuantityOfPeople());
        int avgPeopleValue;
        int columnDecrement = coefficients.getQuantityOfPeople() - 1;

        for (int row = coefficients.getQuantityOfPeople() - 1; row >= 0; row--) {
            int counter = 0;
            avgPeopleValue = binomialDistribution.nextBinomial(coefficients.getQuantityOfPeople(),coefficients.getProbability());
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
        List<Integer> vectorI = getVectorsWithRandomOneOrZeroValues(coefficients.getQuantityOfPeople());
        List<Integer> vectorS = Utils.vectorMinusVector(getVectorWithValuesOne(coefficients.getQuantityOfPeople()), vectorI);
        List<Integer> vectorG = initVectorG(vectorI);
        vectorI = Utils.vectorMinusVector(vectorI, vectorG);
        agents = becameIllPeopleFromVectorI(vectorI, agents);
        MatrixContacts matrixContacts = initMatrixContacts(coefficients);
        List<Integer> vectorRG = getVectorWithRandomElement(matrixContacts, coefficients);
        List<Integer> vectorRI = getVectorWithRandomElement(matrixContacts, coefficients);

        return new VectorsDTO(agents, vectorS, vectorI, vectorG, vectorRG, vectorRI, matrixContacts);
    }

    private static List<Agent> becameIllPeopleFromVectorI(List<Integer> vectorI, List<Agent> agents) {
        if ( vectorI.size() != agents.size()) throw new IllegalArgumentException("Sizes of vectors I and Agent are different.");

        for (int i = 0; i < vectorI.size(); i++) {
            if (vectorI.get(i) == 1) {
                agents.get(i).setIll(true);
            }
        }
        return agents;
    }

    private static List<Agent> getAgents(int quantityOfPeople) {
        List<Agent> agents = new ArrayList<Agent>();

        for (int i = 0; i < quantityOfPeople; i++) {
            agents.add(new Agent());
        }
        return agents;
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
                value = binomialDistribution.nextBinomial(/*coefficients.getMaxContactsBecameIll()*/vectorContacts.get(i),coefficients.getProbability());
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
        vectorG = Utils.vectorMinusVector(vectorG, getVectorsWithRandomOneOrZeroValues(vectorG.size()));
        vectorG = Utils.negativeElementsToZero(vectorG);
        return vectorG;
    }

    private static List<Integer> getVectorsWithRandomOneOrZeroValues(int size) {
        List<Integer> vector = new ArrayList<Integer>();
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            vector.add(random.nextInt(2) > 0 ? 1 : 0);
        }
        return vector;
    }

    private static List<Integer> getVectorWithValuesOne(int size) {
        List<Integer> vectorS = new ArrayList<Integer>();

        for (int i = 0; i < size; i++) {
            vectorS.add(1);
        }
        return vectorS;
    }


}
