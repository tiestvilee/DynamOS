/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.dynamos.image.parser;

import org.dynamos.image.lexer.*;
import org.dynamos.image.node.*;
import org.dynamos.image.analysis.*;
import java.util.*;

import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

@SuppressWarnings("nls")
public class Parser
{
    public final Analysis ignoredTokens = new AnalysisAdapter();

    protected ArrayList nodeList;

    private final Lexer lexer;
    private final ListIterator stack = new LinkedList().listIterator();
    private int last_pos;
    private int last_line;
    private Token last_token;
    private final TokenIndex converter = new TokenIndex();
    private final int[] action = new int[2];

    private final static int SHIFT = 0;
    private final static int REDUCE = 1;
    private final static int ACCEPT = 2;
    private final static int ERROR = 3;

    public Parser(@SuppressWarnings("hiding") Lexer lexer)
    {
        this.lexer = lexer;
    }

    protected void filter() throws ParserException, LexerException, IOException
    {
        // Empty body
    }

    private void push(int numstate, ArrayList listNode, boolean hidden) throws ParserException, LexerException, IOException
    {
        this.nodeList = listNode;

        if(!hidden)
        {
            filter();
        }

        if(!this.stack.hasNext())
        {
            this.stack.add(new State(numstate, this.nodeList));
            return;
        }

        State s = (State) this.stack.next();
        s.state = numstate;
        s.nodes = this.nodeList;
    }

    private int goTo(int index)
    {
        int state = state();
        int low = 1;
        int high = gotoTable[index].length - 1;
        int value = gotoTable[index][0][1];

        while(low <= high)
        {
            int middle = (low + high) / 2;

            if(state < gotoTable[index][middle][0])
            {
                high = middle - 1;
            }
            else if(state > gotoTable[index][middle][0])
            {
                low = middle + 1;
            }
            else
            {
                value = gotoTable[index][middle][1];
                break;
            }
        }

        return value;
    }

    private int state()
    {
        State s = (State) this.stack.previous();
        this.stack.next();
        return s.state;
    }

    private ArrayList pop()
    {
        return ((State) this.stack.previous()).nodes;
    }

    private int index(Switchable token)
    {
        this.converter.index = -1;
        token.apply(this.converter);
        return this.converter.index;
    }

