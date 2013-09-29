package org.openlegacy.rpc.definitions.mock;

import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcPart;

import java.util.List;

@RpcEntity()
public class RpcTreeEntity implements org.openlegacy.rpc.RpcEntity {

	SimplePart simplePart;

	@RpcPart
	public static class SimplePart {

		@RpcField(length = 20)
		String firstName;
	}

	List<ListPart> listPart;

	@RpcPart(occur = 5)
	public static class ListPart {

		@RpcField(length = 20)
		String firstName;
	}

	TreePart treePart;

	@RpcPart
	public static class TreePart {

		private NestedPart nestedPart;

		public NestedPart getNestedPart() {
			return nestedPart;
		}

		public void setNestedPart(NestedPart nestedPart) {
			this.nestedPart = nestedPart;
		}
	}

	@RpcPart
	public static class NestedPart {

		@RpcField(length = 20)
		String innerVariable;
	}

}
