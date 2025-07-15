package com.System.BankBack.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class) public ResponseEntity<Map<String,String>> handle(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error",ex.getMessage()));}
}