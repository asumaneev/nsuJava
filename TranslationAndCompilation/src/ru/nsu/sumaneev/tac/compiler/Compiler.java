package ru.nsu.sumaneev.tac.compiler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import ru.nsu.sumaneev.tac.parser.ParseException;
import ru.nsu.sumaneev.tac.parser.Parser;
import ru.nsu.sumaneev.tac.parser.lexer.Lexer;
import ru.nsu.sumaneev.tac.parser.lexer.LexerException;
import ru.nsu.sumaneev.tac.parser.tree.BinaryNode;
import ru.nsu.sumaneev.tac.parser.tree.BodyNode;
import ru.nsu.sumaneev.tac.parser.tree.FunctionDeclarationNode;
import ru.nsu.sumaneev.tac.parser.tree.FunctionNode;
import ru.nsu.sumaneev.tac.parser.tree.IfNode;
import ru.nsu.sumaneev.tac.parser.tree.Node;
import ru.nsu.sumaneev.tac.parser.tree.WhileNode;
import ru.nsu.sumaneev.tac.parser.tree.Node.NodeValueType;
import ru.nsu.sumaneev.tac.parser.tree.ProgramNode;
import ru.nsu.sumaneev.tac.parser.tree.VarListNode;

public class Compiler {
	
	private String packegeName = "unknownpackage";
	private String className = "UnknownClass";
	
	private PrintStream out = System.out;
	
	
	private ProgramNode parsedProgramRoot = null;
	
	
	private boolean isMainFound = false;
	
	//	key - function name, value - array of types: first - return type, other - parameters' types
	private Map<String, Type[]> functions = new HashMap<String, Type[]>();
	
	public void compile(String fileToCompile) throws IOException, CompilerException {
		
		packegeName = fileToCompile.substring(0, fileToCompile.lastIndexOf('.'));
		className = packegeName.substring(0, 1).toUpperCase() + packegeName.substring(1);
		
		Parser parser = null;
		
		try {
			parser = new Parser( new Lexer(new FileReader(fileToCompile)) );
		} 
		catch (FileNotFoundException e) {
			System.out.println("File \"" + fileToCompile + "\" is not found");
			
			return;
		}
		catch (LexerException e) {
			System.out.println("ERROR: " + e.getMessage());
			
			return;
		}
		
		
		compile(parser);
		
		
		
	}
	
	public void compile(Parser parser) throws IOException, CompilerException {
		
		try {
			parsedProgramRoot = parser.parseProgram();
		} catch (ParseException | LexerException e) {
			System.out.println("ERROR: " + e.getMessage());
			
			return;
		}
		
		out = new PrintStream(className + ".j");
		
		compileProgram(parsedProgramRoot);
		
		if (false == isMainFound) {
			throw new CompilerException("main function is not found");
		}
		
		
		System.out.println("successful compilation");
	}
	
	
	public void compileProgram(ProgramNode programRoot) throws CompilerException {
		
		
		initProgram();
		
		List<Node> functions = programRoot.getChildren();
		
		for (Node function : functions) {
			
			FunctionCompiler functionCompiler = new FunctionCompiler();
			
			functionCompiler.compile((FunctionDeclarationNode) function);
			
		}
	}
	
	public static Type typeOfNumber(String number) {
		
		try {
		
			Integer.parseInt(number);
		}
		catch (NumberFormatException e) {
			return Type.DOUBLE;
		}
		
		return Type.INT;
	}
	
	private void initProgram() {
		//	declare class
		out.println(
				".class public "
				 //+ packegeName + "/"
				 + className
		);
		
		//	inherit Object
		out.println(".super java/lang/Object ");
		
		//	initialization
		
		out.println();
		
		out.println(".method public <init>()V");
		out.println("	aload_0");
		out.println("	invokespecial java/lang/Object/<init>()V");
		out.println("	return");
		out.println(".end method");
	}
	
private class FunctionCompiler {
		
		
		public static final String BEGIN_LABEL = "LABEL_0";
		public static final String END_LABEL = "LABEL_1";
		
		private static final String FIRST_TABULATION = "	";
		
