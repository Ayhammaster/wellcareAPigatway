package com.wellcare.Post.Service.Exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDetails {
    
      private String message;
    private String details; 
    private LocalDateTime timestamp;


    public ErrorDetails(){}


    public ErrorDetails(String message, String details, LocalDateTime timestamp) {
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
    }


}
