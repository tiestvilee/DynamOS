/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.dynamos.image.node;

import org.dynamos.image.analysis.*;

@SuppressWarnings("nls")
public final class TDollar extends Token
{
    public TDollar()
    {
        super.setText("$");
    }

    public TDollar(int line, int pos)
    {
        super.setText("$");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TDollar(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTDollar(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TDollar text.");
    }
}