package org.openlegacy.providers.jt400.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.rpc.actions.RpcActions.READ;

@RpcEntity(name = "DummyGroupEntity")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/GROUPTEST2.PGM", global = false), })
public class DummyGroupEntity implements org.openlegacy.rpc.RpcEntity {

	private Container container;

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	@RpcPart(name = "Param1", helpText = "stam")
	public static class Container {

		@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
		@RpcField(length = 2, originalName = "CHILD1", defaultValue = "10", order = 0)
		private Integer child1;

		@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
		@RpcField(length = 2, originalName = "CHILD2", order = 1)
		private Integer child2;

		private Group1 group1;

		private Group2 group2;

		public Integer getChild1() {
			return child1;
		}

		public void setChild1(Integer child1) {
			this.child1 = child1;
		}

		public Integer getChild2() {
			return child2;
		}

		public void setChild2(Integer child2) {
			this.child2 = child2;
		}

		public Group1 getGroup1() {
			return group1;
		}

		public void setGroup1(Group1 group1) {
			this.group1 = group1;
		}

		public Group2 getGroup2() {
			return group2;
		}

		public void setGroup2(Group2 group2) {
			this.group2 = group2;
		}

	}

	@RpcPart(name = "Group1", virtual = true)
	public static class Group1 {

		@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
		@RpcField(length = 2, originalName = "GROUP1CHILD1", order = 2)
		private Integer group1child1;

		@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
		@RpcField(length = 2, originalName = "GROUP1CHILD2", order = 3)
		private Integer group1child2;

		@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
		@RpcField(length = 2, originalName = "GROUP1CHILD3", order = 4)
		private Integer group1child3;

		public Integer getGroup1child1() {
			return group1child1;
		}

		public void setGroup1child1(Integer group1child1) {
			this.group1child1 = group1child1;
		}

		public Integer getGroup1child2() {
			return group1child2;
		}

		public void setGroup1child2(Integer group1child2) {
			this.group1child2 = group1child2;
		}

		public Integer getGroup1child3() {
			return group1child3;
		}

		public void setGroup1child3(Integer group1child3) {
			this.group1child3 = group1child3;
		}

	}

	@RpcPart(name = "Group2", virtual = true)
	public static class Group2 {

		@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
		@RpcField(length = 2, originalName = "GROUP2CHILD1", order = 5)
		private Integer group2child1;

		@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
		@RpcField(length = 2, originalName = "GROUP2CHILD2", order = 5)
		private Integer group2child2;

		public Integer getGroup2child1() {
			return group2child1;
		}

		public void setGroup2child1(Integer group2child1) {
			this.group2child1 = group2child1;
		}

		public Integer getGroup2child2() {
			return group2child2;
		}

		public void setGroup2child2(Integer group2child2) {
			this.group2child2 = group2child2;
		}
	}
}
