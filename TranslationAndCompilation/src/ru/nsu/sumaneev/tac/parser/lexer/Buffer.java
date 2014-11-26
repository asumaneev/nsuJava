package ru.nsu.sumaneev.tac.parser.lexer;

import java.io.IOException;
import java.io.Reader;

public class Buffer {
	
	public static final String EOF_MESSAGE = "EOF";
	
	private char[] buffer = null;
	
	private Reader reader = null;
	
	private int lastPosition = 0;
	private int bufferEnd = 0;

	private int currentColumn = 0;
	private int currentLine = 1;

	
	public Buffer(int size, Reader reader) throws IOException {

		this.reader = reader;
		
		buffer = new char[size];
		this.bufferEnd = this.reader.read(buffer);
	}
	
	/**
	 * 
	 * @param size - new size of buffer
	 * @return new size of buffer
	 * @throws IllegalArgumentException if size is non-positive or unread data will be lost
	 * @throws IOException if new data cannot be read from reader
	 */
	
	public int resize(int size) throws IllegalArgumentException, IOException {
		
		
		if (size <= 0) {
			throw new IllegalArgumentException("invalid buffer size");			
		}
		
		
		if (size == buffer.length) {
			return size;
		}
		
		//	copy old buffer
		char[] oldBuffer = buffer;
		
		//	create new buffer
		buffer = new char[size];

		//	increase buffer
		if (oldBuffer.length < buffer.length) {
		
			//	copy old buffer in new buffer
			System.arraycopy(oldBuffer, 0, buffer, 0, oldBuffer.length);
			
			//	fill end of the new buffer
			bufferEnd += reader.read(buffer, oldBuffer.length, buffer.length - oldBuffer.length);
			
		}
		//	shrink buffer
		else {
			
			//	check if some characters would be lost
			if (lastPosition < oldBuffer.length - buffer.length) {
				throw new IllegalArgumentException("data will be lost");
			}
			
			//	copy end of old buffer in new buffer
			System.arraycopy(oldBuffer, oldBuffer.length - buffer.length, buffer, 0, buffer.length);
			
			bufferEnd = buffer.length;
		}

		return buffer.length;
	}
	
	public int size() {
		return buffer.length;
	}
	
	/**
	 * returns next character and moves carriage on the one step
	 * @throws IOException if new data cannot be read from reader or EOF
	 */
	
	public Character getNextChar() throws IOException {

		if (-1 == bufferEnd) {
			return null;
		}
		
		//	if last char in buffer was read
		if (bufferEnd == lastPosition) {
			
			bufferEnd = reader.read(buffer);
			
			if ( (-1 == bufferEnd) ) {
				reader.close();
				throw new IOException(EOF_MESSAGE);
			}
			
			lastPosition = 0;
		}
		
		char ch = buffer[lastPosition++];
		
		
		if ('\n' == ch) {
			currentColumn = 0;
			++currentLine;
		}
		else {
			++currentColumn;
		}
		
		return ch;
	}

	
	public int getCurrentColumn() {
		return currentColumn;
	}
	
	public int getCurrentLine() {
		return currentLine;
	}
}
