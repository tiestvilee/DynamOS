package org.dynamos.image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.dynamos.image.analysis.DepthFirstAdapter;
import org.dynamos.image.node.AContinueReferences;
import org.dynamos.image.node.AEndReferences;
import org.dynamos.image.node.AFunctionDefinition;
import org.dynamos.image.node.ANumberOpcodeId;
import org.dynamos.image.node.AOpcodeOpcodeId;
import org.dynamos.image.node.AReferenceObjectDefinition;
import org.dynamos.image.node.ARequiredParameter;
import org.dynamos.image.node.ASlotObjectDefinition;
import org.dynamos.image.node.ASymbolOpcodeParameter;
import org.dynamos.image.node.AValueOpcodeParameter;
import org.dynamos.image.node.Start;
import org.dynamos.image.parser.Parser;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;

public class ImageStreamReader {

	ParserBuilder imageParserBuilder;

	public ImageStreamReader(ParserBuilder imageParserBuilder) {
		this.imageParserBuilder = imageParserBuilder;
	}
	
	public ObjectDOS read(String string) {
		ObjectDOS wrapper = new ObjectDOS();
		read(string, wrapper);
		return wrapper;
	}
	
	public ObjectDOS read(String string, ObjectDOS wrapper) {
		
		Parser imageParser = imageParserBuilder.build(string);
		
		try {
			Start topNode = imageParser.parse();
			Interpreter interpreter = new Interpreter(wrapper);
			topNode.apply(interpreter);
			
			return wrapper;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private static class Interpreter extends DepthFirstAdapter {
		
		Stack<ObjectDOS> currentContext = new Stack<ObjectDOS>();
		
		Interpreter(ObjectDOS item) {
			currentContext.push(item);
		}

		@Override
		public void inASlotObjectDefinition(ASlotObjectDefinition node) {
			ObjectDOS newNode = new ObjectDOS();
			currentContext.peek().setSlot(Symbol.get(node.getId().getText()), newNode);
			currentContext.push(newNode);
	    }

		@Override
		public void outASlotObjectDefinition(ASlotObjectDefinition node) {
			currentContext.pop();
		}
		
		List<OpCode> opCodes = new ArrayList<OpCode>();
		List<Symbol> requiredArgs = new ArrayList<Symbol>();
		Symbol latestSymbol = null;
		int latestValue = 0;
		
		@Override
		public void inARequiredParameter(ARequiredParameter node) {
			requiredArgs.add(Symbol.get(node.getId().getText()));
		}
		
		@Override
		public void inASymbolOpcodeParameter(ASymbolOpcodeParameter node) {
			latestSymbol = Symbol.get(node.getId().getText());
		}
		@Override
		public void inAValueOpcodeParameter(AValueOpcodeParameter node) {
			latestValue = Integer.parseInt(node.getNumber().getText());
		}

		@Override
		public void outANumberOpcodeId(ANumberOpcodeId node) {
			int opCodeId = Integer.parseInt(node.getNumber().getText().toString());

			addOpcode(opCodeId);
		}

		private void addOpcode(int opCodeId) {
			OpCode result = null;
			
			switch(opCodeId ) {
				case 1:
					result = new OpCode.FunctionCall(latestSymbol);
					break;
				case 2:
					result = new OpCode.SetObject(latestSymbol);
					break;
				case 3:
					result = new OpCode.Push(latestSymbol);
					break;
				case 4:
					result = new OpCode.PushSymbol(latestSymbol);
					break;
				case 5:
					result = new OpCode.CreateValueObject(latestValue);
					break;
				case 6:
					result = new OpCode.StartOpCodeList();
					break;
				case 7:
					result = new OpCode.EndOpCodeList();
					break;
			}
			latestSymbol = null;
			
			opCodes.add(result);
		}
		
		@Override
		public void outAOpcodeOpcodeId(AOpcodeOpcodeId node) {
			
			String opcode = node.getMnemonic().getText().toString();
			if("CALL".equals(opcode)) {
				addOpcode(1);
			} else if("OBJ".equals(opcode)) {
				addOpcode(2);
			} else if("PUSH".equals(opcode)) {
				addOpcode(3);
			} else if("SYM".equals(opcode)) {
				addOpcode(4);
			} else if("VAL".equals(opcode)) {
				addOpcode(5);
			} else if("START".equals(opcode)) {
				addOpcode(6);
			} else if("END".equals(opcode)) {
				addOpcode(7);
			}
		}

		@Override
		public void outAFunctionDefinition(AFunctionDefinition node) {
			FunctionDOS function = new FunctionDOS(requiredArgs.toArray(new Symbol[0]), opCodes.toArray(new OpCode[0]));
			currentContext.peek().setFunction(Symbol.get(node.getId().getText()), function );
			opCodes = new ArrayList<OpCode>();
			requiredArgs = new ArrayList<Symbol>();
		}
		
		List<String> reference = new ArrayList<String>();
		
		@Override
		public void outAEndReferences(AEndReferences node) {
			reference.add(node.getId().getText());
		}
		
		@Override
		public void outAContinueReferences(AContinueReferences node) {
			reference.add(node.getId().getText());
		}
		
		@Override
		public void outAReferenceObjectDefinition(AReferenceObjectDefinition node) {
			Collections.reverse(reference);
			ObjectDOS cursor = currentContext.firstElement();
			for(String s : reference) {
				cursor = cursor.getSlot(Symbol.get(s));
			}
			currentContext.peek().setSlot(Symbol.get(node.getId().getText()), cursor);
			reference.clear();
		}
	}

}
