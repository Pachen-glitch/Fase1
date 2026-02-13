package main.java.com.edu.interpreter.opcodes;

public class ArithmeticOpcodes {

    public static void register(OpcodeRegistry registry){

        registry.register("OP_0", (context) -> {
            context.getStack().push("0".getBytes());
        });

        for(int i=1;i<=16;i++){

            final int value = i;

            registry.register("OP_" + i, (context) -> {

                context.getStack().push(
                        String.valueOf(value).getBytes()
                );

            });
        }
    }
}
