/*
 *
 */

package me.melvins.labs.exception.handling;

import me.melvins.labs.exception.KnownException;
import me.melvins.labs.exception.RequestHeaderValidationException;
import me.melvins.labs.exception.UnknownException;
import me.melvins.labs.pojo.vo.RequestHeaderVO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static me.melvins.labs.exception.handling.ErrorCode.EC211;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * The Exception Handler with {@link @ControllerAdvice}.
 *
 * @author Mels
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * @param ex
     * @param webRequest
     * @return
     */
    @ExceptionHandler(value = RequestHeaderValidationException.class)
    public ResponseEntity<Object> handleRequestHeaderValidationException(RequestHeaderValidationException ex,
                                                                         WebRequest webRequest) {

        Set<ConstraintViolation<RequestHeaderVO>> constraintViolations = ex.getConstraintViolations();

        List<String> errorList = new ArrayList<>();
        for (ConstraintViolation<RequestHeaderVO> s : constraintViolations) {
            ErrorCode errorCode = ErrorCode.valueOf(s.getMessage());
            errorList.add(MessageFormat.format(errorCode.toString(), s.getInvalidValue()));
        }

        Error error = new Error(EC211.name(), ex.getMessage(), errorList);

        HttpHeaders httpHeaders = new HttpHeaders();
        return handleExceptionInternal(ex, error, httpHeaders, BAD_REQUEST, webRequest);
    }

    /**
     * @param ex
     * @param webRequest
     * @return
     */
    @ExceptionHandler(value = KnownException.class)
    public ResponseEntity<Object> handleKnownException(KnownException ex, WebRequest webRequest) {

        HttpStatus httpStatus = ex.getHttpStatus();
        ErrorCode errorCode = ex.getErrorCode();
        String errorMessage = ex.getCustomMessage();

        Error error = new Error(errorCode.name(), errorMessage);

        HttpHeaders httpHeaders = new HttpHeaders();
        return handleExceptionInternal(ex, error, httpHeaders, httpStatus, webRequest);
    }

    /**
     * @param ex
     * @param webRequest
     * @return
     */
    @ExceptionHandler(value = UnknownException.class)
    public ResponseEntity<Object> handleUnknownException(UnknownException ex, WebRequest webRequest) {

        HttpStatus httpStatus = ex.getHttpStatus();
        ErrorCode errorCode = ex.getErrorCode();
        String errorMessage = ex.getException().getMessage();

        Error error = new Error(errorCode.name(), errorMessage);

        HttpHeaders httpHeaders = new HttpHeaders();
        return handleExceptionInternal(ex, error, httpHeaders, httpStatus, webRequest);
    }

}
