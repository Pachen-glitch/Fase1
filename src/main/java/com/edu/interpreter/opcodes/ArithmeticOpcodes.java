package main.java.com.edu.interpreter.opcodes;

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

            byte[] b1 = context.getStack().pop();
            byte[] b2 = context.getStack().pop();

            int n1 = Integer.parseInt(new String(b1));
            int n2 = Integer.parseInt(new String(b2));

            int result = n2 + n1; // importante el orden

            context.getStack().push(
                    String.valueOf(result).getBytes()
            );
        });

        // OP_SUB
        registry.register("OP_SUB", (context) -> {

            byte[] b1 = context.getStack().pop();
            byte[] b2 = context.getStack().pop();

            int n1 = Integer.parseInt(new String(b1));
            int n2 = Integer.parseInt(new String(b2));

            int result = n2 - n1;

            context.getStack().push(
                    String.valueOf(result).getBytes()
            );
        });

        // OP_GREATERTHAN
        registry.register("OP_GREATERTHAN", (context) -> {

            byte[] b1 = context.getStack().pop();
            byte[] b2 = context.getStack().pop();

            int n1 = Integer.parseInt(new String(b1));
            int n2 = Integer.parseInt(new String(b2));

            if(n2 > n1)
                context.getStack().push("1".getBytes());
            else
                context.getStack().push("0".getBytes());
        });
    }
}
