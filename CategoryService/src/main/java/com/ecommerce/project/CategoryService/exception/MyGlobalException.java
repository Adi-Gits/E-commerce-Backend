package com.ecommerce.project.CategoryService.exception;

import com.ecommerce.project.CategoryService.configuration.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@ResponseBody
public class MyGlobalException {

    //    @ExceptionHandler(Exception.class) -- generalize method since Exception is parent
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myException(MethodArgumentNotValidException e) {
        Map<String, String> errResponse = new HashMap<>();
        e.getBindingResult()
                .getAllErrors()
                .forEach(err -> {
                    String field = ((FieldError) err).getField();
                    String error = err.getDefaultMessage();
                    errResponse.put(field, error);
                });

        return new ResponseEntity<>(errResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> myResourceNotFoundException(ResourceNotFoundException e) {
        String message = e.getMessage();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIexception.class)
    public ResponseEntity<APIResponse> myAPIexception(APIexception e) {
        APIResponse response = new APIResponse(e.getMessage(),false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoCategoryAvailableException.class)
    public ResponseEntity<String> myNoCategoryAvailableException(NoCategoryAvailableException e) {
        String message = e.getMessage();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Map<String, String> errResponse = new HashMap<>();
        errResponse.put("Message",e.getMessage());
        errResponse.put("PropertyName",e.getPropertyName());
        return new ResponseEntity<>(errResponse,HttpStatus.BAD_REQUEST);
    }
//
//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<Map<String, String>> myException1(ResponseStatusException e) {
//        Map<String, String> errResponse = new HashMap<>();
//        e.getBindingResult()
//                .getAllErrors()
//                .forEach(err -> {
//                    String field = ((FieldError) err).getField();
//                    String error = err.getDefaultMessage();
//                });
//
//        return new ResponseEntity<>(errResponse, HttpStatus.BAD_REQUEST);
//
//    }
}
