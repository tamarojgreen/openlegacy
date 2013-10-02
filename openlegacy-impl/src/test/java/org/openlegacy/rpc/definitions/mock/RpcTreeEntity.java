package org.openlegacy.rpc.definitions.mock;

import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.rpc.definitions.mock.RpcTreeEntity.WPart1.NestedPart;

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
	public static class APart1 {

		@RpcField(length = 20)
		String innerVariable;
	}

	@RpcPart
	public static class WPart1 {

		@RpcField(length = 20)
		String innerVariable;

		@RpcPart
		public static class NestedPart {

			@RpcField(length = 20)
			String innerVariable;
		}
	}

	@RpcPart
	public static class TreePart {

		private NestedPart nestedPart;
		private WPart1 prior;
		private WPart2 after;
		private APart1 p;
		private APart2 a;

	}

	@RpcPart
	public static class WPart2 {

		@RpcField(length = 20)
		String innerVariable;
	}

	@RpcPart
	public static class APart2 {

		@RpcField(length = 20)
		String innerVariable;
	}

}