public class MoveResult {
    private TypeOfMoves type;

    public TypeOfMoves getType() {
        return type;
    }

    private Piece piece;
    public Piece getPiece() {
        return piece;
    }
    public MoveResult(TypeOfMoves type) {
        this(type, null);
    }
    public MoveResult(TypeOfMoves type, Piece piece) {
        this.type = type;
        this.piece = piece;
    }
}
