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

    public void setTeamColor(ChessGame.TeamColor color)
    {
        pieceColor = color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType()
    {
        return type;
    }

    public void setPieceType(PieceType type)
    {
        this.type = type;
    }

    public ArrayList<ChessMove> customMove(ChessPosition firstPosition, ChessPosition currentPosition, int row, int col, ArrayList<ChessMove> newPositions, ChessBoard board)
    {
        int pieceType = board.hasPiece(currentPosition.getRow()-1, currentPosition.getColumn()-1, pieceColor);
        if(currentPosition.getRow()-1 < 0 || currentPosition.getColumn()-1 < 0 || currentPosition.getRow()-1 > 7 || currentPosition.getColumn()-1 > 7 || (currentPosition != firstPosition && pieceType == 0))
        {
            return newPositions;
        }
        else
        {
            if(firstPosition != currentPosition)
            {
                ChessMove move = new ChessMove(firstPosition, currentPosition, null);
                newPositions.add(move);
            }
            if(pieceType == 1)
            {
                return newPositions;
            }
            ChessPosition updatedPosition = new ChessPosition(currentPosition.getRow() + row, currentPosition.getColumn() + col);
            return customMove(firstPosition, updatedPosition, row, col, newPositions, board);
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
            int[][] moveBy = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
            return getChessMoves(board, myPosition, possibleMoves, moveBy);
        }
        else if(type == ChessPiece.PieceType.QUEEN)
        {

            customMove(myPosition, myPosition, 1, 0, possibleMoves, board);
            customMove(myPosition, myPosition, -1, 0, possibleMoves, board);
            customMove(myPosition, myPosition, 0, 1, possibleMoves, board);
            customMove(myPosition, myPosition, 0, -1, possibleMoves, board);
            customMove(myPosition, myPosition, -1, -1, possibleMoves, board);
            customMove(myPosition, myPosition, -1, 1, possibleMoves, board);
            customMove(myPosition, myPosition, 1, -1, possibleMoves, board);
            customMove(myPosition, myPosition, 1, 1, possibleMoves, board);

            return possibleMoves;
        }
        else if(type == ChessPiece.PieceType.BISHOP)
        {
            customMove(myPosition, myPosition, -1, -1, possibleMoves, board);
            customMove(myPosition, myPosition, -1, 1, possibleMoves, board);
            customMove(myPosition, myPosition, 1, -1, possibleMoves, board);
            customMove(myPosition, myPosition, 1, 1, possibleMoves, board);

            return possibleMoves;
        }
        else if(type == ChessPiece.PieceType.KNIGHT)
        {
            int[][] moveBy = {{-2,1},{2,1},{1,2},{-1,2},{-2,-1},{2,-1},{-1,-2},{1,-2}};
            return getChessMoves(board, myPosition, possibleMoves, moveBy);
        }
        else if(type == ChessPiece.PieceType.ROOK)
        {
            customMove(myPosition, myPosition, 1, 0, possibleMoves, board);
            customMove(myPosition, myPosition, -1, 0, possibleMoves, board);
            customMove(myPosition, myPosition, 0, 1, possibleMoves, board);
            customMove(myPosition, myPosition, 0, -1, possibleMoves, board);

            return possibleMoves;
        }
        else if(type == ChessPiece.PieceType.PAWN)
        {
            if(pieceColor == ChessGame.TeamColor.WHITE)
            {
                if(myPosition.getRow() == 2)
                {
                    ChessPosition onePosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                    if(board.getPiece(onePosition) == null)
                    {
                        ChessPosition updatedPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                        int newposrow = updatedPosition.getRow();
                        int newposcol = updatedPosition.getColumn();
                        int check = board.hasPiece(newposrow -1, newposcol -1, pieceColor);
                        if(check == -1 && newposrow > 0 && newposcol < 9)
                        {
                            promotePawn(myPosition, possibleMoves, updatedPosition);
                        }
                    }
                }
                int check = board.hasPiece(myPosition.getRow()-1 + 1, myPosition.getColumn()-1, pieceColor);

                if(check == -1 && myPosition.getRow() + 1 > 0 && myPosition.getRow() + 1 < 9)
                {
                    ChessPosition updatedPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                    promotePawn(myPosition, possibleMoves, updatedPosition);
                }
                int checklftdiag = board.hasPiece(myPosition.getRow()-1 + 1, myPosition.getColumn()-1-1, pieceColor);
                int checkrgtdiag = board.hasPiece(myPosition.getRow()-1 + 1, myPosition.getColumn()-1+1, pieceColor);

                if(checklftdiag == 1 && myPosition.getRow()+ 1 > 0 && myPosition.getRow() + 1 < 9 && myPosition.getColumn() - 1 > 0 && myPosition.getColumn() - 1 < 9)
                {
                    ChessPosition updatedPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()-1);
                    promotePawn(myPosition, possibleMoves, updatedPosition);
                }
                else if(checkrgtdiag == 1 && myPosition.getRow()+ 1 > 0 && myPosition.getRow() + 1 < 9 && myPosition.getColumn()+ 1 > 0 && myPosition.getColumn() + 1 < 9)
                {
                    ChessPosition updatedPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()+1);
                    promotePawn(myPosition, possibleMoves, updatedPosition);
                }
            }
            else
            {
                if(myPosition.getRow() == 7)
                {
                    ChessPosition onePosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    if(board.getPiece(onePosition) == null)
                    {
                        ChessPosition updatedPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                        int check = board.hasPiece(updatedPosition.getRow()-1, updatedPosition.getColumn()-1, pieceColor);
                        if(check == -1 && updatedPosition.getRow() > 0 && updatedPosition.getRow() < 9)
                        {
                            promotePawn(myPosition, possibleMoves, updatedPosition);
                        }
                    }
                }
                int check = board.hasPiece(myPosition.getRow()-1 - 1, myPosition.getColumn()-1, pieceColor);
                if(check == -1 && myPosition.getRow()- 1 > 0 && myPosition.getRow() - 1 < 9)
                {
                    ChessPosition updatedPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    promotePawn(myPosition, possibleMoves, updatedPosition);
                }
                int checklftdiag = board.hasPiece(myPosition.getRow() -1 - 1, myPosition.getColumn() -1 +1, pieceColor);
                int checkrgtdiag = board.hasPiece(myPosition.getRow() -1 - 1, myPosition.getColumn()-1 -1, pieceColor);

                if(checklftdiag == 1 && myPosition.getRow()- 1 > 0 && myPosition.getRow() - 1 < 9 && myPosition.getColumn()+ 1 > 0 && myPosition.getColumn() + 1 < 9)
                {
                    ChessPosition updatedPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()+1);
                    promotePawn(myPosition, possibleMoves, updatedPosition);
                }
                else if(checkrgtdiag == 1 && myPosition.getRow()- 1 > 0 && myPosition.getRow() + 1 < 9 && myPosition.getColumn()- 1 > 0 && myPosition.getColumn() - 1 < 9)
                {
                    ChessPosition updatedPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()-1);
                    promotePawn(myPosition, possibleMoves, updatedPosition);
                }
            }
        }
        return possibleMoves;
    }

    private void promotePawn(ChessPosition myPosition, ArrayList<ChessMove> possibleMoves, ChessPosition updatedPosition)
    {
        if(updatedPosition.getRow() == 8 || updatedPosition.getRow() == 1)
        {
            ChessMove move = new ChessMove(myPosition, updatedPosition, PieceType.KNIGHT);
            possibleMoves.add(move);
            move = new ChessMove(myPosition, updatedPosition, PieceType.ROOK);
            possibleMoves.add(move);
            move = new ChessMove(myPosition, updatedPosition, PieceType.BISHOP);
            possibleMoves.add(move);
            move = new ChessMove(myPosition, updatedPosition, PieceType.QUEEN);
            possibleMoves.add(move);
        }
        else
        {
            ChessMove move = new ChessMove(myPosition, updatedPosition, null);
            possibleMoves.add(move);
        }
    }
    //insert bracket here if breaks

    private ArrayList<ChessMove> getChessMoves(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> possibleMoves, int[][] moveBy)
    {
        for(int x = 0; x<8; x++)
        {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + moveBy[x][0], myPosition.getColumn() + moveBy[x][1]);
            int pieceType = board.hasPiece(newPosition.getRow()-1, newPosition.getColumn()-1, pieceColor);

            if(newPosition.getRow() > 0 && newPosition.getColumn() > 0 && newPosition.getRow() < 9 && newPosition.getColumn() < 9 && pieceType != 0 && myPosition != newPosition)
            {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                possibleMoves.add(move);
            }
        }
        return possibleMoves;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString()
    {
        return type.toString() + " " + pieceColor.toString();
    }
}
