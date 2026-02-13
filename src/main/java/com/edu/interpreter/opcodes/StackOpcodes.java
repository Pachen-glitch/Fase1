package main.java.com.edu.interpreter.opcodes;

public class StackOpcodes {

    public static void register(OpcodeRegistry registry){

        registry.register("OP_DUP", (context) -> {

            byte[] v = context.getStack().pop();

            context.getStack().push(v);
            context.getStack().push(v);

        });

        registry.register("OP_DROP", (context) -> {

            context.getStack().pop();

        });
    }
}
