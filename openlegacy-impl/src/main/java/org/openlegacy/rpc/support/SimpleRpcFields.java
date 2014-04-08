package org.openlegacy.rpc.support;

import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFields;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleRpcFields implements RpcFields {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElements({ @XmlElement(name = "field", type = SimpleRpcFlatField.class),
			@XmlElement(name = "structure", type = SimpleRpcStructureField.class),
			@XmlElement(name = "structure-list", type = SimpleRpcStructureListField.class) })
	List<RpcField> fields = new ArrayList<RpcField>();

	public List<RpcField> getFields() {
		return fields;
	}

	public void add(RpcField rpcField) {
		fields.add(rpcField);

	}

	public void sort() {
		Collections.sort(fields, new RpcOrderFieldComparator());

	}

}
