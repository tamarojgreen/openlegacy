package org.openlegacy.rpc;

import org.openlegacy.exceptions.EntityNotAccessibleException;

public class RpcStructureNotMappedException extends EntityNotAccessibleException {

	private static final long serialVersionUID = 1L;

	public RpcStructureNotMappedException(Exception e) {
		super(e);
	}

	public RpcStructureNotMappedException(String s) {
		super(s);

	}

}
