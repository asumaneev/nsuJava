package ru.nsu.sumaneev.tac.parser.lexer;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import ru.nsu.sumaneev.tac.parser.lexer.Lexeme.LexemeType;

public class Lexer {

	private static final int BUFFER_SIZE = 10;

	private static Map<Character, LexerState> LEXER_STATES = new HashMap<Character, LexerState>();
	
	static {
		
		LEXER_STATES.put('+', LexerState.PLUS_MET);
		LEXER_STATES.put('-', LexerState.MINUS_MET);
		LEXER_STATES.put('*', LexerState.ASTERIX_MET);
		LEXER_STATES.put('/', LexerState.SLASH_MET);
		LEXER_STATES.put('^', LexerState.CARET_MET);
		LEXER_STATES.put('=', LexerState.EQUAL_MET);
		LEXER_STATES.put('<', LexerState.LESS_SIGN_MET);
		LEXER_STATES.put('>', LexerState.GREATER_SIGN_MET);
		LEXER_STATES.put('(', LexerState.OPEN_PARENTHESIS_MET);
		LEXER_STATES.put(')', LexerState.CLOSE_PARENTHESIS_MET);
		LEXER_STATES.put('{', LexerState.OPEN_BRACE_MET);
		LEXER_STATES.put('}', LexerState.CLOSE_BRACE_MET);
		LEXER_STATES.put(',', LexerState.COMMA_MET);
		LEXER_STATES.put('.', LexerState.POINT_MET);
		LEXER_STATES.put(';', LexerState.SEMICOLON_MET);
	}
	
	private Buffer buffer = null;
	
	
	private Character currentChar = null;
	
	private LexerState state = LexerState.DEFAULT;
	
	private int startColumn = 0;
	private int startLine = 0;
	
	
	public Lexer(Reader input) throws IOException {
		buffer = new Buffer(BUFFER_SIZE, input);
		
		currentChar = buffer.getNextChar();
		startColumn = buffer.getCurrentColumn();
		startLine = buffer.getCurrentLine();
		
	}
	
	
	public Lexeme getLexeme() throws IOException, LexerException {

		try {
			
			if (null == currentChar) {
				return null;
			}
			
			//	lexeme was started
			if (LexerState.DEFAULT != state) {
				
				skip();
				
				setState(currentChar);
				
				startColumn = buffer.getCurrentColumn();
				startLine = buffer.getCurrentLine();
				
				Lexeme l = findLexeme();

				
				state = LexerState.DEFAULT;
				
				return l;
				
			}
					
			skip();
			
			switch (state) {
			
			//	if slash was met and it is not comment
			case SLASH_MET: {
			
				Lexeme l = new Lexeme("/", LexemeType.DIVISION, startColumn, startLine);
				
				//	prepare for returning next lexeme
				
				startColumn = buffer.getCurrentColumn();
				startLine = buffer.getCurrentLine();
				
				return l;
			}
			case DEFAULT: {
				setState(currentChar);
				Lexeme l = findLexeme();
				state = LexerState.DEFAULT;
				return l;
			}
			default:
				throw new IllegalArgumentException("invalid lexer state");
			}
			
		}
		catch (IOException e) {
			
			if (0 == e.getMessage().compareTo(Buffer.EOF_MESSAGE)) {
				return null;
			}
			
			throw e;
		}
	}
	
	
	
	private void skip() throws IOException, LexerException {
		
		for (;;) {
			
			//	skip white spaces
			if (Character.isWhitespace(currentChar)) {
				while (Character.isWhitespace( (currentChar =  buffer.getNextChar()) )) {
					
				}
			}
			//	try to skip comments
			else if ('/' == currentChar) {
				
				startColumn = buffer.getCurrentColumn();
				startLine = buffer.getCurrentLine();
				
				state = LexerState.SLASH_MET;
				currentChar = buffer.getNextChar();
				
				//	skip line
				if ('/' == currentChar) {
					state = LexerState.DEFAULT;
					
					int nextLine = buffer.getCurrentLine() + 1;
					
					while (buffer.getCurrentLine() != nextLine) {
						currentChar = buffer.getNextChar();
					}
					
				}
				//	skip until '*/'
				else if ('*' == currentChar) {
					state = LexerState.DEFAULT;
					
					for (;;) {
					
						
						
						try {
						
							//	search next '*'
							while ('*' != (currentChar = buffer.getNextChar())) {
								
							}
							
							//	see next char
							currentChar = buffer.getNextChar();
						
						}
						catch (IOException e) {
							throw new LexerException("unexpected end of file", buffer.getCurrentLine(), buffer.getCurrentColumn());
						}
						
						//	end of comment
						if ('/' == currentChar) {
							
							currentChar = buffer.getNextChar();
							
							//	break from searching end of comment
							break;
						}
					}
				}
				//	not comment
				else {
					
					if (Character.isWhitespace(currentChar)) {
						//state = LexerState.DEFAULT;
					}
					
					return;
				}
			}
			//	nothing to skip
			else {
				return;
			}
		
		}
	}
	
