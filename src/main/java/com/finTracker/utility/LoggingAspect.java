package com.finTracker.utility;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	
	@AfterThrowing(pointcut="execution(* com.finTracker.service.*.*(..))", throwing="exception")
	public void logServiceException(JoinPoint joinPoint, Exception exception) {
		Logger logger = Logger.getLogger(joinPoint.getTarget().getClass().getName());
		logger.severe("Exception in "+joinPoint.getSignature().getName()+" : "+exception.getMessage());
	}

}
