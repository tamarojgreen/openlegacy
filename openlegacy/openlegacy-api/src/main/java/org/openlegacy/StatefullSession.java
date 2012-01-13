package org.openlegacy;


/**
 * Represents a state-full session. Returns a snapshot of the current state
 * 
 */
public interface StatefullSession<S extends Snapshot> extends Session {

	S getSnapshot();

	S fetchSnapshot();

}
