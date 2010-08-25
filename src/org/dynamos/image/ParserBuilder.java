package org.dynamos.image;

import java.io.PushbackReader;
import java.io.StringReader;

import org.dynamos.image.lexer.Lexer;
import org.dynamos.image.parser.Parser;

public class ParserBuilder {

	public Parser build(String string) {
		return new Parser(new Lexer(new PushbackReader(new StringReader(string))));
	}

}
