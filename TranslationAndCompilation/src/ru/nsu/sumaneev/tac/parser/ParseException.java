package ru.nsu.sumaneev.tac.parser;

public class ParseException extends Exception {

	public ParseException(String message) {
		super(message);
	}
	
	
	public ParseException(String message, int line, int column) {
		super(message + ": at " + line + ":" + column);
	}
	
}
