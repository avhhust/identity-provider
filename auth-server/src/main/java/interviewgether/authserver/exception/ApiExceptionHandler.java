package interviewgether.authserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// Service that handles all exceptions
// Helpful when it comes to displaying proper error for the client
// Helps to convert any exception to same ApiErrorResponse class
@RestControllerAdvice
public class ApiExceptionHandler{

    // For handling any exception thrown due to validation errors (e.g. not valid username, empty password etc.)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleInvalidArgument(MethodArgumentNotValidException ex)
    {
        Map<String,String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> errorMap.put(error.getField(), error.getDefaultMessage())
        );
        return errorMap;
    }

    @ExceptionHandler(ApiServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleApiServiceException(ApiServiceException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put(e.getCausedBy(), e.getMessage());
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                httpStatus.value(),
                httpStatus.toString(),
                e.getMessage(),
                errorDetails
        );
        return new ResponseEntity<>(apiErrorResponse, httpStatus);
    }

}

