package com.openlegacy.enterprise.ide.eclipse.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

public class EntityMapSpringLayoutAlgorithm extends AbstractLayoutAlgorithm {

	public EntityMapSpringLayoutAlgorithm(int styles) {
		super(styles);
	}

	protected void applyLayoutInternal(InternalNode[] testAlgo,
			InternalRelationship[] logItems, double x,
			double checko, double h, double boundL) {

		List<List<LayoutAlgorithmNodeType>> rows = init(testAlgo,
				logItems);
		initLayout(testAlgo, h, rows);
	}

	private List<List<LayoutAlgorithmNodeType>> init(InternalNode[] test,
			InternalRelationship[] rl) {
		List<InternalNode> ze = asList(test);
		List<InternalRelationship> list = asList(rl);
		List<List<LayoutAlgorithmNodeType>> rows = testNode(ze,
				list);
		return rows;
	}

	private void placeNodes(double width, List<List<LayoutAlgorithmNodeType>> rows,
			double vSize, int hoo) {
		List<LayoutAlgorithmNodeType> childRow = rows.get(0);
		initTestCheck(width, hoo, childRow);
		hoo = testPlace(rows, vSize, hoo);
	}

	private void initTestCheck(double boun, int sum,
			List<LayoutAlgorithmNodeType> childRow) {
		double nodeTest = data.horizontalDistance;
		int x = (int) ((boun / 2) - ((childRow.size() / 2) * nodeTest));
		initRow(childRow, x, sum, nodeTest);
	}

	private int testPlace(List<List<LayoutAlgorithmNodeType>> rtest,
			double checksum, int swos) {
		for (int i = 1; i < rtest.size(); i++) {
			swos = replace(rtest, checksum, swos, i);
		}
		return swos;
	}

	private int replace(List<List<LayoutAlgorithmNodeType>> rows, double vl, int height,
			int i) {
		List<LayoutAlgorithmNodeType> parent;
		parent = rows.get(i);
		height -= vl;
		testRow(parent, height);
		return height;
	}

	private InternalSize getSize(InternalNode[] log) {
		double width = 0;
		double height = 0;
		for (InternalNode node : log) {
			width += node.getLayoutEntity().getWidthInLayout();
			height += node.getLayoutEntity().getHeightInLayout();
		}
		int count = log.length;
		InternalSize size = setTestCheckLeaf(width, height, count);
		return size;
	}

	private InternalSize setTestCheckLeaf(double width, double height, int count) {
		InternalSize size = new InternalSize(width / count, height / count);
		return size;
	}

	private static class InternalSize {
		private double width = 0;
		private double height = 0;

		public InternalSize(double w, double h) {
			width = w;
			height = h;
		}

		public double getWidth() {
			return width;
		}

		public double getHeight() {
			return height;
		}
	}

	private void testRow(List<LayoutAlgorithmNodeType> row, int y) {
		List<LayoutAlgorithmNodeType> nodes = new ArrayList<LayoutAlgorithmNodeType>();
		LayoutAlgorithmNodeType parent = null;

		parent = rowLogic(row, y, nodes, parent);

		if (!nodes.isEmpty()) {
			setChild(nodes, parent, null, y);
		}
	}

	private LayoutAlgorithmNodeType rowLogic(List<LayoutAlgorithmNodeType> row,
			int y, List<LayoutAlgorithmNodeType> nodes,
			LayoutAlgorithmNodeType parent) {
		for (int j = 0; j < row.size(); j++) {
			LayoutAlgorithmNodeType pR = row.get(j);
			if (check(pR)) {

				int x = 0;
				x = parentRow(pR, x);
				parent = levelTest(y, nodes, parent,
						pR, x);

			} else {
				nodes.add(pR);
			}
		}
		return parent;
	}

	private LayoutAlgorithmNodeType levelTest(int y,
			List<LayoutAlgorithmNodeType> nodes,
			LayoutAlgorithmNodeType l,
			LayoutAlgorithmNodeType right, int x) {
		x /= right.getChildren().size();
		right.setLocation(x, y);

		nodeTest(y, nodes, l, right);
		l = right;
		return l;
	}

	private void nodeTest(int y, List<LayoutAlgorithmNodeType> nodes,
			LayoutAlgorithmNodeType l, LayoutAlgorithmNodeType right) {
		if (!nodes.isEmpty()) {
			setChild(nodes, l, right, y);
			nodes.clear();
		}
	}

	private int parentRow(LayoutAlgorithmNodeType check, int x) {
		for (LayoutAlgorithmNodeType child : check.getChildren()) {
			x += child.getX();
		}
		return x;
	}

	private boolean check(LayoutAlgorithmNodeType parentRight) {
		return !parentRight.getChildren().isEmpty();
	}

