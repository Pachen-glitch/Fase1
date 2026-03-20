package main.java.com.edu.interpreter;

import com.edu.interpreter.engine.ScriptInterpreter;

public class Main {

    public static void main(String[] args) {
        ScriptInterpreter interpreter = new ScriptInterpreter();
        boolean trace = false;

        if (args.length > 0 && args[0].equals("--trace")) {
            trace = true;
            System.out.println("MODO TRACE ACTIVADO");
        }

        System.out.println("DEMOSTRACIONES FASE 2");

        System.out.println("P2PKH Correcto");
        System.out.println("Resultado: " + interpreter.execute("VALIDA PUBKEY OP_DUP OP_HASH160 HASH_PUBKEY OP_EQUALVERIFY OP_CHECKSIG", trace));

        System.out.println("P2PKH Incorrecto");
        System.out.println("Resultado: " + interpreter.execute("INVALIDA PUBKEY OP_DUP OP_HASH160 HASH_PUBKEY OP_EQUALVERIFY OP_CHECKSIG", trace));

        System.out.println("Condicional OP_IF/OP_ELSE");
        System.out.println("Resultado: " + interpreter.execute("1 OP_IF 10 OP_ELSE 0 OP_ENDIF", trace));
    }
}