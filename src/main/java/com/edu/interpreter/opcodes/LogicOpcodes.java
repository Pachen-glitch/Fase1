package main.java.com.edu.interpreter.opcodes;

public class LogicOpcodes {

    public static void register(OpcodeRegistry registry){

        registry.register("OP_EQUAL", (context) -> {

            byte[] a = context.getStack().pop();
            byte[] b = context.getStack().pop();

            boolean equal = java.util.Arrays.equals(a,b);

            if(equal)
                context.getStack().push("1".getBytes());
            else
                context.getStack().push("0".getBytes());

        });

        registry.register("OP_EQUALVERIFY", (context) -> {

            byte[] a = context.getStack().pop();
            byte[] b = context.getStack().pop();

            boolean equal = java.util.Arrays.equals(a,b);

            if(!equal){
                throw new RuntimeException("El script fallo");
            }

        });
    }
}