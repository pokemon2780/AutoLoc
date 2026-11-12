package tn.esprit.autoloc.web.controller;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.EntityNotFoundException;
import tn.esprit.autoloc.web.dto.ApiErrorResponse;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler({ResourceNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<Object> notFound(RuntimeException exception, WebRequest request) {
        return response(HttpStatus.NOT_FOUND, exception.getMessage(), Map.of(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> badRequest(IllegalArgumentException exception, WebRequest request) {
        return response(HttpStatus.BAD_REQUEST, exception.getMessage(), Map.of(), request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolation(ConstraintViolationException exception, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        exception.getConstraintViolations().forEach(error ->
                errors.putIfAbsent(error.getPropertyPath().toString(), error.getMessage()));
        return response(HttpStatus.BAD_REQUEST, "Données invalides", errors, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> conflict(DataIntegrityViolationException exception, WebRequest request) {
        return response(HttpStatus.CONFLICT,
                "Conflit de données : valeur déjà utilisée ou ressource encore référencée", Map.of(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        exception.getBindingResult().getGlobalErrors().forEach(error ->
                errors.putIfAbsent(error.getObjectName(), error.getDefaultMessage()));
        return new ResponseEntity<>(body(status, "Données invalides", errors, request), headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException exception,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = status.is5xxServerError() ? "Erreur interne du serveur" : "Paramètres invalides";
        return new ResponseEntity<>(body(status, message, Map.of(), request), headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object ignored,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = switch (status.value()) {
            case 400 -> "Requête invalide : vérifier le JSON, les types, les dates et les énumérations";
            case 404 -> "Ressource introuvable";
            case 405 -> "Méthode HTTP non autorisée";
            case 406 -> "Format de réponse non acceptable";
            case 415 -> "Type de contenu non pris en charge";
            default -> status.is5xxServerError() ? "Erreur interne du serveur" : "Requête refusée";
        };
        return new ResponseEntity<>(body(status, message, Map.of(), request), headers, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> unexpected(Exception exception, WebRequest request) {
        LOG.error("Erreur interne pendant le traitement d'une requête", exception);
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne du serveur", Map.of(), request);
    }

    private ResponseEntity<Object> response(HttpStatus status, String message,
            Map<String, String> errors, WebRequest request) {
        return ResponseEntity.status(status).body(body(status, message, errors, request));
    }

    private ApiErrorResponse body(HttpStatusCode status, String message,
            Map<String, String> errors, WebRequest request) {
        String path = request instanceof ServletWebRequest servlet
                ? servlet.getRequest().getRequestURI() : "";
        return new ApiErrorResponse(status.value(), message, Instant.now(), path, errors);
    }
}
