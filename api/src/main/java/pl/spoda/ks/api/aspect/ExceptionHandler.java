package pl.spoda.ks.api.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = SpodaApplicationException.class)
    ResponseEntity<BaseResponse> handleApplicationException(SpodaApplicationException ex) {
        log.error( "ERROR --> " + ExceptionUtils.getStackTrace( ex ) );
        return prepareResponse( HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage() );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<BaseResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error( "ERROR --> " + ExceptionUtils.getStackTrace( ex ) );
        return prepareResponse( HttpStatus.BAD_REQUEST,
                ex.getFieldErrors().stream().map( DefaultMessageSourceResolvable::getDefaultMessage ).collect( Collectors.joining(",")) );
    }

    private static ResponseEntity<BaseResponse> prepareResponse(HttpStatus httpStatus, String ex) {
        return ResponseEntity.status( httpStatus ).body(
                BaseResponse.builder()
                        .status( httpStatus.value() )
                        .message( ex )
                        .build()
        );
    }
}
