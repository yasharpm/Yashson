package com.yashoid.yashson;

import java.io.IOException;

public class ValueParseException extends IOException {

    public ValueParseException() {

    }

    public ValueParseException(String message) {
        super(message);
    }

    public ValueParseException(Throwable cause) {
        super(cause);
    }

    public ValueParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
