package com.TaskForge.userService.Exception;

public class EmailSendFailedException extends RuntimeException{
    public EmailSendFailedException(String message, Throwable cause){
        super(message);
    }
}
