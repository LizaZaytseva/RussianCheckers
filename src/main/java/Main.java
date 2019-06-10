import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.lang.invoke.SwitchPoint;

public class Main extends Application {

    static final int tileSize = 100;
    private static final int width = 8;
    private static final int height = 8;
    private Tile[][] board = new Tile[width][height];
    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();
    private int player = -1;
    boolean wightQueen;
    boolean blackQueen;

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(width * tileSize, height * tileSize);
        root.getChildren().addAll(tileGroup, pieceGroup);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Piece ch = null;
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);

                if ((y <= 2) && (x + y) % 2 != 0) {
                    ch = makePiece(Piece.TypeOfPiece.BLACK, x, y);
                }
                if ((y >= 5) && (x + y) % 2 != 0) {
                    ch = makePiece(Piece.TypeOfPiece.WHITE, x, y);
                }
                /*if ((y < 5 && y > 2) && (x + y) % 2 != 0) {
                    ch = makePiece(Piece.TypeOfPiece.EMPTY, x, y);
                }*/
                if (ch != null) {
                    tile.setPiece(ch);
                    pieceGroup.getChildren().add(ch);
                }
            }
        }
        return root;
    }

        private MoveResult tryMove(Piece piece, int x, int y) {
            if (player == -1) {
                if (board[x][y].hasPiece() || (x + y) % 2 == 0) {
                    return new MoveResult(MoveResult.TypeOfMoves.NONE);
                }
                int x0 = toBoard(piece.getOldX());
                int y0 = toBoard(piece.getOldY());
                if (Math.abs(x - x0) == 1 && y - y0 == piece.getType().moveDir && piece.getType() == Piece.TypeOfPiece.WHITE) {
                    player = 1;
                    return new MoveResult(MoveResult.TypeOfMoves.NORMAL);
                } else if (Math.abs(x - x0) == 2 && (Math.abs(y - y0) == 2) && piece.getType() == Piece.TypeOfPiece.WHITE) {
                    int x1 = x0 + (x - x0) / 2;
                    int y1 = y0 + (y - y0) / 2;
                    if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()) {
                        if (canMove(piece, x,y)) {
                            player = -1;
                            return new MoveResult((MoveResult.TypeOfMoves.KILL), board[x1][y1].getPiece());
                        } else return new MoveResult((MoveResult.TypeOfMoves.KILL), board[x1][y1].getPiece());
                    }
                }
            } else {
                if (board[x][y].hasPiece() || (x + y) % 2 == 0) {
                    return new MoveResult(MoveResult.TypeOfMoves.NONE);
                }
                int x0 = toBoard(piece.getOldX());
                int y0 = toBoard(piece.getOldY());
                if (Math.abs(x - x0) == 1 && y - y0 == piece.getType().moveDir && piece.getType() == Piece.TypeOfPiece.BLACK) {
                    player = -1;
                    return new MoveResult(MoveResult.TypeOfMoves.NORMAL);
                } else if (Math.abs(x - x0) == 2 && (Math.abs(y - y0) == 2) && piece.getType() == Piece.TypeOfPiece.BLACK) {
                    int x1 = x0 + (x - x0) / 2;
                    int y1 = y0 + (y - y0) / 2;
                    if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()) {
                        if (canMove(piece, x,y)) {
                            player = 1;
                            return new MoveResult((MoveResult.TypeOfMoves.KILL), board[x1][y1].getPiece());
                        } else return new MoveResult((MoveResult.TypeOfMoves.KILL), board[x1][y1].getPiece());
                    }
                }
            }
            return new MoveResult(MoveResult.TypeOfMoves.NONE);
    }

    private int toBoard(double pixel) {
        return (int) (pixel + tileSize / 2) / tileSize;
    }
    private boolean canMove(Piece piece, int x, int y) {
        if ((x + 2 < 8) && (y + 2 < 8) && board[x + 1][y + 1].getPiece().getType() != null && board[x + 1][y + 1].getPiece().getType() != piece.getType()
                && board[x + 2][y + 2].getPiece().getType() == null) {
            return true;
        } else if ((x - 2 >= 0) && (y + 2 < 8) && board[x - 1][y + 1].getPiece().getType() != null && board[x - 1][y + 1].getPiece().getType() != piece.getType()
                && board[x - 2][y + 2].getPiece().getType() == null) {
            return true;
        } else if ((x + 2 < 8) && (y - 2 >= 0) && board[x + 1][y - 1].getPiece().getType() != null && board[x + 1][y - 1].getPiece().getType() != piece.getType()
                && board[x + 2][y - 2].getPiece().getType() == null) {
            return true;
        } else if ((x - 2 >= 0) && (y - 2 >= 0) && board[x - 1][y - 1].getPiece().getType() != null && board[x - 1][y - 1].getPiece().getType() != piece.getType()
                && board[x - 2][y - 2].getPiece().getType() == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Main");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Piece makePiece(Piece.TypeOfPiece type, int x, int y) {
        Piece piece = new Piece(type, x, y);
        piece.setOnMouseReleased(event -> {
            int newX = toBoard(piece.getLayoutX());
            int newY = toBoard(piece.getLayoutY());

            MoveResult res = tryMove(piece, newX, newY);
            int x0 = toBoard(piece.getOldX());
            int y0 = toBoard(piece.getOldY());
            switch (res.getType()) {
                case KILL:
                    piece.move(newX, newY);
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(piece);
                    Piece other = res.getPiece();
                    board[toBoard(other.getOldX())][toBoard(other.getOldY())].setPiece(null);
                    pieceGroup.getChildren().remove(other);
                    break;
                case NONE:
                    piece.abortMove();
                    break;
                case NORMAL:
                    piece.move(newX, newY);
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(piece);
                    break;
            }
        });
        return piece;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
