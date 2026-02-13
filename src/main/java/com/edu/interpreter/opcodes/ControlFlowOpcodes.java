package main.java.com.edu.interpreter.opcodes;

public class ControlFlowOpcodes {

    public static void register(OpcodeRegistry registry){

        registry.register("PUSHDATA", (context) -> {

            byte[] data = context.getCurrentData(); 
            context.getStack().push(data);

        });

    }
}