package pl.spoda.ks.api.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;

@ControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = SpodaApplicationException.class)
    ResponseEntity<BaseResponse> handleApplicationException(SpodaApplicationException ex) {
        log.info( "END --> " + ExceptionUtils.getStackTrace( ex ) );
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).body(
                BaseResponse.builder()
                        .status( HttpStatus.INTERNAL_SERVER_ERROR.value() )
                        .message( ex.getMessage() )
                        .build()
        );
    }
}
