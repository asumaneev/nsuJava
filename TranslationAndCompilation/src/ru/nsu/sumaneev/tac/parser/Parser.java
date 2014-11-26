package ru.nsu.sumaneev.tac.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ru.nsu.sumaneev.tac.compiler.Compiler.Type;
import ru.nsu.sumaneev.tac.parser.lexer.Lexeme;
import ru.nsu.sumaneev.tac.parser.lexer.Lexeme.LexemeType;
import ru.nsu.sumaneev.tac.parser.lexer.Lexer;
import ru.nsu.sumaneev.tac.parser.lexer.LexerException;
import ru.nsu.sumaneev.tac.parser.tree.BinaryNode;
import ru.nsu.sumaneev.tac.parser.tree.BodyNode;
import ru.nsu.sumaneev.tac.parser.tree.IfNode;
import ru.nsu.sumaneev.tac.parser.tree.Node.NodeType;
import ru.nsu.sumaneev.tac.parser.tree.WhileNode;
import ru.nsu.sumaneev.tac.parser.tree.FunctionDeclarationNode;
import ru.nsu.sumaneev.tac.parser.tree.FunctionNode;
import ru.nsu.sumaneev.tac.parser.tree.Node;
import ru.nsu.sumaneev.tac.parser.tree.Node.NodeValueType;
import ru.nsu.sumaneev.tac.parser.tree.ProgramNode;
import ru.nsu.sumaneev.tac.parser.tree.VarListNode;

public class Parser {

	private Lexer lexer = null;
	
	private Lexeme currentLexeme = null;
	
	private Map<String, ru.nsu.sumaneev.tac.compiler.Compiler.Type> variablesTypes = null;;
	
	public Parser(Lexer lexer) throws IOException, LexerException {
		this.lexer = lexer;
		
		this.currentLexeme = this.lexer.getLexeme();
		
		variablesTypes = new HashMap<String, ru.nsu.sumaneev.tac.compiler.Compiler.Type>();
	}
	/**
	 * 
	 * parse: method method ... | empty
	 * @throws ParseException
	 * @throws IOException
	 * @throws LexerException
	 */
	public ProgramNode parseProgram() throws ParseException, IOException, LexerException {
		ProgramNode resultTreeRoot = new ProgramNode();
		
		while (null != currentLexeme) {
			resultTreeRoot.addChild(parseFunctionDeclaration());
		}
		
		return resultTreeRoot;
	}
	
	/**
	 * parse: type name(varList)body
	 * @throws ParseException 
	 * @throws LexerException 
	 * @throws IOException 
	 * 
	 */	
	public FunctionDeclarationNode parseFunctionDeclaration() throws ParseException, IOException, LexerException {
		
		FunctionDeclarationNode resultTreeRoot = null;
		
		//	parse return type
		if (hasNotTypeOrIsNull(currentLexeme, LexemeType.TYPE)) {
			throw getExceptionString("invalid function declaration", currentLexeme);
		}
		
		String returnType = currentLexeme.getValue();
		
		
		//	parse function name
		currentLexeme = lexer.getLexeme();
		
		if (hasNotTypeOrIsNull(currentLexeme, LexemeType.NAME)) {
			throw getExceptionString("invalid function declaration", currentLexeme);
		}
		
		resultTreeRoot = new FunctionDeclarationNode(currentLexeme.getValue(), NodeValueType.FUNCTION_NAME, returnType);
		
		
		//	parse varList
		currentLexeme = lexer.getLexeme();
		
		resultTreeRoot.setVarList(parseVarList());
		
		
		//	parseBody
		resultTreeRoot
			.setBody(parseBody());
		
		return resultTreeRoot;
	}
	
	public VarListNode parseVarList() throws ParseException, IOException, LexerException {
		
		VarListNode resultTreeRoot = new VarListNode();
		
		if (hasNotTypeOrIsNull(currentLexeme, LexemeType.OPEN_PARENTHESIS)) {
			throw getExceptionString("invalid variable list", currentLexeme);
		}
		
		currentLexeme = lexer.getLexeme();
		
		if (hasType(currentLexeme, LexemeType.CLOSE_PARENTHESIS)) {
			
		}
		else {
			
			for (;; currentLexeme = lexer.getLexeme()) {
				
				resultTreeRoot.addChild(parseVariableDeclaration());
				
				
				if (hasType(currentLexeme, LexemeType.CLOSE_PARENTHESIS)) {
					break;
				}
				
				if (hasNotTypeOrIsNull(currentLexeme, LexemeType.COMMA)) {
					throw getExceptionString("invalid variable list", currentLexeme);
				}
			}
			
			if (hasNotTypeOrIsNull(currentLexeme, LexemeType.CLOSE_PARENTHESIS)) {
				throw getExceptionString("invalid variable list", currentLexeme);
			}
			
		}
		
		currentLexeme = lexer.getLexeme();
		
		return resultTreeRoot;
		
	}
	
