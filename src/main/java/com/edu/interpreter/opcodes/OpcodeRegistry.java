package com.edu.interpreter.opcodes;

import java.util.HashMap;
import java.util.Map;

public class OpcodeRegistry {

    private final Map<String, Opcode> registry = new HashMap<>();

    public void register(String name, Opcode opcode) {
        registry.put(name.toUpperCase(), opcode);
    }

    // NO lanzar excepción aquí
    public Opcode get(String name) {
        return registry.get(name.toUpperCase());
    }
}
