package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
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
}
