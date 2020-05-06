package com.pazzio.raspberry.glove.dtos.handling;

public class GloveException extends RuntimeException {
    private static final long serialVersionUID = 8633391674318795826L;

    public GloveException(GloveExceptionType exceptionType) {
        super(exceptionType.toString());
    }

    public GloveException(GloveExceptionType exceptionType, String message) {
        super(exceptionType.toString() + " " + message);
    }

}

