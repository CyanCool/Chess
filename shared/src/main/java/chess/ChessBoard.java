package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard
{
    private ChessPiece[][] squares;

    public ChessBoard()
    {
        squares = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece)
    {
        if(piece != null)
        {
            squares[position.getRow()-1][position.getColumn()-1] = piece;
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position)
    {
        int row = position.getRow()-1;
        int column = position.getColumn()-1;
        if(row != -1 && column != -1)
        {
            return squares[row][column];
        }
        return null;
    }

    //checks to see if the board has a piece in x, y position
    public int hasPiece(int x, int y, ChessGame.TeamColor homeTeam)
    {
        if((x > -1 && x < squares.length) && (y > -1 && y < squares[x].length) && squares[x][y] != null)
        {
            if(((squares[x][y].getTeamColor() == ChessGame.TeamColor.WHITE) && (homeTeam == ChessGame.TeamColor.WHITE)) || ((squares[x][y].getTeamColor() == ChessGame.TeamColor.BLACK) && (homeTeam == ChessGame.TeamColor.BLACK))) //checks to see if the piece is present and on the same team
            {
                return 0; //same color in position
            }
            else if(((squares[x][y].getTeamColor() == ChessGame.TeamColor.WHITE) && (homeTeam == ChessGame.TeamColor.BLACK)) || ((squares[x][y].getTeamColor() == ChessGame.TeamColor.BLACK) && (homeTeam == ChessGame.TeamColor.WHITE)))//checks to see if the piece is present and on the opposite team
            {
                return 1; //different color in position
            }

        }
        return -1;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode()
    {
        return Arrays.deepHashCode(squares);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard()
    {
        squares = new ChessPiece[8][8]; // in C++ I think I would have to get rid of the old array because it would be stuck in memory but I think Java has garbage collection
    }
}
