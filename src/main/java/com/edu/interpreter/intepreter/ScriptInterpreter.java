package main.java.com.edu.interpreter.intepreter;

import java.util.List;

import main.java.com.edu.interpreter.opcodes.*;

public class ScriptInterpreter {

    private final OpcodeRegistry registry;
    private final ScriptParser parser;

    public ScriptInterpreter() {
        registry = new OpcodeRegistry();
        parser = new ScriptParser();

        // Registrar todos los opcodes
        StackOpcodes.register(registry);
        ArithmeticOpcodes.register(registry);
        LogicOpcodes.register(registry);
        CryptoOpcodes.register(registry);
        ControlFlowOpcodes.register(registry);
    }

    public boolean execute(String script) throws Exception {

        ExecutionContext context = new ExecutionContext();
        List<String> tokens = parser.parse(script);

        for (String token : tokens) {

            Opcode opcode = registry.get(token);

            if (opcode != null) {
                opcode.execute(context);
            } else {
                // Si no es opcode → es literal
                context.getStack().push(token.getBytes());
            }
        }

        // Validación final
        if (context.getStack().isEmpty()) {
            return false;
        }

        String result = new String(context.getStack().peek());

        return !result.equals("0");
    }
}
