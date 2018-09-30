package model;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class Agent {
    private boolean vaccinated = false;
    private boolean ill = false;
    private boolean susceptible = true;

    public boolean isVaccinated() {
        return vaccinated;
    }

    public boolean isIll() {
        return ill;
    }

    public boolean isSusceptible() {
        return susceptible;
    }
}