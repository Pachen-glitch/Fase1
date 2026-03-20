package main.java.com.edu.interpreter.engine;

import main.java.com.edu.interpreter.opcodes.Opcode;
import main.java.com.edu.interpreter.opcodes.OpcodeRegistry;
import main.java.com.edu.interpreter.exception.ScriptException;
import java.util.List;

public class ScriptInterpreter {
    private final OpcodeRegistry registry;
    private final ExecutionContext context;

    public ScriptInterpreter() {
        this.registry = new OpcodeRegistry();
        this.context = new ExecutionContext();
    }

    public boolean execute(List<String> tokens, boolean trace) throws ScriptException {
        for (String token : tokens) {
            if (trace) {
                System.out.println("Ejecutando token: " + token);
            }

            Opcode op = registry.get(token);
            
            if (!context.isExecuting()) {
                if (op != null && (token.equals("OP_ELSE") || token.equals("OP_ENDIF"))) {
                    op.execute(context);
                }
                if (trace) {
                    System.out.println("Pila actual: " + context.getStack().size() + " elementos");
                }
                continue;
            }

            if (op != null) {
                op.execute(context);
            } else {
                context.getStack().push(token.getBytes());
            }
            
            if (trace) {
                System.out.println("Pila actual: " + context.getStack().size() + " elementos");
            }
        }
        return validateFinalState();
    }

    private boolean validateFinalState() throws ScriptException {
        if (context.getStack().isEmpty()) return false;
        byte[] top = context.getStack().peek();
        return top != null && top.length > 0 && !new String(top).equals("0");
    }
}