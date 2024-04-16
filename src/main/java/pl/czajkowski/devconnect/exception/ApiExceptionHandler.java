package pl.czajkowski.devconnect.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(ResourceNotFoundException e, HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceUploadException.class)
    public ResponseEntity<ErrorResponse> handleException(ResourceUploadException e, HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(UserNotFoundException e, HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(EmailAlreadyExistsException e, HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(
                request.getRequestURI(),
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(err, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProjectOwnershipException.class)
    public ResponseEntity<ErrorResponse> handleException(ProjectOwnershipException e, HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(
                request.getRequestURI(),
                HttpStatus.FORBIDDEN.value(),
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ProjectAssignmentException.class)
    public ResponseEntity<ErrorResponse> handleException(ProjectAssignmentException e, HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(
                request.getRequestURI(),
                HttpStatus.FORBIDDEN.value(),
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ProjectContributionException.class)
    public ResponseEntity<ErrorResponse> handleException(ProjectContributionException e, HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(
                request.getRequestURI(),
                HttpStatus.FORBIDDEN.value(),
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TaskNotBelongToProjectException.class)
    public ResponseEntity<ErrorResponse> handleException(TaskNotBelongToProjectException e, HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(
                request.getRequestURI(),
                HttpStatus.FORBIDDEN.value(),
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TaskNotBelongToUserException.class)
    public ResponseEntity<ErrorResponse> handleException(TaskNotBelongToUserException e, HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(
                request.getRequestURI(),
                HttpStatus.FORBIDDEN.value(),
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<String> messages = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> messages.add(error.getDefaultMessage()));

        ErrorResponse err = new ErrorResponse(
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                messages.toString(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
}
