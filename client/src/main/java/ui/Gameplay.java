package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import static tools.EscapeSequences.*;

public class Gameplay
{
    private ChessBoard myBoard;
    private ChessPiece[][] defaultBoard;

    public Gameplay()
    {
        myBoard = new ChessBoard();
        myBoard.resetBoard();
        defaultBoard = myBoard.getSquares();
    }

    public void printBoard(boolean isWhite)
    {
        System.out.print(RESET_BG_COLOR);

        for(int y = isWhite ? 0 : 9; isWhite ? y < 10 : y > -1; y+= isWhite ? 1 : -1)
        {
            for(int x = isWhite ? 0 : 9; isWhite ? x < 10 : x > -1; x+= isWhite ? 1 : -1)
            {
                if(y == 0 || y == 9)
                {
                    alphabetRow(x);
                }
                if(y > 0 && y < 9)
                {
                    //moveCursorToLocation(i,j);
                    if(x == 0 || x == 9)
                    {
                        numberColumn(y-1);
                    }
                    else
                    {
                        //this alternates between even and odd squares to checker the board
                        printSquares(x, y);
                        //Now printing the pieces
                        printPieces(x,y);
                    }

                }

            }
            System.out.println(RESET_BG_COLOR);
        }
        System.out.print(RESET_BG_COLOR);
        System.out.print(RESET_TEXT_COLOR);
    }


    public void alphabetRow(int y)
    {
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        String[] row = {EMPTY,UNICODE_A,UNICODE_B,UNICODE_C,UNICODE_D,UNICODE_E,UNICODE_F,UNICODE_G, UNICODE_H,EMPTY};
        System.out.print(row[y]);
    }

    public void numberColumn(int x)
    {
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        String[] column = {UNICODE_8, UNICODE_7,UNICODE_6,UNICODE_5,UNICODE_4,UNICODE_3,UNICODE_2,UNICODE_1};
        System.out.print(column[x]);
    }

    public void printSquares(int x, int y)
    {
        if(x % 2 == 0)
        {
            if(y % 2 == 0)
            {
                System.out.print(SET_BG_COLOR_WHITE);
            }
            else
            {
                System.out.print(SET_BG_COLOR_BLACK);
            }
        }
        else
        {
            if(y%2 == 0)
            {
                System.out.print(SET_BG_COLOR_BLACK);
            }
            else
            {
                System.out.print(SET_BG_COLOR_WHITE);
            }

        }
    }

    public void printPieces(int x, int y)
    {
        if(!(y > 2 && y < 7))
        {
            String color = "";
            String piece = "";
            if(defaultBoard[y-1][x-1].getTeamColor() == ChessGame.TeamColor.WHITE)
            {
                color = SET_TEXT_COLOR_BLUE;

            }
            else
            {
                color = SET_TEXT_COLOR_RED;
            }
            switch(defaultBoard[y-1][x-1].getPieceType())
            {
                case ROOK:
                    piece = BLACK_ROOK;
                    break;
                case BISHOP:
                    piece = BLACK_BISHOP;
                    break;
                case KNIGHT:
                    piece = BLACK_KNIGHT;
                    break;
                case KING:
                    piece = BLACK_KING;
                    break;
                case QUEEN:
                    piece = BLACK_QUEEN;
                    break;
                case PAWN:
                    piece = BLACK_PAWN;
                    break;
                default:
                    System.out.println("This piece cannot be found");
                    break;
            }
            System.out.print(color + piece);
        }
        else
        {
            System.out.print(EMPTY);
        }
    }
}
