package com.raybiztech.mail.service;

import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
public class SomeHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable t) {
       System.out.println("Error in listener "+ t);
    }
}