	private void setState(char ch) throws LexerException {
		
		
		if (Character.isLetter(ch)) {
			state = LexerState.LETTER_MET;
		} 
		else if (Character.isDigit(ch)) {
			state = LexerState.NUMBER_MET;
		}
		else if (Character.isWhitespace(ch)) {
			state = LexerState.DEFAULT;
		}
		else {
			state = LEXER_STATES.get(ch);
			
			if (null == state) {
				throw new LexerException("unknown lexeme", buffer.getCurrentLine(), buffer.getCurrentColumn());
			}

		}
	}
	
	private Lexeme findLexeme() throws IOException, LexerException {
		
		String lexeme = String.valueOf(currentChar);
		LexemeType type = null;
		
		
		try {
			currentChar = buffer.getNextChar();
		}
		catch (IOException e) {
			
			if (0 != e.getMessage().compareTo(Buffer.EOF_MESSAGE)) {
				throw e;
			}
			
			currentChar = null;
		}
		
		switch (state) {
		case ASTERIX_MET:
			type = LexemeType.MULTIPLICATION;
			break;
		case CARET_MET:
			type = LexemeType.CARET;
			break;
		case CLOSE_BRACE_MET:
			type = LexemeType.CLOSE_BRACE;
			break;
		case CLOSE_PARENTHESIS_MET:
			type = LexemeType.CLOSE_PARENTHESIS;
			break;
		case COMMA_MET:
			type = LexemeType.COMMA;
			break;
		case EQUAL_MET:
			type = LexemeType.EQUAL;
			break;
		case LESS_SIGN_MET:
			type = LexemeType.LESS_SIGN;
			break;
		case GREATER_SIGN_MET:
			type = LexemeType.GREATER_SIGN;
			break;
		case LETTER_MET:
			
			//	name or type
			
			//	search while letter or number
			
			try {
			
				while ( (null != currentChar) && ((Character.isAlphabetic(currentChar)) || (Character.isDigit(currentChar)) || ('_' == currentChar)) ) {
					lexeme += String.valueOf(currentChar);
					currentChar = buffer.getNextChar();
				}
			
			}
			catch (IOException e) {
				
				if (0 != e.getMessage().compareTo(Buffer.EOF_MESSAGE)) {
					throw e;
				}
				
				currentChar = null;
			}
			
			if ((lexeme.equals("int")) || (lexeme.equals("double")) || (lexeme.equals("void"))) {
				type = LexemeType.TYPE;
			}
			else if (lexeme.equals("return")) {
				type = LexemeType.RETURN;
			}
			else if (lexeme.equals("while")) {
				type = LexemeType.WHILE;
			}
			else if (lexeme.equals("if")) {
				type = LexemeType.IF;
			}
			else if (lexeme.equals("else")) {
				type = LexemeType.ELSE;
			}
			else {
				type = LexemeType.NAME;
			}
			
			break;
		case MINUS_MET:
			type = 	LexemeType.MINUS;		
			break;
		case NUMBER_MET:
			//	only number
			
			type = LexemeType.NUMBER;
			
			//	search while point or number
			//	if find point - search while number
			
			try {
				
				while ((null != currentChar) && ((Character.isDigit(currentChar)) || ('.' == currentChar)) ) {
					
					lexeme += String.valueOf(currentChar);
					
					if ('.' == currentChar) {
						currentChar = buffer.getNextChar();
						while (Character.isDigit(currentChar)) {
							lexeme += String.valueOf(currentChar);
							currentChar = buffer.getNextChar();
						}
						
						break;
						
					}
					
					currentChar = buffer.getNextChar();
				}
			}
			catch (IOException e) {
				
				/*
				if (e.getMessage().equals(Buffer.EOF_MESSAGE)) {
					throw e;
				}
				*/
				
				currentChar = null;
			}
		
		
			break;
		case OPEN_BRACE_MET:
			type = LexemeType.OPEN_BRACE;
			break;
		case OPEN_PARENTHESIS_MET:
			type = LexemeType.OPEN_PARENTHESIS;
			break;
		case PLUS_MET:
			type = LexemeType.PLUS;
			break;
		case POINT_MET:
			type = LexemeType.POINT;
			break;
		case SEMICOLON_MET:
			type = LexemeType.SEMICOLON;
			break;
		case SLASH_MET:
			type = LexemeType.DIVISION;
			break;
		default:
			
			throw new LexerException("illegal lexer's state", buffer.getCurrentColumn(), buffer.getCurrentLine());		
		}
		
		
		return new Lexeme(lexeme, type, startColumn, startLine);
	}
	
	
	private enum LexerState {
		
		LETTER_MET,
		NUMBER_MET,
		
		PLUS_MET,
		MINUS_MET,
		SLASH_MET,
		ASTERIX_MET,
		CARET_MET,
		EQUAL_MET,
		GREATER_SIGN_MET,
		LESS_SIGN_MET,
		OPEN_PARENTHESIS_MET,
		CLOSE_PARENTHESIS_MET,
		OPEN_BRACE_MET,
		CLOSE_BRACE_MET,
		COMMA_MET,
		POINT_MET,
		SEMICOLON_MET,
		
		
				
		
		DEFAULT,
		ILLEGAL_CHAR
	}
	
}
