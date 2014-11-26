package ru.nsu.sumaneev.tac.parser.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.io.StringReader;
import java.util.ListIterator;

import org.hamcrest.Matchers;
import org.hamcrest.core.IsNull;
import org.junit.Test;

import ru.nsu.sumaneev.tac.parser.ParseException;
import ru.nsu.sumaneev.tac.parser.Parser;
import ru.nsu.sumaneev.tac.parser.lexer.Lexer;
import ru.nsu.sumaneev.tac.parser.lexer.LexerException;
import ru.nsu.sumaneev.tac.parser.tree.BinaryNode;
import ru.nsu.sumaneev.tac.parser.tree.BodyNode;
import ru.nsu.sumaneev.tac.parser.tree.FunctionDeclarationNode;
import ru.nsu.sumaneev.tac.parser.tree.FunctionNode;
import ru.nsu.sumaneev.tac.parser.tree.IfNode;
import ru.nsu.sumaneev.tac.parser.tree.ListNode;
import ru.nsu.sumaneev.tac.parser.tree.Node;
import ru.nsu.sumaneev.tac.parser.tree.Node.NodeValueType;
import ru.nsu.sumaneev.tac.parser.tree.ProgramNode;
import ru.nsu.sumaneev.tac.parser.tree.VarListNode;
import ru.nsu.sumaneev.tac.parser.tree.WhileNode;

//@RunWith(JUnit4.class)
public class ParserTest {
	
	private String testStringExpressionGood = "f((a + b), 2, -4) + -b / (1 * 2 ^ -3) - 4 * (j ^ (- 1 / 2) - function())";
	private String testStringExpressionWithoutCloseParenthesis = "a + -b / (1 * 2 ^ -3) - 4 * (j ^ (- 1 / 2 - f()";
	private String testStringExpressionWrong = "f(+)";
	
	
	
	private String testStringCommandEquation = "x = " + testStringExpressionGood;
	private String testStringCommandReturnStatement = "return (" + testStringExpressionGood + ")";
	private String testStringCommandVariableDeclaration = "double a";
	private String testStringCommandFunctionCall = "f(a, b, c)";
	private String testStringCommandWhileStmt = "while (a > b){ b = b + 1; }";
	private String testStringCommandIfStmt = "if (a > b) { return a; } else { return b; }";
	private String testStringCommandWrongEquation = "12 = name";
	private String testStringCommandWrongVariableDeclaration = "int 15";
	
	
	private String testStringBodyGood = "{"
			+ "int a; double b; int c; "
			+ "a = " + testStringExpressionGood + ";"
			+ "b = function();"
			+ "c = f(a, b, 15.456);"
			+ "return c;"
			+ "}";
	private String testStringBodyMissedSemicolon = "{ int a; int b int c; }";
	private String testStringBodyMissedOpenBrace = "int a; int b; int c;}";
	private String testStringBodyMissedCloseBrace = "{int a; int b; int c;";
	
	
	private String testStringVarListGood = "(int a, double b, int c)";
	private String testStringVarListEmptyGood = "()";
	private String testStringVarListMissedComma = "(int a, double b int c)";
	private String testStringVarListMissedOpenParenthesis = "int a, double b, int c)";
	private String testStringVarListMissedCloseParenthesis = "(int a, double b, int c";
	

	
	private String testStringFunctionDeclarationGood = 
			"int function" 
			+ testStringVarListGood 
			+ testStringBodyGood;
	private String testStringFunctionDeclarationWrongReturnType = "name functuion() {a = b;}";
	private String testStringFunctionDeclarationWrongFunctionName = "int int() {a = b;}";
	private String testStringFunctionDeclarationWrongWithoutVarList = "int function {a = b;}";
	private String testStringFunctionDeclarationWrongWithoutBody = "int function()";
	
	
	
	private String testStringProgramGood = 
			"void main(){ int a; a = 5; print(a); return; }"
			+ " " + testStringFunctionDeclarationGood;
	
