package main.java.com.edu.interpreter.engine;

import main.java.com.edu.interpreter.stack.ScriptStack;

import java.util.Stack;

public class ExecutionContext {

    private final ScriptStack stack = new ScriptStack();

    //  PUSHDATA 
    private byte[] currentData;

    // Manejo de IF/ELSE anidados
    private final Stack<Boolean> conditionStack = new Stack<>();

    // Indica si el bloque actual debe ejecutarse
    private boolean executing = true;

    //  modo trace
    private boolean traceEnabled = false;

    public ScriptStack getStack() {
        return stack;
    }

    public byte[] getCurrentData() {
        return currentData;
    }

    public void setCurrentData(byte[] data) {
        this.currentData = data;
    }

    public Stack<Boolean> getConditionStack() {
        return conditionStack;
    }

    public boolean isExecuting() {
        return executing;
    }

    public void setExecuting(boolean executing) {
        this.executing = executing;
    }

    public boolean isTraceEnabled() {
        return traceEnabled;
    }

    public void setTraceEnabled(boolean traceEnabled) {
        this.traceEnabled = traceEnabled;
    }
}
