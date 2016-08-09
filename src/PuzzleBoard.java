//main file



import java.util.*;

/**
 * Fifteen puzzle project.
 * TCSS 438 AI.
 * @author Andy Bleich
 */
public class PuzzleBoard {

    private static final ArrayList<String> ValidSearch = new ArrayList<>(Arrays.asList(
                            "BFS", "DFS", "GBFS",
                             "Astar",  "DLS"));

    private static final int BOARDSIZE = 4; //value for board size

    private static final ArrayList<Character> VALIDCHARS =
            new ArrayList<>(Arrays.asList('1', '2', '3', '4',
                                          '5', '6', '7', '8',
                                          '9', 'A', 'B', 'C',
                                          'D', 'E', 'F', ' '));

    private List<String> Created;

    private char[][] Board;

    private TreeNode Current;

    private int maxDepth = 0;

    private int nodesCreated = 0;

    private int nodesExpanded = 0;

    private int maxFringe = 0;

    private TreeNode Root;

    private boolean checkedWin;

    private int blankX; // X position of the blank

    private int blankY; //Y Position of the blank

   // private int tempBlankX;

   // private int tempBlankY;



    public static void main(String[] args) {
        //System.out.println("AYYYYYY");

        boolean validated = true;

        if (args.length > 3 || args.length < 2) {
            System.err.println("Invalid input. Number of args not correct.");
            validated = false;
        }

        String initBoardState = args[0];
        String searchType = args[1];

       // String initBoardState = "123456789AB DEFC";

        //String initBoardState = "234 16785ABC9DEF";
//        String searchType = "DLS";
//        int searchParam = 2;

        int searchParam = 0;
        if (args.length == 3) {
            searchParam = Integer.parseInt(args[2]);
            if (args[2].equals("h1")) {
                searchParam = 1;
            } else if (args[2].equals("h2")) {
                searchParam = 2;
            } else if (searchType.equals("DLS")) {
                searchParam = Integer.parseInt(args[2]);
            }
        }


        if (validated) {
            initBoardState = initBoardState.toUpperCase();
            validated = initBoardState.length() == 16;
        }

        if (validated){
            char[] initChars = initBoardState.toCharArray();
            for (int i = 0; i < initChars.length && validated; i++) {
                validated = VALIDCHARS.contains(initChars[i]);
            }
        }

        if (validated) {
            validated = ValidSearch.contains(searchType);
        }
        if(validated) {
            if (searchType.equals("GBFS") || searchType.equals("Astar")) {
                validated = searchParam == 1 || searchParam == 2;
            } else if (searchType.equals("DLS")) {
                validated = searchParam > 0;
            }
        }

        if (validated) {
            PuzzleBoard puzzle = new PuzzleBoard(initBoardState);

            //input puzzle search method here.
            if (searchType.equals(ValidSearch.get(0))) {
                puzzle.BFS();
            } else if (searchType.equals(ValidSearch.get(1))) {
                puzzle.DFS();
            } else if (searchType.equals(ValidSearch.get(2))) {
                puzzle.GBFS(searchParam);
            } else if (searchType.equals(ValidSearch.get(3))) {
                puzzle.AStar(searchParam);
            } else if (searchType.equals(ValidSearch.get(4))) {
                puzzle.DLS(searchParam);
            }

        } else {
            System.out.println("Input invalid, no searching will be done.");
        }
    }

    public PuzzleBoard(String theInitState) {
        Created  = new ArrayList<>();
        //Created.add("VFHGVHDGSVF");
        //System.out.println(Created.get(0));
        Board = new char[BOARDSIZE][BOARDSIZE];
        populateBoard(theInitState);
        Root = new TreeNode(copyCharArray(Board), null, maxDepth, 0, 0);

    }

