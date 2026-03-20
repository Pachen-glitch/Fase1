package com.edu.interpreter.opcodes;

import com.edu.interpreter.engine.ExecutionContext;
import com.edu.interpreter.exception.ScriptException;
@FunctionalInterface
public interface Opcode {

    void execute(ExecutionContext context) throws ScriptException;

}

