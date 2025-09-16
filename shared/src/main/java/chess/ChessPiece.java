package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece
{
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type)
    {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType
    {
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

    public ArrayList<ChessMove> queenMoves(ChessPosition myPosition, ChessPosition firstPosition, ArrayList<ChessMove> possibleMoves)
    {
        if(myPosition.getRow() < 0 || myPosition.getColumn() < 0)
        {
            return possibleMoves;
        }
        else
        {
            for (int i = -1; i < 2; i++)
            {
                for(int j = -1; j < 2; j++)
                {
                    ChessPosition newPosition = new ChessPosition(myPosition.getRow() -1  + i, myPosition.getColumn() -1 + j);
                    ChessMove move = new ChessMove(firstPosition, newPosition, ChessPiece.PieceType.QUEEN);
                    possibleMoves.add(move);
                    return queenMoves(newPosition, firstPosition, possibleMoves);
                }
            }
            return possibleMoves; //remove if not working

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
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        if (type == ChessPiece.PieceType.KING)
        {
            for (int i = -1; i < 2; i++)
            {
                for(int j = -1; j < 2; j++)
                {
                    ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j);
                    ChessMove move = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KING);
                    possibleMoves.add(move);
                }
            }
            return possibleMoves;
        }
        else if(type == ChessPiece.PieceType.QUEEN)
        {
            possibleMoves = queenMoves(myPosition, myPosition, possibleMoves); //is this redundant, I think the object remembers its contents already
            return possibleMoves;
        }
    }
}
