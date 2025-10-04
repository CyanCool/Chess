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
        ChessPiece myPiece = board.getPiece(startPosition); //get the piece to check its moves
        if(myPiece == null)
        {
            return null;
        }
        ArrayList<ChessMove> possibleMoves = (ArrayList<ChessMove>) myPiece.pieceMoves(board, startPosition);//get all of the piece's possible moves


        for(int x = 0; x < possibleMoves.size(); x++)
        {
            ChessMove newMove = possibleMoves.get(x);
            hboard = tryMove(myPiece, newMove); //make that move hypothetically and see if it puts the king in check

            if(isInCheck(myPiece.getTeamColor()))
            {
                possibleMoves.remove(x);
                x--;
            }
        }
        hboard = (ChessBoard) board.cloneBoard();
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
        if(validMoves == null)
        {
            throw new InvalidMoveException("That move is not allowed!");
        }
        for(int i = 0; i < validMoves.size(); i++)
        {
            if(validMoves.get(i).equals(move)) //checks to see if the move we are making is a valid move
            {
                check = true;
            }
        }
        ChessPiece myPiece = board.getPiece(move.getStartPosition());
        TeamColor myTeam = myPiece.getTeamColor();

        if(check && (myPiece.getTeamColor() == getTeamTurn()))
        {
            ChessPiece.PieceType toPromote = move.getPromotionPiece();
            if(toPromote != null)
            {
                myPiece = new ChessPiece(myTeam, toPromote);
            }
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

    //checks if the current piece can eat the king
    public boolean checkCurrent(ChessPosition myPosition, ChessPosition kingPosition)
    {
        ArrayList<ChessMove> myMoves = new ArrayList<ChessMove>();

        ChessPiece myPiece = hboard.getPiece(myPosition);
        ChessPiece kingPiece = hboard.getPiece(kingPosition);

        if(myPiece != null && kingPiece != null)
        {
            myMoves = (ArrayList<ChessMove>) myPiece.pieceMoves(hboard, myPosition);
            for(int x = 0; x < myMoves.size(); x++)
            {

                if(myMoves.get(x).getEndPosition().equals(kingPosition) && (myPiece.getTeamColor() != kingPiece.getTeamColor()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    //checks if the other pieces can eat the king
    public boolean checkOthers(ArrayList<ChessMove> pieceMoves, ChessPosition myPosition, ChessPosition kingPosition)
    {
        //iterate through all of the moved pieces
        boolean check = false;
        boolean check2 = false;
        ChessPiece myPiece = hboard.getPiece(myPosition);

        if(myPiece != null)
        {
            for(int i = 0; i < pieceMoves.size(); i++)
            {
                hboard = tryMove(myPiece, pieceMoves.get(i));//set the board to reflect the moved piece

                for(int x = 1; x<9; x++)//check all the pieces in the board to see if they can eat the king
                {
                    for(int y = 1; y<9; y++)
                    {
                        ChessPosition currentPosition = new ChessPosition(x,y);
                        check = checkCurrent(currentPosition, kingPosition);//check current piece in the board to see if it can eat the king now
                    }
                }
                if(check)
                {
                    i--;
                }
            }
        }
        return check;
    }

    public boolean isInCheck(TeamColor teamColor)
    {
        ArrayList<ChessMove> pieceMoveOptions = new ArrayList<>();
        ChessPosition currentKing = findKing(teamColor);

        boolean cc = false;
        boolean co = false;

        for(int x = 1; x<9; x++)
        {
            for(int y = 1; y<9; y++)
            {
                ChessPosition myPosition = new ChessPosition(x,y);
                ChessPiece myPiece = hboard.getPiece(myPosition);

                if(myPiece != null)
                {
                    cc = checkCurrent(myPosition, currentKing);
                    //check if current piece can move to eat the king

//                    ArrayList<ChessMove> currentMoves = (ArrayList<ChessMove>) myPiece.pieceMoves(hboard, myPosition);
//
//                    co = checkOthers(currentMoves, myPosition, currentKing); //check if the current piece moves, other pieces can eat the king
                    if(cc)
                    {
                        return true;
                    }
                }
            }
        }
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
        //boolean check = isInCheck(teamColor);
        if(!isInCheck(teamColor))
        {
            return false;
        }
        else
        {
            for(int x = 1; x<9; x++)
            {
                for(int y = 1; y<9; y++)
                {
                    ChessPosition mypos = new ChessPosition(x,y);
                    ArrayList<ChessMove> possibleMoves = (ArrayList<ChessMove>) validMoves(mypos);
                    if(possibleMoves == null)
                    {
                        return true;
                    }
                    else if(!possibleMoves.isEmpty())
                    {
                        return false;
                    }
                }
            }
        }
        return true;
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
        if(checkCurrent)
        {
            return false;
        }
        else
        {
            for(int x = 1; x<9; x++)
            {
                for(int y = 1; y<9; y++)
                {
                    ChessPosition mypos = new ChessPosition(x, y);
                    ArrayList<ChessMove> possibleMoves = (ArrayList<ChessMove>) validMoves(mypos);
                    ChessPiece myPiece = board.getPiece(mypos);
                    if (myPiece != null && myPiece.getTeamColor() == teamColor)
                    {
                        if (!possibleMoves.isEmpty())
                        {
                            return false;
                        }
                    }
                }
            }
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
        hboard = (ChessBoard) board.cloneBoard();
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
