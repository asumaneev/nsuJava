package ru.nsu.sumaneev.tac.parser.lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Reader {

	private BufferedReader input = null;
	
	public Reader() {
	}
	
	public Reader(String fileName) throws IOException {
		setInput(fileName);
	}
	
	public Reader(InputStreamReader input) throws IOException {
		setInput(input);
	}
	
	
	
	public void setInput(String fileName) throws IOException {
		
		close();
		
		input = new BufferedReader(new FileReader(fileName));
	}
	
	public void setInput(InputStreamReader input) throws IOException {
		
		close();
		
		this.input = new BufferedReader(input);
	}

	
	public int read(char[] buffer) throws IOException {
		
		return input.read(buffer);
	}
	
	public int read(char[] buffer, int offset, int length) throws IOException {
		return input.read(buffer, offset, length);
	}
	
	
	public void close() throws IOException {
		
		if (null != input) {
		
			input.close();
		}
	}
	

}
