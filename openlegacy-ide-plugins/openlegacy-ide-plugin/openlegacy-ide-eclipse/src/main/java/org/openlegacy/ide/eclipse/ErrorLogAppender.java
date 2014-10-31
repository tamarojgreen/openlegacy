package org.openlegacy.ide.eclipse;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

/**
 * @author Ivan Bort
 * 
 */
public class ErrorLogAppender extends AppenderSkeleton {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.log4j.Appender#close()
	 */
	public void close() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.log4j.Appender#requiresLayout()
	 */
	public boolean requiresLayout() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
	 */
	@Override
	protected void append(LoggingEvent event) {
		// get the platform log
		ILog log = Platform.getLog(Platform.getBundle(Activator.PLUGIN_ID));

		// create an IStatus status
		IStatus status = new Status(getLevel(event.getLevel()), Activator.PLUGIN_ID, getCode(event), getMessage(event),
				getThrowable(event));

		// log the status
		log.log(status);
	}

	private static int getLevel(Level level) {
		int severity;
		if (level.equals(Level.ALL) || level.equals(Level.ERROR) || level.equals(Level.FATAL)) {
			severity = IStatus.ERROR;
		} else if (level.equals(Level.WARN)) {
			severity = IStatus.WARNING;
		} else if (level.equals(Level.INFO)) {
			severity = IStatus.INFO;
		} else {
			severity = IStatus.INFO;
		}
		return severity;
	}

	private static int getCode(LoggingEvent event) {
		return (int)event.getTimeStamp();
	}

	private static String getMessage(LoggingEvent event) {
		return event.getMessage().toString();
	}

	private static Throwable getThrowable(LoggingEvent event) {
		ThrowableInformation info = event.getThrowableInformation();
		if (info != null) {
			return info.getThrowable();
		} else {
			return null;
		}
	}
}
