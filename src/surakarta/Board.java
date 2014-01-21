/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package surakarta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Shan
 */
public final class Board {

    private String[][] board;  // stores board info
    List<Hole> innerHole = new LinkedList<>();
    List<Hole> reverseInnerHole = new LinkedList<>();
    List<Hole> outerHole = new LinkedList<>();
    List<Hole> reverseOuterHole = new LinkedList<>();
    ArrayList<Node> Nodes = new ArrayList<>();
    ArrayList<Node> possibleNodeMove = new ArrayList<>();
    Integer initX, initY, finalX, finalY;
    String turn, enemyTurn, winner;
    Boolean validLoop;
    Integer eaterPosition, eatenPosition;
    Boolean aiSuccess;
    Integer counterLoop;
    Boolean rangeLoop;
    Integer HoleX, HoleY;
    Integer Fitness;
    ArrayList<NodeFitness> Astar = new ArrayList<>();

    public Integer getFitness() {
        return Fitness;
    }

    public void setFitness(Integer Fitness) {
        this.Fitness = Fitness;
    }

    public ArrayList<Node> getPossibleNodeMove() {
        return possibleNodeMove;
    }

    public void setPossibleNodeMove(ArrayList<Node> possibleNodeMove) {
        this.possibleNodeMove = possibleNodeMove;
    }

    public ArrayList<Node> getNodes() {
        return Nodes;
    }

    public Integer getHoleX() {
        return HoleX;
    }

    public void setHoleX(Integer HoleX) {
        this.HoleX = HoleX;
    }

    public Integer getHoleY() {
        return HoleY;
    }

    public void setHoleY(Integer HoleY) {
        this.HoleY = HoleY;
    }

    public Boolean getRangeLoop() {
        return rangeLoop;
    }

    public void setRangeLoop(Boolean rangeLoop) {
        this.rangeLoop = rangeLoop;
    }

    public Integer getCounterLoop() {
        return counterLoop;
    }

    public void setCounterLoop(Integer counterLoop) {
        this.counterLoop = counterLoop;
    }

    public Boolean getAiSuccess() {
        return aiSuccess;
    }

    public void setAiSuccess(Boolean aiSuccess) {
        this.aiSuccess = aiSuccess;
    }

    public Integer getEaterPosition() {
        return eaterPosition;
    }

    public void setEaterPosition(Integer eaterPosition) {
        this.eaterPosition = eaterPosition;
    }

    public Integer getEatenPosition() {
        return eatenPosition;
    }

    public void setEatenPosition(Integer eatenPosition) {
        this.eatenPosition = eatenPosition;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getEnemyTurn() {
        return enemyTurn;
    }

    public void setEnemyTurn(String enemyTurn) {
        this.enemyTurn = enemyTurn;
    }

    public Boolean getValidLoop() {
        return validLoop;
    }

    public void setValidLoop(Boolean validLoop) {
        this.validLoop = validLoop;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public Integer getInitX() {
        return initX;
    }

    public void setInitX(Integer initX) {
        this.initX = initX;
    }

    public Integer getInitY() {
        return initY;
    }

    public void setInitY(Integer initY) {
        this.initY = initY;
    }

    public Integer getFinalX() {
        return finalX;
    }

    public void setFinalX(Integer finalX) {
        this.finalX = finalX;
    }

    public Integer getFinalY() {
        return finalY;
    }

    public void setFinalY(Integer finalY) {
        this.finalY = finalY;
    }

    public Board(int size) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        board = new String[size][size];
        initBoard();
        ruleInnerHole();
        ruleOuterHole();
    }

    public void initBoard() {
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < board.length; col++) {
                Nodes.add(new Node(row, col, "H"));
            }
        }
        for (int row = 4; row < 6; row++) {
            for (int col = 0; col < board.length; col++) {
                Nodes.add(new Node(row, col, "E"));
            }
        }
        //fill the board
        for (int i = 0; i < Nodes.size(); i++) {
            Node temp = Nodes.get(i);
            board[temp.x][temp.y] = temp.type;
        }

    }

    // Filling the rule of inner circle hole 
    public void ruleInnerHole() {
        innerHole.add(new Hole(0, 1));    //left holes rules
        innerHole.add(new Hole(1, 0)); //right hole rules
        innerHole.add(new Hole(1, 5));    //left holes rules
        innerHole.add(new Hole(0, 4)); //right hole rules
        innerHole.add(new Hole(5, 4));    //left holes rules
        innerHole.add(new Hole(4, 5)); //right hole rules
        innerHole.add(new Hole(4, 0));    //left holes rules
        innerHole.add(new Hole(5, 1)); //right hole rules
    }

    // Filling the rule of outer circle hole
    public void ruleOuterHole() {
        outerHole.add(new Hole(0, 2));
        outerHole.add(new Hole(2, 0));
        outerHole.add(new Hole(2, 5));
        outerHole.add(new Hole(0, 3));
        outerHole.add(new Hole(5, 3));
        outerHole.add(new Hole(3, 5));
        outerHole.add(new Hole(3, 0));
        outerHole.add(new Hole(5, 2));
    }

    //Check wether user move is in the 16 loop holes or not
//    public boolean loopMove(int x, int y) {
//        Hole temp;
//        Iterator i = innerHole.iterator();
//        Iterator j = outerHole.iterator();
//        while (i.hasNext()) {
//            temp = (Hole) i.next();
//            int tempx = temp.a;
//            int tempy = temp.b;
//            if (x == tempx && y == tempy) {
//                return true;
//            }
//        }
//
//        while (j.hasNext()) {
//            temp = (Hole) j.next();
//            int tempx = temp.a;
//            int tempy = temp.b;
//            if (x == tempx && y == tempy) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//check wether the move is legal or not
    public boolean safeMove(int x, int y) {
        int firstX = getInitX();
        int firstY = getInitY();
        int calculateX = Math.abs(firstX - x);
        int calculateY = Math.abs(firstY - y);

        if (calculateX <= 1 || calculateY <= 1) {
            Integer temp = checkValue(x, y);
            if (temp == -1) {
                setFinalX(x);
                setFinalY(y);
                return true;
            } else {
                return false;
            }
        } else {
            setFinalX(x);
            setFinalY(y);
            return true;
        }
    }

//selecting hole
    public boolean selectNode(int x, int y) {
        Integer temp = checkValue(x, y);
        if (temp != -1) {
            String type = Nodes.get(temp).type;
            setInitX(Nodes.get(temp).x);
            setInitY(Nodes.get(temp).y);
        } else {
            return false;
        }
        return false;
    }

    //Placing new position for new node
    //bug di place node soalnya dia pas random move dia maju dari ada type ke kosong
    //which mean pas dia ke node kosong dia bakal save di nodes nya itu null 
    public void placeNode() {

        if (safeMove(finalX, finalY) == true) {
            String temp = board[initX][initY];
            board[getInitX()][getInitY()] = board[getFinalX()][getFinalY()];
            board[getFinalX()][getFinalY()] = temp;
        } else {
            System.out.print("this has a value of " + board[getFinalX()][getFinalY()]);
            System.out.print(getFinalX() + " " + getFinalY());
        }
    }

    public void manageNodes(Node first, Node second) {
        if (second.type == null) {
            second.type = first.type;
        }
        for (int i = 0; i < Nodes.size(); i++) {
            Node temp = new Node(Nodes.get(i).x, Nodes.get(i).y, Nodes.get(i).type);
            if (temp.x == first.x && temp.y == first.y) {
                Nodes.remove(i);
            }
        }
        Nodes.add(second);
    }
