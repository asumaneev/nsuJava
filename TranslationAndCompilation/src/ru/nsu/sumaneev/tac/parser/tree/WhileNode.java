package ru.nsu.sumaneev.tac.parser.tree;

import ru.nsu.sumaneev.tac.parser.ParseException;


public class WhileNode extends Node {

	private Node leftExpression = null;
	private Node rightExpression = null;
	
	private BodyNode body = null;
	
	public WhileNode(String sign) {
		super(sign, NodeValueType.BINARY_OPERATOR);
	}
	
	
	
	public WhileNode setLeftExpression(Node leftExpression) {
		this.leftExpression = leftExpression;
		
		if (stackSize < leftExpression.getStackSize()) {
			stackSize = leftExpression.getStackSize();
		}
		
		return this;
	}
	
	public WhileNode setRightExpression(Node rightExpression) {
		this.rightExpression = rightExpression;
		
		if (stackSize < rightExpression.getStackSize()) {
			stackSize = rightExpression.getStackSize();
		}
		
		return this;
	}
	
	public WhileNode setBody(BodyNode body) {
	
		this.body = body;
		
		variablesNumber = body.getVariablesNumber();
		
		if (stackSize < body.getStackSize()) {
			stackSize = body.getStackSize();
		}
		
		return this;
	}
	
	@Override
	public <T extends Node> T setParent(T parentNode) throws ParseException {
		switch (parentNode.getType()) {
		case BODY_NODE:
			
			((ListNode) parentNode).addChild(this);
			
			break;
		
		default:
			throw new ParseException("invalid parent tree");
		}
		
		return parentNode;
	}

	
	public Node getLeftExpression() {
		return leftExpression;
	}
	public Node getRightExpression() {
		return rightExpression;
	}
	public BodyNode getBody() {
		return body;
	}



	@Override
	public NodeType getType() {
		return NodeType.WHILE_NODE;
	}


}
