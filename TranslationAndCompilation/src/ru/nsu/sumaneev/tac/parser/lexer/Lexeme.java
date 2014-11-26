package ru.nsu.sumaneev.tac.parser.lexer;

public class Lexeme {

	public enum LexemeType {
		TYPE,
		
		NAME,
		NUMBER,
		
		RETURN,
		
		WHILE,
		IF,
		ELSE,
		
		PLUS,
		MINUS,
		DIVISION,
		MULTIPLICATION,
		CARET,
		EQUAL,
		GREATER_SIGN,
		LESS_SIGN,
		
		OPEN_PARENTHESIS,
		CLOSE_PARENTHESIS,
		
		OPEN_BRACE,
		CLOSE_BRACE,
		
		SEMICOLON,
		
		COMMA,
		POINT
	}
	
	private String value = null;
	private LexemeType type = null;
	
	private int column = 0;
	private int line = 0;
	
	
	public Lexeme(String value, LexemeType type, int column, int line) {
		this.value = value;
		this.type = type;
		this.column = column;
		this.line = line;
	}
	
	public LexemeType getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
	
	public int getColumn() {
		return column;
	}
	
	public int getLine() {
		return line;
	}
	
}
