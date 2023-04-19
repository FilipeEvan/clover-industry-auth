package br.com.jacto.cloverindustryauth.infra.exception;

import br.com.jacto.cloverindustryauth.ValidationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleError404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleError400(MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(ValidationErrorData::new).toList());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleError400(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(toJson(ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleError409(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(toJson(ex.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handleBusinessRuleError(ValidationException ex) {
        return ResponseEntity.badRequest().body(toJson(ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity handleBadCredentialsError() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(toJson("Credenciais inválidas"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity handleAuthenticationError() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(toJson("Falha na autenticação"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleAccessDeniedError() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(toJson("Acesso negado"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleError500(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(toJson(ex.getLocalizedMessage()));
    }

    private record ValidationErrorData(String field, String message) {
        public  ValidationErrorData(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

    // Cria um método toJson que retorna um objeto no formato JSON contendo a mensagem de erro
    private ObjectNode toJson(String message) {
        // Cria um objeto ObjectMapper para trabalhar com JSON
        ObjectMapper mapper = new ObjectMapper();
        // Cria um objeto ObjectNode para armazenar a mensagem de erro
        ObjectNode error = mapper.createObjectNode();
        // Adiciona a mensagem de erro ao objeto ObjectNode com a chave "error"
        error.put("message", message);
        // Retorna o objeto ObjectNode com a mensagem de erro
        return error;
    }
}
