package model;

public class Agent {
    private boolean vaccinated = false;
    private boolean ill = false;

    public boolean isVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public boolean isIll() {
        return ill;
    }

    public void setIll(boolean ill) {
        this.ill = ill;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "vaccinated=" + vaccinated +
                ", ill=" + ill +
                '}';
    }
}
