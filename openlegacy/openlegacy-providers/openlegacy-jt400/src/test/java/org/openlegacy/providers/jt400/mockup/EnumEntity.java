package org.openlegacy.providers.jt400.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.definitions.EnumGetValue;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.rpc.actions.RpcActions.READ;

import java.util.ArrayList;
import java.util.List;

@RpcEntity(name = "EnumEntity")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/ENUMTEST.PGM") })
public class EnumEntity implements org.openlegacy.rpc.RpcEntity {

	@RpcField(originalName = "firstColor", length = 20)
	private ColorGroup firstColor;

	@RpcField(originalName = "secondColor", length = 20)
	private ColorGroup secondColor;

	public enum ColorGroup implements EnumGetValue {
		Yellow("1", "THE YELLOW COLOR"),
		Red("2", "THE RED COLOR");

		private String value;
		private String display;

		ColorGroup(String value, String display) {
			this.value = value;
			this.display = display;
		}

		@Override
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return display;
		}
	}

	public ColorGroup getFirstColor() {
		return firstColor;
	}

	public void setFirstColor(ColorGroup firstColor) {
		this.firstColor = firstColor;
	}

	public ColorGroup getSecondColor() {
		return secondColor;
	}

	public void setSecondColor(ColorGroup secondColor) {
		this.secondColor = secondColor;
	}

	@Override
	public List<RpcActionDefinition> getActions() {
		return new ArrayList<RpcActionDefinition>();
	}
}
