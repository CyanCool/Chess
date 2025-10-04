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
    public void makeMove(ChessMove move) throws InvalidMoveException
    {
        ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) validMoves(move.getStartPosition());
        boolean check = false;
        for(int i = 0; i < validMoves.size(); i++)
        {
            if(validMoves.get(i).equals(move))
            {
                check = true;
            }
        }
        ChessPiece myPiece = board.getPiece(move.getStartPosition());
        if(check && (myPiece.getTeamColor() == getTeamTurn()))
        {
            board = tryMove(myPiece, move);
            if(getTeamTurn() == TeamColor.WHITE)
            {
                setTeamTurn(TeamColor.BLACK);
            }
            else if(getTeamTurn() == TeamColor.BLACK)
            {
                setTeamTurn(TeamColor.WHITE);
            }
        }
        else
        {
            throw new InvalidMoveException("That move is not allowed!");
        }

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
        ChessPosition currentKing = findKing(teamColor);

        for(int x = 1; x < 9; x++)
        {
            for(int y = 1; y < 9; y++)
            {
                ChessPosition myPosition = new ChessPosition(x,y);
                ChessPiece myPiece = hboard.getPiece(myPosition); //gets my current piece in the current position


                if(myPiece != null && currentKing != null) //makes sure there is a piece
                {
                    if(myPiece.getTeamColor() != teamColor) //is the current piece on the opposite team, therefore capable of eating the king?
                    {
                        return seeIfEatsKing(myPosition, currentKing); //checks current piece if eats king
                    }
                    //check to see if moving the current piece will cause another piece to eat the king
                    pieceMoveOptions = (ArrayList<ChessMove>) myPiece.pieceMoves(hboard, myPosition); //gets all the positions it can move to

                    for(int i = 0; i < pieceMoveOptions.size(); i++)
                    {
                        //move the piece
                        hboard = tryMove(myPiece, pieceMoveOptions.get(i));

                        for(int k = 1; k < 9; k++)
                        {
                            for(int p = 1; p < 9; p++)
                            {
                                ChessPosition hypPosition = new ChessPosition(k,p);
                                ChessPiece hypePiece = hboard.getPiece(myPosition); //gets my current piece in the current position

                                if(hypePiece != null && currentKing != null)
                                {
                                    if(hypePiece.getTeamColor() != teamColor) //is the current piece on the opposite team, therefore capable of eating the king?
                                    {
                                        return seeIfEatsKing(hypPosition, currentKing); //checks current piece if eats king
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        hboard = board;
        return false;
    }

    public boolean seeIfEatsKing(ChessPosition myPosition, ChessPosition kingPosition) //gets my current piece and sees if any future positions will eat the king
    {
        ArrayList<ChessMove> myMoves = new ArrayList<>();

        ChessPiece myPiece = hboard.getPiece(myPosition);

        if(myPiece != null)
        {
            myMoves = (ArrayList<ChessMove>) myPiece.pieceMoves(hboard, myPosition);
            for(int x = 0; x < myMoves.size(); x++)
            {
                if(myMoves.get(x).getEndPosition().equals(kingPosition))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public ChessPosition findKing(TeamColor teamColor)
    {
        for(int x = 1; x< 9; x++)
        {
            for(int y = 1; y < 9; y++)
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

    public boolean checkAround(TeamColor teamColor)
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
    public boolean isInCheckmate(TeamColor teamColor)
    {
        boolean checkCurrent = isInCheck(teamColor);
        boolean checkElse = checkAround(teamColor);
        if(checkCurrent && checkElse)
        {
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor)
    {
        boolean checkCurrent = isInCheck(teamColor);
        boolean checkElse = checkAround(teamColor);
        if(!checkCurrent && checkElse)
        {
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board)
    {
        this.board = board;
        hboard = board;
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
