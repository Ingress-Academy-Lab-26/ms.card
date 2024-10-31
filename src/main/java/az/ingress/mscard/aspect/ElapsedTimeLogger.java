package az.ingress.mscard.aspect;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ElapsedTimeLogger {
    @SneakyThrows
    @Around("@annotation(az.ingress.mscard.aspect.annotation.LogExecutionTime)")
    public Object elapsedTimeLogger(ProceedingJoinPoint jp){
        long startDate = System.currentTimeMillis();
        var result = jp.proceed();
        long endDate = System.currentTimeMillis();
        log.info("ActionLog.elapsedTimeLogger {} executed in {} ms", jp.getSignature(), endDate - startDate);
        return result;
    }
}
