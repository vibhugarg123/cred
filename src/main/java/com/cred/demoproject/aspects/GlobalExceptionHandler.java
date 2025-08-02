package com.cred.demoproject.aspects;

import com.cred.demoproject.exceptions.InvalidRequestException;
import com.cred.demoproject.exceptions.ResourceNotFoundException;
import java.util.*;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    List<String> errors = new ArrayList<String>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error ->
                errors.add(
                    String.format("Field [%s] %s", error.getField(), error.getDefaultMessage())));

    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errors.toString());
    problemDetail.setTitle(getTitleFromExceptionClass(ex));

    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
  public ProblemDetail handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    problemDetail.setTitle(getTitleFromExceptionClass(ex));
    return problemDetail;
  }

  @ExceptionHandler(InvalidRequestException.class)
  ProblemDetail handleInvalidRequestException(InvalidRequestException ex) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    problemDetail.setTitle(getTitleFromExceptionClass(ex));
    return problemDetail;
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  ProblemDetail handleResourceNotFoundException(InvalidRequestException ex) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    problemDetail.setTitle(getTitleFromExceptionClass(ex));
    return problemDetail;
  }

  @Override
  protected ResponseEntity<Object> handleMissingPathVariable(
      MissingPathVariableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    problemDetail.setTitle(getTitleFromExceptionClass(ex));
    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  protected ResponseEntity<Object> handleBindException(
      BindException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    List<String> errorList =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                error ->
                    String.format("Field [%s] %s", error.getField(), error.getDefaultMessage()))
            .toList();
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorList.toString());
    problemDetail.setTitle(getTitleFromExceptionClass(ex));
    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    problemDetail.setTitle(getTitleFromExceptionClass(ex));
    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    problemDetail.setTitle(getTitleFromExceptionClass(ex));
    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  private String getTitleFromExceptionClass(Exception e) {
    return e.getClass()
        .getSimpleName()
        .replaceAll("(.)([A-Z])", "$1_$2")
        .toUpperCase(Locale.ROOT)
        .replace("_EXCEPTION", "");
  }
}
