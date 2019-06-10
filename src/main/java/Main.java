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
                    ch = makePiece(TypeOfPiece.BLACK, x, y);
                }
                if ((y >= 5) && (x + y) % 2 != 0) {
                    ch = makePiece(TypeOfPiece.WHITE, x, y);
                }
                if (ch != null) {
                    tile.setPiece(ch);
                    pieceGroup.getChildren().add(ch);
                }
            }
        }
        return root;
    }

    private MoveResult tryMove(Piece piece, int x, int y) {
        if (player == -1 && !wightQueen) {
            if (board[x][y].hasPiece() || (x + y) % 2 == 0) {
                return new MoveResult(TypeOfMoves.NONE);
            }
            int x0 = toBoard(piece.getOldX());
            int y0 = toBoard(piece.getOldY());
            if (Math.abs(x - x0) == 1 && y - y0 == piece.getType().moveDir && piece.getType() == TypeOfPiece.WHITE) {
                player = 1;
                return new MoveResult(TypeOfMoves.NORMAL);
            } else if (Math.abs(x - x0) == 2 && (Math.abs(y - y0) == 2) && piece.getType() == TypeOfPiece.WHITE) {
                int x1 = x0 + (x - x0) / 2;
                int y1 = y0 + (y - y0) / 2;
                if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()) {
                    return new MoveResult((TypeOfMoves.KILL), board[x1][y1].getPiece());
                }
            }
        } else if (player == 1 && !blackQueen){
            if (board[x][y].hasPiece() || (x + y) % 2 == 0) {
            return new MoveResult(TypeOfMoves.NONE);
        }
            int x0 = toBoard(piece.getOldX());
            int y0 = toBoard(piece.getOldY());
            if (Math.abs(x - x0) == 1 && y - y0 == piece.getType().moveDir && piece.getType() == TypeOfPiece.BLACK) {
                player = -1;
                return new MoveResult(TypeOfMoves.NORMAL);
            } else if (Math.abs(x - x0) == 2 && (Math.abs(y - y0) == 2) && piece.getType() == TypeOfPiece.BLACK) {
                int x1 = x0 + (x - x0) / 2;
                int y1 = y0 + (y - y0) / 2;
                if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()) {
                    return new MoveResult((TypeOfMoves.KILL), board[x1][y1].getPiece());
                }
            }
        } else if (player == -1 && wightQueen){
            if (board[x][y].hasPiece() || (x + y) % 2 == 0) {
                return new MoveResult(TypeOfMoves.NONE);
            }
            boolean isEmpty = false;
            int x0 = toBoard(piece.getOldX());
            int y0 = toBoard(piece.getOldY());

            if (Math.abs(x - x0) == 1 && y - y0 == piece.getType().moveDir && piece.getType() == TypeOfPiece.WHITE_QUEEN) {
                player = 1;
                return new MoveResult(TypeOfMoves.NORMAL);
            } else if (Math.abs(x - x0) == 2 && (Math.abs(y - y0) == 2) && piece.getType() == TypeOfPiece.WHITE_QUEEN) {
                int x1 = x0 + (x - x0) / 2;
                int y1 = y0 + (y - y0) / 2;
                if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()) {
                    return new MoveResult((TypeOfMoves.KILL), board[x1][y1].getPiece());
                }
            }
        }
        return new MoveResult(TypeOfMoves.NONE);
    }

    private int toBoard(double pixel) {
        return (int)(pixel + tileSize / 2) / tileSize;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Main");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Piece makePiece(TypeOfPiece type, int x, int y){
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
                case KILL_Q:
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(piece);
                    Piece oth = res.getPiece();
                    board[toBoard(oth.getOldX())][toBoard(oth.getOldY())].setPiece(null);
                    pieceGroup.getChildren().remove(oth);
            }
        });
        return piece;
    }

    public static void main(String[] args) {launch(args); }
}
