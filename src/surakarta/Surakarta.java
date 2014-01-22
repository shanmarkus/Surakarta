/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package surakarta;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Shan
 */
public class Surakarta {

    Board a = new Board(6);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


        Board a = new Board(6);
        a.print();

        Integer choices = Integer.parseInt(JOptionPane.showInputDialog("1. move node \n 2. eat node \n 3. play with bot \n 4. play with greedy ai \n 5.exit"));
        if (choices == 1) {
            Integer FirstHoleXCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the first node x-coordinate"));
            Integer FirstHoleYCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the first node y-coordinate"));
            Integer SecondHoleXCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the second node x-coordinate"));
            Integer SecondHoleYCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the second node y-coordinate"));
            a.selectNode(FirstHoleXCoor, FirstHoleYCoor);
            a.safeMove(SecondHoleXCoor, SecondHoleYCoor);
            a.placeNode();
            a.print();
        } else if (choices == 2) {
            Integer FirstHoleXCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the node x-coordinate"));
            Integer FirstHoleYCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the node y-coordinate"));
            Integer HoleXCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the Hole x-coordinate"));
            Integer HoleYCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the Hole y-coordinate"));
            a.selectNode(FirstHoleXCoor, FirstHoleYCoor);
            a.safeLoop(HoleXCoor, HoleYCoor);
            boolean safeLoop = a.getValidLoop();
            if (safeLoop == true) {
                a.eat(HoleXCoor, HoleYCoor);
                Boolean success = a.getAiSuccess();
                if (success == true) {
                    int firstNodePosition = a.checkValue(a.getInitX(), a.getInitY());
                    Node new1 = new Node(a.getFinalX(), a.getFinalY(), a.getTurn());
                    if (firstNodePosition != -1) {
                        a.Nodes.remove(firstNodePosition);
                        a.Nodes.add(new1);
                        int secondNodePosition = a.checkValue(a.getFinalX(), a.getFinalY());
                        if (secondNodePosition != -1) {
                            a.Nodes.remove(secondNodePosition);
                            a.print();
                        }
                    }
                } else {
                    System.out.println("invalid Move for eating");
                }
            }
        } else if (choices == 3) {
            playWithDumbAI();
        } else if (choices == 4) {
            playWithGreedyAI();
        }
    }

    public static void playWithGreedyAI() {
        Board a = new Board(6);
        String win = "B";
        boolean result = a.WinCheck();

        while (result == false) {
            Integer humanChoices = Integer.parseInt(JOptionPane.showInputDialog("1. Move your pawn \n 2. eat enemy"));
            if (humanChoices == 1) {
                Integer FirstHoleXCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the first node x-coordinate"));
                Integer FirstHoleYCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the first node y-coordinate"));
                Integer SecondHoleXCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the second node x-coordinate"));
                Integer SecondHoleYCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the second node y-coordinate"));
                a.selectNode(FirstHoleXCoor, FirstHoleYCoor);
                a.safeMove(SecondHoleXCoor, SecondHoleYCoor);
                //a.placeNode();
                int temp = a.checkValue(FirstHoleXCoor, FirstHoleYCoor);
                Node move = new Node(SecondHoleXCoor, SecondHoleYCoor, null);
                if (temp != -1) {
                    a.manageNodes(a.getNodes().get(temp), move);
                }

                a.print();
                System.out.println("enemy moving turn ------ ");
                a.smartAI();
                result = a.WinCheck();
            } else if (humanChoices == 2) {
                Integer FirstHoleXCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the node x-coordinate"));
                Integer FirstHoleYCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the node y-coordinate"));
                Integer HoleXCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the Hole x-coordinate"));
                Integer HoleYCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the Hole y-coordinate"));
                a.selectNode(FirstHoleXCoor, FirstHoleYCoor);
                a.safeLoop(HoleXCoor, HoleYCoor);
                boolean safeLoop = a.getValidLoop();
                if (safeLoop == true) {
                    a.eat(HoleXCoor, HoleYCoor);
                    Boolean success = a.getAiSuccess();
                    if (success == true) {
                        int firstNodePosition = a.checkValue(a.getInitX(), a.getInitY());
                        Node new1 = new Node(a.getFinalX(), a.getFinalY(), a.getTurn());
                        if (firstNodePosition != -1) {
                            a.Nodes.remove(firstNodePosition);
                            a.Nodes.add(new1);
                            int secondNodePosition = a.checkValue(a.getFinalX(), a.getFinalY());
                            if (secondNodePosition != -1) {
                                a.Nodes.remove(secondNodePosition);
                                a.print();
                            }
                        }
                        System.out.println("enemy moving turn ------ ");
                        a.smartAI();
                        result = a.WinCheck();
                    } else {
                        System.out.println("invalid Move for eating");
                    }
                } else {
                    System.out.println("invalid Hole");
                }
            }
        }
        System.out.println("End Game");
    }

    public static void playWithDumbAI() {
        Board a = new Board(6);
        String win = "B";
        a.setTurn("H");

        while (win.equals("B")) {
            Integer humanChoices = Integer.parseInt(JOptionPane.showInputDialog("1. Move your pawn \n 2. eat enemeny"));
            if (humanChoices == 1) {
                Integer FirstHoleXCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the first node x-coordinate"));
                Integer FirstHoleYCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the first node y-coordinate"));
                Integer SecondHoleXCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the second node x-coordinate"));
                Integer SecondHoleYCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the second node y-coordinate"));
                a.selectNode(FirstHoleXCoor, FirstHoleYCoor);
                a.safeMove(SecondHoleXCoor, SecondHoleYCoor);
                a.placeNode();
                a.print();
                System.out.println("enemy moving turn ------ ");
                a.setTurn("E");
                a.dumbAI();
                a.print();
                a.setTurn("H");
            } else if (humanChoices == 2) {
                Integer FirstHoleXCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the node x-coordinate"));
                Integer FirstHoleYCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the node y-coordinate"));
                Integer HoleXCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the Hole x-coordinate"));
                Integer HoleYCoor = Integer.parseInt(JOptionPane.showInputDialog("Input the Hole y-coordinate"));
                a.selectNode(FirstHoleXCoor, FirstHoleYCoor);
                a.safeLoop(HoleXCoor, HoleYCoor);
                boolean safeLoop = a.getValidLoop();
                if (safeLoop == true) {
                    a.eat(HoleXCoor, HoleYCoor);
                    a.print();
                    a.setTurn("E");
                    System.out.println("enemy moving turn ------ ");
                    a.dumbAI();
                    a.print();
                    a.setTurn("H");
                } else {
                    System.out.println("wrong turn");
                }

            }
        }
    }
}


