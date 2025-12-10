package ui;

import chess.ChessGame;
import chess.ChessPiece;

import static tools.EscapeSequences.*;

public class Gameplay
{
   // private ChessGame myGame;
    public Gameplay()
    {
    }

    public void printBoardWhite()
    {
        System.out.print(RESET_BG_COLOR);
        ChessPiece[][] defaultBoard = defaultBoard();

        for(int y = 0; y<10; y++)
        {
            if(y == 0 || y == 9)
            {
                alphabetRow();
            }
            for(int x = 0; x<10; x++)
            {
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
                        //Now printing the pieces
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

            }
            System.out.println(RESET_BG_COLOR);
        }
        System.out.print(RESET_BG_COLOR);
    }

    public void alphabetRow()
    {
        //System.out.print(UNICODE_ESCAPE);
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        String[] row = {EMPTY,UNICODE_A,UNICODE_B,UNICODE_C,UNICODE_D,UNICODE_E,UNICODE_F,UNICODE_G, UNICODE_H,EMPTY};
        for(int i = 0; i<row.length; i++)
        {
            System.out.print(row[i]);
        }
    }

    public void numberColumn(int x)
    {
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        String[] column = {UNICODE_8, UNICODE_7,UNICODE_6,UNICODE_5,UNICODE_4,UNICODE_3,UNICODE_2,UNICODE_1};
        System.out.print(column[x]);
    }

    public ChessPiece[][] defaultBoard()
    {
        ChessPiece[][] squares = new ChessPiece[8][8];

        for(int x = 0; x < 8; x++)
        {
            squares[1][x] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            squares[6][x] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
        squares[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        squares[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        squares[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        squares[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);

        squares[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        squares[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        squares[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        squares[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

        return squares;
    }
}
