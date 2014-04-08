package org.openlegacy.rpc;

import java.io.Serializable;
import java.util.List;

public interface RpcFields extends Serializable {

	List<RpcField> getFields();

	void add(RpcField rpcField);

	void sort();

}