    private void populateBoard(String theInitState) {
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                Board[i][j] = theInitState.charAt((i * 4) + j);
            }
        }

    }

    /*public void populateTempBoard(String theState) {
        char[] chars = theState.toCharArray();

        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                TempBoard[i][j] = new GameNode(chars[i + j], i, j);
                if (chars[i + j] == ' ') {
                    tempBlankX = i;
                    tempBlankY = j;
                }
            }
        }

    }*/

    public void genLeftMove(Collection<TreeNode> col, int theHChoice, boolean isGBFS) {
        // Moves will be like moving the blank square
        // so a left move would be '[6]' <-- '[]'
        // six is moving right and the blank will replace where 6 was
        if (blankX - 1 >= 0) {
            TreeNode tempNode = null;
            int i = Current.getBlankX();
            int j = Current.getBlankY();
            char[][] TempBoard = copyCharArray(Current.getNodeData());
            char tempData = TempBoard[i - 1][j];
            TempBoard[i][j] = tempData;
            TempBoard[i - 1][j] = ' ';
            if (!Created.contains(charArrayToString(TempBoard))) {
                Created.add(charArrayToString(TempBoard));
                if (isGBFS) {
                    tempNode = new TreeNode(TempBoard, Current, Current.getDepth(),
                            0, theHChoice);
                } else {
                    tempNode = new TreeNode(TempBoard, Current, Current.getDepth(),
                            Current.getHVal(), theHChoice);
                }
                Current.addChild(tempNode);
                col.add(tempNode);
                nodesCreated++;
                //System.out.println(charArrayToString(TempBoard));
            }
        }
    }

    public void genRightMove(Collection<TreeNode> col, int theHChoice, boolean isGBFS) {
        if (blankX + 1 < BOARDSIZE) {
            TreeNode tempNode = null;
            int i = Current.getBlankX();
            int j = Current.getBlankY();
            char[][] TempBoard = copyCharArray(Current.getNodeData());
            char tempData = TempBoard[i + 1][j];
            TempBoard[i][j] = tempData;
            TempBoard[i + 1][j] = ' ';
            if (!Created.contains(charArrayToString(TempBoard))) {
                Created.add(charArrayToString(TempBoard));
                if (isGBFS) {
                    tempNode = new TreeNode(TempBoard, Current, Current.getDepth(),
                            0, theHChoice);
                } else {
                    tempNode = new TreeNode(TempBoard, Current, Current.getDepth(),
                            Current.getHVal(), theHChoice);
                }
                Current.addChild(tempNode);
                col.add(tempNode);
                nodesCreated++;
                //System.out.println(charArrayToString(TempBoard));
            }
        }
    }

    public void genUpMove(Collection<TreeNode> col, int theHChoice, boolean isGBFS) {
        if (blankY - 1 >= 0) {
            TreeNode tempNode = null;
            int i = Current.getBlankX();
            int j = Current.getBlankY();
            char[][] TempBoard = copyCharArray(Current.getNodeData());
            char tempData = TempBoard[i][j - 1];
            TempBoard[i][j] = tempData;
            TempBoard[i][j - 1] = ' ';
            if (!Created.contains(charArrayToString(TempBoard))) {
                Created.add(charArrayToString(TempBoard));
                if (isGBFS) {
                    tempNode = new TreeNode(TempBoard, Current, Current.getDepth(),
                            0, theHChoice);
                } else {
                    tempNode = new TreeNode(TempBoard, Current, Current.getDepth(),
                            Current.getHVal(), theHChoice);
                }
                Current.addChild(tempNode);
                col.add(tempNode);
                nodesCreated++;
                //System.out.println(charArrayToString(TempBoard));
            }
        }
    }

    public void genDownMove(Collection<TreeNode> col, int theHChoice, boolean isGBFS) {
        if (blankY + 1 < BOARDSIZE) {
            TreeNode tempNode = null;
            int i = Current.getBlankX();
            int j = Current.getBlankY();
            char[][] TempBoard = copyCharArray(Current.getNodeData());
            char tempData = TempBoard[i][j + 1];
            TempBoard[i][j] = tempData;
            TempBoard[i][j + 1] = ' ';
            if (!Created.contains(charArrayToString(TempBoard))) {
                Created.add(charArrayToString(TempBoard));
                if (isGBFS) {
                    tempNode = new TreeNode(TempBoard, Current, Current.getDepth(),
                            0, theHChoice);
                } else {
                    tempNode = new TreeNode(TempBoard, Current, Current.getDepth(),
                            Current.getHVal(), theHChoice);
                }
                Current.addChild(tempNode);
                col.add(tempNode);
                nodesCreated++;
                //System.out.println(charArrayToString(TempBoard));
            }
        }
    }

    public String BoardToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                sb.append(Board[i][j]);
            }
        }
        return sb.toString();
    }


    public void BFS() {
        Queue<TreeNode> bfsQueue = new LinkedList<>();
        bfsQueue.add(Root);
        while(!bfsQueue.isEmpty()) {
            Current = bfsQueue.remove();
            blankX = Current.getBlankX();
            blankY = Current.getBlankY();

            checkedWin = Current.checkGoal();
            if(!checkedWin) {
                nodesExpanded++;//Increment for each expanded
                movesHelper(bfsQueue, 0, false);

                if (bfsQueue.size() > maxFringe) {
                    maxFringe = bfsQueue.size();
                }
                if (Current.getDepth() > maxDepth) {
                    maxDepth = Current.getDepth();
                }
            } else {
                bfsQueue.clear();
            }

        }
        //System.out.println(BoardToString());
        printStatistics();

    }

    public void DFS() {
        Stack<TreeNode> dfsStack = new Stack<>();
        dfsStack.add(Root);
        while(!dfsStack.isEmpty()) {
            Current = dfsStack.pop();
            blankX = Current.getBlankX();
            blankY = Current.getBlankY();

            checkedWin = Current.checkGoal();
            if(!checkedWin) {
                nodesExpanded++;//Increment for each expanded
                movesHelper(dfsStack, 0, false);

                if (dfsStack.size() > maxFringe) {
                    maxFringe = dfsStack.size();
                }
                if (Current.getDepth() > maxDepth) {
                    maxDepth = Current.getDepth();
                }
            } else {
                dfsStack.clear();
            }

        }
        //System.out.println(BoardToString());
        printStatistics();

    }

    public void DLS(final int theDepth) {
        int depthLimit = theDepth;
        Stack<TreeNode> dfsStack = new Stack<>();
        dfsStack.add(Root);
        while(!dfsStack.isEmpty()) {
            Current = dfsStack.pop();
            blankX = Current.getBlankX();
            blankY = Current.getBlankY();

            checkedWin = Current.checkGoal();
            if(!checkedWin && Current.getDepth() <= depthLimit) {
                nodesExpanded++;//Increment for each expanded
                movesHelper(dfsStack, 0, false);

                if (dfsStack.size() > maxFringe) {
                    maxFringe = dfsStack.size();
                }
                if (Current.getDepth() > maxDepth) {
                    maxDepth = Current.getDepth();
                }
            } else {
                if (checkedWin) {
                    dfsStack.clear();
                }
            }

        }
        if (!checkedWin) {
            maxDepth = -1;
        }
        //System.out.println(BoardToString());
        printStatistics();

    }

    public void GBFS(final int theHChoice) {
        PriorityQueue<TreeNode> GBFSQueue = new PriorityQueue<>();
        GBFSQueue.add(Root);
        while (!GBFSQueue.isEmpty()) {
            Current = GBFSQueue.poll();
            blankX = Current.getBlankX();
            blankY = Current.getBlankY();
            checkedWin = Current.checkGoal();
            if(!checkedWin) {
                nodesExpanded++;
                movesHelper(GBFSQueue, theHChoice, true);
                if (GBFSQueue.size() > maxFringe) {
                    maxFringe = GBFSQueue.size();
                }
                if (Current.getDepth() > maxDepth) {
                    maxDepth = Current.getDepth();
                }
            } else {
                GBFSQueue.clear();
            }
        }
        //System.out.println(BoardToString());
        printStatistics();
    }

    public void AStar(final int theHChoice) {
        PriorityQueue<TreeNode> AstarQueue = new PriorityQueue<>();
        AstarQueue.add(Root);
        while (!AstarQueue.isEmpty()) {
            Current = AstarQueue.poll();
            blankX = Current.getBlankX();
            blankY = Current.getBlankY();
            checkedWin = Current.checkGoal();
            if(!checkedWin) {
                nodesExpanded++;
                movesHelper(AstarQueue, theHChoice, false);
                if (AstarQueue.size() > maxFringe) {
                    maxFringe = AstarQueue.size();
                }
                if (Current.getDepth() > maxDepth) {
                    maxDepth = Current.getDepth();
                }
            } else {
                AstarQueue.clear();
            }
        }
        //System.out.println(BoardToString());
        printStatistics();
    }
    
    private void movesHelper(Collection<TreeNode> col, int theHChoice, boolean isGBFS) {
        
        genRightMove(col, theHChoice, isGBFS);
        genDownMove(col, theHChoice, isGBFS);
        genLeftMove(col, theHChoice, isGBFS);
        genUpMove(col, theHChoice, isGBFS);
    }
    
