package com.edu.interpreter.opcodes;

import com.edu.interpreter.exception.ScriptException;

public class CryptoOpcodes {

    public static void register(OpcodeRegistry registry){

        // OP_HASH160 
        registry.register("OP_HASH160", (context) -> {

            byte[] value = context.getStack().pop();
            String input = new String(value);

            String hash = "HASH_" + input;

            context.getStack().push(hash.getBytes());
        });

        // OP_CHECKSIG 
        registry.register("OP_CHECKSIG", (context) -> {

            byte[] pubkey = context.getStack().pop();
            byte[] signature = context.getStack().pop();

            String sig = new String(signature);

            boolean valid = sig.equals("VALIDA");

            if(valid)
                context.getStack().push("1".getBytes());
            else
                context.getStack().push("0".getBytes());
        });

        // OP_CHECKSIGVERIFY
        registry.register("OP_CHECKSIGVERIFY", (context) -> {

            byte[] pubkey = context.getStack().pop();
            byte[] signature = context.getStack().pop();

            String sig = new String(signature);

            boolean valid = sig.equals("VALIDA");

            if(!valid){
                throw new ScriptException("OP_CHECKSIGVERIFY failed");
            }
        });
    }
}
