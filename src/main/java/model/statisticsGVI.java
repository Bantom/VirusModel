package model;

public class statisticsGVI {
    private double G;
    private double V;
    private double I;


    public double getG() {
        return G;
    }

    public void setG(double g) {
        G = g;
    }

    public double getV() {
        return V;
    }

    public void setV(double v) {
        V = v;
    }

    public double getI() {
        return I;
    }

    public void setI(double i) {
        I = i;
    }

    public statisticsGVI(double g, double v, double i) {
        G = g;
        V = v;
        I = i;
    }

    public statisticsGVI() {
    }

    @Override
    public String toString() {
        return "GVI{" +
                "G=" + G +
                ", V=" + V +
                ", I=" + I +
                '}';
    }
}
