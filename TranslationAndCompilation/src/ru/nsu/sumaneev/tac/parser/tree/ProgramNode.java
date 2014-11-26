package ru.nsu.sumaneev.tac.parser.tree;

import java.util.List;

import ru.nsu.sumaneev.tac.parser.ParseException;

public class ProgramNode extends ListNode {

	@Override
	public <T extends Node> T setParent(T parentNode) throws ParseException {
		
		throw new ParseException("invalid parent tree");
	}

	@Override
	public ProgramNode addChild(Node child) {

		super.addChild(child);
		
		return this;
	}
	
	public ProgramNode addChildren(List<Node> children) {
		
		super.addChildren(children);
		
		return this;
	}
	
	@Override
	public NodeType getType() {
		return NodeType.PROGRAM_NODE;
	}

}
