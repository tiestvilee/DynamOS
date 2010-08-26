/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.dynamos.image.analysis;

import org.dynamos.image.node.*;

public interface Analysis extends Switch
{
    Object getIn(Node node);
    void setIn(Node node, Object o);
    Object getOut(Node node);
    void setOut(Node node, Object o);

    void caseStart(Start node);
    void caseAImage(AImage node);
    void caseAObjectDefinition(AObjectDefinition node);
    void caseASlots(ASlots node);
    void caseAFunctions(AFunctions node);
    void caseAFunctionDefinition(AFunctionDefinition node);
    void caseARequiredParameters(ARequiredParameters node);
    void caseARequiredParameter(ARequiredParameter node);
    void caseAOpcodes(AOpcodes node);
    void caseAOpcode(AOpcode node);
    void caseASymbolOpcodeParameter(ASymbolOpcodeParameter node);
    void caseAValueOpcodeParameter(AValueOpcodeParameter node);
    void caseAEmptyOpcodeParameter(AEmptyOpcodeParameter node);

    void caseTColon(TColon node);
    void caseTNumber(TNumber node);
    void caseTId(TId node);
    void caseTOpenBrace(TOpenBrace node);
    void caseTCloseBrace(TCloseBrace node);
    void caseTFullstop(TFullstop node);
    void caseTHash(THash node);
    void caseTEquals(TEquals node);
    void caseTBlank(TBlank node);
    void caseEOF(EOF node);
}
