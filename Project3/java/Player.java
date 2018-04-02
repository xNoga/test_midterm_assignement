public class Player {

    private String name;
    private IPiece piece;
    private IDie die;

    public Player(String name, IPiece piece, IDie die) {
        this.name = name;
        this.piece = piece;
        this.die = die;
    }

    public void takeTurn() {
        int numberOfRolls = 2;
        int faceValue = 0;
        for (int i = 0; i < numberOfRolls; i++) {
            faceValue += die.roll();
        }
        int oldPos = piece.getLocation();
        int newPos = getNewPos(oldPos, faceValue);
        piece.setLocation(newPos);
    }

    private int getNewPos(int oldPos, int faceValue) {
        int newPos = oldPos + faceValue;

        if (newPos > 30) {
            // let's just say the board has 30 fields. When we cross the 'starting line' we start over from position 1.
            newPos = newPos - 30;
        }

        return newPos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IPiece getPiece() {
        return piece;
    }

    public void setPiece(IPiece piece) {
        this.piece = piece;
    }
}
