/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.dynamos.image.lexer;

import java.io.*;
import org.dynamos.image.node.*;

@SuppressWarnings("nls")
public class Lexer
{
    protected Token token;
    protected State state = State.INITIAL;

    private PushbackReader in;
    private int line;
    private int pos;
    private boolean cr;
    private boolean eof;
    private final StringBuffer text = new StringBuffer();

    @SuppressWarnings("unused")
    protected void filter() throws LexerException, IOException
    {
        // Do nothing
    }

    public Lexer(@SuppressWarnings("hiding") PushbackReader in)
    {
        this.in = in;
    }
 
    public Token peek() throws LexerException, IOException
    {
        while(this.token == null)
        {
            this.token = getToken();
            filter();
        }

        return this.token;
    }

    public Token next() throws LexerException, IOException
    {
        while(this.token == null)
        {
            this.token = getToken();
            filter();
        }

        Token result = this.token;
        this.token = null;
        return result;
    }

    protected Token getToken() throws IOException, LexerException
    {
        int dfa_state = 0;

        int start_pos = this.pos;
        int start_line = this.line;

        int accept_state = -1;
        int accept_token = -1;
        int accept_length = -1;
        int accept_pos = -1;
        int accept_line = -1;

        @SuppressWarnings("hiding") int[][][] gotoTable = Lexer.gotoTable[this.state.id()];
        @SuppressWarnings("hiding") int[] accept = Lexer.accept[this.state.id()];
        this.text.setLength(0);

        while(true)
        {
            int c = getChar();

            if(c != -1)
            {
                switch(c)
                {
                case 10:
                    if(this.cr)
                    {
                        this.cr = false;
                    }
                    else
                    {
                        this.line++;
                        this.pos = 0;
                    }
                    break;
                case 13:
                    this.line++;
                    this.pos = 0;
                    this.cr = true;
                    break;
                default:
                    this.pos++;
                    this.cr = false;
                    break;
                }

                this.text.append((char) c);

                do
                {
                    int oldState = (dfa_state < -1) ? (-2 -dfa_state) : dfa_state;

                    dfa_state = -1;

                    int[][] tmp1 =  gotoTable[oldState];
                    int low = 0;
                    int high = tmp1.length - 1;

                    while(low <= high)
                    {
                        int middle = (low + high) / 2;
                        int[] tmp2 = tmp1[middle];

                        if(c < tmp2[0])
                        {
                            high = middle - 1;
                        }
                        else if(c > tmp2[1])
                        {
                            low = middle + 1;
                        }
                        else
                        {
                            dfa_state = tmp2[2];
                            break;
                        }
                    }
                }while(dfa_state < -1);
            }
            else
            {
                dfa_state = -1;
            }

            if(dfa_state >= 0)
            {
                if(accept[dfa_state] != -1)
                {
                    accept_state = dfa_state;
                    accept_token = accept[dfa_state];
                    accept_length = this.text.length();
                    accept_pos = this.pos;
                    accept_line = this.line;
                }
            }
            else
            {
                if(accept_state != -1)
                {
                    switch(accept_token)
                    {
                    case 0:
                        {
                            @SuppressWarnings("hiding") Token token = new0(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 1:
                        {
                            @SuppressWarnings("hiding") Token token = new1(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 2:
                        {
                            @SuppressWarnings("hiding") Token token = new2(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 3:
                        {
                            @SuppressWarnings("hiding") Token token = new3(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 4:
                        {
                            @SuppressWarnings("hiding") Token token = new4(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 5:
                        {
                            @SuppressWarnings("hiding") Token token = new5(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 6:
                        {
                            @SuppressWarnings("hiding") Token token = new6(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 7:
                        {
                            @SuppressWarnings("hiding") Token token = new7(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 8:
                        {
                            @SuppressWarnings("hiding") Token token = new8(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 9:
                        {
                            @SuppressWarnings("hiding") Token token = new9(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 10:
                        {
                            @SuppressWarnings("hiding") Token token = new10(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    }
                }
                else
                {
                    if(this.text.length() > 0)
                    {
                        throw new LexerException(
                            "[" + (start_line + 1) + "," + (start_pos + 1) + "]" +
                            " Unknown token: " + this.text);
                    }

                    @SuppressWarnings("hiding") EOF token = new EOF(
                        start_line + 1,
                        start_pos + 1);
                    return token;
                }
            }
        }
    }

    Token new0(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TColon(line, pos); }
    Token new1(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TMnemonic(text, line, pos); }
    Token new2(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TNumber(text, line, pos); }
    Token new3(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TId(text, line, pos); }
    Token new4(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TOpenBrace(line, pos); }
    Token new5(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TCloseBrace(line, pos); }
    Token new6(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TFullstop(line, pos); }
    Token new7(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TDollar(line, pos); }
    Token new8(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new THash(line, pos); }
    Token new9(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TEquals(line, pos); }
    Token new10(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TBlank(text, line, pos); }

    private int getChar() throws IOException
    {
        if(this.eof)
        {
            return -1;
        }

        int result = this.in.read();

        if(result == -1)
        {
            this.eof = true;
        }

        return result;
    }

    private void pushBack(int acceptLength) throws IOException
    {
        int length = this.text.length();
        for(int i = length - 1; i >= acceptLength; i--)
        {
            this.eof = false;

            this.in.unread(this.text.charAt(i));
        }
    }

    protected void unread(@SuppressWarnings("hiding") Token token) throws IOException
    {
        @SuppressWarnings("hiding") String text = token.getText();
        int length = text.length();

        for(int i = length - 1; i >= 0; i--)
        {
            this.eof = false;

            this.in.unread(text.charAt(i));
        }

        this.pos = token.getPos() - 1;
        this.line = token.getLine() - 1;
    }

    private String getText(int acceptLength)
    {
        StringBuffer s = new StringBuffer(acceptLength);
        for(int i = 0; i < acceptLength; i++)
        {
            s.append(this.text.charAt(i));
        }

        return s.toString();
    }

    private static int[][][][] gotoTable;
/*  {
        { // INITIAL
            {{10, 10, 1}, {13, 13, 2}, {32, 32, 3}, {35, 35, 4}, {36, 36, 5}, {46, 46, 6}, {48, 57, 7}, {58, 58, 8}, {61, 61, 9}, {65, 66, 10}, {67, 67, 11}, {68, 68, 10}, {69, 69, 12}, {70, 78, 10}, {79, 79, 13}, {80, 80, 14}, {81, 82, 10}, {83, 83, 15}, {84, 85, 10}, {86, 86, 16}, {87, 90, 10}, {95, 95, 10}, {97, 122, 10}, {123, 123, 17}, {125, 125, 18}, },
            {{10, 32, -2}, },
            {{10, 32, -2}, },
            {{10, 32, -2}, },
            {},
            {},
            {},
            {{48, 57, 19}, {58, 58, 20}, {65, 90, 21}, {95, 95, 21}, {97, 122, 21}, },
            {},
            {},
            {{48, 57, 21}, {58, 122, -9}, },
            {{48, 58, -12}, {65, 65, 22}, {66, 90, 21}, {95, 122, -9}, },
            {{48, 58, -12}, {65, 77, 21}, {78, 78, 23}, {79, 90, 21}, {95, 122, -9}, },
            {{48, 58, -12}, {65, 65, 21}, {66, 66, 24}, {67, 90, 21}, {95, 122, -9}, },
            {{48, 58, -12}, {65, 84, 21}, {85, 85, 25}, {86, 90, 21}, {95, 122, -9}, },
            {{48, 58, -12}, {65, 83, 21}, {84, 84, 26}, {85, 88, 21}, {89, 89, 27}, {90, 90, 21}, {95, 122, -9}, },
            {{48, 58, -12}, {65, 65, 28}, {66, 122, -13}, },
            {},
            {},
            {{48, 122, -9}, },
            {{48, 122, -12}, },
            {{48, 122, -12}, },
            {{48, 58, -12}, {65, 75, 21}, {76, 76, 29}, {77, 90, 21}, {95, 122, -9}, },
            {{48, 58, -12}, {65, 67, 21}, {68, 68, 30}, {69, 90, 21}, {95, 122, -9}, },
            {{48, 58, -12}, {65, 73, 21}, {74, 74, 31}, {75, 90, 21}, {95, 122, -9}, },
            {{48, 58, -12}, {65, 82, 21}, {83, 83, 32}, {84, 90, 21}, {95, 122, -9}, },
            {{48, 58, -12}, {65, 65, 33}, {66, 122, -13}, },
            {{48, 58, -12}, {65, 76, 21}, {77, 77, 34}, {78, 90, 21}, {95, 122, -9}, },
            {{48, 75, -24}, {76, 76, 35}, {77, 122, -24}, },
            {{48, 75, -24}, {76, 76, 36}, {77, 122, -24}, },
            {{48, 122, -12}, },
            {{48, 122, -12}, },
            {{48, 58, -12}, {65, 71, 21}, {72, 72, 37}, {73, 90, 21}, {95, 122, -9}, },
            {{48, 58, -12}, {65, 81, 21}, {82, 82, 38}, {83, 90, 21}, {95, 122, -9}, },
            {{48, 122, -12}, },
            {{48, 122, -12}, },
            {{48, 122, -12}, },
            {{48, 122, -12}, },
            {{48, 83, -17}, {84, 84, 39}, {85, 90, 21}, {95, 122, -9}, },
            {{48, 122, -12}, },
        }
    };*/

    private static int[][] accept;
/*  {
        // INITIAL
        {-1, 10, 10, 10, 8, 7, 6, 2, 0, 9, 3, 3, 3, 3, 3, 3, 3, 4, 5, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 3, 3, 1, 1, 1, 1, 3, 1, },

    };*/

    public static class State
    {
        public final static State INITIAL = new State(0);

        private int id;

        private State(@SuppressWarnings("hiding") int id)
        {
            this.id = id;
        }

        public int id()
        {
            return this.id;
        }
    }

    static 
    {
        try
        {
            DataInputStream s = new DataInputStream(
                new BufferedInputStream(
                Lexer.class.getResourceAsStream("lexer.dat")));

            // read gotoTable
            int length = s.readInt();
            gotoTable = new int[length][][][];
            for(int i = 0; i < gotoTable.length; i++)
            {
                length = s.readInt();
                gotoTable[i] = new int[length][][];
                for(int j = 0; j < gotoTable[i].length; j++)
                {
                    length = s.readInt();
                    gotoTable[i][j] = new int[length][3];
                    for(int k = 0; k < gotoTable[i][j].length; k++)
                    {
                        for(int l = 0; l < 3; l++)
                        {
                            gotoTable[i][j][k][l] = s.readInt();
                        }
                    }
                }
            }

            // read accept
            length = s.readInt();
            accept = new int[length][];
            for(int i = 0; i < accept.length; i++)
            {
                length = s.readInt();
                accept[i] = new int[length];
                for(int j = 0; j < accept[i].length; j++)
                {
                    accept[i][j] = s.readInt();
                }
            }

            s.close();
        }
        catch(Exception e)
        {
            throw new RuntimeException("The file \"lexer.dat\" is either missing or corrupted.");
        }
    }
}
