package pl.spoda.ks.comons.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LogHandler {

    @Around("@annotation(LogEvent)")
    public Object handleLogEvent(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = String.format( "%s.%s", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName() );

        LocalDateTime startTime = LocalDateTime.now();
        Jackson2ObjectMapperBuilder mapperBuilder = new Jackson2ObjectMapperBuilder();
        log.info(String.format("START --> %s, input: %s",
                methodName,
                Arrays.toString( joinPoint.getArgs() )));

        Object object = joinPoint.proceed();
        BigDecimal executionTime = calculateExecutionTime( startTime );

        log.info(String.format("END --> %s, output: %s, executionTime: %s ms",
                methodName,
                mapperBuilder.build().writeValueAsString( object ),
                executionTime));

        return object;
    }

    private static BigDecimal calculateExecutionTime(LocalDateTime startTime) {
        return BigDecimal.valueOf( Duration.between( startTime, LocalDateTime.now() ).getNano() ).divide( BigDecimal.valueOf( 1000000 ), 0, RoundingMode.UP );
    }
}
