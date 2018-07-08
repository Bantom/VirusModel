package model;

import java.util.List;

public class VectorsDTO {
    private List<Agent> agents;
    private List<Integer> vectorS;
    private List<Integer> vectorI;
    private List<Integer> vectorG;
    private MatrixContacts matrixContacts;
    private List<Integer> vectorRG;
    private List<Integer> vectorRI;

    public VectorsDTO(List<Agent> agents, List<Integer> vectorS, List<Integer> vectorI, List<Integer> vectorG, List<Integer> vectorRG, List<Integer> vectorRI, MatrixContacts matrixContacts) {
        this.agents = agents;
        this.vectorS = vectorS;
        this.vectorI = vectorI;
        this.vectorG = vectorG;
        this.matrixContacts = matrixContacts;
        this.vectorRG = vectorRG;
        this.vectorRI = vectorRI;
    }

    public VectorsDTO(List<Agent> agents, List<Integer> vectorI) {
        this.agents = agents;
        this.vectorI = vectorI;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public List<Integer> getVectorS() {
        return vectorS;
    }

    public List<Integer> getVectorI() {
        return vectorI;
    }

    public List<Integer> getVectorG() {
        return vectorG;
    }

    public MatrixContacts getMatrixContacts() {
        return matrixContacts;
    }

    public List<Integer> getVectorRG() {
        return vectorRG;
    }

    public List<Integer> getVectorRI() {
        return vectorRI;
    }
}
