package ru.nsu.sumaneev.tac.parser.lexer;

public class LexerException extends Exception {

	public LexerException(String message, int line, int column) {
		super(message + ": at " + line + ":" + column);
	}
	
}