		private int stackSize = 0;
		private int localVariablesNumber = 0;
		
		
		private String currentFunctionName = null;
		private Type[] currentFunctionReturnAndparametersTypes = new Type[1];
		
		
		private Type returnType = null;
		
		
		private int currentLocalVariable = 0;
		
		
		//	maps variables names on numbers
		private Map<String, AbstractMap.SimpleEntry<String, Type>> localVariablesMaping
				= new HashMap<String, AbstractMap.SimpleEntry<String, Type>>();
		
		/**
		 * compiles function with its own stack and local variables
		 * @param functionDeclarationRoot
		 * @throws CompilerException
		 */
		public void compile(FunctionDeclarationNode functionDeclarationRoot) throws CompilerException {
			
			currentFunctionName = functionDeclarationRoot.getValue();
			localVariablesNumber = functionDeclarationRoot.getVariablesNumber();
			stackSize = functionDeclarationRoot.getStackSize();
			
			if (functions.containsKey(currentFunctionName)) {
				throw new CompilerException("function \"" + currentFunctionName + "\" declared twice");
			}
			
			out.println();
			
			out.print(
					".method public static " 
					+ currentFunctionName
					+ "("
			);
			
			//	main must not have parameters
			if (functionDeclarationRoot.getValue().equals("main")) {
				
				if (0 != functionDeclarationRoot.getVarList().getChildren().size()) {
					throw new CompilerException("function main cannot have parameters");
				}
				
				out.print("[Ljava/lang/String;");
				
				//	zero is String[] args
				++currentLocalVariable;
				++localVariablesNumber;
				
				isMainFound = true;
			}
			else {
				compileVarList(functionDeclarationRoot.getVarList());			
			}
			
			out.print(")");
			
			returnType = Type.getType(functionDeclarationRoot.getReturnType());
			
			out.println( Type.getReturnType((returnType)) );
			
			currentFunctionReturnAndparametersTypes[0] = returnType;
			
			
			out.println(FIRST_TABULATION + ".limit stack " + String.valueOf(stackSize));
			out.println(FIRST_TABULATION + ".limit locals " + String.valueOf(localVariablesNumber));
			
			
			//	declare local variables
			Set<String> variableNames = localVariablesMaping.keySet();
			
			for (String variableName : variableNames) {
				
				SimpleEntry<String, Type> numberType = localVariablesMaping.get(variableName);
				
				out.println(
						".var " + numberType.getKey() 
						+ " is " + variableName + " " + Type.getReturnType(numberType.getValue()) 
						+ " from " + BEGIN_LABEL + " to " + END_LABEL);
				
			}
			
			BodyCompiler mainBodyCompiler = new BodyCompiler(
					BEGIN_LABEL, END_LABEL, 
					localVariablesNumber, currentLocalVariable, 
					localVariablesMaping, 
					returnType,
					FIRST_TABULATION);
			
			out.println();
			mainBodyCompiler.compileBody(functionDeclarationRoot.getBody());
			out.println();
			
			if (false == mainBodyCompiler.isReturnStmtFound()) {
				
				if (Type.VOID ==  returnType) {
					
					out.println("#return_stmt:");
					
					out.println(FIRST_TABULATION + "return");
				}
				else {
					
					throw new CompilerException("return statement is missing");
				}
			}
			else {
				
				out.println(FIRST_TABULATION + "return");
			}
			
			out.println();
			
			out.println(".end method");
			
			functions.put(currentFunctionName, currentFunctionReturnAndparametersTypes);
			
		}
		
		private void compileVarList(VarListNode varList) throws CompilerException {
			
			
			List<Node> variables = varList.getChildren();
			
			currentFunctionReturnAndparametersTypes = new Type[variables.size() + 1];
			
			if (0 == variables.size()) {

				return;
			}
			
			ListIterator<Node> i = variables.listIterator();
			
			while(i.hasNext()) {
				
				if (localVariablesNumber < currentLocalVariable) {
					throw new CompilerException("too many parameters");
				}
				
				
				Node currentVariable = i.next();
				
				int index = i.nextIndex();
				String name = ((BinaryNode) currentVariable).getLeftChild().getValue();
				Type type = Type.getType(currentVariable.getValue());
				
				checkVariableDuplication(name);
				
				localVariablesMaping.put
				(name, new SimpleEntry<String, Compiler.Type>(String.valueOf(currentLocalVariable), type));
				
				
				if (Type.DOUBLE == type) {
					currentLocalVariable += 2;
				}
				else {
					currentLocalVariable += 1;
				}
				
				
				currentFunctionReturnAndparametersTypes[index] = type;
				
				out.print( Type.getReturnType((type)) );
			}
			
		}
		
