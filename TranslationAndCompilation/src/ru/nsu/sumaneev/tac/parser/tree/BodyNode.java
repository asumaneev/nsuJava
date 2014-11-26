package ru.nsu.sumaneev.tac.parser.tree;

import java.util.List;

import ru.nsu.sumaneev.tac.parser.ParseException;

public class BodyNode extends ListNode {

	private boolean isVariablesNumberCalculated = false;
	
	public BodyNode() {
		super();
	}
	
	@Override
	public <T extends Node> T setParent(T parentNode) throws ParseException {

		switch (parentNode.getType()) {
		case WHILE_NODE:
			((WhileNode) parentNode).setBody(this);
			
			break;
		case IF_NODE:
			((IfNode) parentNode).setIfBody(this);
			break;
		case FUNCTION_DECLARATION_NODE:

			((FunctionDeclarationNode) parentNode).setBody(this);
			
			break;
		default:
			throw new ParseException("invalid parent tree");
		}

		return parentNode;
	}

	
	@Override
	public BodyNode addChild(Node child) {
		
		if ( (NodeType.IF_NODE == child.getType()) || (NodeType.WHILE_NODE == child.getType()) ) {
			children.add(child);
		}
		else {
			variablesNumber += child.getVariablesNumber();
			children.add(child);
		}
		
		if (stackSize < child.getStackSize()) {
			stackSize = child.getStackSize();
		}
		
		return this;
	}
	
	public BodyNode addChildren(List<Node> children) {
		
		for (Node child : children) {
			addChild(child);
		}
		
		return this;
	}
	
	@Override
	public int getVariablesNumber() {
		
		if (false == isVariablesNumberCalculated) {
			calculateVariablesNumber();
		}
		
		return variablesNumber;
	}
	
	@Override
	public NodeType getType() {
		return NodeType.BODY_NODE;
	}
	
	
	private void calculateVariablesNumber() {
		
		int currentVariablesNumber = 0;
		
		for (Node child : children) {
			
			if ( (NodeType.IF_NODE == child.getType()) || (NodeType.WHILE_NODE == child.getType()) ) {
				if (currentVariablesNumber + child.getVariablesNumber() > variablesNumber) {
					variablesNumber = currentVariablesNumber + child.getVariablesNumber();
				}
			}
			else {
				currentVariablesNumber += child.getVariablesNumber();
			}
		}
		
	}
}
