package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Coefficients {

    private int quantityOfPeople;
    private int minPeoples;
    private int maxPeoples;
    private int minContactsBecameIll;
    private int maxContactsBecameIll;
    private double probability;
    private double complicationProbabilityY;
    private double complicationProbabilityO;
    private double susceptibleProbability;
    private boolean anotherIll = true;

    public Coefficients(int quantityOfPeople, int minPeoples, int maxPeoples, int minContactsBecameIll, int maxContactsBecameIll, double probability, double complicationProbabilityY, double complicationProbabilityO, double susceptibleProbability) {
        this.quantityOfPeople = quantityOfPeople;
        this.minPeoples = minPeoples;
        this.maxPeoples = maxPeoples;
        this.minContactsBecameIll = minContactsBecameIll;
        this.maxContactsBecameIll = maxContactsBecameIll;
        this.probability = probability;
        this.complicationProbabilityY = complicationProbabilityY;
        this.complicationProbabilityO = complicationProbabilityO;
        this.susceptibleProbability = susceptibleProbability;
    }
}
