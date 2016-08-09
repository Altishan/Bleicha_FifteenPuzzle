import java.util.*;

/**
 * Created by andyb on 7/25/2016.
 */
public class TreeNode implements Comparable<TreeNode> {

    //private final String nodeData;

    private static final int BOARDSIZE = 4; //value for board size

    private static final List<String> WINSTATE= new ArrayList<>(
            Arrays.asList("123456789ABCDEF ", "123456789ABCDFE ")
            );

    private static final char[][] WINNINGBOARD = stringToCharArray(WINSTATE.get(0));

    private final char[][] nodeData;

    private final TreeNode Parent;

    private final List<TreeNode> Children;

    private int Depth = -1;

    private int HVal; //Heuristic value

    private boolean checkedWin;

    private int blankX;

    private int blankY;


    public TreeNode(final char[][] theData, final TreeNode theParent, final int theDepth,
                    final int theHVal, final int theHChoice) {
        //nodeData = theData;
        nodeData = copyCharArray(theData);
        findBlank();
        Parent = theParent;
        Depth = theDepth + 1;
        if (theHChoice == 1) {
            HVal = theHVal + tilesOutOfPlace();
        } else if (theHChoice == 2) {
            HVal = theHVal + getManhattanDist();
        } else {
            HVal = theHVal;
        }
        Children = new ArrayList<>();
    }

    public void addChild(TreeNode theChild) {
        Children.add(theChild);
    }

    public List<TreeNode> getChildren() {
        return Children;
    }

    public char[][] getNodeData() {
        return nodeData;
    }

    public TreeNode getParent() {
        return Parent;
    }

    public int getDepth() {
        return Depth;
    }

    public int getHVal() { return HVal; }

    public void setDepth(final int theDepth) {
        Depth = theDepth;
    }

    public boolean checkGoal() {
        boolean ret = false;
        for (int i = 0; i < WINSTATE.size(); i++) {
            ret = nodeToString().equals(WINSTATE.get(i));
            if (ret) break;
        }
        return ret;
    }

    //Returns node data as a string
    public String nodeToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nodeData.length; i++) {
            for (int j = 0; j < nodeData.length; j++) {
                sb.append(nodeData[i][j]);
            }
        }
        return sb.toString();

    }

    private void findBlank() {
        for (int i = 0; i < nodeData.length; i++) {
            for (int j = 0; j < nodeData.length; j++) {
                if(nodeData[i][j] == ' ') {
                    blankX = i;
                    blankY = j;
                }
            }
        }
    }

    public int getBlankX() { return blankX;}
    public int getBlankY() { return blankY;}

    public char[][] copyCharArray(char[][] toCopy) {
        char[][] toReturn = new char[BOARDSIZE][BOARDSIZE];
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                toReturn[i][j] = toCopy[i][j];
            }
        }
        return toReturn;
    }


    //BUILD ONE THAT PRINTS A MATRIX YOU DINGLE
    //prints the node as a matrix
    public String nodeToStringAsMatrix() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(' ');
            sb.append('-');
        }
        for (int i = 0; i < nodeData.length; i++) {
            sb.append('|');
            for (int j = 0; j < nodeData.length; j++) {
                sb.append(nodeData[i][j]);
                sb.append('|');
            }
            sb.append('\n');
        }
        for (int i = 0; i < 4; i++) {
            sb.append(' ');
            sb.append('-');
        }
        return sb.toString();
    }

    private int tilesOutOfPlace() {
        char[] toCompare = WINSTATE.get(0).toCharArray();
        int toRet = 0;
        for (int i = 0; i < nodeData.length; i++) {
            for (int j = 0; j < nodeData.length; j++) {
                if (nodeData[i][j] != toCompare[(i * 4) + j]) {
                    toRet++;
                }
            }
        }
        return toRet;
    }

    private int getManhattanDist() {
        int totalDist = 0;
        int distHolder;
        Map<Character, IandJholder> manhMap = new HashMap<>();
        buildManhMap(manhMap);
        for (int i = 0; i < nodeData.length; i++) {
            for (int j = 0; j < nodeData.length; j++) {
                distHolder = 0;
                distHolder += Math.abs(manhMap.get(nodeData[i][j]).getI() - i);
                distHolder += Math.abs(manhMap.get(nodeData[i][j]).getJ() - j);
                totalDist += distHolder;
            }
        }
        return totalDist;
    }

    public void buildManhMap(Map<Character, IandJholder> theMap) {
        for (int i = 0; i < WINNINGBOARD.length; i++) {
            for (int j = 0; j < WINNINGBOARD.length; j++) {
                theMap.put(WINNINGBOARD[i][j], new IandJholder(i, j));
            }
        }
    }

    public static char[][] stringToCharArray(String theString) {
        char[][] toRet = new char[BOARDSIZE][BOARDSIZE];
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                toRet[i][j] = theString.charAt((i * 4) + j);
            }
        }
        return toRet;
    }


    @Override
    public int compareTo(TreeNode theOther) {
        int toRet = 0;
        if (this.HVal > theOther.HVal) {
            toRet = 1;
        } else if (this.HVal < theOther.HVal) {
            toRet = -1;
        }
        return toRet;
    }

    private class IandJholder {

        private int I;

        private int J;

        public IandJholder (final int theI, final int theJ) {
            I = theI;
            J = theJ;
        }

        public int getI() {
            return I;
        }

        public int getJ() {
            return J;
        }
    }
}
