package com.boost.rabbit.infraestructure.external;

public class TwilioTokenNotConfiguredException extends IllegalStateException {

    public TwilioTokenNotConfiguredException(String message) {
        super(message);
    }
}