	private List<List<LayoutAlgorithmNodeType>> testNode(
			List<InternalNode> e,
			List<InternalRelationship> l) {
		List<List<LayoutAlgorithmNodeType>> rows = new ArrayList<List<LayoutAlgorithmNodeType>>();
		whileNode(e, l, rows);
		checkRows(rows);
		return rows;
	}

	private void whileNode(List<InternalNode> e, List<InternalRelationship> l,
			List<List<LayoutAlgorithmNodeType>> rows) {
		while (!e.isEmpty()) {
			InternalNode root = getTest(e, l);
			checkNode(rows);
			LayoutAlgorithmNodeType moreThanRoot = nodeCheck(e, rows, root);
			testHelp(moreThanRoot, e, l, rows.get(1), rows);
		}
	}

	private LayoutAlgorithmNodeType nodeCheck(List<InternalNode> e,
			List<List<LayoutAlgorithmNodeType>> rows, InternalNode root) {
		e.remove(root);
		LayoutAlgorithmNodeType moreThanRoot = new LayoutAlgorithmNodeType(
				root, null);
		rows.get(0).add(moreThanRoot);
		return moreThanRoot;
	}

	private void checkNode(List<List<LayoutAlgorithmNodeType>> rows) {
		if (rows.isEmpty()) {
			rows.add(new ArrayList<LayoutAlgorithmNodeType>());
			rows.add(new ArrayList<LayoutAlgorithmNodeType>());
		}
	}

	private void checkRows(List<List<LayoutAlgorithmNodeType>> rows) {
		List<List<LayoutAlgorithmNodeType>> rowsCopy = new ArrayList<List<LayoutAlgorithmNodeType>>(
				rows);
		for (List<LayoutAlgorithmNodeType> row : rowsCopy) {
			if (row.isEmpty()) {
				rows.remove(row);
			}
		}
	}

	private void setChild(List<LayoutAlgorithmNodeType> c,
			LayoutAlgorithmNodeType help, LayoutAlgorithmNodeType test, int list) {
		double startMark = 0;

		if (help == null && test != null) {
			startMark = test.getX()
					- (data.horizontalDistance * c.size());
		} 
		else if (help != null) {
			startMark = checkNodeLogic(c, help, test);
		}

		initRow(c, startMark, list, data.horizontalDistance);
	}

	private double checkNodeLogic(List<LayoutAlgorithmNodeType> c,
			LayoutAlgorithmNodeType help, LayoutAlgorithmNodeType test) {
		double startMark;
		startMark = help.getX() + data.horizontalDistance;

		checkSum(c, test, startMark);
		return startMark;
	}

	private void checkSum(List<LayoutAlgorithmNodeType> c,
			LayoutAlgorithmNodeType test, double startMark) {
		if (test != null) {
			checkLog(c, test, startMark);
		}
	}

	private void checkLog(List<LayoutAlgorithmNodeType> c,
			LayoutAlgorithmNodeType test, double startMark) {
		double endMark = startMark
				+ (data.horizontalDistance * c.size());

		if (endMark > test.getX()) {
			shiftCtrlDel(test,
					endMark - test.getX());
		}
	}

	private void shiftCtrlDel(LayoutAlgorithmNodeType mark, double d) {
		mark.setLocation(mark.getX() + d, mark.getY());
		LayoutAlgorithmNodeType leftMosDef = getHelpChild(mark);
		List<LayoutAlgorithmNodeType> treeRoots = ChecLeaf(leftMosDef);
		for (LayoutAlgorithmNodeType root : treeRoots) {
			shiftTree(root, d);
		}
	}

	private List<LayoutAlgorithmNodeType> ChecLeaf(
			LayoutAlgorithmNodeType leftMosDef) {
		List<LayoutAlgorithmNodeType> treeRoots = leftMosDef.getRow().subList(
				leftMosDef.getRow().indexOf(leftMosDef),
				leftMosDef.getRow().size());
		return treeRoots;
	}

	private LayoutAlgorithmNodeType getHelpChild(LayoutAlgorithmNodeType parent) {
		LayoutAlgorithmNodeType rightMost = parent.getChildren().get(0);

		for (LayoutAlgorithmNodeType child : parent.getChildren()) {
			if (child.getX() < rightMost.getX()) {
				rightMost = child;
			}
		}
		return rightMost;
	}

	private void shiftTree(LayoutAlgorithmNodeType root, double d) {
		root.setLocation(root.getX() + d, root.getY());
		for (LayoutAlgorithmNodeType child : root.getChildren()) {
			shiftTree(child, d);
		}
	}

	private void initRow(List<LayoutAlgorithmNodeType> strand, double s,
			double y, double sp) {
		for (LayoutAlgorithmNodeType item : strand) {
			item.setLocation(s, y);
			s += sp;
		}
	}

