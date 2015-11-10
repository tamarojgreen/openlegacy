package org.openlegacy.rpc.definitions.mock;

import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.annotations.rpc.RpcPartList;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.rpc.definitions.mock.RpcTreeEntity.WPart1.NestedPart;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
@RpcEntity()
public class RpcTreeEntity implements org.openlegacy.rpc.RpcEntity {

	SimplePart simplePart;

	@RpcPart(name = "SimplePart")
	public static class SimplePart {

		@RpcField(length = 20, key = true, keyIndex = 2)
		String firstName;
	}

	@RpcPartList(count = 2)
	List<ListPart> listPart;

	@RpcPart(name = "ListPart")
	public static class ListPart {

		@RpcField(length = 20, key = true)
		String firstName;
	}

	TreePart treePart;

	@RpcPart(name = "APart1")
	public static class APart1 {

		@RpcField(length = 20)
		String innerVariable;
	}

	@RpcPart(name = "WPart1")
	public static class WPart1 {

		@RpcField(length = 20)
		String innerVariable;

		@RpcPart(name = "NestedPart")
		public static class NestedPart {

			@RpcField(length = 20, key = true, keyIndex = 1)
			String innerVariable;
		}
	}

	@RpcPart(name = "TreePart")
	public static class TreePart {

		private NestedPart nestedPart;
		private WPart1 prior;
		private WPart2 after;
		private APart1 p;
		private APart2 a;

	}

	@RpcPart(name = "WPart2")
	public static class WPart2 {

		@RpcField(length = 20)
		String innerVariable;
	}

	@RpcPart(name = "APart2")
	public static class APart2 {

		@RpcField(length = 20)
		String innerVariable;
	}

	@Override
	public List<RpcActionDefinition> getActions() {
		return Collections.emptyList();
	}

}