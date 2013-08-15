package org.openlegacy.rpc.exceptions;

import org.openlegacy.exceptions.EntityNotAccessibleException;

/**
 * An exception indicating a problem with a rpc action execution
 * 
 * @author Ivan Bort
 * 
 */
public class RpcActionException extends EntityNotAccessibleException {

	private static final long serialVersionUID = 1L;

	public RpcActionException(Exception e) {
		super(e);
	}

	public RpcActionException(String s) {
		super(s);
	}

}
