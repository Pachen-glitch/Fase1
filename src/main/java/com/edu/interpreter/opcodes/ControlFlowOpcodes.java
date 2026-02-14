package main.java.com.edu.interpreter.opcodes;

public class ControlFlowOpcodes {

    public static void register(OpcodeRegistry registry){

        // OP_VERIFY
        registry.register("OP_VERIFY", (context) -> {

            byte[] value = context.getStack().pop();
            String v = new String(value);

            if(v.equals("0")){
                throw new RuntimeException("OP_VERIFY failed");
            }
        });

        // OP_RETURN
        registry.register("OP_RETURN", (context) -> {
            throw new RuntimeException("Script terminated by OP_RETURN");
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

            boolean current = context.getConditionStack().pop();
            boolean inverted = !current;

            context.getConditionStack().push(inverted);
            context.setExecuting(inverted);
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
