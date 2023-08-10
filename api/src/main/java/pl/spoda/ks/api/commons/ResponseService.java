package pl.spoda.ks.api.commons;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.commons.model.response.BaseResponse;

@Service
public class ResponseService {

    public ResponseEntity<BaseResponse> prepareResponse(HttpStatus status, String message) {
        return ResponseEntity.status( status ).body(
                BaseResponse.builder()
                        .status( status.value() )
                        .message( message )
                        .build() );
    }

    public ResponseEntity<BaseResponse> prepareResponse(BaseResponse response) {
        response.setStatus( HttpStatus.OK.value() );
        return ResponseEntity.ok( response );
    }

    public ResponseEntity<BaseResponse> prepareResponseCreated(BaseResponse response) {
        response.setStatus( HttpStatus.CREATED.value() );
        return ResponseEntity.ok( response );
    }
}
