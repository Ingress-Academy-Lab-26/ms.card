package az.ingress.mscard.aspect;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class DtoLogger {
    @SneakyThrows
    @Around("@annotation(az.ingress.mscard.aspect.annotation.LogDto)")
    public Object elapsedTimeLogger(ProceedingJoinPoint jp){
        Object[] args = jp.getArgs();
        String methodName = jp.getSignature().getName();
        log.info(">> ActionLog.elapsedTimeLogger requestDto of {}() is {}", methodName, Arrays.toString(args));
        var result = jp.proceed();
        log.info("<< ActionLog.elapsedTimeLogger responseDto of {}() is {}", jp.getSignature().getName(), result);
        return result;
    }
}