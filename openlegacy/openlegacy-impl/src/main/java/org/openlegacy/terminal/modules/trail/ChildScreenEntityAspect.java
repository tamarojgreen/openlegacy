package org.openlegacy.terminal.modules.trail;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

//@Component
@Aspect
public class ChildScreenEntityAspect {

	@Around("execution(* *.get*(..))")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println(joinPoint.getTarget());
		Object result = joinPoint.proceed();
		return result;
	}

}
