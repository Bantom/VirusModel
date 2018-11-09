package Simulation.services;

import model.Agent;

import java.util.List;

public class StatisticsService {

    public static double getStatisticsG(List<Integer> vectorG) {
        return (double) getPeopleQuantity(vectorG) / vectorG.size();
    }

    public static double getStatisticsI(List<Integer> vectorI) {
        return (double) getPeopleQuantity(vectorI) / vectorI.size();
    }

    public static double getStatisticsV(List<Agent> agents) {
        int counter = agents.stream()
                        .mapToInt(agent -> agent.isVaccinated() ? 1 : 0)
                        .sum();
        return (double) counter / agents.size();
    }

    private static int getPeopleQuantity(List<Integer> vector) {
        return vector.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}
