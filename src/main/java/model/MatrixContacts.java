package model;

public class MatrixContacts {

    private int quantityOfPeople;
    public int[][] matrix;

    public MatrixContacts(int quantityOfPeople) {
        this.quantityOfPeople = quantityOfPeople;
        this.matrix = new int[quantityOfPeople][quantityOfPeople];
    }

    public int getQuantityOfPeople() {
        return quantityOfPeople;
    }

    @Override
    public String toString() {
        return "MatrixContacts{" +
                "quantityOfPeople=" + quantityOfPeople +
                ", matrix= \n" +
                getMatrixToPrint() +
                '}';
    }

    private String getMatrixToPrint() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < quantityOfPeople; i++) {
            for (int j = 0; j < quantityOfPeople; j++) {
                builder.append(matrix[i][j]).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
