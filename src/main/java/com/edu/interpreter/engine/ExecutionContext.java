package main.java.com.edu.interpreter.engine;

import main.java.com.edu.interpreter.stack.ScriptStack;

public class ExecutionContext {

    private ScriptStack stack = new ScriptStack();

    private byte[] currentData;

    public ScriptStack getStack() {
        return stack;
    }

    public byte[] getCurrentData() {
        return currentData;
    }

    public void setCurrentData(byte[] data) {
        this.currentData = data;
    }
}
