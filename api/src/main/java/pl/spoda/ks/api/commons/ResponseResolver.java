package pl.spoda.ks.api.commons;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseResolver {

    public ResponseEntity<BaseResponse> prepareResponse(HttpStatus status, String message) {
        return ResponseEntity.status( status ).body(
                BaseResponse.builder()
                        .status( status.value() )
                        .errorMessage( message )
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
