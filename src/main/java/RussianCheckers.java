import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public abstract class RussianCheckers extends Application {

    static final int tileSize = 100;
    private static final int width = 8;
    private static final int height = 8;

    private Parent createContent(){
        Pane root = new Pane();
        root.setPrefSize(width*tileSize, height*tileSize);
        return root;
    }

}