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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

//        Board a = new Board(6);
//        a.print();
//        System.out.println();
//        System.out.println(a.loopMove(0, 2));
//        System.out.println(a.loopMove(5, 2));
//        System.out.println(a.loopMove(0, 0));
//        System.out.println(a.loopMove(5, 0));
//        System.out.println(a.loopMove(0, 5));
//        System.out.println(a.loopMove(5, 5));
//        System.out.println(a.loopMove(0, 1));
//        System.out.println(a.loopMove(1, 0));

        Board a = new Board(6);
        a.print();
        String win = "B";

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
            a.eat(HoleXCoor, HoleYCoor);
            Boolean success = a.getAiSuccess();
            if (success == true) {
               // a.manageNodes(FirstHoleXCoor, FirstHoleYCoor, HoleXCoor, HoleYCoor);
                a.print();
            } else {
                System.out.println("invalid Move for eating");
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

        while (win.equals("B")) {
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
                if(temp != -1){
                    a.manageNodes(a.getNodes().get(temp),move);
                }
            
                a.print();
                System.out.println("enemy moving turn ------ ");
                a.smartAI();
                a.aiMove();
                a.print();
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
                       // a.manageNodes(FirstHoleXCoor, FirstHoleYCoor, HoleYCoor, HoleYCoor);
                        a.print();
                        System.out.println("enemy moving turn ------ ");
                        a.smartAI();
                        a.print();
                    } else {
                        System.out.println("invalid Move for eating");
                    }
                } else {
                    System.out.println("invalid Hole");
                }
            }

        }
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
//        //TESTING DEVELOPMENT
//        Board a = new Board(6);
//        a.print();
//        System.out.println();
//        a.selectNode(1, 0);
//        a.safeMove(2, 0);
//        a.placeNode();
//        a.print();
//        System.out.println();
//        a.selectNode(4, 0);
//        a.safeMove(3, 0);
//        a.placeNode();
//        a.print();
//        System.out.println();
//        a.selectNode(3, 0);
//        a.safeMove(1, 0);
//        a.placeNode();
//        a.print();
//        System.out.println();
//        a.selectNode(0, 1);
//        a.eat(0, 1);
//        a.print();
//        System.out.println();
//        a.dumbAI();
//        a.print();
//        System.out.println("00");
//        a.selectNode(2, 0);
//        a.safeMove(3, 0);
//        a.placeNode();
//        a.print();
//        System.out.println();
//        a.selectNode(5, 0);
//        a.safeMove(4, 0);
//        a.placeNode();
//        a.print();
//        System.out.println();
//        a.selectNode(5, 2);
//        a.eat(5, 2);
//        a.print();
//        System.out.println();
//        a.selectNode(3, 0);
//        a.safeMove(2, 0);
//        a.placeNode();
//        a.print();
//        System.out.println();
//        a.selectNode(0, 2);
//        a.eat(0, 2);
//        a.print();
//        a.dumbAI();

