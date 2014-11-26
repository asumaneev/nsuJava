package ru.nsu.sumaneev.tac.compiler;

import java.io.IOException;

public class TranslationAndCompilationStarter {

	public static void main(String[] args) throws IOException, CompilerException {
		
		Compiler c = new Compiler();
		
		c.compile("Sqrt.mylang");
	}
	
}
