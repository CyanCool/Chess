package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type)
    {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor()
    {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType()
    {
        return type;
    }

    public ArrayList<ChessMove> recur(ArrayList<ChessMove> myMoves, int row, int col, ChessPosition startPosition, ChessPosition currentPosition, ChessBoard board, ChessPiece myPiece)
    {
        if(currentPosition.getRow() < 1 || currentPosition.getRow() > 8 || currentPosition.getColumn() < 1 || currentPosition.getColumn() > 8)
        {
            return myMoves;
        }
        else
        {
            if(startPosition != currentPosition)
            {
                int check = board.checkCollision(pieceColor,currentPosition);

                if(check != 0)
                {
                    ChessMove currentMove = new ChessMove(startPosition, currentPosition, null);
                    myMoves.add(currentMove);
                    if(check == 1)
                    {
                        return myMoves;
                    }
                }
                else
                {
                    return myMoves;
                }
            }
            ChessPosition updatedPosition = new ChessPosition(currentPosition.getRow()+row, currentPosition.getColumn()+col);
            return recur(myMoves, row, col, startPosition, updatedPosition, board, myPiece);
        }

    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        ArrayList<ChessMove> myMoves = new ArrayList<ChessMove>();

        if(type == PieceType.PAWN)
        {
            if(pieceColor == ChessGame.TeamColor.WHITE)
            {
                ChessPosition oneUp = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                ChessPosition twoUp = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
                ChessPosition diagleft = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
                ChessPosition diagright = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);

                if(myPosition.getRow() == 2)
                {
                   startMove(myMoves, myPosition, oneUp, twoUp, board, this);
                }
                defaultMove(myMoves, myPosition, oneUp, board, this);
                diagMove(myMoves, myPosition, diagleft, board, this);
                diagMove(myMoves, myPosition, diagright, board, this);
            }
            else if(pieceColor == ChessGame.TeamColor.BLACK)
            {
                ChessPosition oneUp = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                ChessPosition twoUp = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn());
                ChessPosition diagleft = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
                ChessPosition diagright = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);

                if(myPosition.getRow() == 7)
                {
                    startMove(myMoves, myPosition, oneUp, twoUp, board, this);
                }
                defaultMove(myMoves, myPosition, oneUp, board, this);
                diagMove(myMoves, myPosition, diagleft, board, this);
                diagMove(myMoves, myPosition, diagright, board, this);
            }
        }
        else if(type == PieceType.QUEEN)
        {
            recur(myMoves,1,0,myPosition, myPosition, board, this);
            recur(myMoves,0,1,myPosition, myPosition, board, this);
            recur(myMoves,-1,0,myPosition, myPosition, board, this);
            recur(myMoves,0,-1,myPosition, myPosition, board, this);
            recur(myMoves,1,1,myPosition, myPosition, board, this);
            recur(myMoves,-1,-1,myPosition, myPosition, board, this);
            recur(myMoves,1,-1,myPosition, myPosition, board, this);
            recur(myMoves,-1,1,myPosition, myPosition, board, this);
        }
        else if(type == PieceType.ROOK)
        {
            recur(myMoves,1,0,myPosition, myPosition, board, this);
            recur(myMoves,0,1,myPosition, myPosition, board, this);
            recur(myMoves,-1,0,myPosition, myPosition, board, this);
            recur(myMoves,0,-1,myPosition, myPosition, board, this);
        }
        else if(type == PieceType.BISHOP)
        {
            recur(myMoves,1,1,myPosition, myPosition, board, this);
            recur(myMoves,-1,-1,myPosition, myPosition, board, this);
            recur(myMoves,1,-1,myPosition, myPosition, board, this);
            recur(myMoves,-1,1,myPosition, myPosition, board, this);
        }
        else if(type == PieceType.KING)
        {
            int[][] moves = {{0,1}, {0,-1}, {-1,0}, {1,0}, {1,1}, {-1,-1}, {-1,1}, {1,-1}};
            for(int x = 0; x<8; x++)
            {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow()+moves[x][0], myPosition.getColumn()+moves[x][1]);
                if(newPosition.getRow() > 0 && newPosition.getRow() < 9 && newPosition.getColumn() > 0 && newPosition.getColumn() < 9)
                {
                    int check = board.checkCollision(pieceColor, newPosition);
                    if(check != 0)
                    {
                        ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                        myMoves.add(newMove);
                    }
                }
            }
        }
        else if(type == PieceType.KNIGHT)
        {
            int[][] moves = {{2,-1}, {2,1}, {1,-2}, {1,2}, {-2,-1}, {-2,1}, {-1,-2}, {-1,2}};
            for(int x = 0; x<8; x++)
            {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow()+moves[x][0], myPosition.getColumn()+moves[x][1]);
                if(newPosition.getRow() > 0 && newPosition.getRow() < 9 && newPosition.getColumn() > 0 && newPosition.getColumn() < 9)
                {
                    int check = board.checkCollision(pieceColor, newPosition);
                    if(check != 0)
                    {
                        ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                        myMoves.add(newMove);
                    }
                }
            }
        }
        return myMoves;
    }
    public int startMove(ArrayList<ChessMove> myMoves, ChessPosition start, ChessPosition one, ChessPosition two, ChessBoard board, ChessPiece piece)
    {
        if(one.getRow() > 0 && one.getRow() < 9 && one.getColumn() > 0 && one.getColumn() < 9)
        {
            int checkOne = board.checkCollision(pieceColor, one);
            if(checkOne == -1)
            {
                if(two.getRow() > 0 && two.getRow() < 9 && two.getColumn() > 0 && two.getColumn() < 9 && start != two)
                {
                    int checkTwo = board.checkCollision(pieceColor, two);
                    if(checkTwo == -1)
                    {
                        ChessMove moveTwo = new ChessMove(start, two, null);
                        addPawnMove(myMoves, moveTwo);
                    }
                }
            }
        }
        return -1;
    }

    public int defaultMove(ArrayList<ChessMove> myMoves, ChessPosition start, ChessPosition one, ChessBoard board, ChessPiece piece)
    {
        if(one.getRow() > 0 && one.getRow() < 9 && one.getColumn() > 0 && one.getColumn() < 9)
        {
            int checkOne = board.checkCollision(pieceColor, one);
            if(checkOne == -1 && start != one)
            {
                ChessMove moveOne = new ChessMove(start, one, null);
                addPawnMove(myMoves, moveOne);
            }
        }
        return -1;
    }

    public int diagMove(ArrayList<ChessMove> myMoves, ChessPosition start, ChessPosition diag, ChessBoard board, ChessPiece piece)
    {
        if(diag.getRow() > 0 && diag.getRow() < 9 && diag.getColumn() > 0 && diag.getColumn() < 9)
        {
            int checkDiag = board.checkCollision(pieceColor, diag);
            if(checkDiag == 1 && start != diag)
            {
                ChessMove moveDiag = new ChessMove(start, diag, null);
                addPawnMove(myMoves, moveDiag);
            }
        }
        return -1;
    }

    public int addPawnMove(ArrayList<ChessMove> myMoves, ChessMove diag)
    {
        ChessPosition start = diag.getStartPosition();
        ChessPosition end = diag.getEndPosition();
        if(end.getRow() == 1 || end.getRow() == 8)
        {
            ChessMove a = new ChessMove(start, end, PieceType.QUEEN);
            myMoves.add(a);
            ChessMove b = new ChessMove(start, end, PieceType.ROOK);
            myMoves.add(b);
            ChessMove c = new ChessMove(start, end, PieceType.BISHOP);
            myMoves.add(c);
            ChessMove d = new ChessMove(start, end, PieceType.KNIGHT);
            myMoves.add(d);
        }
        else
        {
            myMoves.add(diag);
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}

