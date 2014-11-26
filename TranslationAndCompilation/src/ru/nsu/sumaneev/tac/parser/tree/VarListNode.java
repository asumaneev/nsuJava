package ru.nsu.sumaneev.tac.parser.tree;

import java.util.List;

import ru.nsu.sumaneev.tac.parser.ParseException;

public class VarListNode extends ListNode {

	@Override
	public <T extends Node> T setParent(T parentNode) throws ParseException {

		switch (parentNode.getType()) {
		
		case FUNCTION_DECLARATION_NODE:
		
			((FunctionDeclarationNode) parentNode).setVarList(this);
			
			break;
		default:
			throw new ParseException("invalid parent tree");
		
		}
		
		
		return parentNode;
	}
	
	@Override
	public VarListNode addChild(Node child) {

		super.addChild(child);
		
		return this;
	}
	
	public VarListNode addChildren(List<Node> children) {
		
		super.addChildren(children);
		
		return this;
	}
	

	@Override
	public NodeType getType() {

		return NodeType.VAR_LIST_NODE;
	}

}