	private BinaryNode goodExpressionTreeRoot = null;
	
	
	private BinaryNode goodCommandEquationTreeRoot = null;
	private BinaryNode goodCommandReturnStatementRoot = null;
	private BinaryNode goodCommandVariableDeclarationRoot = null;
	private FunctionNode goodCommandFunctionCallRoot = null;
	private WhileNode goodCommandWhileStmtRoot = null;
	private IfNode goodCommandIfStmtRoot = null;

	
	private BodyNode goodBodyTreeRoot = null;
	
	
	private VarListNode goodVarListTreeRoot = null;
	private VarListNode goodVarListEmptyTreeRoot = null;
	
	
	private FunctionDeclarationNode goodFunctionDeclarationTreeRoot = null;
	
	
	private ProgramNode goodProgramTreeRoot = null;
	private ProgramNode goodProgramEmptyTreeRoot = null;
	
	public ParserTest() throws ParseException {
		
		//	-b / (1 * 2 ^ -3)
		Node secondTermRoot = 
				
				new BinaryNode("b", NodeValueType.NAME)
				.setParent(new BinaryNode( "-", NodeValueType.UNARY_OPERATOR ))
				.setParent(new BinaryNode( "/", NodeValueType.BINARY_OPERATOR ))
				.setRightChild(
						new BinaryNode("1", NodeValueType.NUMBER)
						.setParent(new BinaryNode( "*", NodeValueType.BINARY_OPERATOR ))
						.setRightChild(
								new BinaryNode("2", NodeValueType.NUMBER)
								. setParent(new BinaryNode( "^", NodeValueType.BINARY_OPERATOR ))
								.setRightChild(
										new BinaryNode("3", NodeValueType.NUMBER)
										.setParent(new BinaryNode( "-", NodeValueType.UNARY_OPERATOR ))
										)
								)
						);
		
		//	4 * (j ^ (- 1 / 2) - function())
		BinaryNode thirdTermRoot = 
				new BinaryNode("4", NodeValueType.NUMBER)
				.setParent(new BinaryNode( "*", NodeValueType.BINARY_OPERATOR ))
				.setRightChild(
						new BinaryNode("j", NodeValueType.NAME)
						.setParent(new BinaryNode( "^", NodeValueType.BINARY_OPERATOR ))
						.setRightChild(
								new BinaryNode("1", NodeValueType.NUMBER)
								.setParent(new BinaryNode( "-", NodeValueType.UNARY_OPERATOR ))
								.setParent(new BinaryNode( "/", NodeValueType.BINARY_OPERATOR ))
								.setRightChild(
										new BinaryNode("2", NodeValueType.NUMBER)
										)
								)
						.setParent(new BinaryNode( "-", NodeValueType.BINARY_OPERATOR ))
						.setRightChild(
								new FunctionNode("function", NodeValueType.FUNCTION_NAME)
								)
						);
		
		//	f((a + b), 2, -4) + -b / (1 * 2 ^ -3) - 4 * (j ^ (- 1 / 2) - function())
		goodExpressionTreeRoot = 
			new FunctionNode("f", NodeValueType.FUNCTION_NAME)
			.addChild(
					new BinaryNode("a", NodeValueType.NAME)
					.setParent(new BinaryNode("+", NodeValueType.BINARY_OPERATOR))
					.setRightChild(new BinaryNode("b", NodeValueType.NAME))
					)
			.addChild(
					new BinaryNode("2", NodeValueType.NUMBER)
					)
			.addChild(
					new BinaryNode("4", NodeValueType.NUMBER)
					.setParent(new BinaryNode("-", NodeValueType.UNARY_OPERATOR))
					)
			.setParent(new BinaryNode( "+", NodeValueType.BINARY_OPERATOR ))
			.setRightChild(secondTermRoot)
			.setParent(new BinaryNode( "-", NodeValueType.BINARY_OPERATOR ))
			.setRightChild(thirdTermRoot);
		
		
		//	x = f((a + b), 2, -4) + -b / (1 * 2 ^ -3) - 4 * (j ^ (- 1 / 2) - function())
		goodCommandEquationTreeRoot = 
				new BinaryNode("x", NodeValueType.NAME)
				.setParent(new  BinaryNode("=", NodeValueType.BINARY_OPERATOR))
				.setRightChild(goodExpressionTreeRoot);
		
		//	return (f((a + b), 2, -4) + -b / (1 * 2 ^ -3) - 4 * (j ^ (- 1 / 2) - function()))
		goodCommandReturnStatementRoot = 
				new BinaryNode("return", NodeValueType.RETURN_STATEMENT)
				.setLeftChild(goodExpressionTreeRoot);
		
		//	double a
		goodCommandVariableDeclarationRoot = 
				new BinaryNode("double", NodeValueType.VARIABLE_DECLARATION)
				.setLeftChild(new BinaryNode("a", NodeValueType.NAME));
		
		//	f(a, b, c)
		goodCommandFunctionCallRoot = 
				new FunctionNode("f", NodeValueType.FUNCTION_NAME)
				.addChild(new BinaryNode("a", NodeValueType.NAME))
				.addChild(new BinaryNode("b", NodeValueType.NAME))
				.addChild(new BinaryNode("c", NodeValueType.NAME));
		
		//	while (a > b){ b = b + 1; }
		goodCommandWhileStmtRoot = 
				new WhileNode(">")
				.setLeftExpression(new BinaryNode("a", NodeValueType.NAME))
				.setRightExpression(new BinaryNode("b", NodeValueType.NAME))
				.setBody(
						new BodyNode()
						.addChild(
								new BinaryNode("b", NodeValueType.NAME)
								.setParent(new  BinaryNode("=", NodeValueType.BINARY_OPERATOR))
								.setRightChild(
										new BinaryNode("b", NodeValueType.NAME)
										.setParent(new  BinaryNode("+", NodeValueType.BINARY_OPERATOR))
										.setRightChild(new  BinaryNode("1", NodeValueType.NUMBER))
								)
						)
				)
				;
		
		//	if (a > b) { return a; } else { return b; }
		goodCommandIfStmtRoot = 
				new IfNode(">")
				.setLeftExpression(new BinaryNode("a", NodeValueType.NAME))
				.setRightExpression(new BinaryNode("b", NodeValueType.NAME))
				.setIfBody(
						new BodyNode()
						.addChild(
								new BinaryNode("return", NodeValueType.RETURN_STATEMENT)
								.setLeftChild(new BinaryNode("a", NodeValueType.NAME))
						)
				)
				.setElseBody(
						new BodyNode()
						.addChild(
								new BinaryNode("return", NodeValueType.RETURN_STATEMENT)
								.setLeftChild(new BinaryNode("b", NodeValueType.NAME))
						)
				)
				;
		
		//	{ 
		//		int a; double b; int c; 
		//		a = testStringExpressionGood; 
		//		b = function();  
		//		c = f(a, b, 15.456); 
		//		return c;
		//	}
		goodBodyTreeRoot = (
				new BodyNode()
				.addChild(
						new BinaryNode("int", NodeValueType.VARIABLE_DECLARATION)
						.setLeftChild(new BinaryNode("a", NodeValueType.NAME))
						)
				.addChild(new BinaryNode("double", NodeValueType.VARIABLE_DECLARATION)
						.setLeftChild(new BinaryNode("b", NodeValueType.NAME))
						)
				.addChild(
						new BinaryNode("int", NodeValueType.VARIABLE_DECLARATION)
						.setLeftChild(new BinaryNode("c", NodeValueType.NAME))
						)
				.addChild(
						new BinaryNode("a", NodeValueType.NAME)
						.setParent(new BinaryNode("=", NodeValueType.BINARY_OPERATOR))
						.setRightChild(goodExpressionTreeRoot)
						)
				.addChild(
						new BinaryNode("b", NodeValueType.NAME)
						.setParent(new BinaryNode("=", NodeValueType.BINARY_OPERATOR))
						.setRightChild(new FunctionNode("function", NodeValueType.FUNCTION_NAME))
						)
				.addChild(
						new BinaryNode("c", NodeValueType.NAME)
						.setParent(new BinaryNode("=", NodeValueType.BINARY_OPERATOR))
						.setRightChild(
								new FunctionNode("f", NodeValueType.FUNCTION_NAME)
								.addChild(new BinaryNode("a", NodeValueType.NAME))
								.addChild(new BinaryNode("b", NodeValueType.NAME))
								.addChild(new BinaryNode("15.456", NodeValueType.NUMBER))
								)
						)
				.addChild(
						new BinaryNode("return", NodeValueType.RETURN_STATEMENT)
						.setLeftChild(new BinaryNode("c", NodeValueType.NAME))
						)
				);
		
		//	()
		goodVarListEmptyTreeRoot = new VarListNode();
		
		//	(int a, double b, int c)
		goodVarListTreeRoot = 
				new VarListNode()
				.addChild(
						new BinaryNode("a", NodeValueType.NAME)
						.setParent(new BinaryNode("int", NodeValueType.VARIABLE_DECLARATION))
						)
				.addChild(
						new BinaryNode("b", NodeValueType.NAME)
						.setParent(new BinaryNode("double", NodeValueType.VARIABLE_DECLARATION))
						)
				.addChild(
						new BinaryNode("c", NodeValueType.NAME)
						.setParent(new BinaryNode("int", NodeValueType.VARIABLE_DECLARATION))
						);
		
		//	int function testStringVarListGood testStringBodyGood
		goodFunctionDeclarationTreeRoot = 
				new FunctionDeclarationNode("function", NodeValueType.FUNCTION_NAME, "int")
				.setVarList(goodVarListTreeRoot)
				.setBody(goodBodyTreeRoot);
		
		
		//	void main(){ int a; a = 5; print(a); }
		goodProgramTreeRoot = 
				new ProgramNode()
				.addChild(
						new FunctionDeclarationNode("main", NodeValueType.FUNCTION_NAME, "void")
						.setVarList(new VarListNode())
						.setBody(
								new BodyNode()
								.addChild(
										new BinaryNode("a", NodeValueType.NAME)
										.setParent(new BinaryNode("int", NodeValueType.VARIABLE_DECLARATION))
										)
								.addChild(
										new BinaryNode("a", NodeValueType.NAME)
										.setParent(
												new BinaryNode("=", NodeValueType.BINARY_OPERATOR)
												.setRightChild(new BinaryNode("5", NodeValueType.NUMBER))
												)
										)
								.addChild(
										new FunctionNode("print", NodeValueType.FUNCTION_NAME)
										.addChild(new BinaryNode("a", NodeValueType.NAME))
										)
								.addChild(
										new BinaryNode("return", NodeValueType.RETURN_STATEMENT)
										)
								)
						)
				.addChild(goodFunctionDeclarationTreeRoot);
		
		//	""
		goodProgramEmptyTreeRoot = new ProgramNode();
		
	}
	
	
	
	
	
