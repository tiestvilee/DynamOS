/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.dynamos.image.node;

import org.dynamos.image.analysis.*;

@SuppressWarnings("nls")
public final class AOpcode extends POpcode
{
    private TNumber _number_;
    private POpcodeParameter _opcodeParameter_;
    private TFullstop _fullstop_;

    public AOpcode()
    {
        // Constructor
    }

    public AOpcode(
        @SuppressWarnings("hiding") TNumber _number_,
        @SuppressWarnings("hiding") POpcodeParameter _opcodeParameter_,
        @SuppressWarnings("hiding") TFullstop _fullstop_)
    {
        // Constructor
        setNumber(_number_);

        setOpcodeParameter(_opcodeParameter_);

        setFullstop(_fullstop_);

    }

    @Override
    public Object clone()
    {
        return new AOpcode(
            cloneNode(this._number_),
            cloneNode(this._opcodeParameter_),
            cloneNode(this._fullstop_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAOpcode(this);
    }

    public TNumber getNumber()
    {
        return this._number_;
    }

    public void setNumber(TNumber node)
    {
        if(this._number_ != null)
        {
            this._number_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._number_ = node;
    }

    public POpcodeParameter getOpcodeParameter()
    {
        return this._opcodeParameter_;
    }

    public void setOpcodeParameter(POpcodeParameter node)
    {
        if(this._opcodeParameter_ != null)
        {
            this._opcodeParameter_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._opcodeParameter_ = node;
    }

    public TFullstop getFullstop()
    {
        return this._fullstop_;
    }

    public void setFullstop(TFullstop node)
    {
        if(this._fullstop_ != null)
        {
            this._fullstop_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._fullstop_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._number_)
            + toString(this._opcodeParameter_)
            + toString(this._fullstop_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._number_ == child)
        {
            this._number_ = null;
            return;
        }

        if(this._opcodeParameter_ == child)
        {
            this._opcodeParameter_ = null;
            return;
        }

        if(this._fullstop_ == child)
        {
            this._fullstop_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._number_ == oldChild)
        {
            setNumber((TNumber) newChild);
            return;
        }

        if(this._opcodeParameter_ == oldChild)
        {
            setOpcodeParameter((POpcodeParameter) newChild);
            return;
        }

        if(this._fullstop_ == oldChild)
        {
            setFullstop((TFullstop) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
