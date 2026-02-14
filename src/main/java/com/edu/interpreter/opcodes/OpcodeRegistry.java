package main.java.com.edu.interpreter.opcodes;

import java.util.HashMap;
import java.util.Map;

public class OpcodeRegistry {

    private final Map<String, Opcode> registry = new HashMap<>();

    public void register(String name, Opcode opcode) {
        registry.put(name.toUpperCase(), opcode);
    }

    public Opcode get(String name) throws ScriptException {
    Opcode opcode = registry.get(name.toUpperCase());
    if (opcode == null) {
        throw new ScriptException("Unknown opcode: " + name);
    }
    return opcode;
}

}
