package main.java.com.edu.interpreter;

import main.java.com.edu.interpreter.engine.ScriptInterpreter;

public class Main {

    public static void main(String[] args) {

        ScriptInterpreter interpreter = new ScriptInterpreter();

        // Caso PDF
        boolean result1 = interpreter.execute("1 2 OP_ADD 5 OP_GREATERTHAN");
        System.out.println("Caso PDF: " + result1);

        // Igualdad 
        boolean result2 = interpreter.execute("1 1 OP_EQUAL");
        System.out.println("1 1 OP_EQUAL: " + result2);

        // P2PKH ( no representa un script real de P2PKH)
        boolean result3 = interpreter.execute(
            "VALIDA PUBKEY OP_DUP OP_HASH160 HASH_PUBKEY OP_EQUALVERIFY OP_CHECKSIG"
        );
        System.out.println("P2PKH: " + result3);
    }
}
