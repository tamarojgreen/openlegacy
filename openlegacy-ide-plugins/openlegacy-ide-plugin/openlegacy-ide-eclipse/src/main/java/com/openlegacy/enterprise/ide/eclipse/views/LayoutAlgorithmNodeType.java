package com.openlegacy.enterprise.ide.eclipse.views;

import org.eclipse.zest.layouts.dataStructures.InternalNode;

import java.util.ArrayList;
import java.util.List;

public class LayoutAlgorithmNodeType {

	private InternalNode m_node;
	private List<LayoutAlgorithmNodeType> m_children;
	private List<LayoutAlgorithmNodeType> m_row;
	private double m_y;
	private double m_x;

	public LayoutAlgorithmNodeType(InternalNode node, InternalNode parent) {
		m_node = node;
		m_children = new ArrayList<LayoutAlgorithmNodeType>();
	}

	public void addedToRow(List<LayoutAlgorithmNodeType> row) {
		m_row = row;
	}

	public void setLocation(double d, double e) {
		m_x = d;
		m_y = e;
		m_node.setLocation(d, e);
	}

	public void addChild(LayoutAlgorithmNodeType currNode) {
		m_children.add(currNode);
	}

	public List<LayoutAlgorithmNodeType> getChildren() {
		return m_children;
	}

	public InternalNode getNode() {
		return m_node;
	}

	public double getY() {
		return m_y;
	}

	public double getX() {
		return m_x;
	}

	public List<LayoutAlgorithmNodeType> getRow() {
		return m_row;
	}
}
