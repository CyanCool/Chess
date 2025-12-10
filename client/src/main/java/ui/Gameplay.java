package ui;

import static tools.EscapeSequences.*;

public class Gameplay
{
    public Gameplay(){}

    public void printBoardWhite()
    {
        System.out.print(RESET_BG_COLOR);
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
                    System.out.print(SET_BG_COLOR_LIGHT_GREY);
                    System.out.print(WHITE_QUEEN);
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
            if(i > 1 && i < 9)
            {
                //System.out.print(EMPTY);
            }
            System.out.print(row[i]);
        }
    }
}
