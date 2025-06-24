package com.api.edutech.utils;

import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

public class ValidationUtils {

    public static Map<String, String> mapearErrores(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage()));
        return errores;
    }
}