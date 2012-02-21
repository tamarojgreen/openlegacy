package org.openlegacy.terminal.table;

import org.openlegacy.modules.table.drilldown.TableScroller;
import org.openlegacy.terminal.TerminalSession;

/**
 * Defines a table scroller on a terminal session and a given screen entity
 * 
 * @param <T>
 *            The screen entity class
 */
public interface ScreenTableScroller<T> extends TableScroller<TerminalSession, T> {

}
