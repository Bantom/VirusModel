package experiments.startingVaccination.services;

import model.Agent;
import model.Coefficients;

import java.util.List;

class ActionsStartingVaccination {

    static double getPercentForStartingExtinction(List<Agent> agents, List<Integer> vectorRI, Coefficients coefficients) {
        double avgQuantityOfVaccinated = (double) agents.stream().filter(Agent::isVaccinated).count() / agents.size();
        double avgHaveBeenIll = (double) agents.stream().filter(Agent::isIll).count() / agents.size();
        double avgR = vectorRI.stream().mapToInt(Integer::intValue).average().getAsDouble();
        double avgContacts = (double)(coefficients.getMaxPeoples() + coefficients.getMinPeoples()) / 2;

        long quantityToVaccinate = Math.round(avgContacts - avgR - avgHaveBeenIll - avgQuantityOfVaccinated);

        return ((double) quantityToVaccinate / agents.size()) * 100;
    }
}
