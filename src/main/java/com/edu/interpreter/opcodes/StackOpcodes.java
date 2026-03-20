package com.edu.interpreter.opcodes;

import com.edu.interpreter.engine.ExecutionContext;

public class StackOpcodes {

    public static void register(OpcodeRegistry registry){

        // OP_DUP
        registry.register("OP_DUP", (context) -> {

            byte[] value = context.getStack().peek();
            context.getStack().push(value);
        });

        // OP_DROP
        registry.register("OP_DROP", (context) -> {

            context.getStack().pop();
        });

        // OP_SWAP
        registry.register("OP_SWAP", (context) -> {

            byte[] top = context.getStack().pop();
            byte[] second = context.getStack().pop();

            context.getStack().push(top);
            context.getStack().push(second);
        });

        // OP_OVER
        registry.register("OP_OVER", (context) -> {

            byte[] top = context.getStack().pop();
            byte[] second = context.getStack().pop();

            context.getStack().push(second);
            context.getStack().push(top);
            context.getStack().push(second);
        });
    }
}
