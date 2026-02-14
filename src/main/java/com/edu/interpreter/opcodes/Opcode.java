package main.java.com.edu.interpreter.opcodes;

import main.java.com.edu.interpreter.engine.ExecutionContext;
import main.java.com.edu.interpreter.exception.ScriptException;
@FunctionalInterface
public interface Opcode {

    void execute(ExecutionContext context) throws ScriptException;

}