    @SuppressWarnings("unchecked")
    public Start parse() throws ParserException, LexerException, IOException
    {
        push(0, null, true);
        List<Node> ign = null;
        while(true)
        {
            while(index(this.lexer.peek()) == -1)
            {
                if(ign == null)
                {
                    ign = new LinkedList<Node>();
                }

                ign.add(this.lexer.next());
            }

            if(ign != null)
            {
                this.ignoredTokens.setIn(this.lexer.peek(), ign);
                ign = null;
            }

            this.last_pos = this.lexer.peek().getPos();
            this.last_line = this.lexer.peek().getLine();
            this.last_token = this.lexer.peek();

            int index = index(this.lexer.peek());
            this.action[0] = Parser.actionTable[state()][0][1];
            this.action[1] = Parser.actionTable[state()][0][2];

            int low = 1;
            int high = Parser.actionTable[state()].length - 1;

            while(low <= high)
            {
                int middle = (low + high) / 2;

                if(index < Parser.actionTable[state()][middle][0])
                {
                    high = middle - 1;
                }
                else if(index > Parser.actionTable[state()][middle][0])
                {
                    low = middle + 1;
                }
                else
                {
                    this.action[0] = Parser.actionTable[state()][middle][1];
                    this.action[1] = Parser.actionTable[state()][middle][2];
                    break;
                }
            }

            switch(this.action[0])
            {
                case SHIFT:
		    {
		        ArrayList list = new ArrayList();
		        list.add(this.lexer.next());
                        push(this.action[1], list, false);
                    }
		    break;
                case REDUCE:
                    switch(this.action[1])
                    {
                    case 0: /* reduce AAimage1Image */
		    {
			ArrayList list = new0();
			push(goTo(0), list, false);
		    }
		    break;
                    case 1: /* reduce AAimage2Image */
		    {
			ArrayList list = new1();
			push(goTo(0), list, false);
		    }
		    break;
                    case 2: /* reduce ASlotObjectDefinition */
		    {
			ArrayList list = new2();
			push(goTo(1), list, false);
		    }
		    break;
                    case 3: /* reduce AReferenceObjectDefinition */
		    {
			ArrayList list = new3();
			push(goTo(1), list, false);
		    }
		    break;
                    case 4: /* reduce AContinueReferences */
		    {
			ArrayList list = new4();
			push(goTo(2), list, false);
		    }
		    break;
                    case 5: /* reduce AEndReferences */
		    {
			ArrayList list = new5();
			push(goTo(2), list, false);
		    }
		    break;
                    case 6: /* reduce ASlots */
		    {
			ArrayList list = new6();
			push(goTo(3), list, false);
		    }
		    break;
                    case 7: /* reduce AAfunctions1Functions */
		    {
			ArrayList list = new7();
			push(goTo(4), list, false);
		    }
		    break;
                    case 8: /* reduce AAfunctions2Functions */
		    {
			ArrayList list = new8();
			push(goTo(4), list, false);
		    }
		    break;
                    case 9: /* reduce AFunctionDefinition */
		    {
			ArrayList list = new9();
			push(goTo(5), list, false);
		    }
		    break;
                    case 10: /* reduce AArequiredparameters1RequiredParameters */
		    {
			ArrayList list = new10();
			push(goTo(6), list, false);
		    }
		    break;
                    case 11: /* reduce AArequiredparameters2RequiredParameters */
		    {
			ArrayList list = new11();
			push(goTo(6), list, false);
		    }
		    break;
                    case 12: /* reduce ARequiredParameter */
		    {
			ArrayList list = new12();
			push(goTo(7), list, false);
		    }
		    break;
                    case 13: /* reduce AAopcodes1Opcodes */
		    {
			ArrayList list = new13();
			push(goTo(8), list, false);
		    }
		    break;
                    case 14: /* reduce AAopcodes2Opcodes */
		    {
			ArrayList list = new14();
			push(goTo(8), list, false);
		    }
		    break;
                    case 15: /* reduce AOpcode */
		    {
			ArrayList list = new15();
			push(goTo(9), list, false);
		    }
		    break;
                    case 16: /* reduce ANumberOpcodeId */
		    {
			ArrayList list = new16();
			push(goTo(10), list, false);
		    }
		    break;
                    case 17: /* reduce AOpcodeOpcodeId */
		    {
			ArrayList list = new17();
			push(goTo(10), list, false);
		    }
		    break;
                    case 18: /* reduce ASymbolOpcodeParameter */
		    {
			ArrayList list = new18();
			push(goTo(11), list, false);
		    }
		    break;
                    case 19: /* reduce AValueOpcodeParameter */
		    {
			ArrayList list = new19();
			push(goTo(11), list, false);
		    }
		    break;
                    case 20: /* reduce AEmptyOpcodeParameter */
		    {
			ArrayList list = new20();
			push(goTo(11), list, false);
		    }
		    break;
                    case 21: /* reduce ATerminal$ObjectDefinition */
		    {
			ArrayList list = new21();
			push(goTo(12), list, true);
		    }
		    break;
                    case 22: /* reduce ANonTerminal$ObjectDefinition */
		    {
			ArrayList list = new22();
			push(goTo(12), list, true);
		    }
		    break;
                    case 23: /* reduce ATerminal$FunctionDefinition */
		    {
			ArrayList list = new23();
			push(goTo(13), list, true);
		    }
		    break;
                    case 24: /* reduce ANonTerminal$FunctionDefinition */
		    {
			ArrayList list = new24();
			push(goTo(13), list, true);
		    }
		    break;
                    case 25: /* reduce ATerminal$RequiredParameter */
		    {
			ArrayList list = new25();
			push(goTo(14), list, true);
		    }
		    break;
                    case 26: /* reduce ANonTerminal$RequiredParameter */
		    {
			ArrayList list = new26();
			push(goTo(14), list, true);
		    }
		    break;
                    case 27: /* reduce ATerminal$Opcode */
		    {
			ArrayList list = new27();
			push(goTo(15), list, true);
		    }
		    break;
                    case 28: /* reduce ANonTerminal$Opcode */
		    {
			ArrayList list = new28();
			push(goTo(15), list, true);
		    }
		    break;
                    }
                    break;
                case ACCEPT:
                    {
                        EOF node2 = (EOF) this.lexer.next();
                        PImage node1 = (PImage) pop().get(0);
                        Start node = new Start(node1, node2);
                        return node;
                    }
                case ERROR:
                    throw new ParserException(this.last_token,
                        "[" + this.last_line + "," + this.last_pos + "] " +
                        Parser.errorMessages[Parser.errors[this.action[1]]]);
            }
        }
    }



