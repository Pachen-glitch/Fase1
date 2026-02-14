package com.edu.interpreter.opcodes;

import com.edu.interpreter.exception.ScriptException;

public class ControlFlowOpcodes {

    public static void register(OpcodeRegistry registry){

        // OP_VERIFY
        registry.register("OP_VERIFY", (context) -> {

            byte[] value = context.getStack().pop();
            String v = new String(value);

            if(v.equals("0")){
                throw new ScriptException("OP_VERIFY failed");
            }
        });

        // OP_RETURN
        registry.register("OP_RETURN", (context) -> {
            throw new ScriptException("Script terminated by OP_RETURN");
        });

        // OP_IF
        registry.register("OP_IF", (context) -> {

            byte[] value = context.getStack().pop();
            boolean condition = !new String(value).equals("0");

            boolean parentExecuting = context.isExecuting();
            boolean shouldExecute = parentExecuting && condition;

            context.getConditionStack().push(shouldExecute);
            context.setExecuting(shouldExecute);
        });

        // OP_ELSE
        registry.register("OP_ELSE", (context) -> {

            if(context.getConditionStack().isEmpty()){
                throw new ScriptException("OP_ELSE without OP_IF");
            }

            boolean previous = context.getConditionStack().pop();

            boolean parentExecuting = true;
            if(!context.getConditionStack().isEmpty()){
                parentExecuting = context.getConditionStack().peek();
            }

            boolean newState = parentExecuting && !previous;

            context.getConditionStack().push(newState);
            context.setExecuting(newState);
        });

        // OP_ENDIF
        registry.register("OP_ENDIF", (context) -> {

            if(context.getConditionStack().isEmpty()){
                throw new ScriptException("OP_ENDIF without OP_IF");
            }

            context.getConditionStack().pop();

            if(context.getConditionStack().isEmpty()){
                context.setExecuting(true);
            } else {
                context.setExecuting(context.getConditionStack().peek());
            }
        });
    }
}
