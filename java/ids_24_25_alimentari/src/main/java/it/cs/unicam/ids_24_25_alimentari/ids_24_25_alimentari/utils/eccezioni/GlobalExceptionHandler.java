package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.eccezioni;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", HttpStatus.BAD_REQUEST.value());
//        response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
//
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getFieldErrors()
//                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
//        response.put("details", errors);
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//    }

//    @ExceptionHandler(MultipartException.class)
//    public ResponseEntity<Map<String, Object>> handleMultipartException(MultipartException ex) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", HttpStatus.BAD_REQUEST.value());
//        response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
//        response.put("details", "Errore nel caricamento dei file: " + ex.getMessage());
//        response.put("stackTrace", Arrays.toString(ex.getStackTrace()));
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//    }
//
//    @ExceptionHandler(CustomJwtException.class)
//    public ResponseEntity<Map<String, Object>> handleJwtException(CustomJwtException ex) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", HttpStatus.UNAUTHORIZED.value());
//        response.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
//        response.put("message", ex.getMessage());
//        response.put("stackTrace", Arrays.toString(ex.getStackTrace()));
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//    }
//
//
//    @ExceptionHandler(NullPointerException.class)
//    public ResponseEntity<Map<String, Object>> handleNullPointerException(NullPointerException ex) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        response.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//        response.put("message", "Riferimento nullo: " + ex.getMessage());
//        response.put("stackTrace", Arrays.toString(ex.getStackTrace()));
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }
//
//    @ExceptionHandler(IOException.class)
//    public ResponseEntity<Map<String, Object>> handleIOException(IOException ex) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        response.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//        response.put("message", "Errore di I/O: " + ex.getMessage());
//        response.put("stackTrace", Arrays.toString(ex.getStackTrace()));
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }
//
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        response.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//        response.put("details", "Errore generico: " + ex.getMessage());
//        response.put("stackTrace", Arrays.toString(ex.getStackTrace()));
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return handleCustomException(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errors,
                Arrays.toString(ex.getStackTrace()));
    }


    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Map<String, Object>> handleMultipartException(MultipartException ex) {
        return handleCustomException(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Errore nel caricamento dei file: " + ex.getMessage(),
                Arrays.toString(ex.getStackTrace()));
    }


    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleJwtException(CustomJwtException ex) {
        return handleCustomException(
                HttpStatus.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ex.getMessage(),
                Arrays.toString(ex.getStackTrace()));
    }


    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, Object>> handleNullPointerException(NullPointerException ex) {
        return handleCustomException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Riferimento nullo: " + ex.getMessage(),
                Arrays.toString(ex.getStackTrace()));
    }


    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, Object>> handleIOException(IOException ex) {
        return handleCustomException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Errore di I/O: " + ex.getMessage(),
                Arrays.toString(ex.getStackTrace()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return handleCustomException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Errore generico: " + ex.getMessage(),
                Arrays.toString(ex.getStackTrace()));
    }


    private ResponseEntity<Map<String, Object>> handleCustomException(HttpStatus status, String error, Object details, String stackTrace) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("error", error);
        response.put("details", details);
        response.put("stackTrace", stackTrace);

        return ResponseEntity.status(status).body(response);
    }


}
