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

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn()
    {
        return teamColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team)
    {
        teamColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition)
    {
        ChessPiece myPiece = board.getPiece(startPosition);
        ArrayList<ChessMove> possibleMoves = (ArrayList<ChessMove>) myPiece.pieceMoves(board, startPosition);

        for(int x = 0; x < possibleMoves.size(); x++)
        {
            ChessMove newMove = possibleMoves.get(x);
            hboard = tryMove(myPiece, newMove);

            if(isInCheck(myPiece.getTeamColor()))
            {
                possibleMoves.remove(x); //idk why suspicious
            }
        }
        hboard = board;
        return possibleMoves;
    }

    public ChessBoard tryMove(ChessPiece piece, ChessMove move)
    {
        ChessBoard currentBoard = board.cloneBoard();
        currentBoard.addPiece(move.getEndPosition(), piece);
        currentBoard.removePiece(move.getStartPosition());

        return currentBoard;

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");

        //if the piece is a king, update the kings position based on its color when its finished

        //if the move doesnt put the king in check
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor)
    {
        ArrayList<ChessMove> pieceMoveOptions = new ArrayList<>();
        if(teamColor == TeamColor.BLACK)
        {
            for(int x = 0; x < 8; x++)
            {
                for(int y = 0; y < 8; y++)
                {
                    ChessPosition myPosition = new ChessPosition(x,y);
                    ChessPiece myPiece = hboard.getPiece(myPosition);

                    ChessPosition currentKing = findKing(teamColor);

                    if(myPiece != null && currentKing != null && (myPiece.getTeamColor() == TeamColor.WHITE))
                    {
                        pieceMoveOptions = (ArrayList<ChessMove>) myPiece.pieceMoves(hboard, myPosition);

                        for(int i = 0; i < pieceMoveOptions.size(); i++)
                        {
                            if(pieceMoveOptions.get(i).getEndPosition().equals(currentKing))
                            {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
        else if(teamColor == TeamColor.WHITE)
        {
            for(int x = 0; x < 8; x++)
            {
                for(int y = 0; y < 8; y++)
                {
                    ChessPosition myPosition = new ChessPosition(x,y);
                    ChessPiece myPiece = hboard.getPiece(myPosition);

                    ChessPosition currentKing = findKing(teamColor);

                    if(myPiece != null && currentKing != null && (myPiece.getTeamColor() == TeamColor.BLACK))
                    {
                        pieceMoveOptions = (ArrayList<ChessMove>) myPiece.pieceMoves(hboard, myPosition);

                        for(int i = 0; i < pieceMoveOptions.size(); i++)
                        {
                            if(pieceMoveOptions.get(i).getEndPosition().equals(currentKing))
                            {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    public ChessPosition findKing(TeamColor teamColor)
    {
        for(int x = 0; x< 8; x++)
        {
            for(int y = 0; y < 8; y++)
            {
                ChessPosition myPosition = new ChessPosition(x,y);
                ChessPiece myPiece = hboard.getPiece(myPosition);

                if(myPiece != null && myPiece.getPieceType() == ChessPiece.PieceType.KING && myPiece.getTeamColor() == teamColor)
                {
                    return myPosition;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor)
    {
        ChessPosition kingPos = findKing(teamColor);
        ChessPiece myKing = board.getPiece(kingPos);
        ArrayList<ChessMove> kingMoves = (ArrayList<ChessMove>) myKing.pieceMoves(board, kingPos);
        boolean isCheck = true;

        for(int x = 0; x<kingMoves.size(); x++)
        {
            ChessMove myMove = kingMoves.get(x);
            hboard = tryMove(myKing, myMove);
            boolean check = isInCheck(myKing.getTeamColor());
            if(!check)
            {
                isCheck = false;
            }
        }

        return isCheck;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board)
    {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard()
    {
        return board;
    }
}
