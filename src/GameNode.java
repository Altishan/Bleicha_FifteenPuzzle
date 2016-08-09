/**
 * Created by andyb on 7/25/2016.
 */


//!!!!!!!!!!!!!!!!!!!!!!!!
//THIS CLASS IS USELESS
//but I don't want to delete it
//because of reasons
//!!!!!!!!!!!!!!!!!!!!!!!!
public class GameNode {

    private char data;

    private int x;

    private int y;

    public GameNode(final char theData, final int theX, final int theY) {
        data = theData;
        x = theX;
        y = theY;
    }

    public void updateX(final int toUpdate) {
        x += toUpdate;
    }

    public void updateY(final int toUpdate) {
        y += toUpdate;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public char getData() {
        return this.data;
    }

    public void setData(final char theData) {
        this.data = theData;
    }
}
