package ru.nsu.sumaneev.tac.parser.tree;

import ru.nsu.sumaneev.tac.parser.ParseException;

public class FunctionDeclarationNode extends Node {

	private String returnType = null;
	
	private VarListNode varList = null;
	private BodyNode body = null;
	
	public FunctionDeclarationNode(String nodeValue, NodeValueType nodeValueType) {
		super(nodeValue, nodeValueType);
		
		if (NodeValueType.FUNCTION_NAME != nodeValueType) {
			throw new IllegalArgumentException("invalid FunctionDeclarationNode type");
		}
	}
	
	public FunctionDeclarationNode(String nodeValue, NodeValueType nodeValueType, String returnType) {
		super(nodeValue, nodeValueType);
		
		if (NodeValueType.FUNCTION_NAME != nodeValueType) {
			throw new IllegalArgumentException("invalid FunctionDeclarationNode type");
		}
		
		this.returnType = returnType;
	}
	
	
	
	@Override
	public <T extends Node> T setParent(T parentNode) throws ParseException {
		
		switch (parentNode.getType()) {
		case PROGRAM_NODE:
			
			((ListNode) parentNode).addChild(this);
			
			break;
		default:
			throw new ParseException("invalid parent tree");
		}
		
		return parentNode;
	}

	
	
	public FunctionDeclarationNode setReturnType(String returnType) {
		
		this.returnType = returnType;
		
		return this;
	}
	
	public FunctionDeclarationNode setVarList(VarListNode varList) {
		
		this.varList = varList;
		
		variablesNumber += varList.getVariablesNumber();
		
		return this;
	}

	public FunctionDeclarationNode setBody(BodyNode body) {
		
		this.body = body;
		
		//stackSize = body.getStackSize();
		//stackSize = 100;
		stackSize = 2 * body.getStackSize();
		variablesNumber += body.getVariablesNumber();
		
		return this;
	}
	
	
	
	
	public String getReturnType() {
		return returnType;
	}
	
	public BodyNode getBody() {
		return body;
	}
	
	public VarListNode getVarList() {
		return varList;
	}
	
	
	@Override
	public NodeType getType() {
		return NodeType.FUNCTION_DECLARATION_NODE;
	}


}
