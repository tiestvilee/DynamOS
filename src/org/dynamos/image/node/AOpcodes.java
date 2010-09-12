/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.dynamos.image.node;

import java.util.*;
import org.dynamos.image.analysis.*;

@SuppressWarnings("nls")
public final class AOpcodes extends POpcodes
{
    private TOpenBrace _openBrace_;
    private final LinkedList<POpcode> _opcode_ = new LinkedList<POpcode>();
    private TCloseBrace _closeBrace_;

    public AOpcodes()
    {
        // Constructor
    }

    public AOpcodes(
        @SuppressWarnings("hiding") TOpenBrace _openBrace_,
        @SuppressWarnings("hiding") List<POpcode> _opcode_,
        @SuppressWarnings("hiding") TCloseBrace _closeBrace_)
    {
        // Constructor
        setOpenBrace(_openBrace_);

        setOpcode(_opcode_);

        setCloseBrace(_closeBrace_);

    }

    @Override
    public Object clone()
    {
        return new AOpcodes(
            cloneNode(this._openBrace_),
            cloneList(this._opcode_),
            cloneNode(this._closeBrace_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAOpcodes(this);
    }

    public TOpenBrace getOpenBrace()
    {
        return this._openBrace_;
    }

    public void setOpenBrace(TOpenBrace node)
    {
        if(this._openBrace_ != null)
        {
            this._openBrace_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._openBrace_ = node;
    }

    public LinkedList<POpcode> getOpcode()
    {
        return this._opcode_;
    }

    public void setOpcode(List<POpcode> list)
    {
        this._opcode_.clear();
        this._opcode_.addAll(list);
        for(POpcode e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    public TCloseBrace getCloseBrace()
    {
        return this._closeBrace_;
    }

    public void setCloseBrace(TCloseBrace node)
    {
        if(this._closeBrace_ != null)
        {
            this._closeBrace_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._closeBrace_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._openBrace_)
            + toString(this._opcode_)
            + toString(this._closeBrace_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._openBrace_ == child)
        {
            this._openBrace_ = null;
            return;
        }

        if(this._opcode_.remove(child))
        {
            return;
        }

        if(this._closeBrace_ == child)
        {
            this._closeBrace_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._openBrace_ == oldChild)
        {
            setOpenBrace((TOpenBrace) newChild);
            return;
        }

        for(ListIterator<POpcode> i = this._opcode_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((POpcode) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        if(this._closeBrace_ == oldChild)
        {
            setCloseBrace((TCloseBrace) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}