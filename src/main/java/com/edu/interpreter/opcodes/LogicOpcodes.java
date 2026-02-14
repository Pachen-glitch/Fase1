package main.java.com.edu.interpreter.opcodes;

import java.util.Arrays;

public class LogicOpcodes {

    public static void register(OpcodeRegistry registry){

        // OP_EQUAL
        registry.register("OP_EQUAL", (context) -> {

            byte[] b1 = context.getStack().pop();
            byte[] b2 = context.getStack().pop();

            boolean equal = Arrays.equals(b2, b1);

            if(equal)
                context.getStack().push("1".getBytes());
            else
                context.getStack().push("0".getBytes());
        });

        // OP_EQUALVERIFY
        registry.register("OP_EQUALVERIFY", (context) -> {

            byte[] b1 = context.getStack().pop();
            byte[] b2 = context.getStack().pop();

            boolean equal = Arrays.equals(b2, b1);

            if(!equal){
                throw new RuntimeException("OP_EQUALVERIFY failed");
            }
        });

        // OP_NOT
        registry.register("OP_NOT", (context) -> {

            byte[] value = context.getStack().pop();
            String v = new String(value);

            if(v.equals("0"))
                context.getStack().push("1".getBytes());
            else
                context.getStack().push("0".getBytes());
        });

        // OP_BOOLAND
        registry.register("OP_BOOLAND", (context) -> {

            byte[] b1 = context.getStack().pop();
            byte[] b2 = context.getStack().pop();

            boolean v1 = !new String(b1).equals("0");
            boolean v2 = !new String(b2).equals("0");

            if(v1 && v2)
                context.getStack().push("1".getBytes());
            else
                context.getStack().push("0".getBytes());
        });

        // OP_BOOLOR
        registry.register("OP_BOOLOR", (context) -> {

            byte[] b1 = context.getStack().pop();
            byte[] b2 = context.getStack().pop();

            boolean v1 = !new String(b1).equals("0");
            boolean v2 = !new String(b2).equals("0");

            if(v1 || v2)
                context.getStack().push("1".getBytes());
            else
                context.getStack().push("0".getBytes());
        });
    }
    private static boolean toBool(byte[] data) {
    return !new String(data).equals("0");
}

    
}
