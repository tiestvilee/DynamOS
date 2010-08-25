package org.dynamos.image;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.Symbol;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class ImageStreamReaderTest {

	private ImageStreamReader reader;
	
	@Before
	public void setup() {
		reader = new ImageStreamReader(new ParserBuilder());
	}
	
	@Test
	public void shouldPassStringThroughToParserBuilder() {
		ParserBuilder parserBuilder = mock(ParserBuilder.class);
		when(parserBuilder.build("something")).thenReturn(new ParserBuilder().build("anObject{}{}"));
		reader = new ImageStreamReader(parserBuilder);
		
		reader.read("something");
		
		verify(parserBuilder).build("something");
	}

	@Test
	public void shouldLoadSimpleObject() {
		
		ObjectDOS wrapper = reader.read("anObject{}{}");
		
		ObjectDOS actual = wrapper.getSlot(Symbol.get("anObject"));
		
		assertThat(actual.getFunctions().size(), is(0));
		assertThat(actual.getSlots().size(), is(0));
	}

	@Test
	public void shouldLoadNestedObject() {
		ObjectDOS wrapper = reader.read("anObject{nestedObject{deeplyNestedObject{}{}}{}anotherNestedObject{}{}}{}");
		
		ObjectDOS actual = wrapper.getSlot(Symbol.get("anObject"));
		
		assertThat(actual.getFunctions().size(), is(0));
		assertThat(actual.getSlot(Symbol.get("nestedObject")).getSlot(Symbol.get("deeplyNestedObject")), notNullValue());
		assertThat(actual.getSlot(Symbol.get("anotherNestedObject")), notNullValue());
	}


	@Test
	public void shouldLoadEmptyFunction() {
		ObjectDOS wrapper = reader.read("anObject{}{aFunction:Does:This:{}{}}");
		
		ObjectDOS actual = wrapper.getSlot(Symbol.get("anObject"));
		
		FunctionDOS function = (FunctionDOS) actual.getFunction(Symbol.get("aFunction:Does:This:"));
		assertThat(function.getOpCodes().length, is(0));
		assertThat(function.getRequiredArguments().length, is(0));
	}
}
