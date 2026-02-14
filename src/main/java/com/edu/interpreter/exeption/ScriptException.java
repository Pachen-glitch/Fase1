package main.java.com.edu.interpreter.exeption;

public class ScriptException extends Exception {

    public ScriptException(String message) {
        super(message);
    }

    public ScriptException(String message, Throwable cause) {
        super(message, cause);
    }
}
