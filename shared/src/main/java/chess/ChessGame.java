package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame
{
    private TeamColor teamColor;
    private ChessBoard board;
    private ChessBoard hboard;

    public ChessGame()
    {
        teamColor = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
        hboard = (ChessBoard) board.cloneBoard();
    }

    public TeamColor getTeamTurn()
    {
        return teamColor;


    }

    public void setTeamTurn(TeamColor team)
    {
        teamColor = team;




    }

    public enum TeamColor
    {


        WHITE,
        BLACK
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition)
    {
        ChessPiece myPiece = board.getPiece(startPosition);
        if (myPiece == null)
        {
            return null;
        }

        ArrayList<ChessMove> possibleMoves = new ArrayList<>(myPiece.pieceMoves(board, startPosition));
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        for (ChessMove move : possibleMoves)
        {
            ChessBoard tempBoard = tryMoveOnBoard(board, myPiece, move);
            if (!isInCheckSimulated(tempBoard, myPiece.getTeamColor()))
            {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    public ChessBoard tryMove(ChessPiece piece, ChessMove move)
    {
        ChessBoard clone = board.cloneBoard();
        clone.removePiece(move.getStartPosition());
        ChessPiece newPiece = piece;
        if (move.getPromotionPiece() != null)
        {
            newPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }
        clone.addPiece(move.getEndPosition(), newPiece);
        return clone;
    }

    private ChessBoard tryMoveOnBoard(ChessBoard baseBoard, ChessPiece piece, ChessMove move)
    {
        ChessBoard clone = baseBoard.cloneBoard();
        clone.removePiece(move.getStartPosition());
        ChessPiece newPiece = piece;
        if (move.getPromotionPiece() != null)
        {
            newPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }
        clone.addPiece(move.getEndPosition(), newPiece);
        return clone;
    }

    private boolean isInCheckSimulated(ChessBoard testBoard, TeamColor teamColor)
    {
        ChessPosition kingPos = findKingOnBoard(testBoard, teamColor);
        for (int x = 1; x <= 8; x++)
        {
            for (int y = 1; y <= 8; y++)
            {
                ChessPosition pos = new ChessPosition(x, y);
                ChessPiece piece = testBoard.getPiece(pos);
                if (piece != null && piece.getTeamColor() != teamColor)
                {
                    for (ChessMove m : piece.pieceMoves(testBoard, pos))
                    {
                        if (m.getEndPosition().equals(kingPos))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private ChessPosition findKingOnBoard(ChessBoard testBoard, TeamColor teamColor)
    {
        for (int x = 1; x <= 8; x++)
        {
            for (int y = 1; y <= 8; y++)
            {
                ChessPosition pos = new ChessPosition(x, y);
                ChessPiece piece = testBoard.getPiece(pos);
                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING)
                {
                    return pos;
                }
            }
        }
        return null;
    }

    public void makeMove(ChessMove move) throws InvalidMoveException
    {
        ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) validMoves(move.getStartPosition());
        boolean check = false;
        if (validMoves == null)
        {
            throw new InvalidMoveException("That move is not allowed!");
        }
        for (int i = 0; i < validMoves.size(); i++)
        {
            if (validMoves.get(i).equals(move))
            {
                check = true;
            }
        }
        ChessPiece myPiece = board.getPiece(move.getStartPosition());
        TeamColor myTeam = myPiece.getTeamColor();

        if (check && (myPiece.getTeamColor() == getTeamTurn()))
        {
            ChessPiece.PieceType toPromote = move.getPromotionPiece();
            if (toPromote != null)
            {
                myPiece = new ChessPiece(myTeam, toPromote);
            }
            board = tryMove(myPiece, move);
            if (getTeamTurn() == TeamColor.WHITE)
            {
                setTeamTurn(TeamColor.BLACK);
            } else if (getTeamTurn() == TeamColor.BLACK)
            {
                setTeamTurn(TeamColor.WHITE);
            }
        } else
        {
            throw new InvalidMoveException("That move is not allowed!");
        }
    }

    public boolean isInCheck(TeamColor teamColor)
    {
        ChessPosition currentKing = findKing(teamColor);
        for (int x = 1; x <= 8; x++)
        {
            for (int y = 1; y <= 8; y++)
            {
                ChessPosition myPosition = new ChessPosition(x, y);
                ChessPiece myPiece = board.getPiece(myPosition);
                if (myPiece != null && myPiece.getTeamColor() != teamColor)
                {
                    ArrayList<ChessMove> myMoves = new ArrayList<>(myPiece.pieceMoves(board, myPosition));
                    for (ChessMove m : myMoves)
                    {
                        if (m.getEndPosition().equals(currentKing))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public ChessPosition findKing(TeamColor teamColor)
    {
        for (int x = 1; x <= 8; x++)
        {
            for (int y = 1; y <= 8; y++)
            {
                ChessPosition myPosition = new ChessPosition(x, y);
                ChessPiece myPiece = board.getPiece(myPosition);
                if (myPiece != null && myPiece.getPieceType() == ChessPiece.PieceType.KING && myPiece.getTeamColor() == teamColor)
                {
                    return myPosition;
                }
            }
        }
        return null;
    }

    public boolean isInCheckmate(TeamColor teamColor)
    {
        if (!isInCheck(teamColor))
        {
            return false;
        }
        for (int x = 1; x <= 8; x++)
        {
            for (int y = 1; y <= 8; y++)
            {
                ChessPosition mypos = new ChessPosition(x, y);
                ChessPiece myPiece = board.getPiece(mypos);
                if (myPiece != null && myPiece.getTeamColor() == teamColor)
                {
                    ArrayList<ChessMove> possibleMoves = (ArrayList<ChessMove>) validMoves(mypos);
                    if (possibleMoves != null && !possibleMoves.isEmpty())
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isInStalemate(TeamColor teamColor)
    {
        if (isInCheck(teamColor))
        {
            return false;
        }
        for (int x = 1; x <= 8; x++)
        {
            for (int y = 1; y <= 8; y++)
            {
                ChessPosition mypos = new ChessPosition(x, y);
                ChessPiece myPiece = board.getPiece(mypos);
                ArrayList<ChessMove> possibleMoves = (ArrayList<ChessMove>) validMoves(mypos);
                if (myPiece != null && myPiece.getTeamColor() == teamColor && !possibleMoves.isEmpty())
                {
                    return false;
                }
            }
        }
        return true;
    }

    public void setBoard(ChessBoard board)
    {
        this.board = board;
        hboard = (ChessBoard) board.cloneBoard();
    }

    public ChessBoard getBoard()
    {
        return board;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamColor == chessGame.teamColor && Objects.equals(board, chessGame.board) && Objects.equals(hboard, chessGame.hboard);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(teamColor, board, hboard);
    }
}