		private void checkVariableDuplication(String name) throws CompilerException {
			if (localVariablesMaping.containsKey(name)) {
				throw new CompilerException("Variable \"" + name + "\" declared twice");
			}
		}
		
	}
	
	private class BodyCompiler {

		private String beginLabel = null;
		private String endLabel = null;
		private int currentInnerBody = 0;
		
		private String tabulation = null;
		
		private int localVariablesNumber = 0;
		
		private boolean isReturnStmtFound = false;
		private Type returnType = null;
		
		
		private int currentLocalVariable = 0;
		
		//	maps variables names on numbers
		private Map<String, AbstractMap.SimpleEntry<String, Type>> localVariablesMaping
			= new HashMap<String, AbstractMap.SimpleEntry<String, Type>>();

		
		public BodyCompiler(
				String beginLabel, String endLabel,
				int localVariablesNumber, int currentLocalVariable, 
				Map<String, AbstractMap.SimpleEntry<String, Type>> localVariablesMaping,
				Type returnType,
				String tabulation) {
			this.beginLabel = beginLabel;
			this.endLabel = endLabel;
			
			this.localVariablesNumber = localVariablesNumber;
			this.currentLocalVariable = currentLocalVariable;
			//this.localVariablesMaping = localVariablesMaping;
			this.returnType = returnType;
			
			Set<String> variables = localVariablesMaping.keySet();
			
			for (String name : variables) {
				this.localVariablesMaping.put(name, localVariablesMaping.get(name));
			}
			
			this.tabulation = tabulation;
		}
		
		public boolean isReturnStmtFound() {
			return isReturnStmtFound;
		}

		public void compileBody(BodyNode bodyRoot) throws CompilerException {
			
			out.println(beginLabel + ":");
			
			List<Node> commands = bodyRoot.getChildren();
			
			for (Node command : commands) {
				
				compileCommand(command);
			}
			
			out.println(endLabel + ":");
		}
		
		public void compileIfElseBody(BodyNode ifBody, BodyNode elseBody, Node leftExpression, Node rightExpression, String sign) throws CompilerException {
			
			compileCondition(leftExpression, rightExpression, sign, endLabel);
			
			out.println(beginLabel + ":");
			
			List<Node> commands = ifBody.getChildren();
			
			for (Node command : commands) {
				
				compileCommand(command);
			}
			
			if (null != elseBody) {
			
				out.println(tabulation + "goto " + endLabel + "_else_end");
			}
			
			out.println(endLabel + ":");
			
			if (null != elseBody) {
			
				BodyCompiler elseBodyCompiler = new BodyCompiler(
						beginLabel + "_else_begin", 
						endLabel + "_else_end", 
						localVariablesNumber, 
						currentLocalVariable, 
						localVariablesMaping, 
						returnType,
						tabulation);
				
				elseBodyCompiler.compileBody(elseBody);
				
				if ((isReturnStmtFound()) && (elseBodyCompiler.isReturnStmtFound())) {
					isReturnStmtFound = true;
				}
			
			}
			
		}
		
