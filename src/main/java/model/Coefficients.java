package model;

public class Coefficients {

    private int quantityOfPeople;
    private int minPeoples;
    private int maxPeoples;
    public int minContactsBecameIll;
    public int maxContactsBecameIll;

    public Coefficients(int quantityOfPeople, int minPeoples, int maxPeoples, int minContactsBecameIll, int maxContactsBecameIll) {
        this.quantityOfPeople = quantityOfPeople;
        this.minPeoples = minPeoples;
        this.maxPeoples = maxPeoples;
        this.minContactsBecameIll = minContactsBecameIll;
        this.maxContactsBecameIll = maxContactsBecameIll;
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
                ", min contacts for agent=" + minPeoples +
                ", max contacts for agent=" + maxPeoples +
                ", min contacts became ill from one person=" + minContactsBecameIll +
                ", max contacts became ill from one person=" + maxContactsBecameIll +
                '}';
    }
}
