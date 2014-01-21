/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package surakarta;

/**
 *
 * @author Shan
 */
public class NodeFitness {



    private Node nodeStart;
    private Node nodeMove;
    private int fitness;

    NodeFitness(Node nodeStart, Node nodeMove, int fitness) {
        this.nodeStart = nodeStart;
        this.nodeMove = nodeMove;
        this.fitness = fitness;
    }
    
        public Node getNodeStart() {
        return nodeStart;
    }

    public void setNodeStart(Node nodeStart) {
        this.nodeStart = nodeStart;
    }

    public Node getNodeMove() {
        return nodeMove;
    }

    public void setNodeMove(Node nodeMove) {
        this.nodeMove = nodeMove;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

}
