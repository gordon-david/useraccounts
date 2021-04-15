package gordon.api.web;

import gordon.api.validation.UsernameExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintDeclarationException;

@ControllerAdvice(basePackageClasses = {AuthController.class, UserController.class})
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return super.handleMethodArgumentNotValid(ex, headers, status, request);
  }

  // 401 UNAUTHORIZED
  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<Object> handleDisabledException(RuntimeException ex, WebRequest request) {
    return handleExceptionInternal(ex, new GenericResponse().setMessage("this account has been disabled"), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
  }

  @ExceptionHandler(LockedException.class)
  public ResponseEntity<Object> handleLockedException(RuntimeException ex, WebRequest request) {
    return handleExceptionInternal(ex, new GenericResponse().setMessage("this account has been locked"), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Object> handleBadCredentialsException(RuntimeException ex, WebRequest request) {
    return handleExceptionInternal(ex, new GenericResponse().setMessage("bad credentials"), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
  }

  // 409 CONFLICT
  @ExceptionHandler(UsernameExistsException.class)
  public ResponseEntity<Object> handleUsernameExistsException(RuntimeException ex, WebRequest request) {
    return handleExceptionInternal(ex, new GenericResponse().setMessage("an account with this username already exists"), new HttpHeaders(), HttpStatus.CONFLICT, request);
  }

  // 400 BAD REQUEST
  @ExceptionHandler(ConstraintDeclarationException.class)
  public ResponseEntity<Object> handleConstraintVioldationException(RuntimeException ex, WebRequest request) {
    return handleExceptionInternal(ex, new GenericResponse().setMessage("constraint violation"), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
  }
}
