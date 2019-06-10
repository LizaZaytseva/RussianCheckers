import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {

    private Piece piece;

    public boolean hasPiece(){
        return piece != null;
    }

    public Piece getPiece(){
        return piece;
    }

    public void setPiece(Piece piece){
        this.piece = piece;
    }

    public Tile(boolean light, int x, int y) {
        setWidth(RussianCheckers.tileSize);
        setHeight(RussianCheckers.tileSize);
        relocate(x * RussianCheckers.tileSize, y * RussianCheckers.tileSize);
        setFill(light ? Color.valueOf("#feb") : Color.valueOf("#A9A9A9"));
    }


}
