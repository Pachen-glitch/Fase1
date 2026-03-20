package com.edu.interpreter.stack;

import java.util.ArrayDeque;
import java.util.Deque;

import com.edu.interpreter.exception.ScriptException;

public class ScriptStack {

    private final Deque<byte[]> stack = new ArrayDeque<>();

    public void push(byte[] data) {
        stack.push(data);
    }

    public byte[] pop() throws ScriptException {
        if (stack.isEmpty()) {
            throw new ScriptException("Stack underflow");
        }
        return stack.pop();
    }

    public byte[] peek() throws ScriptException {
        if (stack.isEmpty()) {
            throw new ScriptException("Stack is empty");
        }
        return stack.peek();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public int size() {
        return stack.size();
    }
}