	@Test
	public void parseRightExpression() throws IOException, LexerException, ParseException {
	
		Parser parser = new Parser(new Lexer(new StringReader(testStringExpressionGood)));
		
		Node resultTreeRoot = parser.parseExpression();
		
		comapreTrees(goodExpressionTreeRoot, resultTreeRoot);
		
	}
	
	@Test
	public void parseExpressionWithoutCloseParenthesisWrongExpression() throws IOException, LexerException, ParseException {
	
		Parser parser = new Parser(new Lexer(new StringReader(testStringExpressionWithoutCloseParenthesis)));

		try {
			parser.parseExpression();
		}
		catch (ParseException e) {
			
			assertThat(e.getMessage(), Matchers.startsWith("close parenthesis not found"));
			
		}
		
		parser = new Parser(new Lexer(new StringReader(testStringExpressionWrong)));
		
		
		try {
			parser.parseExpression();
		}
		catch (ParseException e) {
			
			assertThat(e.getMessage(), Matchers.startsWith("invalid lexeme"));
			
		}
	}

	
	
	
	
	@Test
	public void parseCommandEquationReturnStatementVariableDeclarationFunctionCallWhileIfCondition() 
			throws IOException, LexerException, ParseException {
		
		Parser parserEquation = new Parser(new Lexer(new StringReader(testStringCommandEquation)));
		
		comapreTrees(goodCommandEquationTreeRoot, parserEquation.parseCommand());
		
		
		
		Parser parserReturnStatement = new Parser(new Lexer(new StringReader(testStringCommandReturnStatement)));
		
		comapreTrees(goodCommandReturnStatementRoot, parserReturnStatement.parseCommand());
		


		Parser parserVariableDeclaration = new Parser(new Lexer(new StringReader(testStringCommandVariableDeclaration)));
		
		comapreTrees(goodCommandVariableDeclarationRoot, parserVariableDeclaration.parseCommand());
		
		
		
		Parser parserFunctionCall = new Parser(new Lexer(new StringReader(testStringCommandFunctionCall)));
		
		comapreTrees(goodCommandFunctionCallRoot, parserFunctionCall.parseCommand());
		
		
		Parser parserWhileCondition = new Parser(new Lexer(new StringReader(testStringCommandWhileStmt)));
		
		comapreTrees(goodCommandWhileStmtRoot, parserWhileCondition.parseCommand());
		
		Parser parserIfCondition = new Parser(new Lexer(new StringReader(testStringCommandIfStmt)));
		
		comapreTrees(goodCommandIfStmtRoot, parserIfCondition.parseCommand());
		
	}
	
