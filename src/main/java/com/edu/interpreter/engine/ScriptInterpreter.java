package main.java.com.edu.interpreter.engine;

import java.util.List;

import main.java.com.edu.interpreter.engine.ExecutionContext;
import main.java.com.edu.interpreter.engine.ScriptParser;
import main.java.com.edu.interpreter.opcodes.*;

public class ScriptInterpreter {

    private final OpcodeRegistry registry;
    private final ScriptParser parser;

    public ScriptInterpreter() {
        registry = new OpcodeRegistry();
        parser = new ScriptParser();

        StackOpcodes.register(registry);
        ArithmeticOpcodes.register(registry);
        LogicOpcodes.register(registry);
        CryptoOpcodes.register(registry);
        ControlFlowOpcodes.register(registry);
    }

    public boolean execute(String script) {

    ExecutionContext context = new ExecutionContext();
    List<String> tokens = parser.parse(script);

    try {

        for (String token : tokens) {

            Opcode opcode = registry.get(token);

            if (!context.isExecuting()) {

                if (opcode != null &&
                   (token.equals("OP_ELSE") || token.equals("OP_ENDIF"))) {

                    opcode.execute(context);
                }

                continue;
            }

            if (opcode != null) {
                opcode.execute(context);
            } else {
                context.getStack().push(token.getBytes());
            }
        }

        if (context.getStack().isEmpty())
            return false;

        return !new String(context.getStack().peek()).equals("0");

    } catch (Exception e) {
        return false;
    }
}

}
