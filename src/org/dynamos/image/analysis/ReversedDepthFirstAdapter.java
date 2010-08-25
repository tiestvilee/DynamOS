/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.dynamos.image.analysis;

import java.util.*;
import org.dynamos.image.node.*;

public class ReversedDepthFirstAdapter extends AnalysisAdapter
{
    public void inStart(Start node)
    {
        defaultIn(node);
    }

    public void outStart(Start node)
    {
        defaultOut(node);
    }

    public void defaultIn(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    public void defaultOut(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    @Override
    public void caseStart(Start node)
    {
        inStart(node);
        node.getEOF().apply(this);
        node.getPImage().apply(this);
        outStart(node);
    }

    public void inAImage(AImage node)
    {
        defaultIn(node);
    }

    public void outAImage(AImage node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAImage(AImage node)
    {
        inAImage(node);
        {
            List<PObjectDefinition> copy = new ArrayList<PObjectDefinition>(node.getObjectDefinition());
            Collections.reverse(copy);
            for(PObjectDefinition e : copy)
            {
                e.apply(this);
            }
        }
        outAImage(node);
    }

    public void inAObjectDefinition(AObjectDefinition node)
    {
        defaultIn(node);
    }

    public void outAObjectDefinition(AObjectDefinition node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAObjectDefinition(AObjectDefinition node)
    {
        inAObjectDefinition(node);
        if(node.getFunctions() != null)
        {
            node.getFunctions().apply(this);
        }
        if(node.getSlots() != null)
        {
            node.getSlots().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAObjectDefinition(node);
    }

    public void inASlots(ASlots node)
    {
        defaultIn(node);
    }

    public void outASlots(ASlots node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASlots(ASlots node)
    {
        inASlots(node);
        if(node.getCloseBrace() != null)
        {
            node.getCloseBrace().apply(this);
        }
        if(node.getImage() != null)
        {
            node.getImage().apply(this);
        }
        if(node.getOpenBrace() != null)
        {
            node.getOpenBrace().apply(this);
        }
        outASlots(node);
    }

    public void inAFunctions(AFunctions node)
    {
        defaultIn(node);
    }

    public void outAFunctions(AFunctions node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFunctions(AFunctions node)
    {
        inAFunctions(node);
        if(node.getCloseBrace() != null)
        {
            node.getCloseBrace().apply(this);
        }
        {
            List<PFunctionDefinition> copy = new ArrayList<PFunctionDefinition>(node.getFunctionDefinition());
            Collections.reverse(copy);
            for(PFunctionDefinition e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getOpenBrace() != null)
        {
            node.getOpenBrace().apply(this);
        }
        outAFunctions(node);
    }

    public void inAFunctionDefinition(AFunctionDefinition node)
    {
        defaultIn(node);
    }

    public void outAFunctionDefinition(AFunctionDefinition node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFunctionDefinition(AFunctionDefinition node)
    {
        inAFunctionDefinition(node);
        if(node.getOpcodes() != null)
        {
            node.getOpcodes().apply(this);
        }
        if(node.getRequiredParameters() != null)
        {
            node.getRequiredParameters().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAFunctionDefinition(node);
    }

    public void inARequiredParameters(ARequiredParameters node)
    {
        defaultIn(node);
    }

    public void outARequiredParameters(ARequiredParameters node)
    {
        defaultOut(node);
    }

    @Override
    public void caseARequiredParameters(ARequiredParameters node)
    {
        inARequiredParameters(node);
        if(node.getCloseBrace() != null)
        {
            node.getCloseBrace().apply(this);
        }
        if(node.getOpenBrace() != null)
        {
            node.getOpenBrace().apply(this);
        }
        outARequiredParameters(node);
    }

    public void inAOpcodes(AOpcodes node)
    {
        defaultIn(node);
    }

    public void outAOpcodes(AOpcodes node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAOpcodes(AOpcodes node)
    {
        inAOpcodes(node);
        if(node.getCloseBrace() != null)
        {
            node.getCloseBrace().apply(this);
        }
        if(node.getOpenBrace() != null)
        {
            node.getOpenBrace().apply(this);
        }
        outAOpcodes(node);
    }
}
