package org.openlegacy.adapter.terminal;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ChildScreenEntityAspect {

	@Around("execution(* *.fetch*(..))")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println(joinPoint.getTarget());
		Object result = joinPoint.proceed();
		return result;
	}

}