		public void complineWhileBody(BodyNode bodyRoot, Node leftExpression, Node rightExpression, String sign) throws CompilerException {
			
			out.println(beginLabel + ":");
			
			compileCondition(leftExpression, rightExpression, sign, endLabel);
			
			List<Node> commands = bodyRoot.getChildren();
			
			for (Node command : commands) {
				
				compileCommand(command);
			}
			
			
			
			out.println(tabulation + "goto " + beginLabel);
			
			out.println(endLabel + ":");
		}
		
		
		/**
		 * adds record to localVariablesMaping, initialize new local variable with zero
		 * @param variableDeclarationRoot
		 * @throws CompilerException 
		 */
		private void compileVariableDeclaration(BinaryNode variableDeclarationRoot) throws CompilerException {
			
			String number = String.valueOf(currentLocalVariable);
			String name = variableDeclarationRoot.getLeftChild().getValue();
			Type type = Type.getType(variableDeclarationRoot.getValue());
			
			checkVariableDuplication(name);
			
			if (localVariablesNumber < currentLocalVariable) {
				throw new CompilerException("too many parameters");
			}
			
			localVariablesMaping.put
			(name, new SimpleEntry<String, Compiler.Type>(number, type));
			
			//out.println("#" + variableDeclarationRoot.getLeftChild().getValue() + "_var:");
			out.println();
			out.println(
					".var " + number 
					+ " is " + name + " " + Type.getReturnType(type) 
					+ " from " + beginLabel + " to " + endLabel);
			
			out.println(tabulation + "ldc 0");
			
			addCast(Type.INT, type, tabulation);

			out.println(tabulation + Type.getType(type) + "store " + String.valueOf(currentLocalVariable));
			
			if (Type.DOUBLE == type) {
				currentLocalVariable += 2;
			}
			else {
				currentLocalVariable += 1;
			}
			
		}
		
		/**
		 * check return statement and function's return type
		 * adds return statement
		 * 
		 * returned value is calculated head of stack
		 * 
		 * @param returnRoot
		 * @throws CompilerException
		 */
		private void compileReturnStmt(BinaryNode returnRoot) throws CompilerException {
			
			
			if (Type.VOID == returnType) {
				if (null != returnRoot.getLeftChild()) {
					throw new CompilerException("invalid return statement");
				}
			}
			else {
				if (null == returnRoot.getLeftChild()) {
					throw new CompilerException("invalid return statement");
				
				}
			}
			
			out.println("#return_stmt:");
			
			if (null != returnRoot.getLeftChild()) {
				
				Type expressionType = compileExpression(returnRoot.getLeftChild());
				
				if (false == isCastOk(returnType, expressionType)) {
					throw new CompilerException("cannot cast return statement");
				}
				
				addCast(expressionType, returnType, tabulation);
				
				out.println(tabulation + Type.getType(returnType) + "return");
			}
			else {
				out.println(tabulation + "return");
			}
			
			
			
			isReturnStmtFound = true;
		}
		
		/**
		 * calculates right part of equation in stack
		 * stores head of stack in left part of equation
		 * 
		 * @param equationRoot
		 * @throws CompilerException
		 */
		private void compileEquation(BinaryNode equationRoot) throws CompilerException {
			
			String name = equationRoot.getLeftChild().getValue();
			SimpleEntry<String, Type> numberType = localVariablesMaping.get(name);
			
			checkVariableIsDeclared(name);
			
			out.println("#" + name + "_equation:");
			
			Type expressionType = compileExpression(equationRoot.getRightChild());
			
			if (false == isCastOk(numberType.getValue(), expressionType)) {
				throw new CompilerException("cannot cast in equation");
			}
			
			addCast( expressionType, numberType.getValue(), tabulation );
			
			out.println( tabulation + Type.getType(numberType.getValue()) + "store " + numberType.getKey() );
			
		}
		