    @SuppressWarnings("unchecked")
    ArrayList new0() /* reduce AAimage1Image */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        PImage pimageNode1;
        {
            // Block
        LinkedList listNode2 = new LinkedList();
        {
            // Block
        }

        pimageNode1 = new AImage(listNode2);
        }
	nodeList.add(pimageNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new1() /* reduce AAimage2Image */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PImage pimageNode1;
        {
            // Block
        LinkedList listNode3 = new LinkedList();
        {
            // Block
        LinkedList listNode2 = new LinkedList();
        listNode2 = (LinkedList)nodeArrayList1.get(0);
	if(listNode2 != null)
	{
	  listNode3.addAll(listNode2);
	}
        }

        pimageNode1 = new AImage(listNode3);
        }
	nodeList.add(pimageNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new2() /* reduce ASlotObjectDefinition */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PObjectDefinition pobjectdefinitionNode1;
        {
            // Block
        TId tidNode2;
        PSlots pslotsNode3;
        PFunctions pfunctionsNode4;
        tidNode2 = (TId)nodeArrayList1.get(0);
        pslotsNode3 = (PSlots)nodeArrayList2.get(0);
        pfunctionsNode4 = (PFunctions)nodeArrayList3.get(0);

        pobjectdefinitionNode1 = new ASlotObjectDefinition(tidNode2, pslotsNode3, pfunctionsNode4);
        }
	nodeList.add(pobjectdefinitionNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new3() /* reduce AReferenceObjectDefinition */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PObjectDefinition pobjectdefinitionNode1;
        {
            // Block
        TId tidNode2;
        TDollar tdollarNode3;
        PReferences preferencesNode4;
        tidNode2 = (TId)nodeArrayList1.get(0);
        tdollarNode3 = (TDollar)nodeArrayList2.get(0);
        preferencesNode4 = (PReferences)nodeArrayList3.get(0);

        pobjectdefinitionNode1 = new AReferenceObjectDefinition(tidNode2, tdollarNode3, preferencesNode4);
        }
	nodeList.add(pobjectdefinitionNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new4() /* reduce AContinueReferences */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PReferences preferencesNode1;
        {
            // Block
        TId tidNode2;
        TFullstop tfullstopNode3;
        PReferences preferencesNode4;
        tidNode2 = (TId)nodeArrayList1.get(0);
        tfullstopNode3 = (TFullstop)nodeArrayList2.get(0);
        preferencesNode4 = (PReferences)nodeArrayList3.get(0);

        preferencesNode1 = new AContinueReferences(tidNode2, tfullstopNode3, preferencesNode4);
        }
	nodeList.add(preferencesNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new5() /* reduce AEndReferences */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PReferences preferencesNode1;
        {
            // Block
        TId tidNode2;
        TDollar tdollarNode3;
        tidNode2 = (TId)nodeArrayList1.get(0);
        tdollarNode3 = (TDollar)nodeArrayList2.get(0);

        preferencesNode1 = new AEndReferences(tidNode2, tdollarNode3);
        }
	nodeList.add(preferencesNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new6() /* reduce ASlots */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PSlots pslotsNode1;
        {
            // Block
        TOpenBrace topenbraceNode2;
        PImage pimageNode3;
        TCloseBrace tclosebraceNode4;
        topenbraceNode2 = (TOpenBrace)nodeArrayList1.get(0);
        pimageNode3 = (PImage)nodeArrayList2.get(0);
        tclosebraceNode4 = (TCloseBrace)nodeArrayList3.get(0);

        pslotsNode1 = new ASlots(topenbraceNode2, pimageNode3, tclosebraceNode4);
        }
	nodeList.add(pslotsNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new7() /* reduce AAfunctions1Functions */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PFunctions pfunctionsNode1;
        {
            // Block
        TOpenBrace topenbraceNode2;
        LinkedList listNode3 = new LinkedList();
        TCloseBrace tclosebraceNode4;
        topenbraceNode2 = (TOpenBrace)nodeArrayList1.get(0);
        {
            // Block
        }
        tclosebraceNode4 = (TCloseBrace)nodeArrayList2.get(0);

        pfunctionsNode1 = new AFunctions(topenbraceNode2, listNode3, tclosebraceNode4);
        }
	nodeList.add(pfunctionsNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new8() /* reduce AAfunctions2Functions */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PFunctions pfunctionsNode1;
        {
            // Block
        TOpenBrace topenbraceNode2;
        LinkedList listNode4 = new LinkedList();
        TCloseBrace tclosebraceNode5;
        topenbraceNode2 = (TOpenBrace)nodeArrayList1.get(0);
        {
            // Block
        LinkedList listNode3 = new LinkedList();
        listNode3 = (LinkedList)nodeArrayList2.get(0);
	if(listNode3 != null)
	{
	  listNode4.addAll(listNode3);
	}
        }
        tclosebraceNode5 = (TCloseBrace)nodeArrayList3.get(0);

        pfunctionsNode1 = new AFunctions(topenbraceNode2, listNode4, tclosebraceNode5);
        }
	nodeList.add(pfunctionsNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new9() /* reduce AFunctionDefinition */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PFunctionDefinition pfunctiondefinitionNode1;
        {
            // Block
        TId tidNode2;
        PRequiredParameters prequiredparametersNode3;
        POpcodes popcodesNode4;
        tidNode2 = (TId)nodeArrayList1.get(0);
        prequiredparametersNode3 = (PRequiredParameters)nodeArrayList2.get(0);
        popcodesNode4 = (POpcodes)nodeArrayList3.get(0);

        pfunctiondefinitionNode1 = new AFunctionDefinition(tidNode2, prequiredparametersNode3, popcodesNode4);
        }
	nodeList.add(pfunctiondefinitionNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new10() /* reduce AArequiredparameters1RequiredParameters */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PRequiredParameters prequiredparametersNode1;
        {
            // Block
        TOpenBrace topenbraceNode2;
        LinkedList listNode3 = new LinkedList();
        TCloseBrace tclosebraceNode4;
        topenbraceNode2 = (TOpenBrace)nodeArrayList1.get(0);
        {
            // Block
        }
        tclosebraceNode4 = (TCloseBrace)nodeArrayList2.get(0);

        prequiredparametersNode1 = new ARequiredParameters(topenbraceNode2, listNode3, tclosebraceNode4);
        }
	nodeList.add(prequiredparametersNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new11() /* reduce AArequiredparameters2RequiredParameters */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PRequiredParameters prequiredparametersNode1;
        {
            // Block
        TOpenBrace topenbraceNode2;
        LinkedList listNode4 = new LinkedList();
        TCloseBrace tclosebraceNode5;
        topenbraceNode2 = (TOpenBrace)nodeArrayList1.get(0);
        {
            // Block
        LinkedList listNode3 = new LinkedList();
        listNode3 = (LinkedList)nodeArrayList2.get(0);
	if(listNode3 != null)
	{
	  listNode4.addAll(listNode3);
	}
        }
        tclosebraceNode5 = (TCloseBrace)nodeArrayList3.get(0);

        prequiredparametersNode1 = new ARequiredParameters(topenbraceNode2, listNode4, tclosebraceNode5);
        }
	nodeList.add(prequiredparametersNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new12() /* reduce ARequiredParameter */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PRequiredParameter prequiredparameterNode1;
        {
            // Block
        TId tidNode2;
        TFullstop tfullstopNode3;
        tidNode2 = (TId)nodeArrayList1.get(0);
        tfullstopNode3 = (TFullstop)nodeArrayList2.get(0);

        prequiredparameterNode1 = new ARequiredParameter(tidNode2, tfullstopNode3);
        }
	nodeList.add(prequiredparameterNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new13() /* reduce AAopcodes1Opcodes */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        POpcodes popcodesNode1;
        {
            // Block
        TOpenBrace topenbraceNode2;
        LinkedList listNode3 = new LinkedList();
        TCloseBrace tclosebraceNode4;
        topenbraceNode2 = (TOpenBrace)nodeArrayList1.get(0);
        {
            // Block
        }
        tclosebraceNode4 = (TCloseBrace)nodeArrayList2.get(0);

        popcodesNode1 = new AOpcodes(topenbraceNode2, listNode3, tclosebraceNode4);
        }
	nodeList.add(popcodesNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new14() /* reduce AAopcodes2Opcodes */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        POpcodes popcodesNode1;
        {
            // Block
        TOpenBrace topenbraceNode2;
        LinkedList listNode4 = new LinkedList();
        TCloseBrace tclosebraceNode5;
        topenbraceNode2 = (TOpenBrace)nodeArrayList1.get(0);
        {
            // Block
        LinkedList listNode3 = new LinkedList();
        listNode3 = (LinkedList)nodeArrayList2.get(0);
	if(listNode3 != null)
	{
	  listNode4.addAll(listNode3);
	}
        }
        tclosebraceNode5 = (TCloseBrace)nodeArrayList3.get(0);

        popcodesNode1 = new AOpcodes(topenbraceNode2, listNode4, tclosebraceNode5);
        }
	nodeList.add(popcodesNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new15() /* reduce AOpcode */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        POpcode popcodeNode1;
        {
            // Block
        POpcodeId popcodeidNode2;
        TFullstop tfullstopNode3;
        popcodeidNode2 = (POpcodeId)nodeArrayList1.get(0);
        tfullstopNode3 = (TFullstop)nodeArrayList2.get(0);

        popcodeNode1 = new AOpcode(popcodeidNode2, tfullstopNode3);
        }
	nodeList.add(popcodeNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new16() /* reduce ANumberOpcodeId */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        POpcodeId popcodeidNode1;
        {
            // Block
        TNumber tnumberNode2;
        POpcodeParameter popcodeparameterNode3;
        tnumberNode2 = (TNumber)nodeArrayList1.get(0);
        popcodeparameterNode3 = (POpcodeParameter)nodeArrayList2.get(0);

        popcodeidNode1 = new ANumberOpcodeId(tnumberNode2, popcodeparameterNode3);
        }
	nodeList.add(popcodeidNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new17() /* reduce AOpcodeOpcodeId */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        POpcodeId popcodeidNode1;
        {
            // Block
        TMnemonic tmnemonicNode2;
        POpcodeParameter popcodeparameterNode3;
        tmnemonicNode2 = (TMnemonic)nodeArrayList1.get(0);
        popcodeparameterNode3 = (POpcodeParameter)nodeArrayList2.get(0);

        popcodeidNode1 = new AOpcodeOpcodeId(tmnemonicNode2, popcodeparameterNode3);
        }
	nodeList.add(popcodeidNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new18() /* reduce ASymbolOpcodeParameter */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        POpcodeParameter popcodeparameterNode1;
        {
            // Block
        TEquals tequalsNode2;
        TId tidNode3;
        tequalsNode2 = (TEquals)nodeArrayList1.get(0);
        tidNode3 = (TId)nodeArrayList2.get(0);

        popcodeparameterNode1 = new ASymbolOpcodeParameter(tequalsNode2, tidNode3);
        }
	nodeList.add(popcodeparameterNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new19() /* reduce AValueOpcodeParameter */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        POpcodeParameter popcodeparameterNode1;
        {
            // Block
        THash thashNode2;
        TNumber tnumberNode3;
        thashNode2 = (THash)nodeArrayList1.get(0);
        tnumberNode3 = (TNumber)nodeArrayList2.get(0);

        popcodeparameterNode1 = new AValueOpcodeParameter(thashNode2, tnumberNode3);
        }
	nodeList.add(popcodeparameterNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new20() /* reduce AEmptyOpcodeParameter */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        POpcodeParameter popcodeparameterNode1;
        {
            // Block

        popcodeparameterNode1 = new AEmptyOpcodeParameter();
        }
	nodeList.add(popcodeparameterNode1);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new21() /* reduce ATerminal$ObjectDefinition */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode2 = new LinkedList();
        {
            // Block
        PObjectDefinition pobjectdefinitionNode1;
        pobjectdefinitionNode1 = (PObjectDefinition)nodeArrayList1.get(0);
	if(pobjectdefinitionNode1 != null)
	{
	  listNode2.add(pobjectdefinitionNode1);
	}
        }
	nodeList.add(listNode2);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new22() /* reduce ANonTerminal$ObjectDefinition */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode3 = new LinkedList();
        {
            // Block
        LinkedList listNode1 = new LinkedList();
        PObjectDefinition pobjectdefinitionNode2;
        listNode1 = (LinkedList)nodeArrayList1.get(0);
        pobjectdefinitionNode2 = (PObjectDefinition)nodeArrayList2.get(0);
	if(listNode1 != null)
	{
	  listNode3.addAll(listNode1);
	}
	if(pobjectdefinitionNode2 != null)
	{
	  listNode3.add(pobjectdefinitionNode2);
	}
        }
	nodeList.add(listNode3);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new23() /* reduce ATerminal$FunctionDefinition */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode2 = new LinkedList();
        {
            // Block
        PFunctionDefinition pfunctiondefinitionNode1;
        pfunctiondefinitionNode1 = (PFunctionDefinition)nodeArrayList1.get(0);
	if(pfunctiondefinitionNode1 != null)
	{
	  listNode2.add(pfunctiondefinitionNode1);
	}
        }
	nodeList.add(listNode2);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new24() /* reduce ANonTerminal$FunctionDefinition */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode3 = new LinkedList();
        {
            // Block
        LinkedList listNode1 = new LinkedList();
        PFunctionDefinition pfunctiondefinitionNode2;
        listNode1 = (LinkedList)nodeArrayList1.get(0);
        pfunctiondefinitionNode2 = (PFunctionDefinition)nodeArrayList2.get(0);
	if(listNode1 != null)
	{
	  listNode3.addAll(listNode1);
	}
	if(pfunctiondefinitionNode2 != null)
	{
	  listNode3.add(pfunctiondefinitionNode2);
	}
        }
	nodeList.add(listNode3);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new25() /* reduce ATerminal$RequiredParameter */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode2 = new LinkedList();
        {
            // Block
        PRequiredParameter prequiredparameterNode1;
        prequiredparameterNode1 = (PRequiredParameter)nodeArrayList1.get(0);
	if(prequiredparameterNode1 != null)
	{
	  listNode2.add(prequiredparameterNode1);
	}
        }
	nodeList.add(listNode2);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new26() /* reduce ANonTerminal$RequiredParameter */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode3 = new LinkedList();
        {
            // Block
        LinkedList listNode1 = new LinkedList();
        PRequiredParameter prequiredparameterNode2;
        listNode1 = (LinkedList)nodeArrayList1.get(0);
        prequiredparameterNode2 = (PRequiredParameter)nodeArrayList2.get(0);
	if(listNode1 != null)
	{
	  listNode3.addAll(listNode1);
	}
	if(prequiredparameterNode2 != null)
	{
	  listNode3.add(prequiredparameterNode2);
	}
        }
	nodeList.add(listNode3);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new27() /* reduce ATerminal$Opcode */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode2 = new LinkedList();
        {
            // Block
        POpcode popcodeNode1;
        popcodeNode1 = (POpcode)nodeArrayList1.get(0);
	if(popcodeNode1 != null)
	{
	  listNode2.add(popcodeNode1);
	}
        }
	nodeList.add(listNode2);
        return nodeList;
    }



    @SuppressWarnings("unchecked")
    ArrayList new28() /* reduce ANonTerminal$Opcode */
    {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode3 = new LinkedList();
        {
            // Block
        LinkedList listNode1 = new LinkedList();
        POpcode popcodeNode2;
        listNode1 = (LinkedList)nodeArrayList1.get(0);
        popcodeNode2 = (POpcode)nodeArrayList2.get(0);
	if(listNode1 != null)
	{
	  listNode3.addAll(listNode1);
	}
	if(popcodeNode2 != null)
	{
	  listNode3.add(popcodeNode2);
	}
        }
	nodeList.add(listNode3);
        return nodeList;
    }



    private static int[][][] actionTable;
/*      {
			{{-1, REDUCE, 0}, {3, SHIFT, 1}, },
			{{-1, ERROR, 1}, {4, SHIFT, 5}, {7, SHIFT, 6}, },
			{{-1, ERROR, 2}, {10, ACCEPT, -1}, },
			{{-1, REDUCE, 21}, },
			{{-1, REDUCE, 1}, {3, SHIFT, 1}, },
			{{-1, REDUCE, 0}, {3, SHIFT, 1}, },
			{{-1, ERROR, 6}, {3, SHIFT, 10}, },
			{{-1, ERROR, 7}, {4, SHIFT, 12}, },
			{{-1, REDUCE, 22}, },
			{{-1, ERROR, 9}, {5, SHIFT, 14}, },
			{{-1, ERROR, 10}, {6, SHIFT, 15}, {7, SHIFT, 16}, },
			{{-1, REDUCE, 3}, },
			{{-1, ERROR, 12}, {3, SHIFT, 17}, {5, SHIFT, 18}, },
			{{-1, REDUCE, 2}, },
			{{-1, REDUCE, 6}, },
			{{-1, ERROR, 15}, {3, SHIFT, 10}, },
			{{-1, REDUCE, 5}, },
			{{-1, ERROR, 17}, {4, SHIFT, 22}, },
			{{-1, REDUCE, 7}, },
			{{-1, REDUCE, 23}, },
			{{-1, ERROR, 20}, {3, SHIFT, 17}, {5, SHIFT, 24}, },
			{{-1, REDUCE, 4}, },
			{{-1, ERROR, 22}, {3, SHIFT, 26}, {5, SHIFT, 27}, },
			{{-1, ERROR, 23}, {4, SHIFT, 30}, },
			{{-1, REDUCE, 8}, },
			{{-1, REDUCE, 24}, },
			{{-1, ERROR, 26}, {6, SHIFT, 32}, },
			{{-1, REDUCE, 10}, },
			{{-1, REDUCE, 25}, },
			{{-1, ERROR, 29}, {3, SHIFT, 26}, {5, SHIFT, 33}, },
			{{-1, ERROR, 30}, {1, SHIFT, 35}, {2, SHIFT, 36}, {5, SHIFT, 37}, },
			{{-1, REDUCE, 9}, },
			{{-1, REDUCE, 12}, },
			{{-1, REDUCE, 11}, },
			{{-1, REDUCE, 26}, },
			{{-1, REDUCE, 20}, {8, SHIFT, 41}, {9, SHIFT, 42}, },
			{{-1, REDUCE, 20}, {8, SHIFT, 41}, {9, SHIFT, 42}, },
			{{-1, REDUCE, 13}, },
			{{-1, REDUCE, 27}, },
			{{-1, ERROR, 39}, {6, SHIFT, 45}, },
			{{-1, ERROR, 40}, {1, SHIFT, 35}, {2, SHIFT, 36}, {5, SHIFT, 46}, },
			{{-1, ERROR, 41}, {2, SHIFT, 48}, },
			{{-1, ERROR, 42}, {3, SHIFT, 49}, },
			{{-1, REDUCE, 17}, },
			{{-1, REDUCE, 16}, },
			{{-1, REDUCE, 15}, },
			{{-1, REDUCE, 14}, },
			{{-1, REDUCE, 28}, },
			{{-1, REDUCE, 19}, },
			{{-1, REDUCE, 18}, },
        };*/
    private static int[][][] gotoTable;
/*      {
			{{-1, 2}, {5, 9}, },
			{{-1, 3}, {4, 8}, },
			{{-1, 11}, {15, 21}, },
			{{-1, 7}, },
			{{-1, 13}, },
			{{-1, 19}, {20, 25}, },
			{{-1, 23}, },
			{{-1, 28}, {29, 34}, },
			{{-1, 31}, },
			{{-1, 38}, {40, 47}, },
			{{-1, 39}, },
			{{-1, 43}, {36, 44}, },
			{{-1, 4}, },
			{{-1, 20}, },
			{{-1, 29}, },
			{{-1, 40}, },
        };*/
    private static String[] errorMessages;
/*      {
			"expecting: id, EOF",
			"expecting: '{', '$'",
			"expecting: EOF",
			"expecting: id, '}', EOF",
			"expecting: id, '}'",
			"expecting: id",
			"expecting: '{'",
			"expecting: '}'",
			"expecting: '.', '$'",
			"expecting: '.'",
			"expecting: mnemonic, number, '}'",
			"expecting: '.', '#', '='",
			"expecting: number",
        };*/
    private static int[] errors;
/*      {
			0, 1, 2, 3, 3, 4, 5, 6, 3, 7, 8, 3, 4, 3, 6, 5, 3, 6, 3, 4, 4, 3, 4, 6, 3, 4, 9, 6, 4, 4, 10, 4, 4, 6, 4, 11, 11, 4, 10, 9, 10, 12, 5, 9, 9, 10, 4, 10, 9, 9, 
        };*/

    static 
    {
        try
        {
            DataInputStream s = new DataInputStream(
                new BufferedInputStream(
                Parser.class.getResourceAsStream("parser.dat")));

            // read actionTable
            int length = s.readInt();
            Parser.actionTable = new int[length][][];
            for(int i = 0; i < Parser.actionTable.length; i++)
            {
                length = s.readInt();
                Parser.actionTable[i] = new int[length][3];
                for(int j = 0; j < Parser.actionTable[i].length; j++)
                {
                for(int k = 0; k < 3; k++)
                {
                    Parser.actionTable[i][j][k] = s.readInt();
                }
                }
            }

            // read gotoTable
            length = s.readInt();
            gotoTable = new int[length][][];
            for(int i = 0; i < gotoTable.length; i++)
            {
                length = s.readInt();
                gotoTable[i] = new int[length][2];
                for(int j = 0; j < gotoTable[i].length; j++)
                {
                for(int k = 0; k < 2; k++)
                {
                    gotoTable[i][j][k] = s.readInt();
                }
                }
            }

            // read errorMessages
            length = s.readInt();
            errorMessages = new String[length];
            for(int i = 0; i < errorMessages.length; i++)
            {
                length = s.readInt();
                StringBuffer buffer = new StringBuffer();

                for(int j = 0; j < length; j++)
                {
                buffer.append(s.readChar());
                }
                errorMessages[i] = buffer.toString();
            }

            // read errors
            length = s.readInt();
            errors = new int[length];
            for(int i = 0; i < errors.length; i++)
            {
                errors[i] = s.readInt();
            }

            s.close();
        }
        catch(Exception e)
        {
            throw new RuntimeException("The file \"parser.dat\" is either missing or corrupted.");
        }
    }
}