//Greedy AI

    public void smartAI() {
        Iterator itr = Nodes.iterator();
        //trying to eat
        nodeLoop:
        while (itr.hasNext()) {
            Node node = (Node) itr.next();
            if (node.type.equals("E")) {
                System.out.println(node.x + " " + node.y + " " + node.type);
                System.out.println("1");
                for (Hole holeInner : innerHole) {
                    System.out.println(holeInner.a + " HOLEEE IN " + holeInner.b);
                    selectNode(node.x, node.y);
                    checkLoopRange(holeInner.a, holeInner.b);
                    boolean loopRangeTemp = getRangeLoop();
                    if (loopRangeTemp == true) {
                        System.out.println("1.1");
                        safeLoop(holeInner.a, holeInner.b);
                        boolean safe = getValidLoop();
                        if (safe == true) {
                            System.out.println("1.2");
                            eat(holeInner.a, holeInner.b);
                            boolean success = getAiSuccess();
                            if (success == true) {
                                System.out.println("1.3");
                                System.out.println(node.x + " " + node.y);
                                System.out.println(holeInner.a + " " + holeInner.b);
                                System.out.println("IL");
                                break nodeLoop;
                            }
                        }
                    }
                }

                for (Hole holeOuter : outerHole) {
                    System.out.println(holeOuter.a + " HOLEEE OUT " + holeOuter.b);
                    selectNode(node.x, node.y);//(4,0)
                    checkLoopRange(holeOuter.a, holeOuter.b); //CLR(2,0)
                    boolean loopRangeTemp = getRangeLoop();
                    if (loopRangeTemp == true) {
                        System.out.println("2.1");
                        safeLoop(holeOuter.a, holeOuter.b);
                        boolean safe = getValidLoop();
                        if (safe == true) {
                            System.out.println("2.2");
                            eat(holeOuter.a, holeOuter.b);
                            boolean success = getAiSuccess();
                            if (success == true) {
                                System.out.println(node.x + " " + node.y);
                                System.out.println(holeOuter.a + " " + holeOuter.b);
                                System.out.println("OL");
                                break nodeLoop;
                            }
                        }
                    }
                }
            }
        }
        boolean success = getAiSuccess();
        if (success == true) {
            int firstNodePosition = checkValue(getInitX(), getInitY());
            int secondNodePosition = checkValue(getFinalX(), getFinalY());
            Node new1 = new Node(getFinalX(), getFinalY(), turn);
            if (firstNodePosition != -1 && secondNodePosition != -1) {
                Nodes.remove(firstNodePosition);
                //Nodes.remove(secondNodePosition);
                Nodes.add(new1);
            }
            for (int i = 0; i < Nodes.size(); i++) {
                Node a = Nodes.get(i);
                if (a.x == getFinalX() && a.y == getFinalY()) {
                    Node b = new Node(getFinalX(), getFinalY(), "E");
                    Nodes.remove(i);
                }
            }
        } else {
            System.out.println("AI MOVE");
            smartAIMove();
        }
        //if the AI cannot eat then it just move randomly 03 ilang dari 

    }

    public void smartAIMove() {
        Astar.clear();
        //check dia kalo jalan bisa dimakan lagi ga 
        for (Node node : Nodes) {
            String type = node.type;
            if (type.equals("E")) {
                checkPossibleMove(node);
                //check wether the node has possible move or not
                if (!possibleNodeMove.isEmpty()) {
                    for (Node possibleNode : possibleNodeMove) {
                        //check if the human can eat this node or not 
                        for (Node humanNode : Nodes) {
                            Fitness = 0;
                            //check for human node
                            if (humanNode.type.equals("H")) {
                                setInitX(humanNode.x);
                                setInitY(humanNode.y);
                                //init array to hold possible hole
                                ArrayList<Hole> possibleInnerHole = new ArrayList<>();
                                ArrayList<Hole> possibleOuterHole = new ArrayList<>();


                                //check possible hole in inner hole loop
                                for (Hole inHole : innerHole) {
                                    checkLoopRange(inHole.a, inHole.b);
                                    boolean rangeInnerLoop = getRangeLoop();
                                    if (rangeInnerLoop == true) {
                                        possibleInnerHole.add(inHole);
                                    }
                                }

                                //check possible hole in outer hole loop
                                for (Hole outHole : outerHole) {
                                    checkLoopRange(outHole.a, outHole.b);
                                    boolean rangeOuterLoop = getRangeLoop();
                                    if (rangeOuterLoop == true) {
                                        possibleOuterHole.add(outHole);
                                    }
                                }

                                int innerSize = possibleInnerHole.size();
                                int outerSize = possibleOuterHole.size();

                                // the range is between the inner size loop
                                if (innerSize != 0 && outerSize == 0) {
                                    for (Hole hole : possibleInnerHole) {
                                        checkPathLoop(hole.a, hole.b);
                                        boolean innerSuccess = getAiSuccess();
                                        if (innerSuccess == true) {
                                            //check wether the final x and y is the possible node
                                            if (possibleNode.x == getFinalX() && possibleNode.y == getFinalY()) {
                                                Integer tempFitness = getFitness();
                                                setFitness(tempFitness - 1);
                                                //then check if it can counter or not
                                                //check for AI Node if it can counter attack or not 
                                                for (Node aiNode : Nodes) {
                                                    if (aiNode.type.equals("E")) {
                                                        setInitX(aiNode.x);
                                                        setInitY(aiNode.y);
                                                        //init array to hold possible hole
                                                        ArrayList<Hole> counterInnerHole = new ArrayList<>();
                                                        ArrayList<Hole> counterOuterHole = new ArrayList<>();

                                                        //check possible hole in inner hole loop
                                                        for (Hole inHole : innerHole) {
                                                            checkLoopRange(inHole.a, inHole.b);
                                                            boolean rangeInnerLoop = getRangeLoop();
                                                            if (rangeInnerLoop == true) {
                                                                counterInnerHole.add(inHole);
                                                            }
                                                        }

                                                        //check possible hole in outer hole loop
                                                        for (Hole outHole : outerHole) {
                                                            checkLoopRange(outHole.a, outHole.b);
                                                            boolean rangeOuterLoop = getRangeLoop();
                                                            if (rangeOuterLoop == true) {
                                                                counterOuterHole.add(outHole);
                                                            }
                                                        }

                                                        //getting size of the size of the counter
                                                        int counterInnerSize = possibleInnerHole.size();
                                                        int counterOuterSize = possibleOuterHole.size();

                                                        // the range is between the inner size loop
                                                        if (counterInnerSize != 0 && counterOuterSize == 0) {
                                                            for (Hole aihole : counterInnerHole) {
                                                                checkPathLoop(aihole.a, aihole.b);
                                                                innerSuccess = getAiSuccess();
                                                                if (innerSuccess == true) {
                                                                    //check wether the final x and y is the possible node
                                                                    if (possibleNode.x == getFinalX() && possibleNode.y == getFinalY()) {
                                                                        tempFitness = getFitness();
                                                                        setFitness(tempFitness - 1);
                                                                        //then check if it can counter or not
                                                                    }
                                                                }
                                                            }
                                                            //if it finished looping and find none way to eat then the fitness will + 1
                                                            //then add to Astar arraylist
                                                            tempFitness = getFitness();
                                                            setFitness(tempFitness + 1);
                                                        } else if (counterInnerSize == 0 && counterOuterSize != 0) {
                                                            for (Hole aihole : counterOuterHole) {
                                                                checkPathLoop(aihole.a, aihole.b);
                                                                innerSuccess = getAiSuccess();
                                                                if (innerSuccess == true) {
                                                                    //check wether the final x and y is the possible node
                                                                    if (possibleNode.x == getFinalX() && possibleNode.y == getFinalY()) {
                                                                        tempFitness = getFitness();
                                                                        setFitness(tempFitness - 1);
                                                                        //then check if it can counter or not
                                                                    }
                                                                }
                                                            }
                                                            //if it finished looping and find none way to eat then the fitness will + 1
                                                            //then add to Astar arraylist
                                                            tempFitness = getFitness();
                                                            setFitness(tempFitness + 1);
                                                        } else {
                                                            //it must be on the edge of the board 
                                                            tempFitness = getFitness();
                                                            setFitness(tempFitness + 1);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    //if it finished looping and find none way to eat then the fitness will + 1
                                    //then add to Astar arraylist
                                    Integer tempFitness = getFitness();
                                    setFitness(tempFitness + 1);
                                } //check wether it in the outer loop cycle
                                else if (outerSize != 0 && innerSize == 0) {
                                    for (Hole hole : possibleOuterHole) {
                                        checkPathLoop(hole.a, hole.b);
                                        boolean outerSuccess = getAiSuccess();
                                        if (outerSuccess == true) {
                                            //check wether the final x and y is the possible node
                                            if (possibleNode.x == getFinalX() && possibleNode.y == getFinalY()) {
                                                Integer tempFitness = getFitness();
                                                setFitness(tempFitness - 1);
                                                //then check if it can counter or not
                                                //check for AI Node if it can counter attack or not 
                                                for (Node aiNode : Nodes) {
                                                    if (aiNode.type.equals("E")) {
                                                        setInitX(aiNode.x);
                                                        setInitY(aiNode.y);
                                                        //init array to hold possible hole
                                                        ArrayList<Hole> counterInnerHole = new ArrayList<>();
                                                        ArrayList<Hole> counterOuterHole = new ArrayList<>();

                                                        //check possible hole in inner hole loop
                                                        for (Hole inHole : innerHole) {
                                                            checkLoopRange(inHole.a, inHole.b);
                                                            boolean rangeInnerLoop = getRangeLoop();
                                                            if (rangeInnerLoop == true) {
                                                                counterInnerHole.add(inHole);
                                                            }
                                                        }

                                                        //check possible hole in outer hole loop
                                                        for (Hole outHole : outerHole) {
                                                            checkLoopRange(outHole.a, outHole.b);
                                                            boolean rangeOuterLoop = getRangeLoop();
                                                            if (rangeOuterLoop == true) {
                                                                counterOuterHole.add(outHole);
                                                            }
                                                        }

                                                        //getting size of the size of the counter
                                                        int counterInnerSize = possibleInnerHole.size();
                                                        int counterOuterSize = possibleOuterHole.size();

                                                        // the range is between the inner size loop
                                                        if (counterInnerSize != 0 && counterOuterSize == 0) {
                                                            for (Hole aihole : counterInnerHole) {
                                                                checkPathLoop(aihole.a, aihole.b);
                                                                boolean innerSuccess = getAiSuccess();
                                                                if (innerSuccess == true) {
                                                                    //check wether the final x and y is the possible node
                                                                    if (possibleNode.x == getFinalX() && possibleNode.y == getFinalY()) {
                                                                        tempFitness = getFitness();
                                                                        setFitness(tempFitness - 1);
                                                                        //then check if it can counter or not
                                                                    }
                                                                }
                                                            }
                                                            //if it finished looping and find none way to eat then the fitness will + 1
                                                            //then add to Astar arraylist
                                                            tempFitness = getFitness();
                                                            setFitness(tempFitness + 1);
                                                        } else if (counterInnerSize == 0 && counterOuterSize != 0) {
                                                            for (Hole aihole : counterOuterHole) {
                                                                checkPathLoop(aihole.a, aihole.b);
                                                                boolean innerSuccess = getAiSuccess();
                                                                if (innerSuccess == true) {
                                                                    //check wether the final x and y is the possible node
                                                                    if (possibleNode.x == getFinalX() && possibleNode.y == getFinalY()) {
                                                                        tempFitness = getFitness();
                                                                        setFitness(tempFitness - 1);
                                                                        //then check if it can counter or not
                                                                    }
                                                                }
                                                            }
                                                            //if it finished looping and find none way to eat then the fitness will + 1
                                                            //then add to Astar arraylist
                                                            tempFitness = getFitness();
                                                            setFitness(tempFitness + 1);
                                                        } else {
                                                            //it must be on the edge of the board 
                                                            tempFitness = getFitness();
                                                            setFitness(tempFitness + 1);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    //if it finished looping and find none way to eat then the fitness will + 1
                                    //then add to Astar arraylist
                                    Integer tempFitness = getFitness();
                                    setFitness(tempFitness + 1);
                                } else {
                                    Integer tempFitness = getFitness();
                                    setFitness(tempFitness + 1);
                                }
                            }
                        }
                        NodeFitness temp = new NodeFitness(node, possibleNode, getFitness());
                        Astar.add(temp);
                    }
                }
            }
        }
    }

    public void aiMove() {
        //Astar (Node, Fitness)

        int fitness = Astar.get(0).getFitness();
        int nextfitness = 0;
        int highest_index = 0;
        int highest = fitness;
        for (int i = 1; i < Astar.size(); i++) {
            nextfitness = Astar.get(i).getFitness();
            if (nextfitness > highest) {
                highest = nextfitness;
                highest_index = i;
            }
        }
        Node tempstart = Astar.get(highest_index).getNodeStart();
        int tempX = tempstart.x;
        int tempY = tempstart.y;
        Integer index = checkValue(tempX, tempY);
        if (index != -1) {
            Node start = Nodes.get(index);
            Node move = Astar.get(highest_index).getNodeMove();
            selectNode(start.x, start.y);
            safeMove(move.x, move.y);
            manageNodes(start, move);
        }



    }

    public void checkPossibleMove(Node node) {
        possibleNodeMove.clear();
        Node moveUp = new Node(node.x - 1, node.y, node.type);
        Node moveDown = new Node(node.x + 1, node.y, node.type);
        Node moveRight = new Node(node.x, node.y + 1, node.type);
        Node moveLeft = new Node(node.x, node.y - 1, node.type);

        if (moveUp.x < 0 || moveUp.y < 0 || moveUp.x > 5 || moveUp.y > 5) {
            //do nothing
        } else {
            Integer temp = checkValue(moveUp.x, moveUp.y);
            if (temp == -1) {
                possibleNodeMove.add(moveUp);
            }
        }


        if (moveDown.x < 0 || moveDown.y < 0 || moveDown.x > 5 || moveDown.y > 5) {
            //do nothing
        } else {
            Integer temp = checkValue(moveDown.x, moveDown.y);
            if (temp == -1) {
                possibleNodeMove.add(moveDown);
            }
        }

        if (moveRight.x < 0 || moveRight.y < 0 || moveRight.x > 5 || moveRight.y > 5) {
            //do nothing
        } else {
            Integer temp = checkValue(moveRight.x, moveRight.y);
            if (temp == -1) {
                possibleNodeMove.add(moveRight);
            }
        }

        if (moveLeft.x < 0 || moveLeft.y < 0 || moveLeft.x > 5 || moveLeft.y > 5) {
            //do nothing
        } else {
            Integer temp = checkValue(moveLeft.x, moveLeft.y);
            if (temp == -1) {
                possibleNodeMove.add(moveLeft);
            }
        }

    }

    public void RandomMove() {
        Random rand = new Random();
        Integer temp = rand.nextInt(Nodes.size());
        Node node = Nodes.get(temp);

        if (node.type.equals("E")) {
            boolean safe;
            selectNode(node.x, node.y);
            if (node.x == 0 && node.y != 0) {
                Integer nextX = rand.nextInt(2); //0 + 0
                Integer nextY = rand.nextInt(3) - 1; //1 + 2
                safe = safeMove(nextX + node.x, nextY + node.y);
                if (safe == true) {
                    placeNode();
                    System.out.println("1");
                } else {
                    RandomMove();
                }
            } else if (node.x != 0 && node.y == 0) {
                Integer nextX = rand.nextInt(1) - 1;
                Integer nextY = rand.nextInt(2);
                safe = safeMove(nextX + node.x, nextY + node.y);
                if (safe == true) {
                    placeNode();
                    System.out.println("2");
                } else {
                    RandomMove();
                }
            } else if (node.x == 5 && node.y < 5) {
                Integer nextX = rand.nextInt(2) - 1;
                Integer nextY = rand.nextInt(3) - 1;
                safe = safeMove(nextX + node.x, nextY + node.y);
                if (safe == true) {
                    placeNode();
                    System.out.println("3");
                } else {
                    RandomMove();
                }
            } else if (node.x < 5 && node.y == 5) {
                Integer nextX = rand.nextInt(3) - 1;
                Integer nextY = rand.nextInt(2) - 1;
                safe = safeMove(nextX + node.x, nextY + node.y);
                if (safe == true) {
                    placeNode();
                    System.out.println("4");
                } else {
                    RandomMove();
                }
            } else if (node.x == 5 && node.y == 5) {
                Integer nextX = rand.nextInt(2) - 1;
                Integer nextY = rand.nextInt(2) - 1;
                safe = safeMove(nextX + node.x, nextY + node.y);
                if (safe == true) {
                    System.out.println("5");
                    placeNode();
                } else {
                    RandomMove();
                }
            } else if (node.x == 0 && node.y == 0) {
                Integer nextX = rand.nextInt(2);
                Integer nextY = rand.nextInt(2);
                safe = safeMove(nextX + node.x, nextY + node.y);
                if (safe == true) {
                    placeNode();
                    System.out.println("6");
                } else {
                    RandomMove();
                }
            } else {
                Integer nextX = rand.nextInt(3) - 1;
                Integer nextY = rand.nextInt(3) - 1;
                safe = safeMove(nextX + node.x, nextY + node.y);
                if (safe == true) {
                    placeNode();
                    System.out.println("7");
                } else {
                    RandomMove();
                }
            }
        } else {
            RandomMove();
        }
    }

    //stupid AI 
    public void dumbAI() {
        Random rand = new Random();
        Integer temp = rand.nextInt(Nodes.size());
        Integer arr = rand.nextInt(10);
        Integer holetemp = rand.nextInt(innerHole.size());
        Node node = Nodes.get(temp);
        if (node.type.equals("H")) {
            dumbAI();
        } else {
            if (temp + arr % 2 == 0) {
                Hole inner = innerHole.get(holetemp);
                Hole outer = outerHole.get(holetemp);
                selectNode(node.x, node.y);
                safeLoop(inner.a, outer.b);
                boolean checkInner = getValidLoop();
                if (checkInner == true) {
                    eat(inner.a, outer.b);
                    boolean status = getAiSuccess();
                    if (status == false) {
                        dumbAI();
                    } else {
                        // eat is success then its done 
                    }
                } else {
                    safeLoop(outer.a, outer.b);
                    boolean checkOuter = getValidLoop();
                    if (checkOuter == true) {
                        eat(outer.a, outer.b);
                        boolean status = getAiSuccess();
                        if (status == false) {
                            dumbAI();
                        } else {
                            // eat is success then its done
                        }
                    }
                }
            } else {
                boolean safe;
                selectNode(node.x, node.y);
                if (node.x == 0 && node.y != 0) {
                    Integer nextX = rand.nextInt(2);
                    Integer nextY = rand.nextInt(3) - 1;
                    safe = safeMove(nextX + node.x, nextY + node.y);
                    if (safe == true) {
                        placeNode();
                    } else {
                        dumbAI();
                    }
                } else if (node.x != 0 && node.y == 0) {
                    Integer nextX = rand.nextInt(1) - 1;
                    Integer nextY = rand.nextInt(2);
                    safe = safeMove(nextX + node.x, nextY + node.y);
                    if (safe == true) {
                        placeNode();
                    } else {
                        dumbAI();
                    }
                } else if (node.x == 5 && node.y < 5) {
                    Integer nextX = rand.nextInt(2) - 1;
                    Integer nextY = rand.nextInt(3) - 1;
                    safe = safeMove(nextX + node.x, nextY + node.y);
                    if (safe == true) {
                        placeNode();
                    } else {
                        dumbAI();
                    }
                } else if (node.x < 5 && node.y == 5) {
                    Integer nextX = rand.nextInt(3) - 1;
                    Integer nextY = rand.nextInt(2) - 1;
                    safe = safeMove(nextX + node.x, nextY + node.y);
                    if (safe == true) {
                        placeNode();
                    } else {
                        dumbAI();
                    }
                } else if (node.x == 5 && node.y == 5) {
                    Integer nextX = rand.nextInt(2) - 1;
                    Integer nextY = rand.nextInt(2) - 1;
                    safe = safeMove(nextX + node.x, nextY + node.y);
                    if (safe == true) {
                        placeNode();
                    } else {
                        dumbAI();
                    }
                } else if (node.x == 0 && node.y == 0) {
                    Integer nextX = rand.nextInt(2);
                    Integer nextY = rand.nextInt(2);
                    safe = safeMove(nextX + node.x, nextY + node.y);
                    if (safe == true) {
                        placeNode();
                    } else {
                        dumbAI();
                    }
                } else {
                    Integer nextX = rand.nextInt(3) - 1;
                    Integer nextY = rand.nextInt(3) - 1;
                    safe = safeMove(nextX + node.x, nextY + node.y);
                    if (safe == true) {
                        placeNode();
                    } else {
                        dumbAI();
                    }
                }

            }
        }
    }

    public void checkPathLoop(int x, int y) {
        //rough code
        ArrayList<Hole> rightFlowInnerHole = new ArrayList<>();
        ArrayList<Hole> leftFlowInnerHole = new ArrayList<>();
        ArrayList<Hole> rightFlowOuterHole = new ArrayList<>();
        ArrayList<Hole> leftFlowOuterHole = new ArrayList<>();

        //right hole rules
        rightFlowInnerHole.add(new Hole(1, 0));
        rightFlowInnerHole.add(new Hole(5, 1));
        rightFlowInnerHole.add(new Hole(4, 5));
        rightFlowInnerHole.add(new Hole(0, 4));

        //left holes rules
        leftFlowInnerHole.add(new Hole(0, 1));
        leftFlowInnerHole.add(new Hole(1, 5));
        leftFlowInnerHole.add(new Hole(5, 4));
        leftFlowInnerHole.add(new Hole(4, 0));

        //right outer hole rules
        rightFlowOuterHole.add(new Hole(2, 0));
        rightFlowOuterHole.add(new Hole(5, 2));
        rightFlowOuterHole.add(new Hole(3, 5));
        rightFlowOuterHole.add(new Hole(0, 3));

        //left outer hole rulse
        leftFlowOuterHole.add(new Hole(0, 2));
        leftFlowOuterHole.add(new Hole(2, 5));
        leftFlowOuterHole.add(new Hole(5, 3));
        leftFlowOuterHole.add(new Hole(3, 0));

        //example user input

        Hole userHoleInput = new Hole(x, y);

        boolean leftFlowInnerHoletemp = false;
        boolean rightFlowInnerHoletemp = false;
        boolean leftFlowOuterHoletemp = false;
        boolean rightFlowOuterHoletemp = false;
        int temp = 10;
        int counterLooptemp = getCounterLoop();
        turn = getTurn();


        for (Hole hole : leftFlowInnerHole) {
            if (hole.a == userHoleInput.a && hole.b == userHoleInput.b) {
                leftFlowInnerHoletemp = true;
                for (Hole userhole : innerHole) {
                    if (userhole.a == userHoleInput.a && userhole.b == userHoleInput.b) {
                        temp = innerHole.indexOf(userhole);
                        break;
                    }
                }
                break;
            } else {
                leftFlowInnerHoletemp = false;
            }
        }

        for (Hole hole : rightFlowInnerHole) {
            if (hole.a == userHoleInput.a && hole.b == userHoleInput.b) {
                rightFlowInnerHoletemp = true;
                for (Hole userhole : innerHole) {
                    if (userhole.a == userHoleInput.a && userhole.b == userHoleInput.b) {
                        temp = innerHole.indexOf(userhole);
                        break;
                    }
                }
                break;
            } else {
                rightFlowInnerHoletemp = false;
            }
        }

        for (Hole hole : leftFlowOuterHole) {
            if (hole.a == userHoleInput.a && hole.b == userHoleInput.b) {
                leftFlowOuterHoletemp = true;
                for (Hole userhole : outerHole) {
                    if (userhole.a == userHoleInput.a && userhole.b == userHoleInput.b) {
                        temp = outerHole.indexOf(userhole);
                        break;
                    }
                }
                break;
            } else {
                leftFlowOuterHoletemp = false;
            }
        }

        for (Hole hole : rightFlowOuterHole) {
            if (hole.a == userHoleInput.a && hole.b == userHoleInput.b) {
                rightFlowOuterHoletemp = true;
                for (Hole userhole : outerHole) {
                    if (userhole.a == userHoleInput.a && userhole.b == userHoleInput.b) {
                        temp = outerHole.indexOf(userhole);
                        break;
                    }
                }
                break;
            } else {
                rightFlowOuterHoletemp = false;
            }
        }


        if (rightFlowOuterHoletemp == true) {
            //temp index probably will be at 1 3 5 7
            if (counterLooptemp < 5) {
                if (temp == 1) {
                    Hole firstHole = outerHole.get(0);
                    Hole secondHole = outerHole.get(7);
                    for (int i = firstHole.a; i <= secondHole.a; i++) {
                        if (board[i][firstHole.b] != null) {
                            String pieces = board[i][firstHole.b];
                            if (pieces.equals(turn)) {
                                //failed to eat
                                setAiSuccess(false);
                                break;
                            } else {
                                //eat opponent pieces
                                setFinalX(i);
                                setFinalY(firstHole.b);
                                board[i][firstHole.b] = turn;
                                board[getInitX()][getInitY()] = null;
                                setAiSuccess(true);
                                break;
                            }

                        }
                    }
                    counterLooptemp = counterLooptemp + 1;
                    setCounterLoop(counterLooptemp);
                    checkPathLoop(secondHole.a, secondHole.b);

                } else {
                    Hole firstHole = outerHole.get(temp - 1);
                    Hole secondHole = outerHole.get(temp - 2);
                    Integer tempfirstHoleA = firstHole.a;
                    Integer tempfirstHoleB = firstHole.b;
                    Integer tempsecondHoleA = secondHole.a;
                    Integer tempsecondHoleB = secondHole.b;

                    if (tempfirstHoleB.equals(tempsecondHoleB) == true) {
                        if (tempfirstHoleA > tempsecondHoleA) {
                            for (int i = firstHole.a; i >= secondHole.a; i--) {
                                if (board[i][firstHole.b] != null) {
                                    String pieces = board[i][firstHole.b];
                                    if (pieces.equals(turn)) {
                                        //failed to eat
                                        setAiSuccess(false);
                                        break;

                                    } else {
                                        //eat opponent pieces
                                        setFinalX(i);
                                        setFinalY(firstHole.b);
                                        board[i][firstHole.b] = turn;
                                        board[getInitX()][getInitY()] = null;
                                        setAiSuccess(true);
                                        break;
                                    }

                                }
                            }
                            counterLooptemp = counterLooptemp + 1;
                            setCounterLoop(counterLooptemp);
                            checkPathLoop(secondHole.a, secondHole.b);
                        } else {
                            for (int i = firstHole.a; i <= secondHole.a; i++) {
                                if (board[i][firstHole.b] != null) {
                                    String pieces = board[i][firstHole.b];
                                    if (pieces.equals(turn)) {
                                        //failed to eat
                                        setAiSuccess(false);
                                        break;

                                    } else {
                                        //eat opponent pieces
                                        setFinalX(i);
                                        setFinalY(firstHole.b);
                                        board[i][firstHole.b] = turn;
                                        board[getInitX()][getInitY()] = null;
                                        setAiSuccess(true);
                                        break;
                                    }

                                }
                            }
                            counterLooptemp = counterLooptemp + 1;
                            setCounterLoop(counterLooptemp);
                            checkPathLoop(secondHole.a, secondHole.b);
                        }

                    } else if (tempfirstHoleA.equals(tempsecondHoleA) == true) {
                        if (tempfirstHoleB > tempsecondHoleB) {
                            for (int i = firstHole.b; i >= secondHole.b; i--) {
                                if (board[firstHole.a][i] != null) {
                                    String pieces = board[firstHole.a][i];
                                    if (pieces.equals(turn)) {
                                        //failed to eat
                                        setAiSuccess(false);
                                        break;
                                    } else {
                                        //eat opponent pieces
                                        setFinalX(firstHole.a);
                                        setFinalY(i);
                                        board[firstHole.a][i] = turn;
                                        board[getInitX()][getInitY()] = null;
                                        setAiSuccess(true);
                                        break;
                                    }

                                }
                            }
                            counterLooptemp = counterLooptemp + 1;
                            setCounterLoop(counterLooptemp);
                            checkPathLoop(secondHole.a, secondHole.b);
                        } else {
                            for (int i = firstHole.b; i <= secondHole.b; i++) {
                                if (board[firstHole.a][i] != null) {
                                    String pieces = board[firstHole.a][i];
                                    if (pieces.equals(turn)) {
                                        //failed to eat
                                        setAiSuccess(false);
                                        break;
                                    } else {
                                        //eat opponent pieces
                                        setFinalX(firstHole.a);
                                        setFinalY(i);
                                        board[firstHole.a][i] = turn;
                                        board[getInitX()][getInitY()] = null;
                                        setAiSuccess(true);
                                        break;
                                    }
                                }
                            }
                            counterLooptemp = counterLooptemp + 1;
                            setCounterLoop(counterLooptemp);
                            checkPathLoop(secondHole.a, secondHole.b);
                        }
                    }
                }
            } else {
                setAiSuccess(false);
            }


        } else if (leftFlowOuterHoletemp == true) {
            //user input will be 0,2,4,6
            if (counterLooptemp < 5) {
                if (temp == 6) {
                    Hole firstHole = outerHole.get(7);
                    Hole secondHole = outerHole.get(0);
                    for (int i = firstHole.a; i >= secondHole.a; i--) {
                        if (board[i][firstHole.b] != null) {
                            String pieces = board[i][firstHole.b];
                            if (pieces.equals(turn)) {
                                //failed to eat
                                setAiSuccess(false);
                                break;
                            } else {
                                //eat opponent pieces
                                setFinalX(i);
                                setFinalY(firstHole.b);
                                board[i][firstHole.b] = turn;
                                board[getInitX()][getInitY()] = null;
                                setAiSuccess(true);
                                break;
                            }
                        }
                    }
                    counterLooptemp = counterLooptemp + 1;
                    setCounterLoop(counterLooptemp);
                    checkPathLoop(secondHole.a, secondHole.b);
                } else {
                    Hole firstHole = outerHole.get(temp + 1);
                    Hole secondHole = outerHole.get(temp + 2);
                    Integer tempfirstHoleA = firstHole.a;
                    Integer tempfirstHoleB = firstHole.b;
                    Integer tempsecondHoleA = secondHole.a;
                    Integer tempsecondHoleB = secondHole.b;

                    if (tempfirstHoleB.equals(tempsecondHoleB)) {
                        for (int i = firstHole.a; i <= secondHole.a; i++) {
                            if (board[i][firstHole.b] != null) {
                                String pieces = board[i][firstHole.b];
                                if (pieces.equals(turn)) {
                                    //failed to eat
                                    setAiSuccess(false);
                                    break;
                                } else {
                                    //eat opponent pieces
                                    setFinalX(i);
                                    setFinalY(firstHole.b);
                                    board[i][firstHole.b] = turn;
                                    board[getInitX()][getInitY()] = null;
                                    setAiSuccess(true);
                                    break;
                                }
                            }
                        }
                        counterLooptemp = counterLooptemp + 1;
                        setCounterLoop(counterLooptemp);
                        checkPathLoop(secondHole.a, secondHole.b);
                    } else if (tempfirstHoleA.equals(tempsecondHoleA)) {

                        if (tempfirstHoleB > tempsecondHoleB) {
                            for (int i = firstHole.b; i >= secondHole.b; i--) {
                                if (board[firstHole.a][i] != null) {
                                    String pieces = board[firstHole.a][i];
                                    System.out.println("*******************************");
                                    System.out.println(pieces);
                                    System.out.println(turn);
                                    if (pieces.equals(turn)) {
                                        //failed to eat
                                        setAiSuccess(false);
                                        return;
                                    } else if (!pieces.equals(turn)){
                                        //eat opponent pieces
                                        setFinalX(firstHole.a);
                                        setFinalY(i);
//                                        board[firstHole.a][i] = turn;
//                                        board[getInitX()][getInitY()] = null;
                                        setAiSuccess(true);
                                        return;
                                    }

                                }
                            }
                            counterLooptemp = counterLooptemp + 1;
                            setCounterLoop(counterLooptemp);
                            checkPathLoop(secondHole.a, secondHole.b);
                        } else {
                            for (int i = firstHole.b; i <= secondHole.b; i++) {
                                if (board[firstHole.a][i] != null) {
                                    String pieces = board[firstHole.a][i];
                                    if (pieces.equals(turn)) {
                                        //failed to eat
                                        setAiSuccess(false);
                                        break;
                                    } else {
                                        //eat opponent pieces
                                        setFinalX(firstHole.a);
                                        setFinalY(i);
                                        board[firstHole.a][i] = turn;
                                        board[getInitX()][getInitY()] = null;
                                        setAiSuccess(true);
                                        break;
                                    }
                                }
                            }
                            counterLooptemp = counterLooptemp + 1;
                            setCounterLoop(counterLooptemp);
                            checkPathLoop(secondHole.a, secondHole.b);
                        }
                    }
                }
            } else {
                setAiSuccess(false);
            }

        } else if (rightFlowInnerHoletemp == true) {
            //temp index probably will be at 1 3 5 7
            if (counterLooptemp < 5) {
                if (temp == 1) {
                    Hole firstHole = innerHole.get(0);
                    Hole secondHole = innerHole.get(7);
                    for (int i = firstHole.a; i <= secondHole.a; i++) {
                        if (board[i][firstHole.b] != null) {
                            String pieces = board[i][firstHole.b];
                            if (pieces.equals(turn)) {
                                //failed to eat
                                setAiSuccess(false);
                                break;
                            } else {
                                //eat opponent pieces
                                setFinalX(i);
                                setFinalY(firstHole.b);
                                board[i][firstHole.b] = turn;
                                board[getInitX()][getInitY()] = null;
                                setAiSuccess(true);
                                break;
                            }

                        }
                    }
                    counterLooptemp = counterLooptemp + 1;
                    setCounterLoop(counterLooptemp);
                    checkPathLoop(secondHole.a, secondHole.b);

                } else {
                    Hole firstHole = innerHole.get(temp - 1);
                    Hole secondHole = innerHole.get(temp - 2);
                    Integer tempfirstHoleA = firstHole.a;
                    Integer tempfirstHoleB = firstHole.b;
                    Integer tempsecondHoleA = secondHole.a;
                    Integer tempsecondHoleB = secondHole.b;

                    if (tempfirstHoleB.equals(tempsecondHoleB)) {
                        if (tempfirstHoleA > tempsecondHoleA) {
                            for (int i = firstHole.a; i >= secondHole.a; i--) {
                                if (board[i][firstHole.b] != null) {
                                    String pieces = board[i][firstHole.b];
                                    if (pieces.equals(turn)) {
                                        //failed to eat
                                        setAiSuccess(false);
                                        break;
                                    } else {
                                        //eat opponent pieces
                                        setFinalX(i);
                                        setFinalY(firstHole.b);
                                        board[i][firstHole.b] = turn;
                                        board[getInitX()][getInitY()] = null;
                                        setAiSuccess(true);
                                        break;
                                    }

                                }
                            }
                            counterLooptemp = counterLooptemp + 1;
                            setCounterLoop(counterLooptemp);
                            checkPathLoop(secondHole.a, secondHole.b);
                        } else if (tempfirstHoleA < tempsecondHoleA) {
                            for (int i = firstHole.a; i <= secondHole.a; i++) {
                                if (board[i][firstHole.b] != null) {
                                    String pieces = board[i][firstHole.b];
                                    if (pieces.equals(turn)) {
                                        //failed to eat
                                        setAiSuccess(false);
                                        break;
                                    } else {
                                        //eat opponent pieces
                                        setFinalX(i);
                                        setFinalY(firstHole.b);
                                        board[i][firstHole.b] = turn;
                                        board[getInitX()][getInitY()] = null;
                                        setAiSuccess(true);
                                        break;
                                    }

                                }
                            }
                            counterLooptemp = counterLooptemp + 1;
                            setCounterLoop(counterLooptemp);
                            checkPathLoop(secondHole.a, secondHole.b);
                        }

                    } else if (tempfirstHoleA.equals(tempsecondHoleA)) {
                        if (tempfirstHoleB > tempsecondHoleB) {
                            for (int i = firstHole.b; i >= secondHole.b; i--) {
                                if (board[firstHole.a][i] != null) {
                                    String pieces = board[firstHole.a][i];
                                    if (pieces.equals(turn)) {
                                        //failed to eat
                                        setAiSuccess(false);
                                        break;
                                    } else {
                                        //eat opponent pieces
                                        setFinalX(firstHole.a);
                                        setFinalY(i);
                                        board[firstHole.a][i] = turn;
                                        board[getInitX()][getInitY()] = null;
                                        setAiSuccess(true);
                                        break;
                                    }
                                }
                            }
                            counterLooptemp = counterLooptemp + 1;
                            setCounterLoop(counterLooptemp);
                            checkPathLoop(secondHole.a, secondHole.b);
                        } else {
                            for (int i = firstHole.b; i <= secondHole.b; i++) {
                                if (board[firstHole.a][i] != null) {
                                    String pieces = board[firstHole.a][i];
                                    if (pieces.equals(turn)) {
                                        //failed to eat
                                        setAiSuccess(false);
                                        break;
                                    } else {
                                        //eat opponent pieces
                                        setFinalX(firstHole.a);
                                        setFinalY(i);
                                        board[firstHole.a][i] = turn;
                                        board[getInitX()][getInitY()] = null;
                                        setAiSuccess(true);
                                        break;
                                    }
                                }
                            }
                            counterLooptemp = counterLooptemp + 1;
                            setCounterLoop(counterLooptemp);
                            checkPathLoop(secondHole.a, secondHole.b);
                        }
                    }
                }
            } else {
                setAiSuccess(false);
            }

        } else if (leftFlowInnerHoletemp == true) {
            //user input will be 0,2,4,6
            if (counterLooptemp < 5) {
                if (temp == 6) {
                    Hole firstHole = innerHole.get(7);
                    Hole secondHole = innerHole.get(0);
                    for (int i = firstHole.a; i >= secondHole.a; i--) {
                        if (board[i][firstHole.b] != null) {
                            String pieces = board[i][firstHole.b];
                            if (pieces.equals(turn)) {
                                //failed to eat
                                setAiSuccess(false);
                                break;
                            } else {
                                //eat opponent pieces
                                setFinalX(i);
                                setFinalY(firstHole.b);
                                board[i][firstHole.b] = turn;
                                board[getInitX()][getInitY()] = null;
                                setAiSuccess(true);
                                break;
                            }
                        }
                    }
                    counterLooptemp = counterLooptemp + 1;
                    setCounterLoop(counterLooptemp);
                    checkPathLoop(secondHole.a, secondHole.b);

                } else {
                    Hole firstHole = innerHole.get(temp + 1);
                    Hole secondHole = innerHole.get(temp + 2);
                    Integer tempfirstHoleA = firstHole.a;
                    Integer tempfirstHoleB = firstHole.b;
                    Integer tempsecondHoleA = secondHole.a;
                    Integer tempsecondHoleB = secondHole.b;
                    if (tempfirstHoleB.equals(tempsecondHoleB)) {
                        for (int i = firstHole.a; i <= secondHole.a; i++) {
                            if (board[i][firstHole.b] != null) {
                                String pieces = board[i][firstHole.b];
                                if (pieces.equals(turn)) {
                                    //failed to eat
                                    setAiSuccess(false);
                                    break;
                                } else {
                                    //eat opponent pieces
                                    setFinalX(i);
                                    setFinalY(firstHole.b);
                                    board[i][firstHole.b] = turn;
                                    board[getInitX()][getInitY()] = null;
                                    setAiSuccess(true);
                                    break;
                                }
                            }
                        }
                        counterLooptemp = counterLooptemp + 1;
                        setCounterLoop(counterLooptemp);
                        checkPathLoop(secondHole.a, secondHole.b);
                    } else if (tempfirstHoleA.equals(tempsecondHoleA)) {

                        if (tempfirstHoleB > tempsecondHoleB) {
                            System.out.println("-------------------------------------");
                            for (int i = firstHole.b; i >= secondHole.b; i--) {
                                if (board[firstHole.a][i] != null) {
                                    System.out.println("++++++++++++++++++++++++++++++");
                                    String pieces = board[firstHole.a][i];
                                    System.out.println(pieces);
                                    if (pieces.equals(turn)) {
                                        //failed to loop
                                        setAiSuccess(false);
                                        break;
                                    } else {
                                        //eat opponent pieces
                                        setFinalX(firstHole.a);
                                        setFinalY(i);
                                        board[firstHole.a][i] = turn;
                                        board[getInitX()][getInitY()] = null;
                                        setAiSuccess(true);
                                        break;
                                    }
                                }
                            }
                            counterLooptemp = counterLooptemp + 1;
                            setCounterLoop(counterLooptemp);
                            checkPathLoop(secondHole.a, secondHole.b);
                        } else {
                            for (int i = firstHole.b; i <= secondHole.b; i++) {
                                if (board[firstHole.a][i] != null) {
                                    String pieces = board[firstHole.a][i];
                                    if (pieces.equals(turn)) {
                                        //failed to loop
                                        setAiSuccess(false);
                                        break;
                                    } else {
                                        //eat opponent pieces
                                        setFinalX(firstHole.a);
                                        setFinalY(i);
                                        board[firstHole.a][i] = turn;
                                        board[getInitX()][getInitY()] = null;
                                        setAiSuccess(true);
                                        break;
                                    }
                                }
                            }
                            counterLooptemp = counterLooptemp + 1;
                            setCounterLoop(counterLooptemp);
                            checkPathLoop(secondHole.a, secondHole.b);
                        }
                    }
                }
            } else {
                setAiSuccess(false);
            }
        }
    }

    public void checkLoopRange(int x, int y) {
        Integer tempNodeX = getInitX(); // 5 4 4 
        Integer tempNodeY = getInitY(); // 1 0 0  

        if (tempNodeX == 0 || tempNodeX == 5) {
            //tempnode nya di 4 0

            Hole tempA = new Hole(0, tempNodeY);
            Hole tempB = new Hole(5, tempNodeY);
            if (tempA.a == x && tempA.b == y) {
                setRangeLoop(true);
            } else if (tempB.a == x && tempB.b == y) {
                setRangeLoop(true);
            } else {
                setRangeLoop(false);
            }
        } else if (tempNodeY == 0 || tempNodeY == 5) {
            //user hole nya di 2 0 4 0
            //hole nya yang valid cuma di 4 0 ato ga 4 5

            Hole tempA = new Hole(tempNodeX, 0); //4 0
            Hole tempB = new Hole(tempNodeX, 5); // 4 5
            if (tempA.a == x && tempA.b == y) {
                setRangeLoop(true);
            } else if (tempB.a == x && tempB.b == y) {
                setRangeLoop(true);
            } else {
                setRangeLoop(false);
            }
        } else {
            Hole tempA = new Hole(tempNodeX, 0);
            Hole tempB = new Hole(tempNodeX, 5);
            Hole tempC = new Hole(0, tempNodeY);
            Hole tempD = new Hole(5, tempNodeY);

            if (tempA.a == x && tempA.b == y) {
                setRangeLoop(true);
            } else if (tempB.a == x && tempB.b == y) {
                setRangeLoop(true);
            } else if (tempC.a == x && tempC.b == y) {
                setRangeLoop(true);
            } else if (tempD.a == x && tempD.b == y) {
                setRangeLoop(true);
            } else {
                setRangeLoop(false);
            }
        }
    }

    public void safeLoop(int x, int y) {
        //x = 0 y =1 
        //initx=4 inity=0
        Integer tempNodeX = getInitX();
        Integer tempNodeY = getInitY();
        setCounterLoop(0);
        //yang bole cuma dia if x = 1 -> (1,0) sama di (1,5)


        if (tempNodeX == x && tempNodeY != y) {
            if (tempNodeY < y) {
                for (int i = tempNodeY; i <= y; i++) {
                    if (board[x][i] != null) {
                        setValidLoop(false);
                        break;
                    } else {
                        setValidLoop(true);
                    }
                }
            } else {
                for (int i = tempNodeY; i >= y; i--) {
                    if (board[x][i] != null) {
                        setValidLoop(false);
                        break;
                    } else {
                        setValidLoop(true);
                    }
                }
            }
        } else if (tempNodeY == y && tempNodeX != x) {
            //40 
            //20
            if (tempNodeX < x) {
                for (int i = tempNodeX; i <= x; i++) {
                    if (board[i][y] != null) {
                        setValidLoop(false);
                        break;
                    } else {
                        setValidLoop(true);
                    }
                }
            } else {
                for (int i = tempNodeX; i >= x; i--) {
                    if (board[i][x] != null) {
                        setValidLoop(false);
                        break;
                    } else {
                        setValidLoop(true);
                    }
                }
            }
        } else if (tempNodeY == y && tempNodeX == x) {
            setValidLoop(true);
            //checkPathLoop(x, y);
        } else {
            setValidLoop(false);
        }
    }

    public void eat(int x, int y) {
        boolean validLooptemp = getValidLoop();
        if (validLooptemp == true) {
            int xi = getInitX();
            int yi = getInitY();
            int temp = checkValue(xi, yi);
            if (temp != -1) {
                String type = Nodes.get(temp).type;
                setTurn(type);
                checkPathLoop(x, y);
            }

        } else {
            setAiSuccess(false);
        }
    }

    public void WinCheck() {
        ArrayList<String> Same = new ArrayList<>();
        ArrayList<String> notSame = new ArrayList<>();
        for (Node node : Nodes) {
            if (node.type.equals("H")) {
                Same.add("H");
            } else if (node.type.equals("E")) {
                notSame.add("E");
            }
        }

        Integer countSame = Same.size();
        Integer countNotSame = notSame.size();

        if (countSame == 0 && countNotSame != 0) {
            setWinner("E");
        } else if (countSame != 0 && countNotSame == 0) {
            setWinner("H");
        } else {
            setWinner("B");
        }
    }

    public Integer checkValue(int x, int y) {
        for (Node node : Nodes) {
            if (node.x == x && node.y == y) {
                return Nodes.indexOf(node);
            }
        }
        return -1;
    }

    public void print() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                Integer temp = checkValue(row, col);
                if (temp != -1) {
                    board[row][col] = Nodes.get(temp).type;
                    System.out.print(" " + Nodes.get(temp).type + " ");
                } else {
                    board[row][col] = null;
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
        System.out.println();
        for (Node node : Nodes) {
            System.out.print(node.x);
            System.out.print(node.y);
            System.out.print(node.type);
            System.out.println();
        }
//        System.out.print(Astar.size());
//        for (NodeFitness nodefitness : Astar) {
//            System.out.print(nodefitness.getNodeStart().x + " " + nodefitness.getNodeStart().y);
//            System.out.println();
//            System.out.print(nodefitness.getNodeMove().x + " " + nodefitness.getNodeMove().y);
//            System.out.print(nodefitness.getFitness());
//            System.out.println();
//        }

    }
}