	/**
	 * 
	 * parse: { command; *(command;) }
	 * @throws IOException
	 * @throws LexerException
	 * @throws ParseException
	 */
	public BodyNode parseBody() throws IOException, LexerException, ParseException {
		
		if (hasNotTypeOrIsNull(currentLexeme, LexemeType.OPEN_BRACE)) {
			throw getExceptionString("body should be started with '{'", currentLexeme);
		}
		
		currentLexeme = lexer.getLexeme();
		
		BodyNode resultTreeRoot = 
				new BodyNode()
				.addChild(parseCommand());

		if (hasNotTypeOrIsNull(currentLexeme, LexemeType.SEMICOLON)) {
			throw getExceptionString("semicolon missed", currentLexeme);
		}
		
		currentLexeme = lexer.getLexeme();
		
		//	while close brace does not met - parse commands 
		while (hasNotTypeAndNotNull(currentLexeme, LexemeType.CLOSE_BRACE)) {
			
			Node commandRootNode = parseCommand();
			
			resultTreeRoot.addChild(commandRootNode);
			
			if ( (NodeType.IF_NODE == commandRootNode.getType()) || (NodeType.WHILE_NODE == commandRootNode.getType()) ) {
				continue;
			}
			
			if (hasNotTypeOrIsNull(currentLexeme, LexemeType.SEMICOLON)) {
				throw getExceptionString("semicolon missed", currentLexeme);
			}
			
			currentLexeme = lexer.getLexeme();
		}
		
		if (hasNotTypeOrIsNull(currentLexeme, LexemeType.CLOSE_BRACE)) {
			throw getExceptionString("body should be completed with '}'", currentLexeme);
		}
		
		currentLexeme = lexer.getLexeme();
		
		return resultTreeRoot;
		
	}

