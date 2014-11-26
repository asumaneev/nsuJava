package ru.nsu.sumaneev.tac.parser.tree;

import ru.nsu.sumaneev.tac.parser.ParseException;

public class IfNode extends Node {

	private Node leftExpression = null;
	private Node rightExpression = null;
	
	private BodyNode ifBody = null;
	private BodyNode elseBody = null;
	
	
	public IfNode(String sign) {
		super(sign, NodeValueType.BINARY_OPERATOR);

	}
	
	
	
	public IfNode setLeftExpression(Node leftExpression) {
		this.leftExpression = leftExpression;
		
		if (stackSize < leftExpression.getStackSize()) {
			stackSize = leftExpression.getStackSize();
		}
		
		return this;
	}
	
	public IfNode setRightExpression(Node rightExpression) {
		this.rightExpression = rightExpression;
		
		if (stackSize < rightExpression.getStackSize()) {
			stackSize = rightExpression.getStackSize();
		}
		
		return this;
	}
	
	public IfNode setIfBody(BodyNode ifBody) {
	
		this.ifBody = ifBody;
		
		if (variablesNumber < ifBody.getVariablesNumber()) {
			variablesNumber = ifBody.getVariablesNumber();
		}
		
		if (stackSize < ifBody.getStackSize()) {
			stackSize = ifBody.getStackSize();
		}
				
		return this;
	}
	
	public IfNode setElseBody(BodyNode elseBody) {
		
		this.elseBody = elseBody;
		
		if (variablesNumber < elseBody.getVariablesNumber()) {
			variablesNumber = elseBody.getVariablesNumber();
		}
		
		if (stackSize < elseBody.getStackSize()) {
			stackSize = elseBody.getStackSize();
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
	public BodyNode getIfBody() {
		return ifBody;
	}
	public BodyNode getElseBody() {
		return elseBody;
	}

	
	@Override
	public NodeType getType() {
		return NodeType.IF_NODE;
	}

}
