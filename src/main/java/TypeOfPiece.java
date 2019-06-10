public enum TypeOfPiece {
    BLACK(1), WHITE(-1), BLACK_QUEEN(2), WHITE_QUEEN(-2);
    final int moveDir;

    TypeOfPiece(int moveDir) {
        this.moveDir = moveDir;
    }

    public static TypeOfPiece other(TypeOfPiece a) {
        if (a == TypeOfPiece.BLACK)
            return TypeOfPiece.WHITE;
        else return TypeOfPiece.BLACK;
    }
}
