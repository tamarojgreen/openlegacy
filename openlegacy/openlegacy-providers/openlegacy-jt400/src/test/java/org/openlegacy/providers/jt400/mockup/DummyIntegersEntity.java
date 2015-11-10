package org.openlegacy.providers.jt400.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.rpc.actions.RpcActions.READ;

import java.util.ArrayList;
import java.util.List;

@RpcEntity(name = "DummyIntegersEntity")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/GROUPTEST.PGM", global = false), })
public class DummyIntegersEntity implements org.openlegacy.rpc.RpcEntity {

	private Param param;

	public Param getParam() {
		return param;
	}

	public void setParam(Param param) {
		this.param = param;
	}

	@RpcPart(name = "Param1")
	public static class Param {

		@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
		@RpcField(length = 2, originalName = "CHILD1", defaultValue = "10")
		private Integer child1;

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

		@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
		@RpcField(length = 2, originalName = "CHILD2")
		private Integer child2;

		@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
		@RpcField(length = 2, originalName = "GROUP1CHILD1")
		private Integer group1child1;

		@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
		@RpcField(length = 2, originalName = "GROUP1CHILD2")
		private Integer group1child2;

		@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
		@RpcField(length = 2, originalName = "GROUP1CHILD3")
		private Integer group1child3;

		@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
		@RpcField(length = 2, originalName = "GROUP2CHILD1")
		private Integer group2child1;

		@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
		@RpcField(length = 2, originalName = "GROUP2CHILD2")
		private Integer group2child2;

	}

	@Override
	public List<RpcActionDefinition> getActions() {
		return new ArrayList<RpcActionDefinition>();
	}
}
