package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class VectorsDTO {
    private List<Agent> agents;
    private List<Integer> vectorS;
    private List<Integer> vectorI;
    private List<Integer> vectorG;
    private List<Integer> vectorRG;
    private List<Integer> vectorRI;
    private MatrixContacts matrixContacts;

    public VectorsDTO(List<Agent> agents, List<Integer> vectorI) {
        this.agents = agents;
        this.vectorI = vectorI;
    }
}
