package main.java.com.edu.interpreter.opcodes;

import main.java.com.edu.interpreter.exception.ScriptException;

public class ArithmeticOpcodes {

    public static void register(OpcodeRegistry registry){

        // OP_0
        registry.register("OP_0", (context) -> {
            context.getStack().push("0".getBytes());
        });

        // OP_1 a OP_16
        for(int i = 1; i <= 16; i++){
            final int value = i;

            registry.register("OP_" + i, (context) -> {
                context.getStack().push(
                        String.valueOf(value).getBytes()
                );
            });
        }

        // OP_ADD
        registry.register("OP_ADD", (context) -> {

            int n1 = toInt(context.getStack().pop());
            int n2 = toInt(context.getStack().pop());

            int result = n2 + n1;

            context.getStack().push(
                    String.valueOf(result).getBytes()
            );
        });

        // OP_SUB
        registry.register("OP_SUB", (context) -> {

            int n1 = toInt(context.getStack().pop());
            int n2 = toInt(context.getStack().pop());

            int result = n2 - n1;

            context.getStack().push(
                    String.valueOf(result).getBytes()
            );
        });

        // OP_GREATERTHAN
        registry.register("OP_GREATERTHAN", (context) -> {

            int n1 = toInt(context.getStack().pop());
            int n2 = toInt(context.getStack().pop());

            if(n2 > n1)
                context.getStack().push("1".getBytes());
            else
                context.getStack().push("0".getBytes());
        });

        // OP_LESSTHAN
        registry.register("OP_LESSTHAN", (context) -> {

            int n1 = toInt(context.getStack().pop());
            int n2 = toInt(context.getStack().pop());

            if(n2 < n1)
                context.getStack().push("1".getBytes());
            else
                context.getStack().push("0".getBytes());
        });

        // OP_GREATERTHANOREQUAL
        registry.register("OP_GREATERTHANOREQUAL", (context) -> {

            int n1 = toInt(context.getStack().pop());
            int n2 = toInt(context.getStack().pop());

            if(n2 >= n1)
                context.getStack().push("1".getBytes());
            else
                context.getStack().push("0".getBytes());
        });

        // OP_LESSTHANOREQUAL
        registry.register("OP_LESSTHANOREQUAL", (context) -> {

            int n1 = toInt(context.getStack().pop());
            int n2 = toInt(context.getStack().pop());

            if(n2 <= n1)
                context.getStack().push("1".getBytes());
            else
                context.getStack().push("0".getBytes());
        });

        // OP_NUMEQUALVERIFY
        registry.register("OP_NUMEQUALVERIFY", (context) -> {

            int n1 = toInt(context.getStack().pop());
            int n2 = toInt(context.getStack().pop());

            if(n2 != n1) {
                throw new ScriptException("OP_NUMEQUALVERIFY failed");
            }
        });
    }

    // errores
    private static int toInt(byte[] data) throws ScriptException {
        try {
            return Integer.parseInt(new String(data));
        } catch (NumberFormatException e) {
            throw new ScriptException("Invalid numeric value");
        }
    }
}