//    private void movesHelperHelper(char[][] dequeueTemp, Collection<TreeNode> col) {
//        TreeNode tempNode;
//        String nodeCreated;
//        nodeCreated = TempBoardtoString();
//        if (Created.add(nodeCreated)) {
//            tempNode = new TreeNode(nodeCreated, dequeueTemp);
//            nodesCreated++;//Increment for each created
//            int currentNodeDepth = tempNode.getParent().getDepth() + 1;
//            if (currentNodeDepth > maxDepth) {
//                maxDepth = currentNodeDepth;
//            }
//            tempNode.setDepth(currentNodeDepth);
//            col.add(tempNode);
//            dequeueTemp.addChild(tempNode);
//
//        }
//    }

    public void printStatistics() {
        StringBuilder sb = new StringBuilder();
        if (maxDepth == -1) {
            nodesCreated = 0;
            nodesExpanded = 0;
            maxFringe = 0;
        }

        sb.append("Max Depth: " + maxDepth);
        sb.append(" Nodes created: " + nodesCreated);
        sb.append(" Nodes Expanded: " + nodesExpanded);
        sb.append(" Max Fringe size: " + maxFringe);
        System.out.println(sb.toString());
    }

    public char[][] copyCharArray(char[][] toCopy) {
        char[][] toReturn = new char[BOARDSIZE][BOARDSIZE];
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                toReturn[i][j] = toCopy[i][j];
            }
        }
        return toReturn;
    }

    public String charArrayToString(char[][] data) {
        String ret = "";
        for(int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                ret = ret + data[i][j];
            }
        }
        return ret;
    }

}
