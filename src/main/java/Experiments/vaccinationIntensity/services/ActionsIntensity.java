package Experiments.vaccinationIntensity.services;

import model.Agent;

import java.util.List;

public class ActionsIntensity {

    public static List<Agent> vaccinatePeopleFromS(int weekNumber, List<Integer> vectorS, List<Agent> agents, double intensity) {
        int counterOfVaccinated = 0;
        int peopleQuantityForVaccination = (int) Math.round(vectorS.size() * getPercentsStoV(weekNumber, intensity));

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


    private static double getPercentsStoV(int weekNumber, double intensity) {
        return intensity * (1 / (0.13 * Math.exp((weekNumber + 0.1) * 0.4)) + Math.random()) / 1000;
    }
}
