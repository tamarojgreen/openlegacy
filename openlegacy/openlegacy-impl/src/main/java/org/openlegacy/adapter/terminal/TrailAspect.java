package org.openlegacy.adapter.terminal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class TrailAspect {

	private Log log = LogFactory.getLog(this.getClass());

	// @Before("execution(* TerminalSession.doAction(..))")
	// @Before("execution(* *.*(..))")
	public void logBefore() {
		log.info("**************** The method doAction begins");
	}
}
