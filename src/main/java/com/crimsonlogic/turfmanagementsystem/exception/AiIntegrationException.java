package com.crimsonlogic.turfmanagementsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class AiIntegrationException extends RuntimeException {
    
    public AiIntegrationException(String message) {
        super(message);
    }
    
    public AiIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
