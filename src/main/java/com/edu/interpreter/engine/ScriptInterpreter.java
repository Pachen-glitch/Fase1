package com.edu.interpreter.engine;

import com.edu.interpreter.opcodes.Opcode;
import com.edu.interpreter.opcodes.OpcodeRegistry;
import com.edu.interpreter.exception.ScriptException;
import com.edu.interpreter.opcodes.ArithmeticOpcodes;
import com.edu.interpreter.opcodes.ControlFlowOpcodes;
import com.edu.interpreter.opcodes.CryptoOpcodes;
import com.edu.interpreter.opcodes.LogicOpcodes;
import com.edu.interpreter.opcodes.StackOpcodes;
import java.util.List;

public class ScriptInterpreter {
    private final OpcodeRegistry registry;
    private final ExecutionContext context;

    public ScriptInterpreter() {
        this.registry = new OpcodeRegistry();
        this.context = new ExecutionContext();
        
        ArithmeticOpcodes.register(registry);
        ControlFlowOpcodes.register(registry);
        CryptoOpcodes.register(registry);
        LogicOpcodes.register(registry);
        StackOpcodes.register(registry);
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