	/**
	 * 
	 * parse: name = expression | return expression | type name | name(paramList)
	 * @throws IOException
	 * @throws LexerException
	 * @throws ParseException
	 */
	public Node parseCommand() throws IOException, LexerException, ParseException {
		Node resultTreeRoot = null;
		
		if (null == currentLexeme) {
			throw getExceptionString("invalid command", currentLexeme);
		}
		
		switch (currentLexeme.getType()) {

		//	name = expression
		//	or function call
		case NAME :
		
			Lexeme nameLexeme = currentLexeme;
			
			currentLexeme = lexer.getLexeme();
			
			//	function case
			if (hasType(currentLexeme, LexemeType.OPEN_PARENTHESIS)) {
			
				resultTreeRoot = new FunctionNode( nameLexeme.getValue(), NodeValueType.FUNCTION_NAME);
				
				currentLexeme = lexer.getLexeme();
				
				try {
					
					List<Node> parameters = parseParameters();
					
					if (null != parameters) {
						((FunctionNode) resultTreeRoot).addChildren(parameters);
					}
				}
				catch (ParseException e) {
					if (0 == e.getMessage().compareTo("invalid function parameters")) {
						throw getExceptionString("invalid function parameters", nameLexeme);
					}
				}
			}
			//	equation case
			else if (hasType(currentLexeme, LexemeType.EQUAL)) {
				
				resultTreeRoot = 
						new BinaryNode(currentLexeme.getValue(), NodeValueType.BINARY_OPERATOR)
						.setLeftChild(new BinaryNode(nameLexeme.getValue(), NodeValueType.NAME));
				
				currentLexeme = lexer.getLexeme();
				
				((BinaryNode) resultTreeRoot).setRightChild(parseExpression());
			}
			else {
				throw getExceptionString("invalid command lexeme", currentLexeme);
			}
			
			break;
		//	return statement
		case RETURN:
			
			resultTreeRoot = new BinaryNode(currentLexeme.getValue(), NodeValueType.RETURN_STATEMENT);
			
			currentLexeme = lexer.getLexeme();
			
			if (LexemeType.SEMICOLON == currentLexeme.getType()) {
				break;
			}
			
			((BinaryNode) resultTreeRoot).setLeftChild(parseExpression());
			
			break;
		//	type name
		case TYPE:
			
			resultTreeRoot = parseVariableDeclaration();
			
			break;
		//	while condition
		case WHILE: {
			
			currentLexeme = lexer.getLexeme();
			
			if (hasNotTypeOrIsNull(currentLexeme, LexemeType.OPEN_PARENTHESIS)) {
				throw new ParseException("invalid compare condition", currentLexeme.getLine(), currentLexeme.getColumn());
			}
			currentLexeme = lexer.getLexeme();
			
			Node leftExpression = parseExpression();
			
			if ((hasNotTypeOrIsNull(currentLexeme, LexemeType.GREATER_SIGN)) 
					&& (hasNotTypeOrIsNull(currentLexeme, LexemeType.LESS_SIGN))
					&& (hasNotTypeOrIsNull(currentLexeme, LexemeType.EQUAL))) {
				throw new ParseException("invalid compare condition", currentLexeme.getLine(), currentLexeme.getColumn());
			}
			
			String sign = currentLexeme.getValue();
			currentLexeme = lexer.getLexeme();
			
			
			resultTreeRoot = new WhileNode(sign)
							.setLeftExpression(leftExpression)
							.setRightExpression(parseExpression());
			
			
			if (hasNotTypeOrIsNull(currentLexeme, LexemeType.CLOSE_PARENTHESIS)) {
				throw new ParseException("invalid compare condition", currentLexeme.getLine(), currentLexeme.getColumn());
			}
			currentLexeme = lexer.getLexeme();
			
			((WhileNode) resultTreeRoot).setBody(parseBody());
			
			break;
		}
		//	if condition
		case IF: {
			
			currentLexeme = lexer.getLexeme();
			
			if (hasNotTypeOrIsNull(currentLexeme, LexemeType.OPEN_PARENTHESIS)) {
				throw new ParseException("invalid compare condition");
			}
			currentLexeme = lexer.getLexeme();
			
			Node leftExpression = parseExpression();
			
			if ((hasNotTypeOrIsNull(currentLexeme, LexemeType.GREATER_SIGN)) 
					&& (hasNotTypeOrIsNull(currentLexeme, LexemeType.LESS_SIGN))
					&& (hasNotTypeOrIsNull(currentLexeme, LexemeType.EQUAL))) {
				
				throw new ParseException("invalid compare condition");
			}
			
			String sign = currentLexeme.getValue();
			currentLexeme = lexer.getLexeme();
			
			
			resultTreeRoot = new IfNode(sign)
							.setLeftExpression(leftExpression)
							.setRightExpression(parseExpression());
			
			
			if (hasNotTypeOrIsNull(currentLexeme, LexemeType.CLOSE_PARENTHESIS)) {
				throw new ParseException("invalid compare condition");
			}
			currentLexeme = lexer.getLexeme();
			
			((IfNode) resultTreeRoot).setIfBody(parseBody());
			
			//	add else if it is
			if (hasType(currentLexeme, LexemeType.ELSE)) {
				currentLexeme = lexer.getLexeme();
				
				((IfNode) resultTreeRoot).setElseBody(parseBody());
			}
			
			break;
		}
		default:
			throw new ParseException("invalid command lexeme");
		}
		return resultTreeRoot;
	}
	
	/**
	 * parse: term (+-) term (+-) ...
	 * @throws LexerException 
	 * @throws IOException 
	 * @throws ParseException 
	 * 
	 */
	public Node parseExpression() throws IOException, LexerException, ParseException {

		Node resultTreeRoot = parseTerm();
		
		while (hasType(currentLexeme, LexemeType.PLUS) || hasType(currentLexeme, LexemeType.MINUS)) {
			
			//resultTreeRoot = resultTreeRoot.setParent( NodeType.ARITHMETIC_NODE, currentLexeme.getValue(), NodeValueType.BINARY_OPERATOR);
			resultTreeRoot = 
					resultTreeRoot.setParent( new BinaryNode( currentLexeme.getValue(), NodeValueType.BINARY_OPERATOR ));
				
			currentLexeme = lexer.getLexeme();
			
			((BinaryNode) resultTreeRoot).setRightChild(parseTerm());
		}
		
		return resultTreeRoot;
		
	}
	