	@Test 
	public void parseCommandWrongEquationWrongVariableDeclaration() throws IOException, LexerException {
	
		Parser parser = new Parser(new Lexer(new StringReader(testStringCommandWrongEquation)));
		
		
		try {
			parser.parseCommand();
		}
		catch (ParseException e) {
			
			assertThat(e.getMessage(), Matchers.startsWith("invalid command lexeme"));
		}
		
		parser = new Parser(new Lexer(new StringReader(testStringCommandWrongVariableDeclaration)));
		
		
		try {
			parser.parseCommand();
		}
		catch (ParseException e) {
			
			assertThat(e.getMessage(), Matchers.startsWith("invalid variable declaration"));
		}
	}

	
	
	
	
	@Test
	public void parseBodyWithManyCommands() throws IOException, LexerException, ParseException {
		
		Parser parser = new Parser(new Lexer(new StringReader(testStringBodyGood)));
		
		comapreTrees(goodBodyTreeRoot, parser.parseBody());
	}
	
	@Test
	public void parseBodyWrongMissedSemicolonMissedOpenBraceMissedCloseBrace() throws IOException, LexerException, ParseException {
		Parser parser = new Parser(new Lexer(new StringReader(testStringBodyMissedSemicolon)));
		
		
		try {
			parser.parseBody();
		}
		catch (ParseException e) {
			
			assertThat(e.getMessage(), Matchers.startsWith("semicolon missed"));
		}
		
		parser = new Parser(new Lexer(new StringReader(testStringBodyMissedOpenBrace)));
		
		
		try {
			parser.parseBody();
		}
		catch (ParseException e) {
			
			assertThat(e.getMessage(), Matchers.startsWith("body should be started with '{'"));
		}
		
		parser = new Parser(new Lexer(new StringReader(testStringBodyMissedCloseBrace)));
		
		
		try {
			parser.parseBody();
		}
		catch (ParseException e) {
			
			assertThat(e.getMessage(), Matchers.startsWith("body should be completed with '}'"));
		}
	}

	
	
	
	@Test
	public void parseVarListAndEmptyVarList() throws IOException, LexerException, ParseException {
		
		Parser parserVarList = new Parser(new Lexer(new StringReader(testStringVarListGood)));
		
		comapreTrees(goodVarListTreeRoot, parserVarList.parseVarList());
		
		Parser parserVarListEmpty = new Parser(new Lexer(new StringReader(testStringVarListEmptyGood)));
		
		comapreTrees(goodVarListEmptyTreeRoot, parserVarListEmpty.parseVarList());
	}
	
