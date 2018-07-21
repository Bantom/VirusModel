import services.Actions;
import services.Instantiation;
import services.Utils;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final int quantityOfPeople = 2000;
    private static final String FILE_NAME = "Data.xlsx";
    private static List<statisticsGVI> statistics = new ArrayList<statisticsGVI>();

    public static void main(String[] args) {
        Coefficients coefficients = Instantiation.initCoefficients(quantityOfPeople);
        VectorsDTO vectors = Instantiation.initVectors(coefficients);

        List<Agent> agents = vectors.getAgents();
        List<Integer> vectorS = vectors.getVectorS();
        List<Integer> vectorI = vectors.getVectorI();
        List<Integer> vectorG = vectors.getVectorG();
        MatrixContacts matrixContacts = vectors.getMatrixContacts();
        List<Integer> vectorRG = vectors.getVectorRG();
        List<Integer> vectorRI = vectors.getVectorRI();

        for (int week = 0; week < 33; week++) {
            agents = Actions.vaccinatePeopleFromS(week, vectorS, agents);

            List<Integer> newVectorG = Actions.movePeopleFromStoG(vectorS, vectorG, matrixContacts, vectorRG);
            vectorS = Utils.vectorMinusVector(vectorS, newVectorG);

            VectorsDTO tmp = Actions.movePeopleFromStoI(vectorS, vectorI, matrixContacts, vectorRI, agents);
            List<Integer> newVectorI = tmp.getVectorI();
            agents = tmp.getAgents();
            vectorS = Utils.vectorMinusVector(vectorS, newVectorI);

            vectorS = Actions.movePeopleFromGtoS(vectorG, vectorS);
            vectorS = Actions.movePeopleFromItoS(vectorI, vectorS);
            vectorI = newVectorI;
            vectorG = newVectorG;

            statistics.add(new statisticsGVI(Actions.getStatisticsG(vectorG), Actions.getStatisticsV(agents), Actions.getStatisticsI(vectorI)));
        }

        Actions.showStatisticsGVI(statistics);

        Actions.showCoefficients(coefficients, vectorRG, vectorRI);

        Actions.showErrors(FILE_NAME, statistics);
    }
}
