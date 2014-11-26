package ru.nsu.sumaneev.tac.parser.tree;

import java.util.LinkedList;
import java.util.List;


public abstract class ListNode extends Node {


	protected List<Node> children = null;
	
	public ListNode() {
		super();
		
		children = new LinkedList<Node>();
	}
	
	public ListNode(String nodeValue, NodeValueType nodeValueType) {
		super(nodeValue, nodeValueType);
		
		children = new LinkedList<Node>();
	}
	
	@Override
	public void clear() {
		super.clear();

		children.clear();
	}

	public ListNode addChild(Node child) {

		children.add(child);
		
		variablesNumber += child.variablesNumber;

		return this;
	}
	
	public ListNode addChildren(List<Node> children) {
		
		this.children.addAll(children);
		
		for (Node child : children) {
			variablesNumber += child.variablesNumber;
		}
		
		return this;
	}
	
	public List<Node> getChildren() {
		return children;
	}
}
