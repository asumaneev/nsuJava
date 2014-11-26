package ru.nsu.sumaneev.tac.parser.tree;

import java.util.List;

import ru.nsu.sumaneev.tac.parser.ParseException;

/**
 * root of this node is function name
 * 
 * children are expressions
 * 
 * @author Artem
 * 
 */

public class FunctionNode extends ListNode {

	public FunctionNode(String nodeValue, NodeValueType nodeValueType) {
		super(nodeValue, nodeValueType);

		if (NodeValueType.FUNCTION_NAME != nodeValueType) {
			throw new IllegalArgumentException("invalid FunctionNode type");
		}
	}

	@Override
	public <T extends Node> T setParent(T parentNode) throws ParseException {

		switch (parentNode.getType()) {
		case BINARY_NODE:

			((BinaryNode) parentNode).setLeftChild(this);

			break;
		case WHILE_NODE:
			((WhileNode) parentNode).setLeftExpression(this);
			
			break;
		case IF_NODE:
			((IfNode) parentNode).setLeftExpression(this);
			
			break;
		case FUNCTION_NODE:
		case BODY_NODE:

			((ListNode) parentNode).addChild(this);

			break;

		default:
			throw new ParseException("invalid parent tree");
		}

		return parentNode;
	}
	
	@Override
	public FunctionNode addChild(Node child) {

		super.addChild(child);
		
		if (stackSize < child.getStackSize()) {
			stackSize = child.getStackSize();
		}
		
		return this;
	}
	
	public FunctionNode addChildren(List<Node> children) {
		
		super.addChildren(children);
		
		for (Node child : children) {
			if (stackSize < child.getStackSize()) {
				stackSize = child.getStackSize();
			}
		}
		
		return this;
	}
	
	@Override
	public int getStackSize() {
		return stackSize + 2;
	}
	
	
	@Override
	public NodeType getType() {
		return NodeType.FUNCTION_NODE;
	}

}
