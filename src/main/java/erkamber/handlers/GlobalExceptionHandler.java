package erkamber.handlers;

import erkamber.exceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.mail.MessagingException;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        logger.error("Caught exception: ", exception);

        List<String> errorMessages = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach(error -> errorMessages.add(error.getDefaultMessage()));

        return new ResponseEntity<>(String.join("\n", errorMessages), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
        logger.error("Caught exception: ", exception);
        return new ResponseEntity<>(exception.getConstraintViolations().iterator().next().getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    public ResponseEntity<String> handleUnsatisfiedServletRequestParameterException(UnsatisfiedServletRequestParameterException exception) {
        logger.error("Caught exception: ", exception);
        return new ResponseEntity<>("Invalid request parameters", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        logger.error("Caught exception: ", exception);
        return new ResponseEntity<>("Method Argument Type is Mismatched", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidBody(HttpMessageNotReadableException exception) {
        logger.error("Caught exception: ", exception);
        return new ResponseEntity<>("Invalid request body", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<String> handleConflict(RuntimeException exception) {
        logger.error("Caught exception: ", exception);
        return new ResponseEntity<>("Something went wrong", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleInvalidInputException(InvalidInputException exception) {
        logger.error("Caught exception: ", exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        logger.error("Caught exception: ", exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TextInjectionException.class)
    public ResponseEntity<String> handleTextInjectionException(TextInjectionException exception) {
        logger.error("Caught exception: ", exception);
        return new ResponseEntity<>("Text has possibility of containing SQL injection", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorMismatchException.class)
    public ResponseEntity<String> handleAuthorMismatchException(AuthorMismatchException exception) {
        logger.error("Caught exception: ", exception);
        return new ResponseEntity<>("Author ID of the comment does not match the provided User ID", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotReporterException.class)
    public ResponseEntity<String> handleAuthorNotReporterException(NotReporterException exception) {
        logger.error("Caught exception: ", exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<String> handleForbiddenActionException(ForbiddenActionException exception) {
        logger.error("Caught exception: ", exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<String> handleMessagingException(MessagingException exception) {
        logger.error("Caught exception: ", exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotMatchingPasswordsException.class)
    public ResponseEntity<String> handleNotMatchingPasswords(NotMatchingPasswordsException exception) {
        logger.error("Caught exception: ", exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
