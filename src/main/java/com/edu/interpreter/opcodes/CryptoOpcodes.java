package main.java.com.edu.interpreter.opcodes;

public class CryptoOpcodes {

    public static void register(OpcodeRegistry registry){

        registry.register("OP_HASH160", (context) -> {

            byte[] v = context.getStack().pop();

            int hash = java.util.Objects.hash(new String(v));

            context.getStack().push(
                    String.valueOf(hash).getBytes()
            );

        });

        registry.register("OP_CHECKSIG", (context) -> {

            byte[] pubkey = context.getStack().pop();
            byte[] signature = context.getStack().pop();

            boolean valid = new String(signature).equals("VALIDA");

            if(valid)
                context.getStack().push("1".getBytes());
            else
                context.getStack().push("0".getBytes());

        });
    }
}