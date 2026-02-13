package main.java.com.edu.interpreter.stack;

import java.util.ArrayDeque;
import java.util.Deque;// recomendado en ves de STACK

public class ScriptStack {

    private final Deque<byte[]> stack;

    public ScriptStack() {
        this.stack = new ArrayDeque<>();
    }

    // Push
    public void push(byte[] data) {
        stack.push(data);
    }

    // Pop
    public byte[] pop() {
        if (stack.isEmpty()) {
            throw new RuntimeException("Stack underflow");
        }
        return stack.pop();
    }

    // Peek
    public byte[] peek() {
        if (stack.isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return stack.peek();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public int size() {
        return stack.size();
    }

    // Método  para trace
    public void printStack() {
        System.out.println("Stack: ");
        stack.forEach(item -> System.out.println(new String(item)));
    }
}
