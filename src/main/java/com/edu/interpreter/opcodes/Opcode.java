package main.java.com.edu.interpreter.opcodes;

import javax.script.ScriptException;

import main.java.com.edu.interpreter.engine.ExecutionContext;

@FunctionalInterface
public interface Opcode {

    void execute(ExecutionContext context) throws ScriptException;

}
