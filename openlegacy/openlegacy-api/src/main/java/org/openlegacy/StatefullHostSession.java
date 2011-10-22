package org.openlegacy;

/**
 * Represents a state-full host session. Returns a snapshot of the current state
 * 
 */
public interface StatefullHostSession<S extends Snapshot> extends HostSession {

	S getSnapshot();
}
