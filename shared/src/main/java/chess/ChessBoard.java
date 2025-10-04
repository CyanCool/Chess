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

    public ChessBoard(ChessPiece[][] squares)
    {
        this.squares = squares;
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
        if(row != -1 && column != -1 && row < 8 && column < 8)
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

    public void removePiece(ChessPosition position)
    {
        squares[position.getRow()][position.getColumn()] = null;
    }

    public ChessBoard cloneBoard()
    {
        ChessBoard copy = new ChessBoard();

        for(int x = 0; x < 8; x++)
        {
            copy.squares[x] = Arrays.copyOf(squares[x], squares[x].length);
        }
        return copy;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ChessBoard)) return false;
        ChessBoard other = (ChessBoard) obj;
        return Arrays.deepEquals(this.squares, other.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }



    @Override
    public String toString()
    {
        return Arrays.toString(squares);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard()
    {
        //squares = new ChessPiece[8][8];
        for(int x = 0; x < 8; x++)
        {
            squares[1][x] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            squares[6][x] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
        squares[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        squares[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        squares[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        squares[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);

        squares[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        squares[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        squares[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        squares[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);

    }
}