		/**
		 * finds type of command and compile with right method
		 * @param commandRoot
		 * @throws CompilerException
		 */
		private void compileCommand(Node commandRoot) throws CompilerException {
			
			switch (commandRoot.getType()) {
			case BINARY_NODE: {
				
				switch (commandRoot.getValueType()) {
				
				//	case equation
				case BINARY_OPERATOR: 
					
					compileEquation((BinaryNode) commandRoot);
					
					break;
					
				//case return statement
				case RETURN_STATEMENT:
					
					compileReturnStmt((BinaryNode) commandRoot);
					
					break;
					
				//	case variable declaration
				case VARIABLE_DECLARATION:
					
					compileVariableDeclaration((BinaryNode) commandRoot);
					
					break;
					
				default:
					
					throw new CompilerException("can not compile command: invalid root value type: " + commandRoot.getType());
				}
				
				break;
			}				
			//	case function call
			case FUNCTION_NODE: {
				
				compileFunctionCall((FunctionNode) commandRoot);
				
				Type[] t = functions.get(commandRoot.getValue());
				
				if ((null != t) && (Type.VOID != t[0])) {
					out.println(tabulation + "pop");
				}
				
				break;
			}
			case IF_NODE: {
				
				IfNode ifRoot = (IfNode) commandRoot;
				
				BodyCompiler ifBodyCompiler = new BodyCompiler(
						beginLabel + "x" + currentInnerBody, 
						endLabel + "x" + currentInnerBody, 
						localVariablesNumber, 
						currentLocalVariable, 
						localVariablesMaping, 
						returnType,
						tabulation + "	");
				++currentInnerBody;
				
				ifBodyCompiler.compileIfElseBody(
						ifRoot.getIfBody(), 
						ifRoot.getElseBody(), 
						ifRoot.getLeftExpression(), 
						ifRoot.getRightExpression(), 
						ifRoot.getValue());
				
				isReturnStmtFound |= ifBodyCompiler.isReturnStmtFound();
				
				/*
				compileCondition(ifRoot.getLeftExpression(), ifRoot.getRightExpression(), ifRoot.getValue(), endLabel + "x" + currentInnerBody);
				
				out.println();
				ifBodyCompiler.compileBody(ifRoot.getIfBody());
				out.println(tabulation + "goto " + endLabel + "x" + currentInnerBody);
				out.println();
				
				if (null != ifRoot.getElseBody()) {
				
					BodyCompiler elseBodyCompiler = new BodyCompiler(
							beginLabel + "x" + currentInnerBody, 
							endLabel + "x" + currentInnerBody, 
							localVariablesNumber, 
							currentLocalVariable, 
							localVariablesMaping, 
							returnType,
							tabulation + "	");
					
					++currentInnerBody;
					
					out.println();
					elseBodyCompiler.compileBody(ifRoot.getElseBody());
					out.println();
					
					if ((ifBodyCompiler.isReturnStmtFound()) && (elseBodyCompiler.isReturnStmtFound())) {
						isReturnStmtFound = true;
					}
				
				}
				*/
				
				
				break;
			}
			case WHILE_NODE: {
				
				
				WhileNode whileRoot = (WhileNode) commandRoot;
				
				BodyCompiler whileBodyCompiler = new BodyCompiler(
						beginLabel + "x" + currentInnerBody, 
						endLabel + "x" + currentInnerBody, 
						localVariablesNumber, 
						currentLocalVariable, 
						localVariablesMaping, 
						returnType,
						tabulation + "	");
				
				++currentInnerBody;
				
				out.println();
				whileBodyCompiler.complineWhileBody(
						whileRoot.getBody(), 
						whileRoot.getLeftExpression(), 
						whileRoot.getRightExpression(), 
						whileRoot.getValue());
				out.println();
				
				/*
				if (whileBodyCompiler.isReturnStmtFound()) {
					isReturnStmtFound = true;
				}
				*/
				
				break;
			}
			default:
				throw new CompilerException("can not compile command: invalid tree");
			}
			
		}
		
