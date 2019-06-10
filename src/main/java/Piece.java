import javafx.scene.layout.StackPane;
import javafx.scene.shape.Ellipse;
import javafx.scene.paint.Color;
import java.awt.*;

public class Piece extends StackPane {

    static final int tileSize = 100;
    private double mouseX, mouseY;
    private double oldX, oldY;
    private TypeOfPiece type;

    public TypeOfPiece getType() {
        return  type;
    }
    public double getOldY() {
        return oldY;
    }
    public double getOldX() {
        return oldX;
    }

    public Piece(TypeOfPiece type, int x, int y) {
        this.type = type;

        move(x, y);

        Ellipse shadow = new Ellipse(tileSize * 0.3125, tileSize * 0.26);
        shadow.setFill(Color.BLACK);
        shadow.setStrokeWidth(tileSize * 0.03);
        shadow.setTranslateX((tileSize - tileSize * 0.3125 * 2) / 2);
        shadow.setTranslateY((tileSize - tileSize * 0.26 * 2) / 2 + tileSize * 0.05);

        Ellipse checker = new Ellipse(tileSize * 0.3125, tileSize * 0.26);
        checker.setFill(type == TypeOfPiece.BLACK ? Color.valueOf("696969") : Color.valueOf("#FFF0F5"));
        checker.setStroke(Color.BLACK);
        checker.setStrokeWidth(tileSize * 0.03);
        checker.setTranslateX((tileSize - tileSize * 0.3125 * 2) / 2);
        checker.setTranslateY((tileSize - tileSize * 0.26 * 2) / 2);


        getChildren().addAll(shadow, checker);
        setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });
        setOnMouseDragged(event -> {
            relocate(event.getSceneX() - mouseX + oldX, event.getSceneY() - mouseY + oldY);
        });
    }
    public void move(int x, int y){
        oldX = x * tileSize;
        oldY = y * tileSize;
        relocate(oldX, oldY);
    }
    public void abortMove() {
        relocate(oldX, oldY);
    }
}
