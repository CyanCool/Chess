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

    public ArrayList<ChessMove> customMove(ChessPiece.PieceType type, ChessPosition firstPosition, ChessPosition currentPosition, int row, int col, ArrayList<ChessMove> newPositions)
    {
        if(currentPosition.getRow() < 0 || currentPosition.getColumn() < 0)
        {
            return newPositions;
        }
        else
        {
            ChessPosition updatedPosition = new ChessPosition(currentPosition.getRow() -1  + row, currentPosition.getColumn() -1 + col);
            ChessMove move = new ChessMove(firstPosition, updatedPosition, type);
            newPositions.add(move);

            return customMove(type, firstPosition, updatedPosition, row, col, newPositions);
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
            possibleMoves = customMove(PieceType.QUEEN, myPosition, myPosition, 1, 0, possibleMoves);
            possibleMoves.addAll(customMove(PieceType.QUEEN, myPosition, myPosition, -1, 0, possibleMoves));
            possibleMoves.addAll(customMove(PieceType.QUEEN, myPosition, myPosition, 0, 1, possibleMoves));
            possibleMoves.addAll(customMove(PieceType.QUEEN, myPosition, myPosition, 0, -1, possibleMoves));
            possibleMoves.addAll(customMove(PieceType.QUEEN, myPosition, myPosition, -1, -1, possibleMoves));
            possibleMoves.addAll(customMove(PieceType.QUEEN, myPosition, myPosition, -1, 1, possibleMoves));
            possibleMoves.addAll(customMove(PieceType.QUEEN, myPosition, myPosition, 1, -1, possibleMoves));
            possibleMoves.addAll(customMove(PieceType.QUEEN, myPosition, myPosition, 1, 1, possibleMoves));

            return possibleMoves;
        }
        else if(type == ChessPiece.PieceType.BISHOP)
        {
            possibleMoves.addAll(customMove(PieceType.BISHOP, myPosition, myPosition, -1, -1, possibleMoves));
            possibleMoves.addAll(customMove(PieceType.BISHOP, myPosition, myPosition, -1, 1, possibleMoves));
            possibleMoves.addAll(customMove(PieceType.BISHOP, myPosition, myPosition, 1, -1, possibleMoves));
            possibleMoves.addAll(customMove(PieceType.BISHOP, myPosition, myPosition, 1, 1, possibleMoves));

            return possibleMoves;
        }
        else if(type == ChessPiece.PieceType.KNIGHT)
        {
            int[][] moveBy = {{-2,1},{2,1},{1,2},{-1,2},{-2,-1},{2,-1},{-1,-2},{1,-2}};
            for(int x = 0; x<8; x++)
            {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + moveBy[x][0], myPosition.getColumn() + moveBy[x][1]);
                if(newPosition.getRow() >= 0 && newPosition.getColumn() >= 0)
                {
                    ChessMove move = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT);
                    possibleMoves.add(move);
                }
            }
            return possibleMoves;
        }
        else if(type == ChessPiece.PieceType.ROOK)
        {
            possibleMoves = customMove(PieceType.ROOK, myPosition, myPosition, 1, 0, possibleMoves);
            possibleMoves.addAll(customMove(PieceType.ROOK, myPosition, myPosition, -1, 0, possibleMoves));
            possibleMoves.addAll(customMove(PieceType.ROOK, myPosition, myPosition, 0, 1, possibleMoves));
            possibleMoves.addAll(customMove(PieceType.ROOK, myPosition, myPosition, 0, -1, possibleMoves));

            return possibleMoves;
        }
        return null;
    }
}
