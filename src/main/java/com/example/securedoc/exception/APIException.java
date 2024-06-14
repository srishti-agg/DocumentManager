package com.example.securedoc.exception;

public class APIException extends RuntimeException{

    public APIException(){
        super("An exception occurred");
    }
    public APIException(String message){
        super(message);
    }

}