	/**
	 * parse: factor (/*) factor (/*) ...
	 * @throws ParseException 
	 * @throws LexerException 
	 * @throws IOException 
	 * 
	 */
	public Node parseTerm() throws IOException, LexerException, ParseException {
		
		Node resultTreeRoot = parseFactor();
		
		while (hasType(currentLexeme, LexemeType.MULTIPLICATION) || hasType(currentLexeme, LexemeType.DIVISION)) {

			resultTreeRoot = 
					resultTreeRoot.setParent(new BinaryNode( currentLexeme.getValue(), NodeValueType.BINARY_OPERATOR ));
			
			currentLexeme = lexer.getLexeme();
			
			((BinaryNode) resultTreeRoot).setRightChild(parseFactor());
		}
		
		return resultTreeRoot;
	}
	
	/**
	 * parse: power^factor | power
	 * @throws ParseException 
	 * @throws LexerException 
	 * @throws IOException 
	 * 
	 */
	public Node parseFactor() throws IOException, LexerException, ParseException {

		Node resultTreeRoot = parsePower();
		
		//	power^factor case
		if (hasType(currentLexeme, LexemeType.CARET)) {
			
			resultTreeRoot = 
					resultTreeRoot.setParent(new BinaryNode( currentLexeme.getValue(), NodeValueType.BINARY_OPERATOR ));
			
			currentLexeme = lexer.getLexeme();
			
			((BinaryNode) resultTreeRoot).setRightChild(parsePower());
			
		}

		return resultTreeRoot;
	}
	
	/**
	 * parse: atom | -atom | +atom
	 * @throws ParseException 
	 * @throws LexerException 
	 * @throws IOException 
	 * 
	 */
	public Node parsePower() throws IOException, LexerException, ParseException {
		
		Node resultTreeRoot = null;
		
		//	+atom | - atom case
		if (hasType(currentLexeme, LexemeType.MINUS) || hasType(currentLexeme, LexemeType.PLUS)) {
		
			resultTreeRoot = new BinaryNode(currentLexeme.getValue(), NodeValueType.UNARY_OPERATOR );
			
			currentLexeme = lexer.getLexeme();
			
		
			((BinaryNode) resultTreeRoot).setLeftChild(parseAtom());
		}
		else {
			resultTreeRoot = parseAtom();
		}
		
		return resultTreeRoot;
	}
	
	/**
	 * 
	 * parse: name | number | (expression) | name(paramList)
	 * @throws IOException
	 * @throws LexerException
	 * @throws ParseException
	 */
	public Node parseAtom() throws IOException, LexerException, ParseException {
		
		Node resultTreeRoot = null;

		if (null == currentLexeme) {
			throw getExceptionString("invalid atom", currentLexeme);
		}
		
		switch (currentLexeme.getType()) {
		case NAME:
			
			Lexeme nameLexeme = currentLexeme;
			
			currentLexeme = lexer.getLexeme();
			
			//	function case
			if (hasType(currentLexeme, LexemeType.OPEN_PARENTHESIS)) {
			
				resultTreeRoot = new FunctionNode( nameLexeme.getValue(), NodeValueType.FUNCTION_NAME );
				
				currentLexeme = lexer.getLexeme();
				
				try {
					
					List<Node> parameters = parseParameters();
					
					if (null != parameters) {
						((FunctionNode) resultTreeRoot).addChildren(parameters);
					}
				}
				catch (ParseException e) {
					if (0 == e.getMessage().compareTo("invalid function parameters")) {
						throw getExceptionString("invalid function parameters", nameLexeme);						
					}
				}
			}
			// simple name case
			else {
				resultTreeRoot = new BinaryNode( nameLexeme.getValue(), NodeValueType.NAME );
				
				Type t = variablesTypes.get(nameLexeme.getValue());
				
				if (Type.DOUBLE == t) {
					((BinaryNode)resultTreeRoot).setStackSize(2);
				}
				else {
					((BinaryNode)resultTreeRoot).setStackSize(1);
				}
			}
			
			break;
		case NUMBER:
			
			resultTreeRoot = new BinaryNode( currentLexeme.getValue(), NodeValueType.NUMBER );
			
			Type t = ru.nsu.sumaneev.tac.compiler.Compiler.typeOfNumber(currentLexeme.getValue());
			
			if (Type.DOUBLE == t) {
				((BinaryNode)resultTreeRoot).setStackSize(2);
			}
			else {
				((BinaryNode)resultTreeRoot).setStackSize(1);
			}
			
			currentLexeme = lexer.getLexeme();
			break;
		case OPEN_PARENTHESIS:
			
			currentLexeme = lexer.getLexeme();
					
			resultTreeRoot = parseExpression();
			
			
			if (hasNotTypeOrIsNull(currentLexeme, LexemeType.CLOSE_PARENTHESIS)) {
				throw getExceptionString("close parenthesis not found", currentLexeme);
			}
			
			currentLexeme = lexer.getLexeme();
			
			break;
		default:
			throw getExceptionString("invalid atom", currentLexeme);	
		}
		
		return resultTreeRoot;
	}
	