	@Test
	public void parseVarListMissedCommaMissedOpenParenthesisMissedCloseParenthesis() throws IOException, LexerException {
		Parser parser = new Parser(new Lexer(new StringReader(testStringVarListMissedComma)));
		
		
		try {
			parser.parseVarList();
		}
		catch (ParseException e) {
			
			assertThat(e.getMessage(), Matchers.startsWith("invalid variable list"));
		}
		
		parser = new Parser(new Lexer(new StringReader(testStringVarListMissedOpenParenthesis)));
		
		
		try {
			parser.parseVarList();
		}
		catch (ParseException e) {
			
			assertThat(e.getMessage(), Matchers.startsWith("invalid variable list"));
		}
		
		parser = new Parser(new Lexer(new StringReader(testStringVarListMissedCloseParenthesis)));
		
		
		try {
			parser.parseVarList();
		}
		catch (ParseException e) {
			
			assertThat(e.getMessage(), Matchers.startsWith("invalid variable list"));
		}
	}
	
	
	
	@Test
	public void parseFunctionDeclarationGood() throws IOException, LexerException, ParseException {
		Parser parser = new Parser(new Lexer(new StringReader(testStringFunctionDeclarationGood)));
		
		comapreTrees(goodFunctionDeclarationTreeRoot, parser.parseFunctionDeclaration());
	}

	
//	private String testStringFunctionDeclarationWrongReturnType = "name functuion() {a = b;}";
//	private String testStringFunctionDeclarationWrongFunctionName = "int int() {a = b;}";
//	private String testStringFunctionDeclarationWrongWithoutVarList = "int function {a = b;}";
//	private String testStringFunctionDeclarationWrongWithoutBody = "int function()";
	
