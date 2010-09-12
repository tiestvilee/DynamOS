/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.dynamos.image.parser;

import org.dynamos.image.node.*;
import org.dynamos.image.analysis.*;

class TokenIndex extends AnalysisAdapter
{
    int index;

    @Override
    public void caseTColon(@SuppressWarnings("unused") TColon node)
    {
        this.index = 0;
    }

    @Override
    public void caseTMnemonic(@SuppressWarnings("unused") TMnemonic node)
    {
        this.index = 1;
    }

    @Override
    public void caseTNumber(@SuppressWarnings("unused") TNumber node)
    {
        this.index = 2;
    }

    @Override
    public void caseTId(@SuppressWarnings("unused") TId node)
    {
        this.index = 3;
    }

    @Override
    public void caseTOpenBrace(@SuppressWarnings("unused") TOpenBrace node)
    {
        this.index = 4;
    }

    @Override
    public void caseTCloseBrace(@SuppressWarnings("unused") TCloseBrace node)
    {
        this.index = 5;
    }

    @Override
    public void caseTFullstop(@SuppressWarnings("unused") TFullstop node)
    {
        this.index = 6;
    }

    @Override
    public void caseTDollar(@SuppressWarnings("unused") TDollar node)
    {
        this.index = 7;
    }

    @Override
    public void caseTHash(@SuppressWarnings("unused") THash node)
    {
        this.index = 8;
    }

    @Override
    public void caseTEquals(@SuppressWarnings("unused") TEquals node)
    {
        this.index = 9;
    }

    @Override
    public void caseEOF(@SuppressWarnings("unused") EOF node)
    {
        this.index = 10;
    }
}