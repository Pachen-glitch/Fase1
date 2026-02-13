package main.java.com.edu.interpreter;

import main.java.com.edu.interpreter.opcodes.*;
import main.java.com.edu.interpreter.engine.ExecutionContext;
public class Main {

    public static void main(String[] args) throws Exception {

        OpcodeRegistry registry = new OpcodeRegistry();

        ArithmeticOpcodes.register(registry);
        StackOpcodes.register(registry);
        LogicOpcodes.register(registry);
        CryptoOpcodes.register(registry);

        ExecutionContext context = new ExecutionContext();

        registry.get("OP_2").execute(context);
        registry.get("OP_2").execute(context);
        registry.get("OP_EQUAL").execute(context);

        context.getStack().printStack();

    }
}