		private Type compileFunctionCall(FunctionNode functionRoot) throws CompilerException {
			
			//	calculate
			//	push parameters into stack
			//	call function
			
			List<Node> parameters = functionRoot.getChildren();
			
			out.println("#call_" + functionRoot.getValue() + ":");
			
			if (functionRoot.getValue().equals("print")) {
				
				if (1 != parameters.size()) {
					throw new CompilerException("too many parameters for \"print\"");
				}
				
				out.println(tabulation + "getstatic java/lang/System/out Ljava/io/PrintStream;");
				
				Type returnedType = compileExpression(parameters.get(0));
				
				out.println(tabulation + "invokevirtual java/io/PrintStream/println(" + Type.getReturnType(returnedType) + ")V");
				
				return returnedType;
			}
			else {
				
				if (false == functions.containsKey(functionRoot.getValue())) {
					throw new CompilerException("function \"" + functionRoot.getValue() + "\" is not declared");
				}
				
				Type[] returnAndParametersTypes = functions.get(functionRoot.getValue());
				
				
				ListIterator<Node> currentParamter = parameters.listIterator();
				
				//	add parameters to stack
				while (currentParamter.hasNext()) {
					
					Type typeToCast = returnAndParametersTypes[currentParamter.nextIndex()];
					
					Node parameterNode = currentParamter.next();
					
					addCast(compileExpression(parameterNode), typeToCast, tabulation);
					
				}
				
				out.println(
						tabulation + "invokestatic " 
						+ getFunctionSignature(functionRoot.getValue(), returnAndParametersTypes)
						);
				
				
				return returnAndParametersTypes[0];
			}
			
		}
		
		
		private Type compileExpression(Node expressionRoot) throws CompilerException {
			
			Type currentType = null;
			
			switch (expressionRoot.getType()) {
			case BINARY_NODE:
				
				BinaryNode root = (BinaryNode) expressionRoot;
				
				//	leaf case
				if ((null == root.getLeftChild()) && (null == root.getRightChild())) {
					
					return compileExpressionLeaf(root);
				}
				
				//	unary node
				if (null == root.getRightChild()) {
					
					return compileExpressionUnaryOperation(root);
					
				}
				//else {
					if (root.getValue().equals("^")) {
						currentType = Type.DOUBLE;
					}
				
					Type leftExpressionType = compileExpression(root.getLeftChild());
					if (null != currentType) {
						addCast(leftExpressionType, currentType, tabulation);
					}
					
					Type rightExpressionType = compileExpression(root.getRightChild());
					if (null != currentType) {
						addCast(rightExpressionType, currentType, tabulation);
					}
					else if (leftExpressionType == rightExpressionType) {
						currentType = leftExpressionType;						
					}
					else {
						
						if (Type.DOUBLE == leftExpressionType) {
							currentType = leftExpressionType;
							
							addCast(rightExpressionType, currentType, tabulation);
						}
						else {
							currentType = rightExpressionType;
							
							out.println(tabulation + "swap");
							addCast(leftExpressionType, currentType, tabulation);
							out.println(tabulation + "swap");
						}
						
					}
					
					
					//	head - second expression
					//	under head - first expression
					switch (root.getValue()) {
				
					
					case "+":
						
						out.println(tabulation + Type.getType(currentType) + "add");
						
						break;
						
					case "-":
						
						out.println(tabulation + Type.getType(currentType) + "sub");
						
						break;
						
					case "*":
						
						out.println(tabulation + Type.getType(currentType) + "mul");
						
						break;
						
					case "/":
						
						out.println(tabulation + Type.getType(currentType) + "div");
						
						break;
						
					case "^":
						
						out.println(tabulation + "invokestatic java/lang/Math/pow(DD)D");
						
						break;
						
					default:
						throw new CompilerException("Invalid binary operator: " + root.getValue());
					}
				
				return currentType;
				
			case FUNCTION_NODE:
				
				return compileFunctionCall((FunctionNode) expressionRoot);
			default:
				
				throw new CompilerException("can not compile expression from both not binary node and not function node");
			}
				
		}
		
		
		private Type compileExpressionLeaf(BinaryNode root) throws CompilerException {
			
			Type leafType = null;
			
			if (NodeValueType.NAME == root.getValueType()) {

				checkVariableIsDeclared(root.getValue());
				
				SimpleEntry<String, Type> numberType = localVariablesMaping.get(root.getValue());
				
				out.println(tabulation + Type.getType(numberType.getValue()) + "load " + numberType.getKey());
				
				
				leafType = numberType.getValue();
			}
			//	number
			else {
				
				if (Type.DOUBLE == typeOfNumber(root.getValue())) {
				
					out.print(tabulation + "ldc2_w ");
				
				}
				else {
					out.print(tabulation + "ldc ");
				}
				
				out.println(root.getValue());
				
				leafType = typeOfNumber(root.getValue()); 
			}
			
			return leafType;
		}
		
		
		private Type compileExpressionUnaryOperation(BinaryNode root) throws CompilerException {
			
			Type returnedType = compileExpression(root.getLeftChild());
			
			//	unary minus case
			if (0 == root.getValue().compareTo("-")) {
				
				out.println(tabulation + "ldc -1");
				
				addCast(Type.INT, returnedType, tabulation);
				
				out.println(tabulation + Type.getType(returnedType) + "mul");
				
			}
			//	unary plus case
			else if (0 == root.getValue().compareTo("+")) {
				
			}
			else {
				throw new CompilerException("unknown unary operator \"" + root.getValue() + "\"");
			}
			
			return returnedType;
			
		}
		
