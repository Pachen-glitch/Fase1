package main.java.com.edu.interpreter.opcodes;

import java.util.HashMap;
import java.util.Map;

public class OpcodeRegistry {

    private Map<String, Opcode> registry = new HashMap<>();

    public void register(String name, Opcode opcode) {
        registry.put(name, opcode);
    }

    public Opcode get(String name) {
        return registry.get(name);
    }
}