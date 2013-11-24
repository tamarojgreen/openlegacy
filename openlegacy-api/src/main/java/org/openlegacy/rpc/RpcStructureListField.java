package org.openlegacy.rpc;

import java.util.List;

public interface RpcStructureListField extends RpcField {

	List<RpcField> getChildren(int i);

	List<List<RpcField>> getChildren();

	Integer count();

}
