package ru.nsu.sumaneev.tac.parser.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


import ru.nsu.sumaneev.tac.parser.lexer.Lexeme;
import ru.nsu.sumaneev.tac.parser.lexer.Lexeme.LexemeType;
import ru.nsu.sumaneev.tac.parser.lexer.Lexer;
import ru.nsu.sumaneev.tac.parser.lexer.LexerException;


public class LexerTest {

	
	private Lexer lexer = null;
	
	private static final String[] lexemeStrings = {"a", "b", "c", "asdds",
			",", ",", ",", ";", 
			"12345.5454", "return",
			"+", "-", "*", "/", "=", "^",
			"double", "void", "int", "while", "if", "<", ">", "else"};	
	
	
	private static final LexemeType[] lexemeTypes = {LexemeType.NAME, LexemeType.NAME, LexemeType.NAME, LexemeType.NAME, 
			LexemeType.COMMA, LexemeType.COMMA, LexemeType.COMMA, LexemeType.SEMICOLON,
			LexemeType.NUMBER, LexemeType.RETURN, 
			LexemeType.PLUS, LexemeType.MINUS, LexemeType.MULTIPLICATION, LexemeType.DIVISION, LexemeType.EQUAL, LexemeType.CARET,
			LexemeType.TYPE, LexemeType.TYPE, LexemeType.TYPE, 
			LexemeType.WHILE, LexemeType.IF, LexemeType.LESS_SIGN, LexemeType.GREATER_SIGN, LexemeType.ELSE};
	
	private static final String testString = ( lexemeStrings[0] + "               "
			+ lexemeStrings[1] + "		\n\n		 "
			+ lexemeStrings[2] + " "
			+ lexemeStrings[3]
			+ lexemeStrings[4] + lexemeStrings[5] + "   " + lexemeStrings[6] + lexemeStrings[7]
			+ "/*not **** /* insteresing*/"
			+ "// another comment \n "
			+ lexemeStrings[8]
			+ "// /* */ and another \n"
			+ lexemeStrings[9] 
			+ lexemeStrings[10]
			+ lexemeStrings[11]
			+ lexemeStrings[12]
			+ lexemeStrings[13] + "  " 
			+ lexemeStrings[14]
			+ lexemeStrings[15]
			+ lexemeStrings[16] + " "
			+ lexemeStrings[17] + " "
			+ lexemeStrings[18] + "  "
			+ lexemeStrings[19] + "  "
			+ lexemeStrings[20] + " "
			+ lexemeStrings[21]
			+ lexemeStrings[22]
			+ lexemeStrings[23]
			);

	@Test
	public void compareLexemesFromStringAndCheckEndOfString() throws IOException, LexerException {
		
		lexer = new Lexer(new InputStreamReader(new ByteArrayInputStream(testString.getBytes())));
		
		
		for (int i = 0; i < lexemeStrings.length; ++i) {
			Lexeme rightLexeme = new Lexeme(lexemeStrings[i], lexemeTypes[i], 0, 0);
			
			Lexeme resultLexeme = lexer.getLexeme();
			
			
			assertThat(resultLexeme.getValue(), is(rightLexeme.getValue()));
			assertThat(resultLexeme.getType(), is(rightLexeme.getType()));
			
		}
		
		assertThat(null, is(lexer.getLexeme()));
		
	}
	
}