	@Test
	public void parseFunctionDeclarationWrongReturnTypeWrongFunctionNameWrongWithoutVarListWrongWithoutBody() throws IOException, LexerException {
		
		Parser parser = new Parser(new Lexer(new StringReader(testStringFunctionDeclarationWrongReturnType)));
		
		
		try {
			parser.parseFunctionDeclaration();
		}
		catch (ParseException e) {
			
			assertThat(e.getMessage(), Matchers.startsWith("invalid function declaration"));
		}
		
		
		
		parser = new Parser(new Lexer(new StringReader(testStringFunctionDeclarationWrongFunctionName)));
		
		
		try {
			parser.parseFunctionDeclaration();
		}
		catch (ParseException e) {
			
			assertThat(e.getMessage(), Matchers.startsWith("invalid function declaration"));
		}

		
		
		parser = new Parser(new Lexer(new StringReader(testStringFunctionDeclarationWrongWithoutVarList)));
		
		
		try {
			parser.parseFunctionDeclaration();
		}
		catch (ParseException e) {
			
			assertThat(e.getMessage(), Matchers.startsWith("invalid variable list"));
		}
		
		
		parser = new Parser(new Lexer(new StringReader(testStringFunctionDeclarationWrongWithoutBody)));
		
		
		try {
			parser.parseFunctionDeclaration();
		}
		catch (ParseException e) {
			
			assertThat(e.getMessage(), Matchers.startsWith("body should be started with '{'"));
		}
		
	}
	
	
	
	@Test
	public void parseProgramAndEmptyProgram() throws IOException, LexerException, ParseException {
		Parser parser = new Parser(new Lexer(new StringReader(testStringProgramGood)));
		
		
		comapreTrees(goodProgramTreeRoot, parser.parseProgram());
		
		
		parser = new Parser(new Lexer(new StringReader("")));
		
		
		comapreTrees(goodProgramEmptyTreeRoot, parser.parseProgram());
	}
	
	
	
