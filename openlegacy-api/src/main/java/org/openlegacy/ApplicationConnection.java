package org.openlegacy;

/**
 * Represent an connection to an abstract application. Currently implemented for
 * {@link org.openlegacy.terminal.TerminalConnection}.
 * 
 * Provides API for retrieving a snapshot with/without cached, and API for common abstract application interaction
 * 
 * @author Roi Mor
 * 
 * @param <S>
 *            The type of snapshot used
 * @param <A>
 *            The type of send action used
 */
public interface ApplicationConnection<S extends Snapshot, A extends SendAction> {

	S getSnapshot();

	S fetchSnapshot();

	Object getDelegate();

	boolean isConnected();

	void disconnect();

	void doAction(A sendAction);
}
