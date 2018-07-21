package model;

public class Coefficients {

    private int quantityOfPeople;
    private int minPeoples;
    private int maxPeoples;
    private int minContactsBecameIll;
    private int maxContactsBecameIll;
    private double probability;

    public Coefficients(int quantityOfPeople, int minPeoples, int maxPeoples, int minContactsBecameIll, int maxContactsBecameIll, double probability) {
        this.quantityOfPeople = quantityOfPeople;
        this.minPeoples = minPeoples;
        this.maxPeoples = maxPeoples;
        this.minContactsBecameIll = minContactsBecameIll;
        this.maxContactsBecameIll = maxContactsBecameIll;
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }

    public int getQuantityOfPeople() {
        return quantityOfPeople;
    }

    public int getMinPeoples() {
        return minPeoples;
    }

    public void setMinPeoples(int minPeoples) {
        this.minPeoples = minPeoples;
    }

    public int getMaxPeoples() {
        return maxPeoples;
    }

    public void setMaxPeoples(int maxPeoples) {
        this.maxPeoples = maxPeoples;
    }

    public int getMinContactsBecameIll() {
        return minContactsBecameIll;
    }

    public int getMaxContactsBecameIll() {
        return maxContactsBecameIll;
    }

    @Override
    public String toString() {
        return "Coefficients{" +
                "quantityOfPeople=" + quantityOfPeople +
                ", minPeoples=" + minPeoples +
                ", maxPeoples=" + maxPeoples +
                ", minContactsBecameIll=" + minContactsBecameIll +
                ", maxContactsBecameIll=" + maxContactsBecameIll +
                ", probability=" + probability +
                '}';
    }
}