	/**
	 * 
	 * parse: (expression % ',') | empty 
	 * @throws ParseException
	 * @throws IOException
	 * @throws LexerException
	 */
	public List<Node> parseParameters() throws ParseException, IOException, LexerException {
		List<Node> parameters = null;
		
		if (null == currentLexeme) {
			throw getExceptionString("invalid function parameters", currentLexeme);
		}
		
		if (hasNotTypeOrIsNull(currentLexeme, LexemeType.CLOSE_PARENTHESIS)) {
			
			parameters = new LinkedList<Node>();
			
			//	until currentLexeme is not null or ')' - parse parameters
			for (; ; currentLexeme = lexer.getLexeme()) {

				parameters.add(parseExpression());
				
				//	if currentLexeme is not ')' and is not ',' - invalid parameters
				if ( !(hasType(currentLexeme, LexemeType.CLOSE_PARENTHESIS)) && !(hasType(currentLexeme, LexemeType.COMMA)) ) {
					throw getExceptionString("invalid function parameters", currentLexeme);
				}
				
				if (hasType(currentLexeme, LexemeType.CLOSE_PARENTHESIS)) {
					break;
				}
			}
			
		}
		
		currentLexeme = lexer.getLexeme();
		
		return parameters;
	}

	/**
	 * 
	 * parse: type name
	 * @throws IOException
	 * @throws LexerException
	 * @throws ParseException
	 */
	public Node parseVariableDeclaration() throws IOException, LexerException, ParseException {
		
		if (null == currentLexeme) {
			throw new ParseException("unexpected end of file");
		}
		
		if (currentLexeme.getValue().equals("void")) {
			throw new ParseException("invalid variable type", currentLexeme.getLine(), currentLexeme.getColumn());
		}
		
		
		Node resultTreeRoot = new BinaryNode(currentLexeme.getValue(), NodeValueType.VARIABLE_DECLARATION);
		
		Type variableType = Type.getType(currentLexeme.getValue());
		
		currentLexeme = lexer.getLexeme();
		
		if (hasNotTypeOrIsNull(currentLexeme, LexemeType.NAME)) {
			throw new ParseException("invalid variable declaration", currentLexeme.getLine(), currentLexeme.getColumn());
		}
		
		((BinaryNode) resultTreeRoot).setLeftChild(new BinaryNode(currentLexeme.getValue(), NodeValueType.NAME));
		
		variablesTypes.put(currentLexeme.getValue(), variableType);
		
		currentLexeme = lexer.getLexeme();
		
		return resultTreeRoot;
	}
	
	private boolean hasType(Lexeme lexeme, LexemeType type) {
		
		if ((null != lexeme) && (type == lexeme.getType())) {
			return true;
		}
		
		return false;
	}
	
	private boolean hasNotTypeOrIsNull(Lexeme lexeme, LexemeType type) {

		if ((null == lexeme) || (type != lexeme.getType())) {
			return true;
		}
		
		return false;
	}
	
	private boolean hasNotTypeAndNotNull(Lexeme lexeme, LexemeType type) {

		if ((null != lexeme) && (type != lexeme.getType())) {
			return true;
		}
		
		return false;
	}

	
	private ParseException getExceptionString(String message, Lexeme lexeme) {
		
		if (null == lexeme) {
			return new ParseException(message);
		}
		else {
			return new ParseException(message, lexeme.getLine(), lexeme.getColumn());
		}
		
	}
}