		private void checkVariableDuplication(String name) throws CompilerException {
			if (localVariablesMaping.containsKey(name)) {
				throw new CompilerException("Variable \"" + name + "\" declared twice");
			}
		}
		
		private void checkVariableIsDeclared(String name) throws CompilerException {
			if (false == localVariablesMaping.containsKey(name)) {
				throw new CompilerException("Variable \"" + name + "\" is  not declared");
			}
		}
		
		private void compileCondition(Node leftExpression, Node rightExpression, String sign, String endLabel) throws CompilerException {
			
			Type leftExpressionType = compileExpression(leftExpression);
			Type rightExpressionType = compileExpression(rightExpression);
			
			Type commonType = null;
			if (leftExpressionType == rightExpressionType) {
				commonType = leftExpressionType;
			}
			if (Type.DOUBLE == leftExpressionType) {
				commonType = Type.DOUBLE;
				
				addCast(rightExpressionType, leftExpressionType, tabulation);
			}
			else if (Type.DOUBLE == rightExpressionType) {
				commonType = Type.DOUBLE;
				
				out.println(tabulation + "swap");
				addCast(leftExpressionType, rightExpressionType, tabulation);
				out.println(tabulation + "swap");
			}
			
			out.println();
			if (Type.DOUBLE == commonType) {
				out.println(tabulation + "dcmpg");
			}
			
			out.println(tabulation + getBrachCommand(sign, commonType) + " " + endLabel);
			
		}
		
		private String getBrachCommand(String sign, Type type) {
			
			if (Type.DOUBLE == type) {
				switch (sign) {
				
				case "<":
					
					return "ifge";
					
				case ">":
					
					return "ifle";
				case "=":
					
					return "ifne";
				default:
					return null;
				}	
			
			}
			else {
				switch (sign) {
				
				case "<":
					
					return "if_icmpge";
					
				case ">":
					
					return "if_icmple";
				case "=":
					
					return "if_icmpne";
				default:
					return null;
				}	
			}
			
		}
	}
	
	private void addCast(Type fromType, Type toType, String tabulation) {
		
		if (fromType != toType) {
			out.println(tabulation + Type.getType(fromType) + "2" + Type.getType(toType));
		}
		
	}
	
	private boolean isCastOk(Type left, Type right) {
		
		if ((Type.INT == left) && (Type.DOUBLE == right)) {
			return false;
		}
		
		return true;
		
	}

	private String getFunctionSignature(String functionName, Type[] returnAndParametersTypes) {
		
		String result = (
				//packegeName + "/" +
				className + "/"
				+ functionName + "("
				);
		
		for (int i = 1; i < returnAndParametersTypes.length; ++i) {
			result = result + Type.getReturnType(returnAndParametersTypes[i]);
		}
		
		result = result + ")" + Type.getReturnType(returnAndParametersTypes[0]);
		
		return result;
		
	}
	
	public enum Type {
		
		INT,
		DOUBLE,
		VOID;
		
		public static Type getType(String typeName) {
			
			switch (typeName) {
			case "int":
				return INT;
			case "double":
				return DOUBLE;
			case "void":
				return VOID;
			default:
				return null;
			}
		}
		
		public static String getReturnType(Type t) {
			switch (t) {
			case DOUBLE:
				return "D";
			case INT:
				return "I";
			case VOID:
				return "V";
			default:
				return null;
			
			}
		}
		
		public static String getType(Type t) {
			switch (t) {
			case DOUBLE:
				return "d";
			case INT:
				return "i";
			default:
				return null;
			
			}
		}
	}
}
