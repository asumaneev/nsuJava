package ru.nsu.sumaneev.tac.parser.tree;

import ru.nsu.sumaneev.tac.parser.ParseException;


public abstract class Node {

	protected String nodeValue = null;
	protected NodeValueType nodeValueType = null;
	
	protected int stackSize = 0;
	protected int variablesNumber = 0;
	
	public Node() {
		this.nodeValue = null;
		this.nodeValueType = null;
	}
	
	public Node(String nodeValue, NodeValueType nodeValueType) {
		
		this.nodeValue = nodeValue;
		this.nodeValueType = nodeValueType;
	}
	
	public void clear() {
		
		nodeValue = null;
		nodeValueType = null;
		
		variablesNumber = 0;
		stackSize = 0;
	}
	
	public abstract <T extends Node> T setParent(T parentNode)
			throws ParseException;
	
	public String getValue() {
		return nodeValue;
	}
	
	public NodeValueType getValueType() {
		return nodeValueType;
	}
	
	public int getVariablesNumber() {
		return variablesNumber;
	}

	public int getStackSize() {
		return stackSize;
	}
	
	public abstract NodeType getType();
	
	public enum NodeType {
		BINARY_NODE,
		WHILE_NODE,
		IF_NODE,
		FUNCTION_NODE,
		BODY_NODE,
		VAR_LIST_NODE,
		FUNCTION_DECLARATION_NODE,
		PROGRAM_NODE
	}
	
	public enum NodeValueType {
		NUMBER,
		NAME,
		BINARY_OPERATOR,
		UNARY_OPERATOR,
		FUNCTION_NAME,
		RETURN_STATEMENT,
		VARIABLE_DECLARATION
	}
}
