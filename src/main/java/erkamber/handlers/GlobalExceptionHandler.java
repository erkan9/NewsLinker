package erkamber.handlers;

import erkamber.exceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleInvalidInputException(InvalidInputException exception) {
        logger.error("Caught exception: ", exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        logger.error("Caught exception: ", exception);
        return new ResponseEntity<>("Resource not found. Please try again", HttpStatus.NOT_FOUND);
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
    public ResponseEntity<String> authorNotReporterException(NotReporterException exception) {
        logger.error("Caught exception: ", exception);
        return new ResponseEntity<>("The User is not Reporter. Cannot subscribe", HttpStatus.NOT_FOUND);
    }
}