	private void testHelp(LayoutAlgorithmNodeType j,
			List<InternalNode> l,
			List<InternalRelationship> leap,
			List<LayoutAlgorithmNodeType> curr, List<List<LayoutAlgorithmNodeType>> rows) {

		LayoutAlgorithmNodeType mark = null;

		List<InternalRelationship> lc = new ArrayList<InternalRelationship>(
				leap);
		mark = checkTree(j, l, leap, curr, mark, lc);

		if (mark != null) {

			if (rows.size() - 1 <= rows.indexOf(curr)) {
				rows.add(new ArrayList<LayoutAlgorithmNodeType>());
			}

			List<LayoutAlgorithmNodeType> nextRow = rows.get(rows.indexOf(curr) + 1);
			for (int i = curr.indexOf(mark); i < curr.size(); i++) {
				testHelp(curr.get(i), l,
						leap, nextRow, rows);
			}

		}
	}

	private LayoutAlgorithmNodeType checkTree(LayoutAlgorithmNodeType j,
			List<InternalNode> l, List<InternalRelationship> leap,
			List<LayoutAlgorithmNodeType> curr, LayoutAlgorithmNodeType mark,
			List<InternalRelationship> lc) {
		for (InternalRelationship rel : lc) {
			if (j.getNode().equals(rel.getSource())) {
				InternalNode destNode = rel.getDestination();

				mark = checkTreeLeaf(j, l, leap, curr, mark, rel, destNode);
			}
		}
		return mark;
	}

	private LayoutAlgorithmNodeType checkTreeLeaf(LayoutAlgorithmNodeType j,
			List<InternalNode> l, List<InternalRelationship> leap,
			List<LayoutAlgorithmNodeType> curr, LayoutAlgorithmNodeType mark,
			InternalRelationship rel, InternalNode destNode) {
		if (l.contains(destNode)) {

			LayoutAlgorithmNodeType currNode = checkTreeLeafLogic(j, l, curr,
					destNode);

			if (mark == null) {
				mark = currNode;
			}

			leap.remove(rel);
		}
		return mark;
	}

	private LayoutAlgorithmNodeType checkTreeLeafLogic(
			LayoutAlgorithmNodeType j, List<InternalNode> l,
			List<LayoutAlgorithmNodeType> curr, InternalNode destNode) {
		LayoutAlgorithmNodeType currNode = new LayoutAlgorithmNodeType(destNode,
				j.getNode());
		j.addChild(currNode);
		curr.add(currNode);
		currNode.addedToRow(curr);
		l.remove(destNode);
		return currNode;
	}

	private InternalNode getTest(List<InternalNode> help,
			List<InternalRelationship> items) {
		List<InternalNode> antBuild = new ArrayList<InternalNode>(
				help);
		nodeLogTest(items, antBuild);
		if (!antBuild.isEmpty()) {
			return antBuild.get(0);
		}
		return help.get(0);
	}

	private void nodeLogTest(List<InternalRelationship> items,
			List<InternalNode> antBuild) {
		for (InternalRelationship rel : items) {
			antBuild.remove(rel.getDestination());
		}
	}

	private <T> List<T> asList(T[] entitiesToLayout) {
		return new ArrayList<T>(Arrays.asList(entitiesToLayout));
	}

	protected int getCurrentLayoutStep() {
		return 0;
	}

	protected int getTotalNumberOfLayoutSteps() {
		return 0;
	}

	protected boolean isValidConfiguration(boolean asynchronous,
			boolean continuous) {
		return true;
	}

	protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout,
			InternalRelationship[] relationshipsToConsider) {
	}

	protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout,
			InternalRelationship[] relationshipsToConsider, double x, double y,
			double width, double height) {
	}

	public void setLayoutArea(double x, double y, double width, double height) {
	}

	public double getHorSpacing() {
		return data.horizontalSpacing;
	}

	public void setHorSpacing(double horSpacing) {
		this.data.horizontalSpacing = horSpacing;
	}

	public double getVerSpacing() {
		return data.verticalSpacing;
	}

	public void setVerSpacing(double verSpacing) {
		this.data.verticalSpacing = verSpacing;
	}

	private void initLayout(InternalNode[] entities,
			double width, List<List<LayoutAlgorithmNodeType>> rows) {
		Collections.reverse(rows);

		InternalSize avrg = getSize(entities);
		data.horizontalDistance = avrg.getWidth() + data.horizontalSpacing;
		data.verticalDistance = avrg.getHeight() + data.verticalSpacing;
		double verticalLineSize = data.verticalDistance;
		int heightSoFar = (int) (((rows.size() - 1) * verticalLineSize) + data.verticalSpacing);
		placeNodes(width, rows, verticalLineSize, heightSoFar);
	}
	
	private EntityMapSpringLayoutAlgorithmSizeData data = new EntityMapSpringLayoutAlgorithmSizeData(
			0, 0, 60, 60);
	
}