	private void comapreTrees(Node reasonTreeRoot, Node assertionTreeRoot) {
		
		assertThat(assertionTreeRoot.getType(), is(reasonTreeRoot.getType()));
		assertThat(assertionTreeRoot.getValue(), is(reasonTreeRoot.getValue()));
		assertThat(assertionTreeRoot.getValueType(), is(reasonTreeRoot.getValueType()));
		
		switch (assertionTreeRoot.getType()) {
		case BINARY_NODE: {

			if (null != ((BinaryNode) reasonTreeRoot).getLeftChild()) {
				comapreTrees( ((BinaryNode) reasonTreeRoot).getLeftChild(), ((BinaryNode) assertionTreeRoot).getLeftChild() );
			}
			else {
				assertThat( ((BinaryNode) assertionTreeRoot).getLeftChild(), is(IsNull.nullValue()) );
			}
			
			
			if (null != ((BinaryNode) reasonTreeRoot).getRightChild()) {
				comapreTrees( ((BinaryNode) reasonTreeRoot).getRightChild(), ((BinaryNode) assertionTreeRoot).getRightChild() );
			}
			else {
				assertThat( ((BinaryNode) assertionTreeRoot).getRightChild(), is(IsNull.nullValue())) ;
			}
			
			break;
		}
		case FUNCTION_NODE: {
			
			assertThat(
					((FunctionNode) assertionTreeRoot).getChildren().size(), 
					is(((FunctionNode) reasonTreeRoot).getChildren().size()));
			
			ListIterator<Node> reasonParameter = ((FunctionNode) reasonTreeRoot).getChildren().listIterator();
			ListIterator<Node> assertionParameter = ((FunctionNode) assertionTreeRoot).getChildren().listIterator();
			
			while (reasonParameter.hasNext()) {
				comapreTrees(reasonParameter.next(), assertionParameter.next());
			}
			
			break;
		}
		case PROGRAM_NODE:
		case BODY_NODE:
		case VAR_LIST_NODE: {
			
			assertThat(((ListNode) assertionTreeRoot).getValue(), is(IsNull.nullValue()));
			assertThat(((ListNode) assertionTreeRoot).getValueType(), is(IsNull.nullValue()));
			assertThat(((ListNode) assertionTreeRoot).getType(), is(((ListNode) reasonTreeRoot).getType()));
			
			assertThat(
					((ListNode) assertionTreeRoot).getChildren().size(), 
					is(((ListNode) reasonTreeRoot).getChildren().size()));
			
			ListIterator<Node> reasonParameter = ((ListNode) reasonTreeRoot).getChildren().listIterator();
			ListIterator<Node> assertionParameter = ((ListNode) assertionTreeRoot).getChildren().listIterator();
			
			while (reasonParameter.hasNext()) {
				comapreTrees(reasonParameter.next(), assertionParameter.next());
			}
			
			break;
		}
		case FUNCTION_DECLARATION_NODE: {
			
			assertThat( ((FunctionDeclarationNode) assertionTreeRoot).getReturnType(), is( ((FunctionDeclarationNode) reasonTreeRoot).getReturnType() ) );
			
			comapreTrees(((FunctionDeclarationNode) reasonTreeRoot).getVarList(), ((FunctionDeclarationNode) assertionTreeRoot).getVarList());
			comapreTrees(((FunctionDeclarationNode) reasonTreeRoot).getBody(), ((FunctionDeclarationNode) assertionTreeRoot).getBody());
			
			break;
		}
		case WHILE_NODE:
			
			comapreTrees( ((WhileNode) reasonTreeRoot).getLeftExpression(), ((WhileNode) assertionTreeRoot).getLeftExpression() );
			comapreTrees( ((WhileNode) reasonTreeRoot).getRightExpression(), ((WhileNode) assertionTreeRoot).getRightExpression() );
			
			comapreTrees( ((WhileNode) reasonTreeRoot).getBody(), ((WhileNode) assertionTreeRoot).getBody() );
			
			break;	
		case IF_NODE:
		
			comapreTrees( ((IfNode) reasonTreeRoot).getLeftExpression(), ((IfNode) assertionTreeRoot).getLeftExpression() );
			comapreTrees( ((IfNode) reasonTreeRoot).getRightExpression(), ((IfNode) assertionTreeRoot).getRightExpression() );
			
			comapreTrees( ((IfNode) reasonTreeRoot).getIfBody(), ((IfNode) assertionTreeRoot).getIfBody() );
			comapreTrees( ((IfNode) reasonTreeRoot).getElseBody(), ((IfNode) assertionTreeRoot).getElseBody() );
			
			break;
		default:
			throw new IllegalArgumentException("unknown node type: " + assertionTreeRoot.getType());
		
		}
	
		
	}
}
