package com.edu.interpreter;

import com.edu.interpreter.engine.ScriptInterpreter;

public class Main {

    public static void main(String[] args) {

        ScriptInterpreter interpreter = new ScriptInterpreter();

        System.out.println(" PRUEBAS ");

        // 1 Igualdad correcta
        boolean test1 = interpreter.execute("1 1 OP_EQUAL");
        System.out.println("1 1 OP_EQUAL " + test1);
        System.out.println("1 OP_VERIFY 1 ? " +
    interpreter.execute("1 OP_VERIFY 1"));


        // 2 Igualdad incorrecta
        boolean test2 = interpreter.execute("1 2 OP_EQUAL");
        System.out.println("1 2 OP_EQUAL " + test2);

        // 3 Caso PDF
        boolean test3 = interpreter.execute("1 2 OP_ADD 5 OP_GREATERTHAN");
        System.out.println("1 2 OP_ADD 5 OP_GREATERTHAN → " + test3);

        // 4 OP_VERIFY

        System.out.println("1 OP_VERIFY 1 ? " +
    interpreter.execute("1 OP_VERIFY 1"));

        boolean test4 = interpreter.execute("1 OP_VERIFY");
        System.out.println("1 OP_VERIFY 1 " + test4);

        boolean test5 = interpreter.execute("0 OP_VERIFY");
        System.out.println("0 OP_VERIFY  " + test5);

        // 5 P2PKH simulado
        boolean test6 = interpreter.execute(
            "VALIDA PUBKEY OP_DUP OP_HASH160 HASH_PUBKEY OP_EQUALVERIFY OP_CHECKSIG"
        );
        System.out.println("P2PKH válido " + test6);

        boolean test7 = interpreter.execute(
            "INVALIDA PUBKEY OP_DUP OP_HASH160 HASH_PUBKEY OP_EQUALVERIFY OP_CHECKSIG"
        );
        System.out.println("P2PKH inválido " + test7);

        System.out.println(" FIN ");
    }
}
