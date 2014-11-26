package ru.nsu.sumaneev.tac.parser.tree;

import ru.nsu.sumaneev.tac.parser.ParseException;

/**
 * Tree for / + - * ^ 	(with two children).
 * 
 * Tree for - ()		(with one child).
 * 
 * Tree for =			(left child - name, right child - expression).
 * 
 * Tree for return statement.
 * 
 * Tree for "type name".
 * 
 * Tree for function call
 *
 */
public class BinaryNode extends Node {

	private Node leftChild = null;
	private Node rightChild = null;

	
	public BinaryNode(String nodeValue, NodeValueType nodeValueType) {
		super(nodeValue, nodeValueType);
		
		stackSize = Integer.MIN_VALUE;
		
		if (NodeValueType.VARIABLE_DECLARATION == nodeValueType) {
			
			if (nodeValue.equals("double")) {
				variablesNumber = 2;
				stackSize = 2;
			}
			else {
				variablesNumber = 1;
				stackSize = 1;
			}
		}
	}
	
	@Override
	public void clear() {
		super.clear();
		
		leftChild = null;
		rightChild = null;
	}
	
	/**
	 * 
	 * @param leftChild
	 * @return parent of leftChild
	 */
	public BinaryNode setLeftChild(Node leftChild) {
		
		this.leftChild = leftChild;
		
		return this;
	}
	
	/**
	 * 
	 * @param rightChild
	 * @return parent of rightChild
	 */
	public BinaryNode setRightChild(Node rightChild) {
		
		this.rightChild = rightChild;

		return this;
	}
	
	/**
	 * current tree become left child of new root
	 * current tree become child of of new root
	 * 
	 * @param parentNodeValue
	 * @param parentNodeValueType
	 * @return root of new tree
	 * @throws ParseException 
	 */
	@Override
	public <T extends Node> T setParent(T parentNode) throws ParseException {
		
		switch (parentNode.getType()) {
		case BINARY_NODE:
			((BinaryNode) parentNode).setLeftChild(this);
			
			break;
		case FUNCTION_NODE:
		case BODY_NODE:
			
			((ListNode) parentNode).addChild(this);
			
			break;
		case WHILE_NODE:
			((WhileNode) parentNode).setLeftExpression(this);
			
			break;
		case IF_NODE:
			((IfNode) parentNode).setLeftExpression(this);
			
			break;
		default:
			throw new ParseException("invalid parent tree");
		}
		
		return parentNode;
	}
	
	public BinaryNode setStackSize(int stackSize) {
		this.stackSize = stackSize;
		
		return this;
	} 
	
	public Node getLeftChild() {
		return leftChild;
	}
	
	public Node getRightChild() {
		return rightChild;
	}

	@Override
	public NodeType getType() {
		
		return NodeType.BINARY_NODE;
	}
	
	@Override
	public int getStackSize() {
		calculateStackSize();
		
		return stackSize;
	}
	
	private void calculateStackSize() {
	
		//	leaf
		if (	(NodeValueType.NAME == nodeValueType) 
				|| (NodeValueType.NUMBER == nodeValueType) 
				|| (NodeValueType.VARIABLE_DECLARATION == nodeValueType)) {
			return;
		}
		if (NodeValueType.RETURN_STATEMENT == nodeValueType) {
			stackSize = leftChild.getStackSize();
		}
		//	unary operator
		else if (NodeValueType.UNARY_OPERATOR == nodeValueType) {
			stackSize = 2 * leftChild.getStackSize(); 
		}
		else {

			if (	((NodeValueType.UNARY_OPERATOR == leftChild.getValueType()) 
						|| (NodeValueType.NAME == leftChild.getValueType())
						|| (NodeValueType.NUMBER == leftChild.getValueType())
						|| (NodeValueType.FUNCTION_NAME == leftChild.getValueType()))
					&& ((NodeValueType.UNARY_OPERATOR == rightChild.getValueType()) 
						|| (NodeValueType.NAME == rightChild.getValueType())
						|| (NodeValueType.NUMBER == rightChild.getValueType())
						|| (NodeValueType.FUNCTION_NAME == leftChild.getValueType()))
				) {
				
				stackSize = leftChild.getStackSize() + rightChild.getStackSize();
			}
			else {
				stackSize = Math.max(leftChild.getStackSize(), rightChild.getStackSize());
			}
			
		}
	}
	
}
